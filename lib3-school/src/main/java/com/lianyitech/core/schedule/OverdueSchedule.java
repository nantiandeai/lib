package com.lianyitech.core.schedule;

import com.lianyitech.core.marc.ReadBookDirMarc;
import com.lianyitech.modules.circulate.dao.BillDao;
import com.lianyitech.modules.circulate.entity.CirculateLogDTO;
import com.lianyitech.modules.sys.entity.MsgPush;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.lianyitech.core.config.ConfigurerConstants.RABBIT_QUEUE_NAME;

/**
 * 定时执行超期检测
 * @author jordan jiang
 * @version 2017/1/4
 */
//@Component
//@PropertySource("classpath:project.properties")
public class OverdueSchedule {

    private static Logger logger = LoggerFactory.getLogger(ReadBookDirMarc.class);

    private int i=0;

    @Autowired
    private BillDao billDao;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment environment;


//    @Scheduled(cron = "${schedule.overdue}")
    public void execute(){
        try {
            logger.info("超期检测任务开始!");

            CirculateLogDTO clDto = new CirculateLogDTO();
            clDto.setPastDay(0);
            clDto.setLogType("'0','1','2','7','8'");//流通记录列表查询中只显示借书，还书，续借，丢失，污损

            //图书超期提醒
            List<CirculateLogDTO> bookList = billDao.findBookCirculates(clDto);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            for (CirculateLogDTO dto : bookList){
                MsgPush msgPush = new MsgPush();
                msgPush.setContent("亲爱的读者 "+ dto.getName()
                        +" ，您于"+ sdf.format(dto.getBorrowDate())+"借阅的图书《"+dto.getTitle()+"》已超期，请尽快归还。");
                msgPush.setUserId(dto.getReaderCardId());
                msgPush.setUnitId(dto.getOrgId());
                rabbitTemplate.convertAndSend(environment.getProperty(RABBIT_QUEUE_NAME),msgPush);
            }

            //期刊超期提醒
            List<CirculateLogDTO> periList = billDao.findPeriCirculates(clDto);
            for (CirculateLogDTO dto : periList){
                MsgPush msgPush = new MsgPush();
                msgPush.setContent("亲爱的读者 "+ dto.getName()
                        +" ，您于"+ sdf.format(dto.getBorrowDate())+"借阅的期刊《"+dto.getTitle()+"》已超期，请尽快归还。");
                msgPush.setUserId(dto.getReaderCardId());
                msgPush.setUnitId(dto.getOrgId());
                rabbitTemplate.convertAndSend(environment.getProperty(RABBIT_QUEUE_NAME),msgPush);
            }

        } catch (Exception e) {
            logger.error("操作失败",e);
        }finally {
            logger.info("超期检测任务结束!");
        }
    }

}
