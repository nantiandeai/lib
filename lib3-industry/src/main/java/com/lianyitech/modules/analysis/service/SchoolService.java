package com.lianyitech.modules.analysis.service;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.analysis.dao.SchoolDao;
import com.lianyitech.modules.analysis.entity.School;
import com.lianyitech.modules.common.ExcelTransformer;
import com.lianyitech.modules.common.ExportExcel;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.TransformerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jordan jiang on 2017/4/17.
 */
@Service
public class SchoolService extends CrudService<SchoolDao,School> {
    @Autowired
    private SchoolDao schoolDao;
    /**
     * 无录入信息的学校列表
     */
    public Page<School> listNoInputSchool(Page<School> page, School school) {
        school.setPage(page);
        page.setList(schoolDao.listNoInputSchool(school));
        return page;
    }

    public void exportNoInputSchool(School school, HttpServletResponse response){
        ExportExcel exportExcel = new ExportExcel();
        Map<String, Object> functionMap = new HashMap<>();
        Context context = new Context();
        InputStream is = null;
        OutputStream os = null;
        try {
            exportExcel.preProcessing(response, "无录入信息的学校列表");

            is = this.getClass().getClassLoader().getResourceAsStream("template" + File.separator + "无录入信息的学校列表.xls");
            os = response.getOutputStream();

            Transformer transformer = TransformerFactory.createTransformer(is, os);
            JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();

            functionMap.put("et", new ExcelTransformer());
            evaluator.getJexlEngine().setFunctions(functionMap);

            List<School> detailList = schoolDao.listNoInputSchool(school);
            context.putVar("detailList", detailList);

            AreaBuilder areaBuilder = new XlsCommentAreaBuilder(transformer);
            List<Area> xlsAreaList = areaBuilder.build();
            Area xlsArea = xlsAreaList.get(0);
            xlsArea.applyAt(new CellRef("结果!A1"), context);
            transformer.write();
        }catch (UnsupportedEncodingException e){
            logger.error("操作失败",e);
        }catch (IOException e){
            logger.error("操作失败",e);
        }finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                logger.error("操作失败",e);
            }
        }
    }
}
