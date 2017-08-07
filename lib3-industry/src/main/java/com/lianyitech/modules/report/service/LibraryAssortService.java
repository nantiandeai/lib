package com.lianyitech.modules.report.service;

import com.lianyitech.common.service.CrudService;
import com.lianyitech.common.utils.DoubleUtils;
import com.lianyitech.modules.common.ReportCommon;
import com.lianyitech.modules.report.dao.LibraryAssortDao;
import com.lianyitech.modules.report.entity.LibraryAssort;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lianyitech.common.utils.DoubleUtils.formatDouble;
import static java.math.RoundingMode.HALF_UP;

/**
 * 藏书分类统计
 * Created by luzhihuai on 2016/11/10.
 */
@Service

public class LibraryAssortService extends CrudService<LibraryAssortDao, LibraryAssort> {
    @Autowired
    private LibraryAssortDao libraryAssortDao;
    @Autowired
    private AreaService areaService;
    /**
     * 22大类查询
     */
    public Map<String, Object> libraryStatis(LibraryAssort libraryAssort) {

        libraryAssort.setConditionQuery(3);

        Map<String, Object> result = new HashMap<>();
        List<LibraryAssort> list;

        BigDecimal ba = new BigDecimal(0.00);
        BigDecimal bn = new BigDecimal(0);
        BigDecimal bs = new BigDecimal(0);

        if ( StringUtils.isEmpty(libraryAssort.getProvince()) ){
            libraryAssort = (LibraryAssort)areaService.getArea(libraryAssort);
        }

        if (libraryAssort!=null){
            list = libraryAssortDao.libraryStatis(libraryAssort);
        }else{
            list = new ArrayList<>();
        }

        for (LibraryAssort la : list) {
            ba = ba.add(BigDecimal.valueOf(new BigDecimal(la.getBooksAmount()).doubleValue()));
            bn = bn.add(new BigDecimal(la.getBookNum()));
            bs = bs.add(new BigDecimal(la.getBookSpecies()));
        }

        for (LibraryAssort la : list) {
            if (BigDecimal.ZERO.compareTo(bs)==0) {
                la.setBookSpeciesRate("0.00");
            } else {
                la.setBookSpeciesRate(new BigDecimal(la.getBookSpecies() * 100).divide(bs, 2, HALF_UP).toString());
            }
            if (BigDecimal.ZERO.compareTo(bn)==0) {
                la.setBookNumRate("0.00");
            } else {
                la.setBookNumRate(new BigDecimal(la.getBookNum() * 100).divide(bn, 2, HALF_UP).toString());
            }
            if (BigDecimal.ZERO.compareTo(ba)==0) {
                la.setBooksAmountRate("0.00");
            } else {
                la.setBooksAmountRate(new BigDecimal(la.getBooksAmount() * 100).divide(ba, 2, HALF_UP).toString());
            }
        }
        result.put("list", list);
        result.put("totalBookSpecies", bs.toString());
        result.put("totalBookNum", bn.toString());
        result.put("totalBooksAmounts", ba);
        if (BigDecimal.ZERO.compareTo(bs)==0) {
            result.put("totalBookSpeciesRate", "0.00%");
        } else {
            result.put("totalBookSpeciesRate", "100%");
        }
        if (BigDecimal.ZERO.compareTo(bn)==0) {
            result.put("totalBookNumRate", "0.00%");
        } else {
            result.put("totalBookNumRate", "100%");
        }
        if (BigDecimal.ZERO.compareTo(ba)==0) {
            result.put("totalBooksAmountRate", "0.00%");
        } else {
            result.put("totalBooksAmountRate", "100%");
        }
        return result;
    }

    /**
     * 22大类查询导出
     */
    public List<LibraryAssort> exportBookCollection(LibraryAssort libraryAssort) {
        List<LibraryAssort> list;
        LibraryAssort total = new LibraryAssort();

        if ( StringUtils.isEmpty(libraryAssort.getProvince()) ){
            libraryAssort = (LibraryAssort)areaService.getArea(libraryAssort);
        }

        if (libraryAssort!=null){
            list = libraryAssortDao.libraryStatis(libraryAssort);
        }else{
            list = new ArrayList<>();
        }

        try {
            ReportCommon.totalcommon(list, new String[]{"bookSpecies", "bookNum", "booksAmount"}, total);
        } catch (Exception e) {
            total = null;
        }
        BigDecimal ba = new BigDecimal(0);
        BigDecimal bn = new BigDecimal(0);
        BigDecimal bs = new BigDecimal(0);

        for (LibraryAssort la : list) {
            ba = ba.add(new BigDecimal(la.getBooksAmount()));
            bn = bn.add(new BigDecimal(la.getBookNum()));
            bs = bs.add(new BigDecimal(la.getBookSpecies()));
        }

        for (LibraryAssort la : list) {
            if (BigDecimal.ZERO.compareTo(bs)==0) {
                la.setBookSpeciesRate("0.00");
            } else {
                la.setBookSpeciesRate(new BigDecimal(la.getBookSpecies() * 100).divide(bs, 2, HALF_UP).toString());
            }
            if (BigDecimal.ZERO.compareTo(bn)==0) {
                la.setBookNumRate("0.00");
            } else la.setBookNumRate(new BigDecimal(la.getBookNum() * 100).divide(bn, 2, HALF_UP).toString());
            if (BigDecimal.ZERO.compareTo(ba)==0) {
                la.setBooksAmountRate("0.00");
            } else la.setBooksAmountRate(new BigDecimal(la.getBooksAmount() * 100).divide(ba, 2, HALF_UP).toString());
        }
        total.setSmallClassesCode("合计");
        if (BigDecimal.ZERO.compareTo(ba)==0) {
            total.setBooksAmountRate("0.00");
        } else {
            total.setBooksAmountRate("100");
        }
        if (BigDecimal.ZERO.compareTo(bs)==0) {
            total.setBookSpeciesRate("0.00");
        } else {
            total.setBookSpeciesRate("100");
        }
        if (BigDecimal.ZERO.compareTo(bn)==0) {
            total.setBookNumRate("0.00");
        } else {
            total.setBookNumRate("100");
        }
        list.add(total);
        return list;
    }

    /**
     * 五大类查询
     */
    public Map<String, Object> fiveClassStatis(LibraryAssort libraryAssort) {

        libraryAssort.setConditionQuery(3);

        Map<String, Object> result = new HashMap<>();
        List<LibraryAssort> list;

        if ( StringUtils.isEmpty(libraryAssort.getProvince()) ){
            libraryAssort = (LibraryAssort)areaService.getArea(libraryAssort);
        }

        if (libraryAssort!=null){
            list = libraryAssortDao.fiveClassStatis(libraryAssort);
        }else{
            list = new ArrayList<>();
        }

        BigDecimal ba = new BigDecimal(0.00);
        BigDecimal bn = new BigDecimal(0);
        BigDecimal bs = new BigDecimal(0);

        for (LibraryAssort la : list) {
            ba = ba.add(BigDecimal.valueOf(new BigDecimal(la.getBooksAmount()).doubleValue()));
            la.setBooksAmount(formatDouble(new BigDecimal(la.getBooksAmount())).doubleValue());
            bn = bn.add(new BigDecimal(la.getBookNum()));
            bs = bs.add(new BigDecimal(la.getBookSpecies()));
        }
        ba = formatDouble(ba);
        for (LibraryAssort la : list) {
            if (BigDecimal.ZERO.compareTo(bs)==0) {
                la.setBookSpeciesRate("0.00");
            } else {
                la.setBookSpeciesRate(new BigDecimal(la.getBookSpecies() * 100).divide(bs, 2, HALF_UP).toString());
            }
            if (BigDecimal.ZERO.compareTo(bn)==0) {
                la.setBookNumRate("0.00");
            } else {
                la.setBookNumRate(new BigDecimal(la.getBookNum() * 100).divide(bn, 2, HALF_UP).toString());
            }
            if (BigDecimal.ZERO.compareTo(ba)==0) {
                la.setBooksAmountRate("0.00");
            } else {
                la.setBooksAmountRate(new BigDecimal(la.getBooksAmount() * 100).divide(ba, 2, HALF_UP).toString());
            }
        }


        result.put("list", list);
        result.put("totalBookSpecies", (bs.toString()));
        result.put("totalBookNum", bn.toString());
        result.put("totalBooksAmounts", ba.toString());
        if (BigDecimal.ZERO.compareTo(bs)==0) {
            result.put("totalBookSpeciesRate", "0.00%");
        } else {
            result.put("totalBookSpeciesRate", "100%");
        }
        if (BigDecimal.ZERO.compareTo(bn)==0) {
            result.put("totalBookNumRate", "0.00%");
        } else {
            result.put("totalBookNumRate", "100%");
        }
        if (BigDecimal.ZERO.compareTo(ba)==0) {
            result.put("totalBooksAmountRate", "0.00%");
        } else {
            result.put("totalBooksAmountRate", "100%");
        }
        return result;
    }

    /**
     * 五大类导出
     */
    public List<LibraryAssort> exportFiveClass(LibraryAssort libraryAssort) {
        List<LibraryAssort> listex;
        LibraryAssort total = new LibraryAssort();

        if ( StringUtils.isEmpty(libraryAssort.getProvince()) ){
            libraryAssort = (LibraryAssort)areaService.getArea(libraryAssort);
        }

        if (libraryAssort!=null){
            listex = libraryAssortDao.fiveClassStatis(libraryAssort);
        }else{
            listex = new ArrayList<>();
        }

        try {
            ReportCommon.totalcommon(listex, new String[]{"bookNum", "bookSpecies", "booksAmount"}, total);
        } catch (Exception e) {
            logger.error("操作失败",e);
            total = null;
        }

        BigDecimal ba = new BigDecimal(0);
        BigDecimal bn = new BigDecimal(0);
        BigDecimal bs = new BigDecimal(0);

        for (LibraryAssort la : listex) {
            ba = ba.add(new BigDecimal(la.getBooksAmount()));
            bn = bn.add(new BigDecimal(la.getBookNum()));
            bs = bs.add(new BigDecimal(la.getBookSpecies()));
        }
        for (LibraryAssort la : listex) {
            if (BigDecimal.ZERO.compareTo(bs)==0) {
                la.setBookSpeciesRate("0.00");
            } else la.setBookSpeciesRate(new BigDecimal(la.getBookSpecies() * 100).divide(bs, 2, HALF_UP).toString());
            if (BigDecimal.ZERO.compareTo(bn)==0) {
                la.setBookNumRate("0.00");
            } else la.setBookNumRate(new BigDecimal(la.getBookNum() * 100).divide(bn, 2, HALF_UP).toString());
            if (BigDecimal.ZERO.compareTo(ba)==0) {
                la.setBooksAmountRate("0.00");
            } else la.setBooksAmountRate(new BigDecimal(la.getBooksAmount() * 100).divide(ba, 2, HALF_UP).toString());
        }
        total.setMajorClassesCode("合计");
        if (BigDecimal.ZERO.compareTo(ba)==0) {
            total.setBooksAmountRate("0.00");
        } else {
            total.setBooksAmountRate("100");
        }
        if (BigDecimal.ZERO.compareTo(bs)==0) {
            total.setBookSpeciesRate("0.00");
        } else {
            total.setBookSpeciesRate("100");
        }
        if (BigDecimal.ZERO.compareTo(bn)==0) {
            total.setBookNumRate("0.00");
        } else {
            total.setBookNumRate("100");
        }
        listex.add(total);
        return listex;
    }

}
