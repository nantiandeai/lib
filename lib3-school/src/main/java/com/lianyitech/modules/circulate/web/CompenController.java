package com.lianyitech.modules.circulate.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.utils.DateUtils;
import com.lianyitech.common.utils.DoubleUtils;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.core.utils.CsvUtils;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.catalog.service.CopyService;
import com.lianyitech.modules.circulate.dao.BillDao;
import com.lianyitech.modules.circulate.entity.Bill;
import com.lianyitech.modules.circulate.entity.CirculateDTO;
import com.lianyitech.modules.circulate.entity.CompenRecord;
import com.lianyitech.modules.circulate.entity.Rule;
import com.lianyitech.modules.circulate.service.BillService;
import com.lianyitech.modules.circulate.service.CompenService;
import com.lianyitech.modules.circulate.service.RuleService;
import com.lianyitech.modules.sys.service.FileService;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.lianyitech.core.utils.CommonUtils.parseDouble;
import static com.lianyitech.core.utils.CommonUtils.setCvsConfig;

/**
 * 赔付管理Controller
 *
 * @author
 * @version
 */
@RestController
@RequestMapping(value = "/api/compen")
public class CompenController extends ApiController {

    @Autowired
    CompenService compenService;

    @Autowired
    FileService fileService;

    @Autowired
    RuleService ruleService;

    @Autowired
    CopyService copyService;

    @Autowired
    BillDao billDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> list(CompenRecord compenRecord, HttpServletRequest request, HttpServletResponse response) {
        compenRecord.setOrgId(UserUtils.getOrgId());
        Page<CompenRecord> page = compenService.findPage(new Page<>(request, response), compenRecord);
        return new ResponseEntity<>(success(page), HttpStatus.OK);
    }

    /**
     * 赔付导出
     * @param response response
     */
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void export(CompenRecord compenRecord, HttpServletResponse response) throws IOException {
        compenRecord.setOrgId(UserUtils.getOrgId());
        List<CompenRecord> detailList = compenService.findList(compenRecord);
        setCvsConfig(response,"赔罚清单");
        List<String> resultList = new ArrayList<>();
        resultList.add("读者证号,读者姓名,读者组织,原条形码,题名,操作行为,赔偿类型,罚款金额,新条形码,操作人,操作日期");
        for(int i = 0 ; i < detailList.size() ; i ++) {
            resultList.add(CsvUtils.parserCompen(detailList.get(i)));
        }
        fileService.writeCsvFile(resultList,fileService.generateFilePath(),response);
    }

    /**
     * 赔书导出
     * @param response response
     */
    @RequestMapping(value = "/exportCompenBook", method = RequestMethod.GET)
    public void export2(CompenRecord compenRecord, HttpServletResponse response) throws IOException {
        compenRecord.setOrgId(UserUtils.getOrgId());
        List<CompenRecord> detailList = compenService.findList(compenRecord);
        setCvsConfig(response,"赔书清单");
        List<String> resultList = new ArrayList<>();
        resultList.add("读者证号,读者姓名,读者组织,原条形码,题名,操作行为,赔偿类型,新条形码,备注,操作人,操作日期");
        for(int i = 0 ; i < detailList.size() ; i ++) {
            resultList.add(CsvUtils.parserCompenBook(detailList.get(i)));
        }
        fileService.writeCsvFile(resultList,fileService.generateFilePath(),response);
    }


    /**
     *  查询赔付明细
     * @param compenRecord
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "getCompenDetail", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> getCompenDetail(CompenRecord compenRecord, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtils.isBlank(compenRecord.getBarcode()) || StringUtils.isBlank(compenRecord.getOpType())) {
            return new ResponseEntity<>(fail("必填参数不能为空"), HttpStatus.BAD_REQUEST);
        }
        Map result = new HashedMap();
        if(StringUtils.isBlank(compenRecord.getReaderId()) || StringUtils.isBlank(compenRecord.getReaderCard())){//没有传读者ID或者读者证，需要去查询一把
            CirculateDTO dto = new CirculateDTO();
            dto.setOrgId(UserUtils.getOrgId());
            dto.setBarcode(compenRecord.getBarcode());
            dto.setStatus("'01','71'");
            Bill bill = billDao.findLastByCon(dto);
            if(bill==null) {
                return new ResponseEntity<>(fail("没有找到对应的单据"), HttpStatus.BAD_REQUEST);
            }
            compenRecord.setReaderId(bill.getReaderId());
            compenRecord.setReaderCard(bill.getCard());
            compenRecord.setBillId(bill.getId());
            result.put("name",bill.getReaderName());
            result.put("borrowDate", DateUtils.formatDateTime(bill.getCreateDate()));
            result.put("shouldReturnDate",DateUtils.formatDateTime(bill.getShouldReturnDate()));
            result.put("groupName",bill.getGroupName());
        }

        String orgId = UserUtils.getOrgId();
        Rule rule = ruleService.findRuleByReader(compenRecord.getReaderId());
        Copy paramsCopy = new Copy();
        paramsCopy.setOrgId(orgId);
        paramsCopy.setBarcode(compenRecord.getBarcode());
        Copy copy = copyService.findByBarCode(paramsCopy);
        String status = "0" ;
        if(copy!=null) {
            if(compenRecord.getOpType().equals("0")){//丢失
                status = rule.getLossFine();
            } else if (compenRecord.getOpType().equals("1")){//污损
                status = rule.getStainFine();
            } else if (compenRecord.getOpType().equals("2")) {//超期
                status = rule.getExceedFine();
            }
            if(status.equals("1")) {
                Map map = compenService.caculateMoney(compenRecord,rule,copy);
                result.putAll(map);
                if(map.get("money")!=null && Double.valueOf(map.get("money").toString())<=0D) {
                    status  =  "0" ;
                }
            }
        }
        if(result.get("money")!=null) {
            result.put("money", parseDouble(result.get("money").toString()));
        }
        if(result.get("price")!=null) {
            result.put("price", parseDouble(result.get("price").toString()));
        }
        if(result.get("exceedFineDayAmount")!=null) {
            result.put("exceedFineDayAmount", parseDouble(result.get("exceedFineDayAmount").toString()));
        }
        if(result.get("exceedFineMaxAmount")!=null) {
            result.put("exceedFineMaxAmount", parseDouble(result.get("exceedFineMaxAmount").toString()));
        }
        result.put("status",status);
        return new ResponseEntity<>(success(result), HttpStatus.OK);
    }

    /**
     * 赔付
     * @param compenRecord
     * @return
     */
    @RequestMapping(value = "compen", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> compen(CompenRecord compenRecord) {
        if(StringUtils.isBlank(compenRecord.getOpType()) || StringUtils.isBlank(compenRecord.getCompenType())
                || compenRecord.getAmount()==null || StringUtils.isBlank(compenRecord.getReaderCard()) || StringUtils.isBlank(compenRecord.getReaderId())  ) {
            return new ResponseEntity<>(fail("必填参数不能为空"),HttpStatus.BAD_REQUEST);
        }
        String orgId = UserUtils.getOrgId();
        compenRecord.setOrgId(orgId);
        compenService.save(compenRecord);
        return new ResponseEntity<>(success("操作成功"), HttpStatus.OK);
    }

    /**
     * 校验条形码是否存在
     * @param compenRecord
     * @return
     */
    @RequestMapping(value = "checkBarcode", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> checkBarcode(CompenRecord compenRecord) {
        String orgId = UserUtils.getOrgId();
        Copy paramsCopy = new Copy();
        paramsCopy.setOrgId(orgId);
        paramsCopy.setBarcode(compenRecord.getBarcode());
        paramsCopy.setStatus("'0','1','5'");
        Copy copy = copyService.findByBarCode(paramsCopy);
        if(copy==null) {
            return new ResponseEntity<>(fail("条形码不存在"),HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (copy.getStatus().equals("1")) {
            return new ResponseEntity<>(fail("该图书已借出"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        CompenRecord paramCompenRecord = new CompenRecord();
        paramCompenRecord.setOrgId(orgId);
        paramCompenRecord.setNewBarcode(compenRecord.getBarcode());
        paramCompenRecord.setId(compenRecord.getId());
        List<CompenRecord> compenRecords = compenService.findList(paramCompenRecord);
        if(compenRecords!=null && compenRecords.size()>0) {
            return new ResponseEntity<>(fail("该条形码已有赔书记录"),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(success("条形码存在"), HttpStatus.OK);
    }


    /**
     * 查询赔付记录，只传入条形码
     * @param compenRecord
     * @return
     */
    @RequestMapping(value = "getCompenByBarcode", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> getCompen(CompenRecord compenRecord) {
        if(StringUtils.isBlank(compenRecord.getBarcode())) {
            return new ResponseEntity<>(fail("条形码不能为空"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        compenRecord.setOrgId(UserUtils.getOrgId());
        //compenRecord.setCompenType("1");
        List<CompenRecord> compenRecords = compenService.findList(compenRecord);
        if(compenRecords!=null && compenRecords.size()>0 && compenRecords.get(0).getCompenType().equals("1")) {
            return new ResponseEntity<>(success(compenRecords.get(0)),HttpStatus.OK);
        }
        return new ResponseEntity<>(success(""),HttpStatus.OK);
    }

    /**
     * 保存
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> save(CompenRecord compenRecord) {
        String orgId = UserUtils.getOrgId();
        compenRecord.setOrgId(orgId);
        compenService.save(compenRecord);
        return new ResponseEntity<>(success("操作成功"), HttpStatus.OK);
    }
}