package com.lianyitech.modules.circulate.service;

import com.lianyitech.common.utils.BeanUtilsExt;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.enmu.*;
import com.lianyitech.modules.catalog.dao.CopyDao;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.catalog.service.CopyService;
import com.lianyitech.modules.circulate.dao.BillDao;
import com.lianyitech.modules.circulate.dao.ReaderCardDao;
import com.lianyitech.modules.circulate.dao.RuleDao;
import com.lianyitech.modules.circulate.entity.Bill;
import com.lianyitech.modules.circulate.entity.CirculateDTO;
import com.lianyitech.modules.circulate.entity.CirculateLogDTO;
import com.lianyitech.modules.circulate.entity.Reader;
import com.lianyitech.modules.peri.dao.BindingDao;
import com.lianyitech.modules.sys.entity.MsgPush;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.lianyitech.core.config.ConfigurerConstants.RABBIT_QUEUE_NAME;

/**
 * 流通service
 * Created by tangwei on 2016/9/12.
 */
@Service
public class CirculateService {
    @Autowired
    RuleService ruleService;
    @Autowired
    CopyService copyService;
    @Autowired
    BillService billService;
    @Autowired
    ReaderCardDao readerCardDao;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    ReaderService readerService;
    @Autowired
    CopyDao copyDao;
    @Autowired
    BindingDao bindingDao;
    @Autowired
    private Environment environment;
    @Autowired
    RuleDao ruleDao;
    @Autowired
    BillDao billDao;

    @Autowired
    CompenService compenService;

    private static final String COMMA = ",";

    private static final String VERTICAL_LINE = "|";

    /**
     * 创建单个操作流程
     * @param dto 输入参数
     * @return 结果
     */
    @Transactional
    public HashMap<String, String> createSingle(CirculateDTO dto) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return this.create(dto);
    }

    /**
     * 创建多个操作流程
     * @param dto 输入参数
     * @return 结果
     */
    @Transactional
    public List<Map> createMulti(CirculateDTO dto) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List<Map> list = new ArrayList<>();
        String[] barcodes = dto.getBarcode().split(COMMA);
        for (String barcode: barcodes) {
            CirculateDTO d = new CirculateDTO();
            BeanUtilsExt.copyProperties(d, dto);
            d.setBarcode(barcode);
            Map<String,String>  result = this.create(d);
            result.put("barcode",barcode);
            list.add(result);
            //result.put(barcode, this.create(d));
        }
        return list;
    }

    private String getBillTypeByStatus(String Status) {
        if (EnumTypeAndStatus.TYPE_BORROW_STATUS.getValue().contains(Status)) {//借书单
            return EnumBillType.BORROW_BILL.getValue();
        } else if (EnumTypeAndStatus.TYPE_ORDER_BORROW_STATUS.getValue().contains(Status)) {//预借单
            return EnumBillType.ORDER_BORROW_BILL.getValue();
        } else if (EnumTypeAndStatus.TYPE_SCRAP_STATUS.getValue().contains(Status)) {//报废单
            return EnumBillType.SCRAP_BILL.getValue();
        } else if (EnumTypeAndStatus.TYPE_ORDER_STATUS.getValue().contains(Status)) {//预约单
            return EnumBillType.ORDER_BILL.getValue();
        }
//        else if (EnumTypeAndStatus.TYPE_STAINED_STATUS.getValue().contains(Status)) {//污损单
//            return EnumBillType.STAINED_BILL.getValue();
//        }
        return "";
    }
    @Transactional
    HashMap<String, String> create(CirculateDTO dto) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException{
        dto.setCreateDate(new Date());
        dto.setUpdateDate(new Date());
        HashMap<String, String> result;
        String type = dto.getType();//得到操作类型（0借阅  1归还  2丢失  3剔旧 4报废  5预约  6预借  7续借  8污损 9取消预约  10取消预借）
        Copy copy = getCopy(dto);
        result = checkCopy(copy,type);//验证复本信息
        if(result.containsKey("fail")){
            return result;
        }
        dto.setDirType(result.get("dirType"));//设置操作的书目类型(0图书 1期刊)
        String status = VERTICAL_LINE + dto.getType() + copy.getStatus() + VERTICAL_LINE;//操作类型跟图书状态组合 OPERATE_MAPPER.get(dto.getType())改成 EnumCirculateLogType.parse(dto.getType()).getName()
        //dto.setBillType(getBillTypeByStatus(status));//根据操作类型和图书状态组合得到具体操作单（若干种能明确，有的组合在不同单据种存在不能明确单据）
        setCirculateDto(dto,status,result);//查询对应的单据设置到dto实体类
        if (result.containsKey("fail")) {
            return result;
        }
        if(dto.getBillType() == null || "".equals(dto.getBillType())){
            result.put("fail","不存在此单据类型");
            return result;
        }
        switch (dto.getBillType()) {
            case "0"://0借书单
                lendOrBorrowBill(dto, copy, result);
                break;
            case "1"://1预借单
                lendOrBorrowBill(dto, copy, result);
                break;
            case "3"://预约单
                orderBill(dto, copy, result);
                break;
            default://其他单据报废
                scrapDefiledBill(dto,copy,result);
                break;
        }
        if(!result.containsKey("fail")){
            result.put("success",EnumCirculateLogType.parse(type).getName()+"操作成功");
        }
        return result;
    }

    /**
     * 根据借阅流程实体类查询对应的复本
     * @param dto 借阅流程实体类
     * @return copy复本信息
     */
    private Copy getCopy(CirculateDTO dto){
        Copy copy = new Copy();
        copy.setBarcode(dto.getBarcode());
        return copy;
    }

    /**
     * 消息推送
     * @param dto 借阅流程实体类
     */
    private void msgPush(CirculateDTO dto){
        Map<String,String> map = new HashMap<>();
        map.put("orgId",dto.getOrgId());
        map.put("card", dto.getCard());
        Reader reader = readerService.findByCard(map);
        List<Bill> billList = readerService.findCirculateLogByReader(reader);
        if (billList!=null && billList.size()>0){
            for (Bill bill : billList){
                if (bill.getId().equals(dto.getBillId())){
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

                    MsgPush msgPush = new MsgPush();
                    msgPush.setContent("亲爱的读者 "+ reader.getName()
                            +" ，您于"+ sdf.format(bill.getReserveDate())+"预约/借的图书《"+bill.getTitle()+"》已取消，请知悉。");
                    msgPush.setUserId(dto.getCard());
                    msgPush.setUnitId(dto.getOrgId());
                    rabbitTemplate.convertAndSend(environment.getProperty(RABBIT_QUEUE_NAME),msgPush);
                    break;
                }
            }
        }
    }

    /**
     * 借阅流通复本信息验证
     * @param tmpCopy 复本实体类
     * @param type 操作类型
     * @return result 返回参数
     */
    private HashMap<String, String> checkCopy(Copy tmpCopy,String type){
        HashMap<String, String> result = new HashMap<>();
        String orgId = UserUtils.getOrgId();
        String dirType = "0";
        Map<String, String> map = copyDao.findStatusByBarCode(orgId, tmpCopy.getBarcode());
        if (map == null) {
            map = bindingDao.findStatusByBarCode(orgId, tmpCopy.getBarcode());
            if (null != map) {
                dirType = "1";//代表期刊操作
            }
        }
        String status = VERTICAL_LINE + type + map.get("status") + VERTICAL_LINE;//操作类型跟图书状态组合 OPERATE_MAPPER.get(dto.getType())改成 EnumCirculateLogType.parse(dto.getType()).getName()
        if (!EnumTypeAndStatus.TYPE_STATUS_ALLOW.getValue().contains(status)) {//不符合流程的操作及状态组合 STATUS_MAPPER.get(copy.getStatus()) 改成 EnumLibStoreStatus.parse(copy.getStatus()).getName()
            // 前置状态判断     （此图书已？，？失败。） （状态，操作） 借阅->在馆/预借， 归还->借出， 丢失->借出， 剔旧->在馆/预借， 报废-> 剔旧， 预约->借出， 预借->在馆，续借->借出， 污损->在馆/预借
            result.put("fail", "此图书已" + EnumLibStoreStatus.parse(map.get("status")).getName() + "，" + EnumCirculateLogType.parse(type).getName() + "失败。");
            return result;
        }
        if (map == null) {
            result.put("fail", "此图书不存在！");
            return result;
        } else if ((EnumCirculateLogType.BORROW.getValue().equals(type) || EnumCirculateLogType.RENEW.getValue().equals(type)) && "0".equals(map.get("stockAttr"))) {//剔旧、报废的情况下（还的情况也加上虽然不会存在（馆藏地状态修改是否限制？？））
            result.put("fail", "此图书不能外借！");
            return result;
        }
        if(type.equals("1")){
            Integer borrowDay = billDao.borrowDay(UserUtils.getOrgId(),tmpCopy.getBarcode());
            Integer shortDay = ruleDao.shortDay(UserUtils.getOrgId(),tmpCopy.getBarcode());
            if (shortDay!=null && borrowDay !=null && borrowDay<shortDay){
                result.put("fail", "当前读者不允许还书，您已经设置最短借阅天数为"+shortDay+"天。");
                return result;
            }
        }
        tmpCopy.setStatus(map.get("status"));
        result.put("dirType", dirType);
        return result;
    }

    /**
     * 设置对应流通实体类参数
     * @param dto 借阅流通实体类
     * @param status 操作+复本状态组合字符串
     * @param result 返回map结果
     */
    private void setCirculateDto(CirculateDTO dto,String status,HashMap<String, String> result) {
        if (StringUtils.isBlank(dto.getOrgId())) {
            dto.setOrgId(UserUtils.getOrgId());//设置机构--此功能中跟机构有关联的直接会将此机构id复制过去的，无需每次去重新设置下
        }
        dto.setCreateBy(UserUtils.getLoginName());//操作人
        dto.setUpdateBy(UserUtils.getLoginName());//修改人
        dto.setBillType(getBillTypeByStatus(status));//根据操作类型和图书状态组合得到具体操作单（若干种能明确，有的组合在不同单据种存在不能明确单据）
        if (EnumTypeAndStatus.TYPE_OTHER_STATUS.getValue().contains(status)) {//如果是11归还 和 21丢失  71 续借3种情况要话单独处理14
            dto.setStatus("'" + EnumBillStatus.BORROW.getValue() + "','" + EnumBillStatus.RENEW.getValue() + "','" + EnumBillStatus.LOSS.getValue()+ "'");// '01','71','14' 状态查询借阅成功和续借成功的， 只有这2种这两种状态下面才可能存在归还、丢失、续借 为了下面缩小查询范围）
        }
        //bill修改状态下的需要去得到最新bill单据
        if (EnumTypeAndStatus.TYPE_STATUS_ALLOW_UPDATE.getValue().contains(status)) {   //如果是修改的情况下则要根据信息取获取对应的bill单信息
            Bill bill = billService.findLastByCon(dto);//根据条件查找最新的bill单据
            if (bill != null && bill.getId()!=null) {
                dto.setBillId(bill.getId());
                dto.setBillType(bill.getBillType());
            } else{
                result.put("fail","不存在对应的单据!");
            }
        }
    }

    //读者和借阅规则等验证
    private HashMap<String,String> checkReaderAndRule(CirculateDTO dto,Copy copy, HashMap<String, String> result) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException{
        if (StringUtils.isNotEmpty(dto.getCard())) {
            //获取 读者证 借阅规则 可操作次数
            HashMap<String, Object> readerMap = ruleService.findRuleOperateCountByCard(dto);
            //下面单独提出来是避免转换的时候报错
            int allow_count = 0;
            int same = 0;
            if (readerMap!=null && readerMap.get("ALLOWCOUNT") != null && !("".equals(readerMap.get("ALLOWCOUNT").toString()))) {
                allow_count = Integer.parseInt(readerMap.get("ALLOWCOUNT").toString());
            }
            if (readerMap!=null && readerMap.get("SAME") != null && !("".equals(readerMap.get("SAME").toString()))) {
                same = Integer.parseInt(readerMap.get("SAME").toString());
            }
            boolean isInvalid = readerMap == null || readerMap.get("READERID") == null || "".equals(readerMap.get("READERID"));
            if (isInvalid) {
                //读者证是否存在
                result.put("fail", "该读者证不存在！");
            } else if (!EnumTypeAndStatus.TYPE_RETURN_LOSS.getValue().contains(dto.getType())  &&!EnumCertStatus.VALID.getValue().equals(readerMap.get("STATUS")) && !EnumCertStatus.STOPBY.getValue().equals(readerMap.get("STATUS"))) {
                //不是归还操作，读者证是否挂失 改成了判断读者证是否有效
                result.put("fail", "该读者证不是有效状态。");
            } else if (!EnumTypeAndStatus.TYPE_RETURN_LOSS.getValue().contains(dto.getType()) && EnumCertStatus.STOPBY.getValue().equals(readerMap.get("STATUS"))) {
                result.put("fail", "该读者已经设置成停借状态，需解除后才可继续借阅");
            }
            else if (EnumTypeAndStatus.RULE_VALIDATE_TYPE.getValue().contains(dto.getType()) && allow_count <= 0) {//为0:借阅 5:预约 6:预借 7:续借 的情况下并且允许数小于0是验证不通过
                //是否符合借阅规则的要求 OPERATE_MAPPER.get(dto.getType()) 改成 EnumCirculateLogType.parse(dto.getType()).getName()
                result.put("fail", "您的" + EnumCirculateLogType.parse(dto.getType()).getName() +("7".equals(dto.getType()) ? "次" : "册") +"数已满，" + EnumCirculateLogType.parse(dto.getType()).getName() + "失败。");
            } else if (EnumTypeAndStatus.TYPE_BORROW_TYPE_ORDER.getValue().contains(dto.getType()) && same > 0) {
                String msg;
                if(copy.getStatus().equals("5")){
                    msg = "预借";
                } else {
                    msg = "预约";
                }
                //预借状态下的借阅操作，需要判断是否是同一个人，预约和预借状态下是否被其他人预约了
                result.put("fail", "此图书已经被" + msg + "，" + EnumCirculateLogType.parse(dto.getType()).getName() + "失败。");
            } else if(EnumCirculateLogType.BORROW.getValue().equals(dto.getType()) && readerMap.get("EXCEEDLIMIT") != null && "0".equals(readerMap.get("EXCEEDLIMIT").toString())){//借阅规则（超期不可借）的情况下的借阅 要验证是否存在超期记录
                //这里查询下是否存在超期未还的书
                Bill b = new Bill();
                BeanUtilsExt.copyProperties(b, dto);
                b.setReaderId(readerMap.get("READERID").toString());
                int exceed_count =  billService.countExceed(b);
                if(exceed_count>0){
                    result.put("fail", "存在超期未还的书，不能再次借阅！");
                    return result;
                }
            }
        } else {
            result.put("fail", "读者证不能为空");
        }
        return result;
    }
    //预约单
    private HashMap<String, String> orderBill(CirculateDTO dto,Copy copy,HashMap<String, String> result) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        checkReaderAndRule(dto,copy,result);
        if (result.containsKey("fail")) {
            return result;
        }
        String status = VERTICAL_LINE + dto.getType() + copy.getStatus() + VERTICAL_LINE;
        createBill(dto,status);
        //如果是取消预约的情况需要消息推送
        if (EnumCirculateLogType.CANCEL_ORDER.getValue().equals(dto.getType())) {
            msgPush(dto);
        }
        return result;
    }
    //报废、污损单据
    private HashMap<String, String> scrapDefiledBill(CirculateDTO dto,Copy copy,HashMap<String, String> result) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        String status = VERTICAL_LINE + dto.getType() + copy.getStatus() + VERTICAL_LINE;
        createBill(dto,status);

        return result;
    }
        //预借或者借书单
    private HashMap<String, String> lendOrBorrowBill(CirculateDTO dto,Copy copy,HashMap<String, String> result) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException{
        checkReaderAndRule(dto,copy,result);
        if (result.containsKey("fail")) {
            return result;
        }
        String status = VERTICAL_LINE + dto.getType() + copy.getStatus() + VERTICAL_LINE;
        createBill(dto,status);
        //如果是还书的情况下，要去查这本书是否已经预约，如果预约了就自动生成一条预借单 之后将原来的预约单状态改成15
        if (EnumCirculateLogType.RETURN.getValue().equals(dto.getType())) { //还书情况下查询是否存在该书预约，存在则进行下一步操作 ，还得将之前此本书续借的信息状态改成在馆
            orderCopyReturn(dto);
        }
        //如果是取消预借的情况需要消息推送
        if (EnumCirculateLogType.CANCEL_ORDER_BORROW.getValue().equals(dto.getType())) {
            msgPush(dto);
        }

        if( dto.getCompenRecord()!=null && StringUtils.isNotBlank(dto.getCompenRecord().getOpType())) {//赔付处理
            compenService.compen(dto.getCompenRecord());
        }

        return result;
    }
    //预约的书归还情况下自动产生单据
    private void orderCopyReturn(CirculateDTO dto) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //第一步查询是否存在该本书预约单
        dto.setBillType(EnumBillType.ORDER_BILL.getValue());//预约单
        dto.setStatus(EnumBillStatus.ORDER.getValue());//状态是预约成功的
        dto.setCard(null);//这里不需要传入读者
        Bill bill = billService.findLastByCon(dto);
        //如果该本书存在预约的情况下
        if (bill != null && bill.getId() != null) {
            //1.将该本书的预约单状态改成15
            bill.setStatus(EnumBillStatus.ORDER_TO_ORDER_BORROW.getValue());//15 改成用枚举
            bill.setUpdateBy(dto.getUpdateBy());//修改人
            bill.setUpdateDate(dto.getUpdateDate());
            billService.modifyBill(bill);
            //2.将该本书的状态改成预借
            dto.setType(EnumCirculateLogType.ORDER_BORROW.getValue());//改成预借
            copyService.updateByBillType(changeToMap(dto));//修改复本状态
            //3.新增个预借单
            CirculateDTO dtonew = new CirculateDTO();
            dtonew.setBillType(EnumBillType.ORDER_BORROW_BILL.getValue());//预借单 1
            dtonew.setType(EnumCirculateLogType.ORDER_BORROW.getValue());//预借操作 6
            dtonew.setReaderid(bill.getReaderId());
            dtonew.setBarcode(dto.getBarcode());
            dtonew.setOrgId(dto.getOrgId());
            dtonew.setUpdateDate(dto.getUpdateDate());
            billService.create(dtonew);//创建新的单据
            //4.新增操作日志
            CirculateLogDTO logDTOnew = new CirculateLogDTO();
            logDTOnew.setBillId(dtonew.getBillId());
            logDTOnew.setLogType(EnumCirculateLogType.ORDER_BORROW.getValue());//预借操作
            logDTOnew.setUpdateDate(dto.getUpdateDate());
            logDTOnew.setCreateDate(dto.getCreateDate());
            billService.createlog(logDTOnew);//插入一条操作日志记录
        }
    }
    private Map<String,Object> changeToMap(CirculateDTO dto){
        HashMap<String,Object> result = new HashMap<>();
        result.put("type",dto.getType());
        result.put("updateBy",dto.getUpdateBy());
        result.put("barcode",dto.getBarcode());
        result.put("orgId",dto.getOrgId());
        result.put("updateDate",dto.getUpdateDate());
        result.put("dirType",dto.getDirType());
        return result;
    }


    private void createBill(CirculateDTO dto,String status) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //第一修复复本状态
        copyService.updateByBillType(changeToMap(dto));
        if (EnumTypeAndStatus.TYPE_STATUS_ALLOW_CREATE.getValue().contains(status)) {
            //创建单据
            billService.create(dto);
        } else if (EnumTypeAndStatus.TYPE_STATUS_ALLOW_UPDATE.getValue().contains(status)) {
            if( dto.getBillId()!=null){
                billService.modify(dto);//修改对应的单据
            }
        }
        CirculateLogDTO logDTO = new CirculateLogDTO();
        BeanUtilsExt.copyProperties(logDTO, dto);//复制属性
        logDTO.setLogType(dto.getType());//操作日志的操作行为
        logDTO.setBillId(dto.getBillId());
        logDTO.setOrgId(dto.getOrgId());
        logDTO.setCreateDate(dto.getCreateDate());
        logDTO.setUpdateDate(dto.getUpdateDate());
        billService.createlog(logDTO);//插入一条操作日志记录
    }

    public void stopBy(CirculateDTO dto) throws Exception {
        dto.setStatus("5");
        dto.setUpdateDate(new Date());
        readerCardDao.updateStatus(dto);
    }

    public void relieve(CirculateDTO dto) throws Exception {
        dto.setStatus("0");
        dto.setUpdateDate(new Date());
        readerCardDao.updateStatus(dto);
    }

}
