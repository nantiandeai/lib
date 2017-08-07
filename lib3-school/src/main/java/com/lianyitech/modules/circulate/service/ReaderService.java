package com.lianyitech.modules.circulate.service;

import com.lianyitech.common.utils.*;
import com.lianyitech.core.enmu.EnumCertStatus;
import com.lianyitech.core.enmu.EnumCertType;
import com.lianyitech.core.enmu.EnumReaderType;
import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;

import com.lianyitech.core.jxls.ReaderImporter;
import com.lianyitech.core.utils.BarcodeUtil;
import com.lianyitech.core.utils.ImageIODemo;
import com.lianyitech.core.utils.SystemUtils;
import com.lianyitech.modules.catalog.dao.BarcodeRecordDao;
import com.lianyitech.modules.catalog.dao.ImportRecordDao;
import com.lianyitech.modules.catalog.entity.ImportRecord;
import com.lianyitech.modules.catalog.service.BookDirectoryService;
import com.lianyitech.modules.catalog.service.ImportService;
import com.lianyitech.modules.circulate.dao.*;
import com.lianyitech.modules.circulate.entity.*;
import com.lianyitech.modules.circulate.entity.Reader;
import com.lianyitech.modules.common.SystemService;
import com.lianyitech.modules.peri.service.BindingService;
import com.lianyitech.modules.sys.entity.User;
import com.lianyitech.modules.sys.service.FileService;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;


/**
 * 读者管理Service
 *
 * @author zengzy
 * @version 2016-09-09
 */
@Service
@Transactional(readOnly = true)
public class ReaderService extends CrudService<ReaderDao, Reader> {
    @Autowired
    private BillDao billDao;
    @Autowired
    private ReaderCardService readerCardService;
    @Autowired
    private ReaderCardDao readerCardDao;
    @Autowired
    private GroupService groupService;
    @Autowired
    private ReaderDao readerDao;
    @Autowired
    private CirculateService circulateService;
    @Autowired
    ImportRecordDao importRecordDao;
    @Autowired
    private ImportService importService;
    @Autowired
    GroupDao groupDao;
    @Autowired
    private BindingService bindingService;
    @Autowired
    private BarcodeRecordDao barcodeRecordDao ;
    @Autowired
    private Environment environment;
    @Autowired
    private BookDirectoryService bookDirectoryService;
    @Autowired
    private DepositRecordDao depositRecordDao;

    @Autowired
    CardPringConfigService cardPringConfigService;
    @Autowired
    FileService fileService;

    private List<String> cardList = new ArrayList<String>();

    private Map<String,Group> groupMap = new HashMap<String,Group>();
    private final static String OPERATOR_SUCCESS = "success";

    public Reader queryByReaderPlatfrom(Reader reader) {
        return dao.queryByReaderPlatfrom(reader);
    }
//    public ReaderVo get(String id) {
//        return super.get(id);
//    }

    public void  updateTempByItself(String tableName){
        dao.updateTempByItself(tableName);
    }

    public Reader get(Reader reader) {
        return super.get(reader);
    }

    public Page<Reader> findPage(Page<Reader> page, Reader reader) {
//        if(page.getOrderBy()!=null && page.getOrderBy().indexOf("a.name")!=-1) {
//            page.setOrderBy(page.getOrderBy().replace("a.name","nlssort(a.name, 'NLS_SORT=SCHINESE_PINYIN_M')"));
//        }
//        if(page.getOrderBy()!=null && page.getOrderBy().indexOf("a.name")!=-1) {
//            page.setOrderBy(page.getOrderBy().replace("a.name","nlssort(a.name, 'NLS_SORT=SCHINESE_PINYIN_M&apos;)"));
//        }

        if(StringUtils.isNotBlank(page.getOrderBy())) {
            reader.setOrderBy(page.getOrderBy().replace("a.name","nlssort(a.name, 'NLS_SORT=SCHINESE_PINYIN_M')"));
        }
        reader.setOrgId(UserUtils.getOrgId());
        return super.findPage(page, reader);
    }

    @Transactional
    public void save(Reader reader) {
        try {
            reader.setOrgId(UserUtils.getOrgId());
            //如果组织机构ID换了的话，与之相关的读者流通信息组织也应该换
            if(StringUtils.isNotBlank(reader.getId())){
                if(!Objects.equals(get(reader).getGroupId(),reader.getGroupId())){
                    billDao.updateBillGroup(reader.getGroupId(),reader.getId(),UserUtils.getOrgId());
                }
            }
            super.save(reader);
            //更据读者ID来得到读者证
            ReaderCard readerCard = readerCardDao.findByReaderId(reader);
            if (readerCard != null) {
                readerCard.setEndDate(reader.getEndDate());
                readerCard.setReaderId(reader.getId());
                readerCard.setOrgId(reader.getOrgId());
                if (StringUtils.isNotEmpty(reader.getStatus()) && !reader.getStatus().equals("4")) {
                    readerCard.setStatus("0");
                }
                readerCard.setPassword(readerCard.getPassword());
                readerCardService.save(readerCard);
            } else {
                readerCard = new ReaderCard();
                readerCard.setCard(reader.getCard());
                readerCard.setStartDate(reader.getStartDate());
                readerCard.setEndDate(reader.getEndDate());
                readerCard.setReaderId(reader.getId());
                readerCard.setStatus("0");
                readerCard.setPassword(SystemService.entryptPassword("888888"));
                readerCard.setOrgId(reader.getOrgId());
                readerCardService.save(readerCard);
            }

        } catch (Exception e) {
            logger.error("操作失败",e);
        }


    }

    public int checkCard(Reader reader) {
        reader.setOrgId(UserUtils.getOrgId());
        return readerDao.checkCard(reader);
    }

    @Transactional
    public int delete(String ids) {
        return super.delete(ids);
    }

    public Reader findByCard(Map<String,String> map) {
        return dao.findByCard(map);
    }

    @Transactional
    public String logOutReaderAndCard(String ids) {
        String[] idArr = ids.split(",");
        List<String> idList = Arrays.asList(idArr);
        //第一步验证
        Reader r = new Reader();
        r.setIdList(idArr);
        r.setOrgId(UserUtils.getOrgId());
        List<Reader> borrow_readers = readerDao.findBorrowReader(r);
        if (borrow_readers != null && borrow_readers.size() > 0) {
            throw new RuntimeException(this.return_msg(borrow_readers)+" 借阅的图书未归还，注销失败");
        }
        for (String id : idList) {
            Reader tmpReader = new Reader();
            tmpReader.setId(id);
            tmpReader.setOrgId(UserUtils.getOrgId());
            Reader reader = get(tmpReader);
            try {
                logOutReader(reader);//注销读者（包括自动取消预约预借）
            } catch (Exception e) {
                logger.error("操作失败",e);
                return "操作失败！";
            }
        }
        return "";
    }
    //根据读者集合返回名称
    private String return_msg(List<Reader> borrow_readers){
        StringBuilder msg = new StringBuilder();
        int num = 0;
        for (Reader reader : borrow_readers) {
            if(num>5){
                return msg.toString();
            }
            if(num>0){
                msg.append(",");
            }
            msg.append(reader.getName());
            num++;
        }
        return msg.toString();
    }
    public void logOutReader(Reader reader) throws Exception{
        reader.preUpdate();
        reader.setOrgId(UserUtils.getOrgId());
        readerDao.logOutReaderCard(reader);//修改读者证状态
        this.cancelReaderOrder(reader);
    }
    //注销读者（包括：取消读者预约预借cancelReaderOrder）
    public void cancelReaderOrder(Reader reader) throws Exception{
        //查询此读者是否存在预约预借的信息
        Bill bill = new Bill();
        bill.setReaderId(reader.getId());
        bill.setStatus("'65','51'");//预约预借
        bill.setOrgId(UserUtils.getOrgId());
        List<Bill> listbill = billDao.findCirculateByReader(bill);
        if (listbill != null && listbill.size() > 0) {
            for (Bill b : listbill) {
                CirculateDTO dto = new CirculateDTO();
                BeanUtils.copyProperties(dto, b);
                if ("65".equals(b.getStatus())) {
                    dto.setType("10");//取消预借
                } else {
                    dto.setType("9");//取消预约
                }
                circulateService.create(dto);
            }
        }
    }

    @Transactional
    public Map regain(String ids) {
        //1判断读者组织是否有效.2判断读者证是否过期
        Map<String, Object> map = new HashMap<>();
        String[] idArr = ids.split(",");
        List<String> idList = Arrays.asList(idArr);
        for (String id : idList) {
            Reader tmpReader = new Reader();
            tmpReader.setId(id);
            tmpReader.setOrgId(UserUtils.getOrgId());
            Reader reader = get(tmpReader);
            Group group = readerDao.findGroupByReader(reader);
            if (group == null) {
                throw new RuntimeException("读者所在的组织已失效，失败");
            }
            ReaderCard readerCard = readerDao.findCardByReader(reader);
            String times = readerCard.getTimes();
            if (times.equals("1")) {//
                throw new RuntimeException("该读者证已过期，失败，请进行换证。");
            }
            reader.setUpdateDate(new Date());
            readerDao.regainReaderCard(reader);
        }
        return map;

    }

    public List<Reader> exportReader(Reader reader) {
        reader.setOrgId(UserUtils.getOrgId());
        List<Reader> list = readerDao.findList(reader);
        return list;
    }

    @Transactional(readOnly = false)
    private void saveThisAndsaveCard(Reader reader) {
        if (reader.getGroupId() == null || reader.getGroupId().equals("")) {
            Group group = new Group();
            group.setName(reader.getGroupName());
            group.setStatus("0");
            group.setGroupType("0");

            if (reader.getReaderTypeName().equals("学生")) {
                group.setGroupType("0");
            } else {
                group.setGroupType("1");
            }

            try {
                groupService.saveSuper(group);
                reader.setGroupId(group.getId());
            } catch (Exception e) {
                logger.error("操作失败",e);
            }
        }

        super.save(reader);

        if (reader.getCard() != null && !reader.getCard().equals("")) {
            ReaderCard readerCard = new ReaderCard();
            readerCard.setReaderId(reader.getId());
            readerCard.setStatus("0");
            readerCard.setStartDate(new Date());
            readerCard.setEndDate(reader.getEndDate());
            readerCard.setCard(reader.getCard());
            try {
                readerCardService.save(readerCard);
            } catch (Exception e) {
                logger.error("操作失败",e);
            }
        }
    }


    public void batchSave(List<Reader> list){
        List<Group> groupList = new ArrayList<Group>();
        List<ReaderCard> readerCardList = new ArrayList<ReaderCard>();
        for (Reader reader : list) {
            if (reader.getGroupId() == null || reader.getGroupId().equals("")) {
                Group tmpGroup = groupMap.get(reader.getGroupName()+reader.getOrgId());
                if(tmpGroup!=null) {
                    reader.setGroupId(tmpGroup.getId());
                } else {
                    Group group = new Group();
                    group.setName(reader.getGroupName());
                    group.setStatus("0");
                    group.setGroupType("0");
                    group.setId(IdGen.uuid());
                    group.setCreateBy(reader.getCreateBy());
                    group.setCreateDate(new Date());
                    group.setOrgId(reader.getOrgId());
                    if (reader.getReaderTypeName().equals("学生")) {
                        group.setGroupType("0");
                    } else {
                        group.setGroupType("1");
                    }
                    groupList.add(group);
                    reader.setGroupId(group.getId());
                    groupMap.put(group.getName()+group.getOrgId(),group);
                }
            }

            if (reader.getCard() != null && !reader.getCard().equals("")) {
                ReaderCard readerCard = new ReaderCard();
                readerCard.setReaderId(reader.getId());
                readerCard.setStatus("0");
                readerCard.setStartDate(new Date());
                readerCard.setEndDate(reader.getEndDate());
                readerCard.setCard(reader.getCard());
                readerCardList.add(readerCard);
                readerCard.setOrgId(reader.getOrgId());
                readerCard.setCreateBy(reader.getCreateBy());
                readerCard.setCreateDate(new Date());
                readerCard.setId(IdGen.uuid());
                readerCard.setOrgId(reader.getOrgId());
                readerCard.setPassword(SystemService.entryptPassword("888888"));
            }
        }


//        super.save(reader);

        dao.batchSave(list);
        if(readerCardList!=null && !readerCardList.isEmpty()){
            readerCardService.batchSave(readerCardList);
        }
        if(groupList!=null && !groupList.isEmpty()){
            groupService.batchSave(groupList);
        }
    }
    /**public void batchSave(List<ReaderVo> list) {
     dao.batchSave(list);
     }

     /**
     * 读者数据上传
     *
     * @param multiFile 前端上传文件
     */
    public void upReaderFile(MultipartFile multiFile) throws IOException {

        long size = multiFile.getSize();
        if (size <= 0) {
            throw new RuntimeException("读者数据文件是空的文件");
        }

        String fileName = multiFile.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        final List<String> extensionList = Arrays.asList(".xls", ".xlsx");

        if (extensionList.indexOf(extension) == -1) {
            throw new RuntimeException("不支持的类型，支持xls，xlsx两种格式。");
        } else if (size > 10485760) {
            throw new RuntimeException("文件过大，支持10M内的Excel文件。");
        }

        String uploadFileName = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME)
                .replaceAll(":", "") + "_" + UUID.randomUUID().toString().replaceAll("-", "") + extension;

        String uploadPath = String.join(
                File.separator,
                new String[]{
                        environment.getProperty("upload.linux.path"),
                        LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE).replaceAll("-", ""),
                        "bookDir"
                }
        );

        File file = new File(uploadPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        String filePath = uploadPath + File.separator + uploadFileName;
        file = new File(filePath);
        if (file.createNewFile()) {
            FileCopyUtils.copy(multiFile.getBytes(), file);
            ImportRecord recode = importService.initImportRecord(fileName, filePath, extensionList.indexOf(extension), 2);

            Object[] objects = new Object[]{UserUtils.getUser(), recode};
            //第一种--单个mq
            //readerTemplate.convertAndSend(BOOK_DIRECTORY_QUEUE_NAME, new Object[]{UserUtils.getUser(), recode});
            //第二种--动态创建mq
            try {
                bookDirectoryService.sendMq(objects);
            } catch (Exception e) {
                logger.error("操作失败",e);
            }
        }
    }

    public void createTableTemp(String tableName) {
        dao.createTableTemp(tableName);
    }

    public void dropTableTemp(String tableName) {
        dao.dropTableTemp(tableName);
    }

    public void batchSaveTemp(List<Reader> list, String tableName) {
        for (Reader reader : list) {
            reader.setPassword(SystemService.entryptPassword("888888"));
        }
        dao.batchSaveTemp(list, tableName);
    }

    public void insertNotExistsGroup(String tableName) {
        dao.insertNotExistsGroup(tableName);
    }

    public void updateGroupInfoTemp(String tableName) {
        dao.updateGroupInfoTemp(tableName);
    }

    public void checkGroupInfoTemp(String tableName) {
        dao.checkGroupInfoTemp(tableName);
    }

    public void checkReaderCardTemp(String orgId, String tableName) {
        dao.checkReaderCardTemp(orgId, tableName);
    }

    public void insertNotExistsReaderCard(String tableName) {
        dao.insertNotExistsReaderCard(tableName);
    }

    public int insertReaderFromTemp(Date updateDate,String tableName) {
        return dao.insertReaderFromTemp(updateDate,tableName);
    }

    public List <Reader> findErrorListTemp(String tableName) {
        return dao.findErrorListTemp(tableName);
    }

    public synchronized boolean checkReader(Reader reader) {
        String result;
        result = checkParams(reader);

        if (!result.equals(OPERATOR_SUCCESS)) {
            reader.setErrorinfo(result);
            return false;
        }


        if ( StringUtils.isEmpty(reader.getReaderTypeName()) ) {
            reader.setErrorinfo("读者类型不能为空!");
            return false;
        } else {
            String readerTypeName = reader.getReaderTypeName().trim();
            if(Objects.equals(readerTypeName,"集体")){
                reader.setErrorinfo("集体类型不允许导入");
                return false;
            }
            String ReaderType;
            try {
                ReaderType = EnumReaderType.parseName(readerTypeName).getValue();
            } catch (IllegalArgumentException e){
                reader.setErrorinfo("读者类型不符合规范!");
                return false;
            }

            if ( StringUtils.isNotEmpty(ReaderType) ) {
                reader.setReaderType(ReaderType);
            }
        }
        if ( StringUtils.isNotEmpty(reader.getSex())&&"男".equals(reader.getSex()) ) {
            reader.setSex("0");
        }else if(StringUtils.isNotEmpty(reader.getSex()))
        {
            reader.setSex("1");
        }
        if ( StringUtils.isNotEmpty(reader.getCertName()) ) {
            String certName = reader.getCertName().trim();
            String CertType;
            try {
                CertType = EnumCertType.parseName(certName).getValue();
            }catch (IllegalArgumentException e){
                reader.setErrorinfo("证件类型不符合规范!");
                return false;
            }

            if ( StringUtils.isNotEmpty(CertType) ) {
                reader.setCertType(CertType);
            }
        }

        if ( StringUtils.isEmpty(reader.getGroupName()) ) {
            reader.setErrorinfo("读者组织不能为空!");
            return false;
        }
       return ReaderImporter.checkLength(reader); //读者若干字段长度限制
    }

    private String checkParams(Reader reader) {
        String result = OPERATOR_SUCCESS;

        if ( StringUtils.isEmpty(reader.getCard())
                && StringUtils.isEmpty(reader.getName())
                && StringUtils.isEmpty(reader.getTerminationDate())
                && StringUtils.isEmpty(reader.getReaderTypeName())
                && StringUtils.isEmpty(reader.getGroupName()) ) {
            result = "空记录";
            return result;
        }

        if (StringUtils.isEmpty(reader.getCard()) || !Pattern.matches("[a-zA-Z0-9]*", reader.getCard())) {
            result = "读者证号不能为空，或者读者证号格式不对！";
            return result;
        }

        if (StringUtils.isEmpty(reader.getName()) || !Pattern.matches("[a-zA-Z\u4e00-\u9fa5]*", reader.getName())) {
            result = "读者名称不能为空，或者读者名称格式不对！";
            return result;
        }

        if (StringUtils.isNotEmpty(reader.getCertNum()) && !Pattern.matches("[a-zA-Z0-9]*", reader.getCertNum())) {
            result = "证件号码格式不对!";
            return result;
        }

        String phone = reader.getPhone();
        if ( StringUtils.isNotEmpty(phone) ) {
            try {
                BigDecimal bd = new BigDecimal(phone);
                reader.setPhone(bd.toPlainString());
            } catch (Exception e) {
                result = "手机格式不对!" + e.getMessage();
                return result;
            }

            if ( !Pattern.matches("(\\+\\d+)?1[34578]\\d{9}$", phone) ) {
                result = "手机号码格式不对!";
                return result;
            }
        }

        //终止日期逻辑判断
        String terminationDate = reader.getTerminationDate();
        if (StringUtils.isEmpty(terminationDate)) {
            result = "终止日期不能为空!";
            return result;
        } else {
            terminationDate = terminationDate.replace(".", "-").replace("/", "-");
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

            if ( !Pattern.matches("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}", terminationDate) ) {
                result = "终止日期不符合规范!";
                return result;
            }

            try {
                Date date = sf.parse(terminationDate);
                terminationDate = sf.format(date);
            } catch (ParseException e) {
                result = "终止日期不符合规范!!";
                return result;
            }

            Date sysDate = new Date();
            Date endDate;
            java.util.Calendar cSysDate = java.util.Calendar.getInstance();
            java.util.Calendar cEndDate = java.util.Calendar.getInstance();
            try {
                endDate = sf.parse(terminationDate);
                reader.setEndDate(endDate);

                cSysDate.setTime(sf.parse(sf.format(sysDate)));
                cEndDate.setTime(endDate);

                int re = cSysDate.compareTo(cEndDate);
                if (re > 0) {
                    result = "终止日期不能小于当天!";
                }

                cSysDate.add(Calendar.YEAR, 15);
                re = cSysDate.compareTo(cEndDate);
                if (re < 0) {
                    result = "终止日期不得大于15年后的日期!";
                }
            } catch (Exception e) {
                result = "终止日期不符合规范!!" + e.getMessage();
                return result;
            }

            return result;
        }
    }

    /**
     * 读者判断代码抽取
     */
    public String util(Reader reader, List<Reader> errorList) throws Exception {
        String result = OPERATOR_SUCCESS;
        if (StringUtils.isBlank(reader.getOrgId())) {
            reader.setOrgId(UserUtils.getOrgId());
        }
        if (reader.getCard() == null || reader.getCard().equals("") || !Pattern.matches("[a-zA-Z0-9]*", reader.getCard())) {
            result = "读者证号不能为空!或者读者证号格式不对";
        } else if (reader.getName() == null || reader.getName().equals("") || !Pattern.matches("[a-zA-Z\u4e00-\u9fa5]*", reader.getName())) {
            result = "读者名称不能为空!或者读者名称格式不对";
        } else if (reader.getEndDate() == null || reader.getEndDate().equals("")) {
            result = "终止日期不能为空!";
        }

        if (reader.getCertNum() != null) {
            if (!Pattern.matches("[a-zA-Z0-9]*", reader.getCertNum())) {
                result = "证件号码格式不对!";
            }
        }

        if (reader.getPhone() != null && !("".equals(reader.getPhone()))) {
            //手机判断
            String phone = reader.getPhone();
            try {
                BigDecimal bd = new BigDecimal(phone);
                reader.setPhone(bd.toPlainString());
            } catch (Exception e) {
                result = "手机格式不对!" + e.getMessage();
            }
            if (reader.getPhone() == null || !Pattern.matches("(\\+\\d+)?1[3458]\\d{9}$", reader.getPhone())) {
                result = "手机号码格式不对!";
            }
        }

        //终止日期逻辑判断
        if (reader.getTerminationDate() != null) {
            String terminationDate = reader.getTerminationDate().replace(".", "-").replace("/","-");
            if (terminationDate != null && !terminationDate.equals("") && terminationDate.matches("-?[0-9]+.*[0-9]*")) {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                Boolean isok = false;
                try {
                    double d = Double.parseDouble(terminationDate);
                    date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(d);
                    isok = true;
                } catch (Exception e) {
                    isok = false;
                }
                if (isok) terminationDate = sf.format(date);
            }
            reader.setTerminationDate(terminationDate);

            Date sysDate = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date endDate;
            java.util.Calendar cSysDate = java.util.Calendar.getInstance();
            java.util.Calendar cEndDate = java.util.Calendar.getInstance();
            try {
                endDate = format.parse(terminationDate);
                reader.setEndDate(endDate);

                cSysDate.setTime(format.parse(format.format(sysDate)));
                cEndDate.setTime(endDate);

                int re = cSysDate.compareTo(cEndDate);
                if (re > 0) {
                    result = "终止日期不能小于当天!";
                }

                cSysDate.add(Calendar.YEAR, 15);
                re = cSysDate.compareTo(cEndDate);
                if (re < 0) {
                    result = "终止日期不得大于15年后的日期!";
                }
            } catch (Exception e) {
                result = "终止日期不符合规范!" + e.getMessage();
            }
        }
        if(!SystemUtils.isTrueCardBarcode(reader.getCard(),reader.getId())){
            result = "读者证号重复或与图书条形码重复!";
        }

        if (!result.equals(OPERATOR_SUCCESS)) {
            if (reader.getCard() != null || reader.getName() != null || reader.getTerminationDate() != null ||
                    reader.getReaderTypeName() != null || reader.getGroupName() != null) {
                reader.setErrorinfo(result);
                errorList.add(reader);
            }
        }

        return result;
    }

    /**
     * 批量修改
     */
    @Transactional
    public void updateReader(Reader reader) {
        String[] ids = reader.getId().split(",");
        Group group;
        ReaderCard readerCard;
        for (String id : ids) {
            Reader reader1 = new Reader();
            reader1.setId(id);
            reader1.preUpdate();
            reader1.setOrgId(UserUtils.getOrgId());
            reader1.setReaderType(reader.getReaderType());
            group = groupService.get(reader.getGroupId());
            if (group.getStatus().equals("0")) {
                reader1.setGroupId(reader.getGroupId());
                readerDao.updateTypeGroup(reader1);
                readerCard = readerCardDao.findByReaderId(reader1);
                readerCard.setOrgId(UserUtils.getOrgId());
                readerCard.setEndDate(reader.getEndDate());
                readerCard.preUpdate();
                readerCardDao.updateEndDate(readerCard);
            }
        }
    }


    /**
     * 查询读者有没有流通记录
     *
     * @return billList
     */
    List<Bill> findCirculateLogByReader(Reader reader) {
        reader.setOrgId(UserUtils.getOrgId());
        return billDao.findCirculateLogByReader(reader);
    }

    @Transactional
    public Map deleteReader(String ids) {
        Map<String, Object> map = new HashMap<>();
        String[] idArr = ids.split(",");
        List<String> list = Arrays.asList(idArr);
        for (String string : list) {
            Reader tmpReader = new Reader();
            tmpReader.setId(string);
            tmpReader.setOrgId(UserUtils.getOrgId());
            Reader reader = get(tmpReader);
            List<Bill> billList = billDao.findCirculateLogByReader(reader);
            if (billList.size() > 0) {
                map.put("fail", "读者证存在记录信息，删除失败");
                //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return map;
            }
        }
        for (String string : list) {
            Reader tmpReader = new Reader();
            tmpReader.setId(string);
            tmpReader.setOrgId(UserUtils.getOrgId());
            Reader reader = get(tmpReader);
            ReaderCard readerCard = readerDao.findCardByReader(reader);
            if(readerCard.getDeposit()>0){
                map.put("fail","该读者证存在押金不可删除，请先退还押金");
                return map;
            }
            readerCard.setUpdateDate(new Date());
            readerCardDao.deleteReaderCard(readerCard);
            reader.setUpdateDate(new Date());
            readerDao.deleteReader(reader);
        }
        return null;
    }

    @Transactional(readOnly = false)
    public String changeStatusAction(ReaderCard readerCard) {
        if (StringUtils.isBlank(readerCard.getOrgId())) {
            readerCard.setOrgId(UserUtils.getOrgId());
        }
        Map<String, Object> map = new HashMap<>();
        try {
            if (readerCard.getStatusAction().equals("0")) {//挂失0
                //如果读者证不是有效，不允许挂失。
                readerCard = readerCardDao.findReaderCardByCard(readerCard);
                if (readerCard!=null && !readerCard.getStatus().equals("0")){
                    return "该读者证不是有效的，不能挂失。";
                }
                readerCard.setUpdateDate(new Date());
                readerCardDao.updateStatusToLoss(readerCard);
            } else if (readerCard.getStatusAction().equals("1")) {//换证1
                return this.changeReaderCard(readerCard);
            } else if (readerCard.getStatusAction().equals("2")) {//解挂2
                readerCard = readerCardDao.findReaderCardByCard(readerCard);
                Long endTime = readerCard.getEndDate().getTime();
                Long nowTime = new Date().getTime();
                if (nowTime - endTime > 0) {
                    return "该读者证已过期，解挂失败，请进行换证。";
                }
                readerCard.setUpdateDate(new Date());
                readerCardDao.updateStatusRemoveLoss(readerCard);
            } else if (readerCard.getStatusAction().equals("3")) {//续期3
                Long endTime = readerCard.getEndDate().getTime();
                Long nowTime = new Date().getTime();
                if (endTime < nowTime) {
                    return "输入续期时间小于当前时间,请重新输入";
                }
                ReaderCard rc = readerCardDao.findReaderCardByCard(readerCard);
                rc.setEndDate(readerCard.getEndDate());
                rc.setStatus("0");
                readerCardService.save(rc);
            }
        } catch (Exception e) {
            return "操作失败!";
        }
        return "";
    }
    //单独把换证的代码提出来，放上面一起太混乱---（没时间整理）
    public String changeReaderCard(ReaderCard readerCard) throws Exception{
        User user = UserUtils.getUser();
        readerCard.setOrgId(user.getOrgId());
        Reader reader = new Reader();
        BeanUtilsExt.copyProperties(reader, readerCard);
        ReaderCard rc = readerCardDao.findReaderCardByCard(readerCard);
        reader.setId(rc.getReaderId());
        //首先验证下所在组织是否失效了--直接用现成的代码
        Group group = readerDao.findGroupByReader(reader);
        if (group == null) {
            return "读者所在的组织已失效，操作失败";
        }
        if(!SystemUtils.isTrueCardBarcode(reader.getNewCard(),readerCard.getId())){
            return "读者证号重复 ,请重新输入";
        }
        //将旧读者证注销即可（也就是失效）
        reader.setId(rc.getReaderId());
        reader.setStatus("4");//变成旧证
        reader.setCard(rc.getCard());
        reader.setNewCard(readerCard.getNewCard());
        this.logOutReader(reader);//变成旧证
        rc.setCard(readerCard.getNewCard());
        rc.setStatus("0");
        rc.setStartDate(new Date());
        rc.setDelFlag("0");
        rc.setId(null);//就是要让他新增
        readerCardService.save(rc);
        //readerCardDao.updateInvalidLog(rc);//旧读者证的预约预借记录自动失效，不转移至新证下。
        //readerCardService.delete(readerCardDao.findReaderCardByCard(readerCard.getCard()).getId());//旧读者证将不在页面上显示

        /**************************************换证押金转移操作**************************************/
        Map<String, Object> cardMap = readerDao.findReaderInfoByCard(readerCard.getCard(), readerCard.getOrgId());
        if (null != cardMap) {
            Double amount = Double.valueOf(cardMap.get("deposit") + "");
            String readerId=cardMap.get("readerId") + "";
            String readerName=cardMap.get("readerName") + "";

            if (amount > 0) {
                /*************************************旧证转出*************************************/
                //修改旧读者证账户押金
                readerCardDao.updateDeposit(readerCard.getCard(), readerCard.getOrgId(), 0d, new Date());

                //添加记录
                DepositRecord oldDepositRecord = new DepositRecord();
                oldDepositRecord.setId(IdGen.uuid());
                oldDepositRecord.setReaderId(readerId);
                oldDepositRecord.setReaderName(readerName);
                oldDepositRecord.setReaderCard(readerCard.getCard());
                oldDepositRecord.setAmount(amount);
                oldDepositRecord.setOpType("2");
                oldDepositRecord.setOrgId(readerCard.getOrgId());
                oldDepositRecord.setCreateBy(user.getLoginName());
                oldDepositRecord.setCreateDate(new Date());
                depositRecordDao.insert(oldDepositRecord);

                /*************************************新证转入*************************************/
                //修改新读者证账户押金
                readerCardDao.updateDeposit(readerCard.getNewCard(), readerCard.getOrgId(), amount, new Date());

                //添加记录
                DepositRecord newDepositRecord = new DepositRecord();
                newDepositRecord.setId(IdGen.uuid());
                newDepositRecord.setReaderId(readerId);
                newDepositRecord.setReaderName(readerName);
                newDepositRecord.setReaderCard(readerCard.getNewCard());
                newDepositRecord.setOpType("3");
                newDepositRecord.setAmount(amount);
                newDepositRecord.setOrgId(readerCard.getOrgId());
                newDepositRecord.setCreateBy(user.getLoginName());
                newDepositRecord.setCreateDate(new Date());
                depositRecordDao.insert(newDepositRecord);
            }
        }

        return "";
    }

    /**
     * 换证的列表，做了分页
     * @param page
     * @param reader
     * @return
     */
    public Page findRenewalPage (Page<Reader> page, Reader reader){
        String orgId = UserUtils.getOrgId();
        reader.setOrgId(orgId);
        reader.setPage(page);
        page.setList(readerCardDao.findRenewalPage(reader));
        return page;
    }

    public List<Reader> exportRenewal(Reader reader) {
        return readerCardDao.findRenewalPage(reader);
    }

    /**
     * 根据读者证号查询相关读者信息(押金界面查询用)
     * @param card
     * @return
     */
    public Map<String, Object> findReaderInfoByCard(String card) {
        Map<String, Object> map = dao.findReaderInfoByCard(card, UserUtils.getOrgId());
        if (null != map) {
            map.put("status", EnumCertStatus.parse(map.get("status") + "").toName());
            map.put("readerType", EnumReaderType.parse(map.get("readerType") + "").toName());
            if(map.get("deposit")!=null){
                map.put("deposit",DoubleUtils.formatDoubleToString(Double.valueOf(map.get("deposit").toString())));
            }
        }
        return map;
    }


    public String printOneImage(List<Reader> readers,CardPrintConfig cardPrintConfig){
        String fileName = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME).replaceAll(":", "") + "_" + UUID.randomUUID().toString().replaceAll("-", "")+"."+ ImageIODemo.picType;
        String rootPath = Global.getUploadRootPath() ;
        String filePath = fileService.generateFilePath(fileName);
        ImageIODemo.writeImage(readers,cardPrintConfig,rootPath+ File.separator+filePath);
        return filePath;
    }

    public void resolveBarcodeImage(Reader reader) {
        if(StringUtils.isNotBlank(reader.getCardImg())) {
            return ;
        }
        String fileName = reader.getCard() + "_" + UUID.randomUUID().toString().replaceAll("-", "")+"."+ ImageIODemo.picType;
        String rootPath = Global.getUploadRootPath() ;
        String filePath = fileService.generateFilePath(fileName);
        BarcodeUtil.generateFile(reader.getCard(),rootPath+ File.separator+filePath);
        reader.setCardImg(filePath);
        dao.updateCardImg(reader);
    }

    public List<String> printReadCard(List<Reader> list){
        List<String> imageList = new ArrayList<>();
        String orgId = UserUtils.getOrgId();
        CardPrintConfig paramsCardPrintConfig = new CardPrintConfig();
        paramsCardPrintConfig.setOrgId(orgId);
        CardPrintConfig cardPrintConfig = cardPringConfigService.get(paramsCardPrintConfig);
        int count = 0 ;
        List<Reader> oneImageList = new ArrayList<Reader>();
        for (Reader reader1 : list) {
            count ++ ;
            oneImageList.add(reader1);
            reader1.setOrgId(orgId);
            resolveBarcodeImage(reader1);
            if(count%8==0 || (count==list.size() && oneImageList.size()>0 )) {
                imageList.add(printOneImage(oneImageList,cardPrintConfig));
                oneImageList = new ArrayList<Reader>();
            }
        }
        return imageList;
    }
}
