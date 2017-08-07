package com.lianyitech.core.jxls;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.TransformerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于JXLS的Excel生成器
 */
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class ExcelGenerator {

    private String templatePath;
    private OutputStream out;
    private List<?> data;
    private Object et;

    public void generate() throws IOException, InvalidFormatException {
        try(InputStream is = this.getClass().getClassLoader().getResourceAsStream(templatePath)) {
            Workbook workbook = WorkbookFactory.create(is);
            Transformer transformer = PoiTransformer.createSxssfTransformer(workbook, 5, false);
            AreaBuilder areaBuilder = new XlsCommentAreaBuilder(transformer);
            List<Area> xlsAreaList = areaBuilder.build();
            Area xlsArea = xlsAreaList.get(0);
            Context context = new Context();
            context.putVar("detailList", data);
            xlsArea.applyAt(new CellRef("结果!A1"), context);
            context.getConfig().setIsFormulaProcessingRequired(false); // with SXSSF you cannot use normal formula processing
            workbook.setForceFormulaRecalculation(true);
            workbook.setActiveSheet(0);
            workbook.removeSheetAt(1);
            ((PoiTransformer) transformer).getWorkbook().write(out);
        }
    }
}