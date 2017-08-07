package com.lianyitech.modules.offline.utils;


import com.lianyitech.core.utils.SystemUtils;
import com.lianyitech.modules.offline.entity.CopyVo;
import com.lianyitech.modules.offline.entity.PeriodicalVo;
import com.lianyitech.modules.offline.entity.ReaderVo;
import com.lianyitech.modules.offline.entity.ServerCirculateLog;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OfflineUtils {
    public static String parseReaderDataLine(Map<String,Object> rMap,String splitstr){
        StringBuffer result = new StringBuffer();
        String id = rMap.get("id")!=null?rMap.get("id").toString():"";
        String orgId = rMap.get("orgId")!=null?rMap.get("orgId").toString():"";
        String groupName = rMap.get("groupName")!=null?rMap.get("groupName").toString():"";
        String card = rMap.get("card")!=null?rMap.get("card").toString():"";
        String name = rMap.get("name")!=null?rMap.get("name").toString():"";
        String email = rMap.get("email")!=null?rMap.get("email").toString():"";
        String phone = rMap.get("phone")!=null?rMap.get("phone").toString():"";
        String certType = rMap.get("certType")!=null?rMap.get("certType").toString():"";
        String certNum = rMap.get("certNum")!=null?rMap.get("certNum").toString():"";
        String readerType = rMap.get("readerType")!=null?rMap.get("readerType").toString():"";
        String borrowNumber = rMap.get("borrowNumber")!=null?rMap.get("borrowNumber").toString():"";
        String countBorrow = rMap.get("countBorrow")!=null?rMap.get("countBorrow").toString():"";
        String borrowDays = rMap.get("borrowDays")!=null?rMap.get("borrowDays").toString():"";
        String startDate = rMap.get("startDate")!=null?rMap.get("startDate").toString():"";
        String endDate = rMap.get("endDate")!=null?rMap.get("endDate").toString():"";
        String createDate = rMap.get("createDate")!=null?rMap.get("createDate").toString():"";
        String updateDate = rMap.get("updateDate")!=null?rMap.get("updateDate").toString():"";
        String status = rMap.get("status")!=null?rMap.get("status").toString():"";
        result.append(id).append(splitstr).append(orgId).append(splitstr).append(groupName).append(splitstr)
        .append(card).append(splitstr).append(name).append(splitstr).append(email).append(splitstr)
        .append(phone).append(splitstr).append(certType).append(splitstr).append(certNum).append(splitstr)
        .append(readerType).append(splitstr).append(borrowNumber).append(splitstr).append(countBorrow).append(splitstr)
        .append(borrowDays).append(splitstr).append(startDate).append(splitstr).append(endDate).append(splitstr)
        .append(createDate).append(splitstr).append(updateDate).append(splitstr).append(status).append("\r\n");
        return result.toString();
    }
    public static String parseReaderLine(ReaderVo readerVo, String splitstr){
        StringBuffer result = new StringBuffer();
        String id = readerVo.getId()!=null?readerVo.getId().toString():"";
        String orgId = readerVo.getOrgId()!=null?readerVo.getOrgId().toString():"";
        String groupName = readerVo.getGroupName()!=null?readerVo.getGroupName().toString():"";
        String card = readerVo.getCard()!=null?readerVo.getCard().toString():"";
        String name = readerVo.getName()!=null?readerVo.getName().toString():"";
        String email = readerVo.getEmail()!=null?readerVo.getEmail().toString():"";
        String phone = readerVo.getPhone()!=null?readerVo.getPhone().toString():"";
        String certType = readerVo.getCertType()!=null?readerVo.getCertType().toString():"";
        String certNum = readerVo.getCertNum()!=null?readerVo.getCertNum().toString():"";
        String readerType = readerVo.getReaderType() != null ? readerVo.getReaderType().toString() : "";
        String borrowNumber = readerVo.getBorrowNumber()!=null?readerVo.getBorrowNumber().toString():"";
        String countBorrow = readerVo.getCountBorrow()!=null?readerVo.getCountBorrow().toString():"";
        String borrowDays = readerVo.getBorrowDays()!=null?readerVo.getBorrowDays().toString():"";
        String startDate = parseDate(readerVo.getStartDate());
        String endDate =  parseDate(readerVo.getEndDate());
        String createDate = parseDate(readerVo.getCreateDate());
        String updateDate = parseDate(readerVo.getUpdateDate());
        String status = readerVo.getStatus()!=null?readerVo.getStatus().toString():"";
        String delFlag = readerVo.getDelFlag()!=null?readerVo.getDelFlag().toString():"";
        result.append(id).append(splitstr).append(orgId).append(splitstr).append(groupName).append(splitstr)
                .append(card).append(splitstr).append(name).append(splitstr).append(email).append(splitstr)
                .append(phone).append(splitstr).append(certType).append(splitstr).append(certNum).append(splitstr)
                .append(readerType).append(splitstr).append(borrowNumber).append(splitstr).append(countBorrow).append(splitstr)
                .append(borrowDays).append(splitstr).append(startDate).append(splitstr).append(endDate).append(splitstr)
                .append(createDate).append(splitstr).append(updateDate).append(splitstr).append(status).append(splitstr).append(delFlag);
        return result.toString();
    }

    public static String parseCopyDataLine(Map<String,Object> rMap,String splitstr){
        StringBuffer result = new StringBuffer();
        String id = rMap.get("id")!=null?rMap.get("id").toString():"";
        String bookDirectoryId = rMap.get("bookDirectoryId")!=null?rMap.get("bookDirectoryId").toString():"";
        String orgId = rMap.get("orgId")!=null?rMap.get("orgId").toString():"";
        String barCode = rMap.get("barCode")!=null?rMap.get("barCode").toString():"";
        String batchNo = rMap.get("batchNo")!=null?rMap.get("batchNo").toString():"";
        String status = rMap.get("status")!=null?rMap.get("status").toString():"";
        String isRenew = rMap.get("isRenew")!=null?rMap.get("isRenew").toString():"";
        String isStained = rMap.get("isStained")!=null?rMap.get("isStained").toString():"";
        String isbn = rMap.get("isbn")!=null?rMap.get("isbn").toString():"";
        String title = rMap.get("title")!=null?rMap.get("title").toString():"";
        String subTitle = rMap.get("subTitle")!=null?rMap.get("subTitle").toString():"";
        String tiedTitle = rMap.get("tiedTitle")!=null?rMap.get("tiedTitle").toString():"";
        String partName = rMap.get("partName")!=null?rMap.get("partName").toString():"";
        String partNum = rMap.get("partNum")!=null?rMap.get("partNum").toString():"";
        String seriesName = rMap.get("seriesName")!=null?rMap.get("seriesName").toString():"";
        String author = rMap.get("author")!=null?rMap.get("author").toString():"";
        String subAuthor = rMap.get("subAuthor")!=null?rMap.get("subAuthor").toString():"";
        String seriesEditor = rMap.get("seriesEditor")!=null?rMap.get("seriesEditor").toString():"";
        String translator = rMap.get("translator")!=null?rMap.get("translator").toString():"";
        String publishingName = rMap.get("publishingName")!=null?rMap.get("publishingName").toString():"";
        String publishingAddress = rMap.get("publishingAddress")!=null?rMap.get("publishingAddress").toString():"";
        String publishingTime = rMap.get("publishingTime")!=null?rMap.get("publishingTime").toString():"";
        String collectionsiteName = rMap.get("collectionsiteName")!=null?rMap.get("collectionsiteName").toString():"";
        String librarsortCode = rMap.get("librarsortCode")!=null?rMap.get("librarsortCode").toString():"";
        String price = rMap.get("price")!=null?rMap.get("price").toString():"";
        String edition = rMap.get("edition")!=null?rMap.get("edition").toString():"";
        String language = rMap.get("language")!=null?rMap.get("language").toString():"";
        String measure = rMap.get("measure")!=null?rMap.get("measure").toString():"";
        String pageNo = rMap.get("pageNo")!=null?rMap.get("pageNo").toString():"";
        String bindingForm = rMap.get("bindingForm")!=null?rMap.get("bindingForm").toString():"";
        String bestAge = rMap.get("bestAge")!=null?rMap.get("bestAge").toString():"";
        String attachmentNote = rMap.get("attachmentNote")!=null?rMap.get("attachmentNote").toString():"";
        String subject = rMap.get("subject")!=null?rMap.get("subject").toString():"";
        String tanejiNo = rMap.get("tanejiNo")!=null?rMap.get("tanejiNo").toString():"";
        String assNo = rMap.get("assNo")!=null?rMap.get("assNo").toString():"";
        String bookNo = rMap.get("bookNo")!=null?rMap.get("bookNo").toString():"";
        String indexNum = SystemUtils.getIndexNum1(librarsortCode,tanejiNo,assNo,bookNo);
        String createDate = rMap.get("createDate")!=null?rMap.get("createDate").toString():"";
        String updateDate = rMap.get("updateDate")!=null?rMap.get("updateDate").toString():"";
        result.append(id).append(splitstr).append(bookDirectoryId).append(splitstr).append(orgId).append(splitstr)
                .append(barCode).append(splitstr).append(batchNo).append(splitstr).append(status).append(splitstr)
                .append(isRenew).append(splitstr).append(isStained).append(splitstr).append(isbn).append(splitstr)
                .append(title).append(splitstr).append(subTitle).append(splitstr).append(tiedTitle).append(splitstr)
                .append(partName).append(splitstr).append(partNum).append(splitstr).append(seriesName).append(splitstr).append(author).append(splitstr)
                .append(subAuthor).append(splitstr).append(seriesEditor).append(splitstr).append(translator).append(splitstr)
                .append(publishingName).append(splitstr).append(publishingAddress).append(splitstr).append(publishingTime).append(splitstr)
                .append(collectionsiteName).append(splitstr).append(librarsortCode).append(splitstr).append(price).append(splitstr)
                .append(edition).append(splitstr).append(language).append(splitstr).append(measure).append(splitstr)
                .append(pageNo).append(splitstr).append(bindingForm).append(splitstr).append(bestAge).append(splitstr)
                .append(attachmentNote).append(splitstr).append(subject).append(splitstr).append(indexNum).append(splitstr)
                .append(createDate).append(splitstr).append(updateDate).append("\r\n");
        return result.toString();
    }
    public static String parseErrorCopyData(Map<String,Object> rMap){
        StringBuffer result = new StringBuffer();
        String id = rMap.get("id")!=null?rMap.get("id").toString():"";
        result.append(id).append("\r\n");
        return result.toString();
    }
    public static boolean createFile(File file, String uploadPath,String uploadFileName){
        if (!file.exists()) {
            file.mkdirs();
        } else {
            File fileInfo = new File(String.join(File.separator,uploadPath,uploadFileName));
            //如果文件已经存在并且存在内容的情况下则不需要往下走
            if(fileInfo.exists() && fileInfo.length()>0){
                return true;
            } else{
                fileInfo.delete();
            }
        }
        return false;
    }
    public static String parseErrorSyncLine(ServerCirculateLog log,String splitStr){
        StringBuffer result = new StringBuffer();
        result.append(log.getId()!=null?log.getId():"").append(splitStr).append(log.getStatus()).append(splitStr).append(log.getErrorInfo()!=null?log.getErrorInfo():"").append("\r\n");
        return result.toString();
    }
    public static String parseSyncLine(ServerCirculateLog log,String splitStr){
        StringBuffer result = new StringBuffer();
        result.append(log.getId()!=null?log.getId():"").append(splitStr).append(log.getStatus()).append(splitStr).append(log.getErrorInfo()!=null?log.getErrorInfo():"");
        return result.toString();
    }
    public static String parseDate(Date date) {
        String result = "" ;
        if(date!=null)
            result =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        return result;
    }
    public static String parseCopyLine(CopyVo copyVo, String splitstr){
        StringBuffer result = new StringBuffer();
        String id = copyVo.getId()!=null?copyVo.getId().toString():"";
        String bookDirectoryId = copyVo.getBookDirectoryId()!=null?copyVo.getBookDirectoryId().toString():"";
        String orgId = copyVo.getOrgId()!=null?copyVo.getOrgId().toString():"";
        String barCode = copyVo.getBarCode()!=null?copyVo.getBarCode().toString():"";
        String batchNo = copyVo.getBatchNo()!=null?copyVo.getBatchNo().toString():"";
        String status = copyVo.getStatus()!=null?copyVo.getStatus().toString():"";
        String isRenew = copyVo.getIsRenew()!=null?copyVo.getIsRenew().toString():"";
        String isStained = copyVo.getIsRenew()!=null?copyVo.getIsRenew().toString():"";
        String isbn = copyVo.getIsbn()!=null?copyVo.getIsbn().toString():"";
        String title = copyVo.getTitle()!=null?copyVo.getTitle().toString():"";
        String subTitle = copyVo.getSubTitle()!=null?copyVo.getSubTitle().toString():"";
        String tiedTitle = copyVo.getTiedTitle()!=null?copyVo.getTiedTitle().toString():"";
        String partName = copyVo.getPartName()!=null?copyVo.getPartName().toString():"";
        String partNum = copyVo.getPartNum()!=null?copyVo.getPartNum().toString():"";
        String seriesName = copyVo.getSeriesName()!=null?copyVo.getSeriesName().toString():"";
        String author = copyVo.getAuthor()!=null?copyVo.getAuthor().toString():"";
        String subAuthor = copyVo.getAuthor()!=null?copyVo.getAuthor().toString():"";
        String seriesEditor = copyVo.getSeriesEditor()!=null?copyVo.getSeriesEditor().toString():"";
        String translator = copyVo.getTranslator()!=null?copyVo.getTranslator().toString():"";
        String publishingName = copyVo.getPublishingName()!=null?copyVo.getPublishingName().toString():"";
        String publishingAddress = copyVo.getPublishingAddress()!=null?copyVo.getPublishingAddress().toString():"";
        String publishingTime = copyVo.getPublishingTime()!=null?copyVo.getPublishingTime().toString():"";
        String collectionsiteName = copyVo.getCollectionSiteName()!=null?copyVo.getCollectionSiteName().toString():"";
        String librarsortCode = copyVo.getLibrarsortCode()!=null?copyVo.getLibrarsortCode().toString():"";
        String price = copyVo.getPrice()!=null?copyVo.getPrice().toString():"";
        String edition = copyVo.getEdition()!=null?copyVo.getEdition().toString():"";
        String language = copyVo.getLanguage()!=null?copyVo.getLanguage().toString():"";
        String measure = copyVo.getMeasure()!=null?copyVo.getMeasure().toString():"";
        String pageNo = copyVo.getPageNo()!=null?copyVo.getPageNo().toString():"";
        String bindingForm = copyVo.getBindingForm()!=null?copyVo.getBindingForm().toString():"";
        String bestAge = copyVo.getBestAge()!=null?copyVo.getBestAge().toString():"";
        String attachmentNote = copyVo.getAttachmentNote()!=null?copyVo.getAttachmentNote().toString():"";
        String subject = copyVo.getSubject()!=null?copyVo.getSubject().toString():"";
        String tanejiNo = copyVo.getTanejiNo()!=null?copyVo.getTanejiNo().toString():"";
        String assNo = copyVo.getAssNo()!=null?copyVo.getAssNo().toString():"";
        String bookNo = copyVo.getBookNo()!=null?copyVo.getBookNo().toString():"";
        String indexNum = SystemUtils.getIndexNum1(librarsortCode,tanejiNo,assNo,bookNo);
        String createDate = parseDate(copyVo.getCreateDate());
        String updateDate = parseDate(copyVo.getUpdateDate());
        String delFlag = copyVo.getDelFlag()!=null?copyVo.getDelFlag():"";
        result.append(id).append(splitstr).append(bookDirectoryId).append(splitstr).append(orgId).append(splitstr)
                .append(barCode).append(splitstr).append(batchNo).append(splitstr).append(status).append(splitstr)
                .append(isRenew).append(splitstr).append(isStained).append(splitstr).append(isbn).append(splitstr)
                .append(title).append(splitstr).append(subTitle).append(splitstr).append(tiedTitle).append(splitstr)
                .append(partName).append(splitstr).append(partNum).append(splitstr).append(seriesName).append(splitstr).append(author).append(splitstr)
                .append(subAuthor).append(splitstr).append(seriesEditor).append(splitstr).append(translator).append(splitstr)
                .append(publishingName).append(splitstr).append(publishingAddress).append(splitstr).append(publishingTime).append(splitstr)
                .append(collectionsiteName).append(splitstr).append(librarsortCode).append(splitstr).append(price).append(splitstr)
                .append(edition).append(splitstr).append(language).append(splitstr).append(measure).append(splitstr)
                .append(pageNo).append(splitstr).append(bindingForm).append(splitstr).append(bestAge).append(splitstr)
                .append(attachmentNote).append(splitstr).append(subject).append(splitstr).append(indexNum).append(splitstr)
                .append(createDate).append(splitstr).append(updateDate).append(splitstr).append(delFlag);
        return result.toString();
    }
    public static String parsePeriodicalLine(PeriodicalVo periodicalVo, String splitstr){
        StringBuffer result = new StringBuffer();
        String id = periodicalVo.getId()!=null?periodicalVo.getId().toString():"";
        String periDirectoryId = periodicalVo.getPeriDirectoryId()!=null?periodicalVo.getPeriDirectoryId().toString():"";
        String orgId = periodicalVo.getOrgId()!=null?periodicalVo.getOrgId().toString():"";
        String barCode = periodicalVo.getBarCode()!=null?periodicalVo.getBarCode().toString():"";
        String isRenew = periodicalVo.getIsRenew()!=null?periodicalVo.getIsRenew().toString():"";
        String isOrder = periodicalVo.getIsOrder()!=null?periodicalVo.getIsOrder().toString():"";
        String issn = periodicalVo.getIssn()!=null?periodicalVo.getIssn().toString():"";
        String title = periodicalVo.getTitle()!=null?periodicalVo.getTitle().toString():"";
        String subTitle = periodicalVo.getSubTitle()!=null?periodicalVo.getSubTitle().toString():"";
        String author = periodicalVo.getAuthor()!=null?periodicalVo.getAuthor().toString():"";
        String publishingName = periodicalVo.getPublishingName()!=null?periodicalVo.getPublishingName().toString():"";
        String publishingYear= periodicalVo.getPublishingYear()!=null?periodicalVo.getPublishingYear().toString():"";
        String collectionsiteName = periodicalVo.getCollectionSiteName()!=null?periodicalVo.getCollectionSiteName().toString():"";
        String librarsortCode = periodicalVo.getLibrarsortCode()!=null?periodicalVo.getLibrarsortCode().toString():"";
        String price = periodicalVo.getPrice()!=null?periodicalVo.getPrice().toString():"";
        String periNum = periodicalVo.getPeriNum()!=null?periodicalVo.getPeriNum().toString():"";
        String emailNum = periodicalVo.getEmailNum()!=null?periodicalVo.getEmailNum().toString():"";
        String language = periodicalVo.getLanguage()!=null?periodicalVo.getLanguage().toString():"";
        String lev = periodicalVo.getLev()!=null?periodicalVo.getLev().toString():"";
        String bindingNum = periodicalVo.getBindingNum()!=null?periodicalVo.getBindingNum().toString():"";
        String bookTimeNo = periodicalVo.getBookTimeNo()!=null?periodicalVo.getBookTimeNo().toString():"";
        String assNo = periodicalVo.getAssNo()!=null?periodicalVo.getAssNo().toString():"";
        String somNo = SystemUtils.getSomNo(librarsortCode,bookTimeNo,assNo);
        String status = periodicalVo.getStatus()!=null?periodicalVo.getStatus().toString():"";
        String publishingFre = periodicalVo.getPublishingFre()!=null?periodicalVo.getPublishingFre().toString():"";
        String bookSize = periodicalVo.getBookSize()!=null?periodicalVo.getBookSize().toString():"";
        String periType = periodicalVo.getPeriType()!=null?periodicalVo.getPeriType().toString():"";
        String createDate = parseDate(periodicalVo.getCreateDate());
        String updateDate = parseDate(periodicalVo.getUpdateDate());
        String delFlag = periodicalVo.getDelFlag()!=null?periodicalVo.getDelFlag():"";
        result.append(id).append(splitstr).append(periDirectoryId).append(splitstr).append(orgId).append(splitstr)
                .append(barCode).append(splitstr).append(isRenew).append(splitstr).append(isOrder).append(splitstr)
                .append(issn).append(splitstr).append(title).append(splitstr).append(subTitle).append(splitstr)
                .append(author).append(splitstr).append(publishingName).append(splitstr).append(publishingYear).append(splitstr)
                .append(collectionsiteName).append(splitstr).append(librarsortCode).append(splitstr).append(price).append(splitstr).append(periNum).append(splitstr)
                .append(emailNum).append(splitstr).append(language).append(splitstr).append(lev).append(splitstr)
                .append(bindingNum).append(splitstr).append(somNo).append(splitstr).append(status).append(splitstr)
                .append(publishingFre).append(splitstr).append(bookSize).append(splitstr).append(periType).append(splitstr)
                .append(createDate).append(splitstr).append(updateDate).append(splitstr).append(delFlag);
        return result.toString();
    }

    public static void main(String[] args) {
        String a = "@td@@td@sd@td@@td@33@td@";
        String as[] = a.split("@td@");

        Jedis jedis = new Jedis("192.168.2.243", 6379);
        //        while (true) {
//            String s = jedis.spop("haliyou_1");
//            if(s==null){
//                break;
//            }
//        }
//        while (true) {
//            String s = jedis.spop("haliyou_2");
//            if(s==null){
//                break;
//            }
//        }
       while (true) {
            String s = jedis.spop("update_habao_2");
            if(s==null){
                break;
            }
        }
       /* while (true) {
            String s = jedis.spop("haliyou_1_status");
            if(s==null){
                break;
            }
        }*/
    }

}
