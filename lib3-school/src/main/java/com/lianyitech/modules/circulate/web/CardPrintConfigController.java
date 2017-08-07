package com.lianyitech.modules.circulate.web;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.utils.Global;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.common.web.ApiController;
import com.lianyitech.common.web.ResponseData;
import com.lianyitech.core.jxls.WriteBookLabelExportExcel;
import com.lianyitech.core.jxls.WriteToOut;
import com.lianyitech.core.utils.CsvUtils;
import com.lianyitech.core.utils.ImageIODemo;
import com.lianyitech.modules.circulate.entity.CardPrintConfig;
import com.lianyitech.modules.circulate.entity.CirculateDTO;
import com.lianyitech.modules.circulate.entity.Reader;
import com.lianyitech.modules.circulate.entity.ReaderCard;
import com.lianyitech.modules.circulate.service.CardPringConfigService;
import com.lianyitech.modules.circulate.service.CirculateService;
import com.lianyitech.modules.circulate.service.ReaderService;
import com.lianyitech.modules.sys.service.FileService;
import com.lianyitech.modules.sys.utils.UserUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.lianyitech.core.utils.CommonUtils.setCvsConfig;

/**
 * 读者证打印Controller
 *
 * @author zengzy
 * @version 2016-09-09
 */
@RestController
@RequestMapping(value = "/api/card/print")
public class CardPrintConfigController extends ApiController {

    @Autowired
    CardPringConfigService cardPringConfigService;


    @Autowired
    FileService fileService;
    /**
     * 保存设置
     * @param cardPrintConfig
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<ResponseData> save(CardPrintConfig cardPrintConfig) {
        cardPrintConfig.setOrgId(UserUtils.getOrgId());
        cardPringConfigService.save(cardPrintConfig);
        return new ResponseEntity<>(success("保存成功"), HttpStatus.OK);
    }


    /**
     * 获取打印设置
     * @param paramsCardPrintConfig
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> get(CardPrintConfig paramsCardPrintConfig) {
        CardPrintConfig cardPrintConfig = cardPringConfigService.get(paramsCardPrintConfig);
        return new ResponseEntity<>(success(cardPrintConfig), HttpStatus.OK);
    }

    /**
     * 预览设置（参数取自前端）
     * @param paramsCardPrintConfig
     * @return
     */
    @RequestMapping(value = "view", method = RequestMethod.GET)
    public ResponseEntity<ResponseData> view(CardPrintConfig paramsCardPrintConfig) {
        Reader reader = new Reader();
        reader.setCard("A0001B0001C0001D00001E00001");
        reader.setName("张三");
        reader.setCardImg(WriteBookLabelExportExcel.class.getClassLoader().getResource("").getPath() + "imgs/defaultCard.png");
        paramsCardPrintConfig.setSchoolBadge(Global.getUploadRootPath() + paramsCardPrintConfig.getSchoolBadge());
        String fileName = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME).replaceAll(":", "") + "_" + UUID.randomUUID().toString().replaceAll("-", "")+"."+ ImageIODemo.picType;
        String rootPath = Global.getUploadRootPath() ;
        String filePath = fileService.generateFilePath(fileName);
        ImageIODemo.writeOneImage(paramsCardPrintConfig,reader,rootPath+ File.separator+filePath);
        return new ResponseEntity<>(success(filePath), HttpStatus.OK);
    }
}