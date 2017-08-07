/**
 *
 */
package com.lianyitech.modules.circulate.service;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.core.enmu.EnumCertStatus;
import com.lianyitech.modules.circulate.dao.BillDao;
import com.lianyitech.modules.circulate.dao.DepositRecordDao;
import com.lianyitech.modules.circulate.dao.ReaderCardDao;
import com.lianyitech.modules.circulate.dao.ReaderDao;
import com.lianyitech.modules.circulate.entity.DepositRecord;
import com.lianyitech.modules.circulate.entity.Reader;
import com.lianyitech.modules.circulate.entity.ReaderUnionBook;
import com.lianyitech.modules.sys.entity.User;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 押金记录管理Service
 *
 * @author zengzy
 * @version 2017-07-13
 */
@Service
public class DepositRecordService extends CrudService<DepositRecordDao, DepositRecord> {

    @Autowired
    private ReaderDao readerDao;
    @Autowired
    private ReaderCardDao readerCardDao;
    @Autowired
    private BillDao billDao;

    public DepositRecord get(String id) {
        return super.get(id);
    }

    public Page<DepositRecord> findPage(Page<DepositRecord> page, DepositRecord depositRecord) {
        depositRecord.setOrgId(UserUtils.getOrgId());
        return super.findPage(page, depositRecord);
    }

    /**
     * 交退押金操作
     *
     * @param depositRecord
     * @return
     */
    @Transactional
    public String saveDeposit(DepositRecord depositRecord) {
        String msg = "";
        User user = UserUtils.getUser();
        depositRecord.setOrgId(user.getOrgId());
        depositRecord.setCreateBy(user.getLoginName());
        Map<String, Object> map = readerDao.findReaderInfoByCard(depositRecord.getReaderCard(), depositRecord.getOrgId());
        if (null == map) {
            msg = "该读者证不存在";
            return msg;
        }
        if (!EnumCertStatus.VALID.value().equals(map.get("status") + "")) {
            msg = "读者证不是有效状态，不可交/退押金";
            return msg;
        }

        Double amount = 0d;
        if ("0".equals(depositRecord.getOpType())) {//交押金
            if (Double.valueOf(map.get("deposit") + "") > 0) {
                msg = "该读者证已交押金";
                return msg;
            }
            amount = depositRecord.getAmount();
        } else if ("1".equals(depositRecord.getOpType())) {//退押金
            if (Double.valueOf(map.get("deposit") + "").equals(0d)) {
                msg = "该读者证未交押金";
                return msg;
            }

            //判断是否有未还记录
            Reader reader = new Reader();
            reader.setId(map.get("readerId") + "");
            reader.setOrgId(depositRecord.getOrgId());
            List<ReaderUnionBook> list = billDao.findBorrowByBill(reader);
            if (null != list && list.size() > 0) {
                msg = "该读者存在已借未还图书，不可退还押金";
                return msg;
            }
            depositRecord.setAmount(Double.valueOf(map.get("deposit") + ""));
        }

        //1.修改读者证账户押金
        readerCardDao.updateDeposit(depositRecord.getReaderCard(), depositRecord.getOrgId(), amount, new Date());

        //2.添加记录
        depositRecord.setReaderId(map.get("readerId") + "");
        depositRecord.setReaderName(map.get("readerName") + "");
        super.save(depositRecord);
        return msg;
    }
}
