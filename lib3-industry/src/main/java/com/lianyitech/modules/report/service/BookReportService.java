/**
 *
 */
package com.lianyitech.modules.report.service;

import com.lianyitech.common.service.CrudService;
import com.lianyitech.common.utils.BeanUtilsExt;
import com.lianyitech.modules.common.Compare;
import com.lianyitech.modules.common.ReportCommon;
import com.lianyitech.modules.report.dao.BookReportDao;
import com.lianyitech.modules.report.entity.BookReport;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 跟书目有关统计Service
 * @author zym
 * @version 2016-10-29
 */
@Service

public class BookReportService extends CrudService<BookReportDao,BookReport> {
    @Autowired
    private BookReportDao bookReportDao;
    @Autowired
    private AreaService areaService;

    /**
     * 藏书量统计报表--目前行馆报表都不需要带分页
     */
    public Map<String, Object> listCollectionStat(BookReport bookReport) throws Exception{
        Map<String, Object> result = new HashMap<>();
        //定义一个类用来获取求和的属性
        BookReport total_bookReport = new BookReport();
        List<BookReport> resultList;

        if ( StringUtils.isEmpty(bookReport.getProvince()) ){
            bookReport = (BookReport)areaService.getArea(bookReport);
        }

        if (bookReport!=null) {
            resultList = collectionStat(bookReport);
            //对list集合里面属性为bookSpecies、bookNum、booksAmount进行求和赋值到新实体类
            ReportCommon.totalcommon(resultList, new String[]{"bookSpecies", "bookNum", "booksAmount"}, total_bookReport);
        }else{
            resultList = null;
        }

        result.put("list", resultList);
        result.put("total", total_bookReport);
        return result;
    }

    public List<BookReport> collectionStat(BookReport bookReport){

        List<BookReport> listSchool;
        List<BookReport> listSchoolArea;
        List<BookReport> resultList;
        if (bookReport.getConditionQuery()==3
                && ( StringUtils.isNotEmpty(bookReport.getCounty())||StringUtils.isNotEmpty(bookReport.getUnitName()) )
                ) {//如果查询的全部数据（包括学区），先是查询学区外的学校，然后再查询学区。
            listSchool = bookReportDao.listCollectionStat(bookReport);
            bookReport.setConditionQuery(2);
            listSchoolArea = bookReportDao.listCollectionStat(bookReport);
            if (listSchoolArea!=null && listSchoolArea.size()>0){
                listSchoolArea.addAll(listSchool);
                resultList = listSchoolArea;
            }else{
                resultList = listSchool;
            }
        }else{
            resultList = bookReportDao.listCollectionStat(bookReport);
        }

        //这里默认按照复本降序排序下
        Collections.sort(resultList, Compare.bookNum_Desc);
        return resultList;
    }

    /**
     * 藏书量统计报表导出
     */
    public List<BookReport> exportCollectionStatis(BookReport bookReport) throws InvocationTargetException, IllegalAccessException {
        BookReport tmpBookReport = new BookReport();
        BeanUtilsExt.copyProperties(tmpBookReport, bookReport);

        List<BookReport> resultList = collectionStat(bookReport);

        BookReport totalbookReport = new BookReport();
        totalbookReport.setUnitCode("合计");
        try {
            ReportCommon.totalcommon(resultList, new String[]{"bookSpecies", "bookNum", "booksAmount"}, totalbookReport);
        } catch (Exception e) {
            totalbookReport = null;
        }
        totalbookReport.setBookSpecies(bookType(tmpBookReport));
        resultList.add(totalbookReport);
        return resultList;
    }

    public int bookType(BookReport bookReport){
        if ( StringUtils.isEmpty(bookReport.getProvince()) ){
            bookReport = (BookReport)areaService.getArea(bookReport);
        }

        if (bookReport!=null) {
            return dao.bookType(bookReport);
        }else{
            return 0;
        }
    }

    /**
     * 生均及增量统计--目前不按分页走
     *
     */
    public Map<String,Object> listStuAddBookNum(BookReport bookReport) throws Exception{
        Map<String,Object> result = new HashMap<>();
        List<BookReport> resultList;
        //totoal 通过list
        BookReport totalbookReport = new BookReport();

        if ( StringUtils.isEmpty(bookReport.getProvince()) ){
            bookReport = (BookReport)areaService.getArea(bookReport);
        }

        if (bookReport!=null){
            resultList = stuAddBookNum(bookReport);
            ReportCommon.totalcommon(resultList,new String[]{"bookNum","addBookNum","studentNum"},totalbookReport);
        }else{
            resultList = null;
        }

        result.put("list", resultList);
        result.put("total",totalbookReport);
        return result;
    }

    public List<BookReport> stuAddBookNum(BookReport bookReport){

        List<BookReport> listSchool;
        List<BookReport> listSchoolArea;
        List<BookReport> resultList;
        if (bookReport.getConditionQuery()==3
                && ( StringUtils.isNotEmpty(bookReport.getCounty())||StringUtils.isNotEmpty(bookReport.getUnitName()) )
                ) {//如果查询的全部数据（包括学区），先是查询学区外的学校，然后再查询学区。
            listSchool = bookReportDao.listStuAddBookNum(bookReport);
            bookReport.setConditionQuery(2);
            listSchoolArea = bookReportDao.listStuAddBookNum(bookReport);
            if (listSchoolArea!=null && listSchoolArea.size()>0){
                listSchoolArea.addAll(listSchool);
                resultList = listSchoolArea;
            }else{
                resultList = listSchool;
            }
        }else{
            resultList = bookReportDao.listStuAddBookNum(bookReport);
        }

        //这里默认按照生均册数倒序排列
        Collections.sort(resultList, Compare.stuBookNum_Desc);
        return resultList;

    }

    /**
     * 生均及增量统计报表导出
     */
    public List<BookReport> exportStuAddBookNum(BookReport bookReport){
        List<BookReport> resultList = stuAddBookNum(bookReport);

        BookReport totalbookReport = new BookReport();
        totalbookReport.setUnitCode("合计");
        try {
            ReportCommon.totalcommon(resultList,new String[]{"bookNum","addBookNum","studentNum"},totalbookReport);
        } catch (Exception e) {
            totalbookReport = null;
        }
        resultList.add(totalbookReport);

        return resultList;
    }

    public int countSchool(BookReport bookReport){
        if (areaService.getArea(bookReport)!=null) {
            return bookReportDao.countSchool(bookReport);
        }else{
            return 0;
        }
    }

}