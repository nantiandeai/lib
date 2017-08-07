package com.lianyitech.modules.circulate.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.core.utils.CsvUtils;
import com.lianyitech.modules.circulate.entity.DepositRecord;
import com.lianyitech.modules.circulate.service.DepositRecordService;
import com.lianyitech.modules.sys.service.FileService;
import com.lianyitech.modules.sys.utils.UserUtils;
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

import static com.lianyitech.core.utils.CommonUtils.setCvsConfig;

/**
 * 押金管理Controller
 *
 * @author zengzy
 * @version 2017-07-13
 */
@RestController
@RequestMapping(value = "/api/circulate/deposit")
public class DepositRecordController extends ApiController {

    @Autowired
    private DepositRecordService depositRecordService;
    @Autowired
    private FileService fileService;

    /**
     * 押金记录
     *
     * @param depositRecord
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> list(DepositRecord depositRecord, HttpServletRequest request, HttpServletResponse response) {
        Page<DepositRecord> page = depositRecordService.findPage(new Page<>(request, response), depositRecord);
        return new ResponseEntity<>(success(page), HttpStatus.OK);
    }


    /**
     * 交、退押金操作
     *
     * @param depositRecord
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> save(DepositRecord depositRecord) {
        try {
            if (StringUtils.isEmpty(depositRecord.getOpType())) {
                return new ResponseEntity<>(fail("请选择操作类型"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (StringUtils.isEmpty(depositRecord.getReaderCard())) {
                return new ResponseEntity<>(fail("请输入读者证号"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (depositRecord.getOpType().equals("0") && (null == depositRecord.getAmount() || depositRecord.getAmount() == 0d)) {
                return new ResponseEntity<>(fail("请输入金额"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            String msg = depositRecordService.saveDeposit(depositRecord);
            if (StringUtils.isNotEmpty(msg)) {
                return new ResponseEntity<>(fail(msg), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(success("操作成功"), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("操作失败", e);
            return new ResponseEntity<>(fail("操作失败"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * 押金记录导出
     * @param depositRecord
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void export(DepositRecord depositRecord, HttpServletResponse response) throws IOException {
        depositRecord.setOrgId(UserUtils.getOrgId());
        List<DepositRecord> list = depositRecordService.findList(depositRecord);
        setCvsConfig(response,"押金记录");
        List<String> resultList = new ArrayList<>();
        resultList.add("序号,读者证号,读者姓名,金额,操作类型,操作人,操作时间");
        for (int i=0;i<list.size();i++) {
            resultList.add(CsvUtils.parseDepositRecord(list.get(i),i));
        }
        fileService.writeCsvFile(resultList,fileService.generateFilePath(),response);
    }


}