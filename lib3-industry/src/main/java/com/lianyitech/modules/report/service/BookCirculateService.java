package com.lianyitech.modules.report.service;

import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.common.Compare;
import com.lianyitech.modules.common.ReportCommon;
import com.lianyitech.modules.report.dao.BookCirculteDao;
import com.lianyitech.modules.report.entity.BookCirculte;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * 藏书流通有关统计报表（生均借阅统计和读者借阅率统计、图书流通率统计）
 * Created by luzhihuai on 2016/10/29.
 */
@Service
public class BookCirculateService extends CrudService<BookCirculteDao,BookCirculte> {
    @Autowired
    private BookCirculteDao bookCirculteDao;
    @Autowired
    private AreaService areaService;
    /**
     *生均借阅统计
     */
    public Map<String, Object> listStuStatistics(BookCirculte bookCirculte) throws Exception{
        Map<String, Object> result = new HashMap<>();
        BookCirculte totalBookCirculte = new BookCirculte();
        List<BookCirculte> resultList;

        if ( StringUtils.isEmpty(bookCirculte.getProvince())){
            bookCirculte = (BookCirculte)areaService.getArea(bookCirculte);
        }

        if (bookCirculte!=null) {
            resultList = stuStatistics(bookCirculte);
            ReportCommon.totalcommon(resultList, new String[]{"studentNum", "stuBorrowNum"}, totalBookCirculte);
        }else{
            resultList = null;
        }

        result.put("list", resultList);
        result.put("total", totalBookCirculte);
        return result;
    }

    public List<BookCirculte> stuStatistics(BookCirculte bookCirculte){

        List<BookCirculte> listSchool;
        List<BookCirculte> listSchoolArea;
        List<BookCirculte> resultList;
        if (bookCirculte.getConditionQuery()==3
                && ( StringUtils.isNotEmpty(bookCirculte.getCounty())||StringUtils.isNotEmpty(bookCirculte.getUnitName()) )
                ) {//如果查询的全部数据（包括学区），先是查询学区外的学校，然后再查询学区。
            listSchool = bookCirculteDao.listStuStatistics(bookCirculte);
            bookCirculte.setConditionQuery(2);
            listSchoolArea = bookCirculteDao.listStuStatistics(bookCirculte);
            if (listSchoolArea!=null && listSchoolArea.size()>0){
                listSchoolArea.addAll(listSchool);
                resultList = listSchoolArea;
            }else{
                resultList = listSchool;
            }
        }else{
            resultList = bookCirculteDao.listStuStatistics(bookCirculte);
        }

        //这里默认按照生均借阅册数倒序排列
        Collections.sort(resultList, Compare.stuAverRate_Desc);
        return resultList;
    }

    public List<BookCirculte> exportNewBooksReports(BookCirculte bookCirculte){
        List<BookCirculte> resultList = stuStatistics(bookCirculte);

        BookCirculte totalBookCirculte = new BookCirculte();
        totalBookCirculte.setUnitCode("合计");
        try {
            ReportCommon.totalcommon(resultList, new String[]{"studentNum", "stuBorrowNum"}, totalBookCirculte);
        } catch (Exception e) {
            totalBookCirculte = null;
        }

        resultList.add(totalBookCirculte);

        return resultList;
    }

    /**
     * 读者借阅率统计
     */
    public Map<String,Object> readStatistics(BookCirculte bookCirculte) throws Exception{
        Map<String, Object> result = new HashMap<>();
        List<BookCirculte> resultList;
        BookCirculte totalBookCirculte1 = new BookCirculte();

        if ( StringUtils.isEmpty(bookCirculte.getProvince())){
            bookCirculte = (BookCirculte)areaService.getArea(bookCirculte);
        }

        if (bookCirculte!=null) {
            resultList = readStatisticsBasic(bookCirculte);
            ReportCommon.totalcommon(resultList, new String[]{"studentNum", "stuBorrowNum"}, totalBookCirculte1);
        }else{
            resultList = null;
        }

        result.put("list", resultList);
        result.put("total", totalBookCirculte1);
        return result;
    }

    public List<BookCirculte> readStatisticsBasic(BookCirculte bookCirculte){

        List<BookCirculte> listSchool;
        List<BookCirculte> listSchoolArea;
        List<BookCirculte> resultList;
        if (bookCirculte.getConditionQuery()==3
                && ( StringUtils.isNotEmpty(bookCirculte.getCounty())||StringUtils.isNotEmpty(bookCirculte.getUnitName()) )
                ) {//如果查询的全部数据（包括学区），先是查询学区外的学校，然后再查询学区。
            listSchool = bookCirculteDao.readStatistics(bookCirculte);
            bookCirculte.setConditionQuery(2);
            listSchoolArea = bookCirculteDao.readStatistics(bookCirculte);
            if (listSchoolArea!=null && listSchoolArea.size()>0){
                listSchoolArea.addAll(listSchool);
                resultList = listSchoolArea;
            }else{
                resultList = listSchool;
            }
        }else{
            resultList = bookCirculteDao.readStatistics(bookCirculte);
        }

        //这里默认按照借阅率倒序排列倒序排列
        Collections.sort(resultList, Compare.averRate_Desc);
        return resultList;
    }

    /**
     * 读者借阅率统计导出
     */
    public List<BookCirculte> exportReadStaReports(BookCirculte bookCirculte){
        List<BookCirculte> resultList = readStatisticsBasic(bookCirculte);

        BookCirculte totalBookCirculte1 = new BookCirculte();
        totalBookCirculte1.setUnitCode("合计");
        try {
            ReportCommon.totalcommon(resultList, new String[]{"studentNum", "stuBorrowNum"}, totalBookCirculte1);
        } catch (Exception e) {
            totalBookCirculte1 = null;
        }
        resultList.add(totalBookCirculte1);

        return resultList;
    }

    /**
     * 图书流通率统计
     */
    public Map<String,Object> bookCirStatistic(BookCirculte bookCirculte) throws Exception{
        Map<String, Object> result = new HashMap<>();
        List<BookCirculte> resultList;
        BookCirculte totalBookCirculte = new BookCirculte();

        if ( StringUtils.isEmpty(bookCirculte.getProvince())){
            bookCirculte = (BookCirculte)areaService.getArea(bookCirculte);
        }

        if (bookCirculte!=null) {
            resultList = bookCirStatisticBasic(bookCirculte);
            ReportCommon.totalcommon(resultList, new String[]{"bookNum", "stuBorrowNum"}, totalBookCirculte);
        }else {
            resultList = null;
        }

        result.put("list", resultList);
        result.put("total", totalBookCirculte);
        return result;
    }

    public List<BookCirculte> bookCirStatisticBasic(BookCirculte bookCirculte){

        List<BookCirculte> listSchool;
        List<BookCirculte> listSchoolArea;
        List<BookCirculte> resultList;
        if (bookCirculte.getConditionQuery()==3
                && ( StringUtils.isNotEmpty(bookCirculte.getCounty())||StringUtils.isNotEmpty(bookCirculte.getUnitName()) )
                ) {//如果查询的全部数据（包括学区），先是查询学区外的学校，然后再查询学区。
            listSchool = bookCirculteDao.bookCirStatistic(bookCirculte);
            bookCirculte.setConditionQuery(2);
            listSchoolArea = bookCirculteDao.bookCirStatistic(bookCirculte);
            if (listSchoolArea!=null && listSchoolArea.size()>0){
                listSchoolArea.addAll(listSchool);
                resultList = listSchoolArea;
            }else{
                resultList = listSchool;
            }
        }else{
            resultList = bookCirculteDao.bookCirStatistic(bookCirculte);
        }

        //这里默认按照图书流通率倒序排列
        Collections.sort(resultList, Compare.circulationRate_Desc);
        return resultList;
    }

    /**
     * 图书流通率统计导出
     */
    public List<BookCirculte> exportBookCirReports(BookCirculte bookCirculte){
        List<BookCirculte> resultList = bookCirStatisticBasic(bookCirculte);

        BookCirculte totalBookCirculte1 = new BookCirculte();
        totalBookCirculte1.setUnitCode("合计");
        try {
            ReportCommon.totalcommon(resultList, new String[]{"bookNum", "stuBorrowNum"}, totalBookCirculte1);
        } catch (Exception e) {
            totalBookCirculte1 = null;
        }
        resultList.add(totalBookCirculte1);

        return resultList;
    }

    /**
     * 首页流通率
     */
    public List<BookCirculte> circulationRate(BookCirculte bookCirculte){
        List<BookCirculte> list;

        if ( StringUtils.isEmpty(bookCirculte.getProvince())){
            bookCirculte = (BookCirculte)areaService.getArea(bookCirculte);
        }

        if (bookCirculte!=null) {
            list = bookCirculteDao.circulationRate(bookCirculte);
        }else{
            list = null;
        }
        return list;
    }
}
