/**
 *
 */
package com.lianyitech.modules.circulate.service;

import com.lianyitech.common.service.CrudService;
import com.lianyitech.common.utils.DoubleUtils;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.circulate.dao.BillDao;
import com.lianyitech.modules.circulate.dao.CompenDao;
import com.lianyitech.modules.circulate.entity.Bill;
import com.lianyitech.modules.circulate.entity.CompenRecord;
import com.lianyitech.modules.circulate.entity.Reader;
import com.lianyitech.modules.circulate.entity.Rule;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 赔付管理Service
 *
 * @author
 * @version
 */
@Service
public class CompenService extends CrudService<CompenDao, CompenRecord> {

    @Autowired
    private CompenDao compenDao;
    @Autowired
    BillDao billDao;
    @Autowired
    RuleService ruleService;
    @Autowired
    ReaderService readerService;
    @Autowired
    BillService billService;
    /**
     * 赔付
     * @param compenRecord
     */
        public void compen(CompenRecord compenRecord){
        if(compenRecord!=null) {
            compenRecord.setOrgId(UserUtils.getOrgId());
            Reader paramsReader = new Reader();
            paramsReader.setOrgId(compenRecord.getOrgId());
            paramsReader.setId(compenRecord.getReaderId());
            paramsReader.setCard(compenRecord.getReaderCard());
            Reader reader = readerService.findList(paramsReader).get(0);
            compenRecord.setReaderName(reader.getName());
            compenRecord.setReaderCard(reader.getCard());
            compenRecord.setReaderGroup(reader.getGroupName());
            compenRecord.setReaderId(reader.getId());
            if(compenRecord.getCompenType().equals("0")) {
                compenRecord.setAmount(null);
            }
            super.save(compenRecord);
        }
    }

    public Map caculateMoney (CompenRecord compenRecord , Rule rule , Copy copy) {
        Map result = new HashMap<String,String>();
        Double price = copy.getPrice()==null ? 0L : copy.getPrice();
        Double money = 0D;
        result.put("price",price);
        result.put("publishingTime",copy.getPublishingTime());
        if(compenRecord.getOpType().equals("0") || compenRecord.getOpType().equals("1")) {//丢失+污损
            Double rate = compenRecord.getOpType().equals("0") ? rule.getLossFineMulti() : rule.getStainFineMulti() ;
            money = DoubleUtils.multi(price,rate);
            if(money>99999.99) {
                money = 99999.99;
            }
        } else if (compenRecord.getOpType().equals("2")) {//超期
            Bill bill = billDao.queryPastDay(compenRecord.getBillId());
            Integer pastDay = bill.getPastDay();
            if(pastDay>0) {
                money = caculateMoney(pastDay,rule);
            }
            //billService.findBorrowByBill();
            result.put("pastDay",pastDay);
            result.put("exceedFineDayAmount",rule.getExceedFineDayAmount());
            result.put("exceedFineMaxAmount",rule.getExceedFineMaxAmount());
        }
        result.put("money",money);
        return result ;
    }

    public Double caculateMoney(Integer pastDay, Rule rule) {
        Double result = 0D;
        result = DoubleUtils.multi(rule.getExceedFineDayAmount(),Double.valueOf(pastDay));
        if(result > rule.getExceedFineMaxAmount()) {
            result = rule.getExceedFineMaxAmount();
        }
        return result ;
    }
}
