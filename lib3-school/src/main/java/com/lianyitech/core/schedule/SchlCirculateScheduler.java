package com.lianyitech.core.schedule;

import com.lianyitech.core.marc.ReadBookDirMarc;
import com.lianyitech.modules.circulate.dao.ReaderCardDao;
import com.lianyitech.modules.circulate.dao.ReaderDao;
import com.lianyitech.modules.circulate.entity.CirculateDTO;
import com.lianyitech.modules.circulate.entity.Reader;
import com.lianyitech.modules.circulate.entity.ReaderCard;
import com.lianyitech.modules.circulate.service.BillService;
import com.lianyitech.modules.circulate.service.CirculateService;
import com.lianyitech.modules.circulate.web.SendIndustryHttp;
import com.lianyitech.modules.circulate.entity.BillOverdueRecall;
import com.lianyitech.modules.lib.entity.WaitingHandle;
import com.lianyitech.modules.sys.entity.MsgPush;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.lianyitech.core.config.ConfigurerConstants.RABBIT_QUEUE_NAME;

/**
 * 定时执行借阅管理
 * @author jordan jiang
 * @version 2016/9/18
 */
@Component
public class SchlCirculateScheduler {

    private static Logger logger = LoggerFactory.getLogger(ReadBookDirMarc.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment environment;

    @Autowired
    private ReaderDao readerDao;

    @Resource
    private BillService iSchlBillService;

    @Autowired
    private CirculateService circulateService;

    @Autowired
    private ReaderCardDao readerCardDao;


    @Scheduled(cron = "0 0 23 * * ?")
    public void execute(){
        logger.info("定时执行借阅管理-超期检测任务开始!");

        List<BillOverdueRecall> billList = iSchlBillService.findRecallorOverdue();

        if ( billList!=null && billList.size()>0 ){
            for(BillOverdueRecall bill:billList){
                Integer code = (Integer)sendIndustry(bill).get("code");
                if ( ( code!=null )&&( code==0 ) ){
//                    iSchlBillService.updateRecallDate(bill);
                }
                if (sendMsgPush(bill)){
                    iSchlBillService.updateRecallDate(bill);
                }
            }
        }
        logger.info("定时执行借阅管理-超期检测任务结束!");
    }

    //@Scheduled(cron = "0 0 0/1 * * ?")
    public void checkOverduePreBorrow(){
        logger.info("定时执行预约/借失效检测任务开始!");

        List<CirculateDTO> billList = iSchlBillService.findOverduePreBorrow();

        if ( billList!=null && billList.size()>0 ){
            for(CirculateDTO bill : billList){
                if ( bill.getStatus().equals("51") ){
                    bill.setType("9");
                }else if (bill.getStatus().equals("65")){
                    bill.setType("10");
                }
                bill.setRemarks("该预约/借已经失效，系统自动取消预约/借！");
                try {
                    circulateService.createSingle(bill);
                }catch (Exception e){
                    logger.error("操作失败",e);
                }
            }
        }
        logger.info("定时执行预约/借失效检测任务结束!");
    }

    private boolean sendMsgPush(BillOverdueRecall bill){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calSysNow = Calendar.getInstance();
        Calendar calShouldReturn = Calendar.getInstance();

        try {
            calSysNow.setTime( format.parse(format.format(bill.getSysNowDate())) );
            calShouldReturn.setTime( format.parse(format.format(bill.getShouldReturnDate())));
        } catch (Exception e) {
            logger.error("操作失败",e);
        }

        String themeDescribe;
        //应还日期小于当前日期, re = -1;
        //应还日期大于当前日期, re = 1;
        int re = calShouldReturn.compareTo(calSysNow);
        if (re < 0) {
            themeDescribe = "亲爱的读者 " + bill.getReaderName()
                    + "：您于" + format.format(bill.getShouldReturnDate()) + "借阅的《" + bill.getTitle() + "》已超期，请尽快归还！";
        }else if (re == 0) {
            themeDescribe = "亲爱的读者 " + bill.getReaderName()
                    + "：您借阅的《" + bill.getTitle() + "》于" + format.format(bill.getShouldReturnDate()) + "到期，请尽快归还！";
        }else{
            themeDescribe = "亲爱的读者 " + bill.getReaderName()
                    + "：您借阅的《" + bill.getTitle() + "》将于" + format.format(bill.getShouldReturnDate()) + "到期，请尽快归还！";
        }

        MsgPush msgPush = new MsgPush();
        msgPush.setContent(themeDescribe);
        Reader reader = new Reader();
        reader.setId(bill.getReaderId());
        reader.setDelFlag("0");
        reader.setOrgId(bill.getOrgId());
        ReaderCard readerCard = readerDao.findCardByReader(reader);
        if (readerCard!=null){
            msgPush.setUserId(readerCard.getCard());
        }
        msgPush.setUnitId(bill.getOrgId());
        try {
            rabbitTemplate.convertAndSend(environment.getProperty(RABBIT_QUEUE_NAME),msgPush);
        }catch (Exception e){
            logger.error("操作失败",e);
            return false;
        }
        return true;
    }

    private HashMap<String, Object> sendIndustry(BillOverdueRecall bill){
        HashMap<String, Object> result;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calSysNow = Calendar.getInstance();
        Calendar calShouldReturn = Calendar.getInstance();

        try {
            calSysNow.setTime( format.parse(format.format(bill.getSysNowDate())) );
            calShouldReturn.setTime( format.parse(format.format(bill.getShouldReturnDate())));
        } catch (final Exception e) {
            logger.error("操作失败",e);
            new HashMap<String, Object>(){{put("code", -1);put("desc", e.getMessage());}};
        }

        String themeDescribe;
        //应还日期小于当前日期, re = -1;
        //应还日期大于当前日期, re = 1;
        int re = calShouldReturn.compareTo(calSysNow);
        if (re <= 0) {
            themeDescribe = bill.getReaderName() + "借阅的《" + bill.getTitle() + "》应该在" + format.format(bill.getShouldReturnDate()) + "归还！";
        }else {
            themeDescribe = bill.getReaderName() + "借阅的《" + bill.getTitle() + "》将于" + format.format(bill.getShouldReturnDate()) + "到期！";
        }

        WaitingHandle waitingHandle = new WaitingHandle();
        waitingHandle.setName("催还通知");               // 待办名称
        waitingHandle.setThemeDescribe(themeDescribe);  // 主题描述
        waitingHandle.setOtherThemeDescribe("New");		// 主题副描述
        waitingHandle.setWaitingType("1");	            // 类型  0公告通知   1待办事项
        waitingHandle.setUserType("1");		            // 适用应用对象   0单管   1读者

        result = SendIndustryHttp.sendIndustryHandle(waitingHandle, bill);

        return result;
    }

    /**
     * 每两分钟执行一次
     */
    @Scheduled(cron = "0 0 22 * * ?" )
    public void readerCardOverdue(){
        logger.info("定时执行读者证-过期检测任务开始!");
        //查询过期但状态没改的读者证
        List<ReaderCard> cardList = readerCardDao.findAllOverdueReaderCard();
        if ( cardList != null && cardList.size() > 0 ){
            for (ReaderCard readerCard : cardList){
                //遍历并把状态改为3过期
                readerCardDao.updateReaderCardOverdueStatus(readerCard);
            }
        }
        logger.info("定时执行读者证-过期检测任务结束!");
    }


}
