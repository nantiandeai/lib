package com.lianyitech.modules.circulate.service;

import com.lianyitech.common.service.CrudService;
import com.lianyitech.modules.circulate.dao.ReaderCardDao;
import com.lianyitech.modules.circulate.entity.ReaderCard;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 读者管理Service
 * @author zengzy
 * @version 2016-09-09
 */
@Service
public class ReaderCardService extends CrudService<ReaderCardDao, ReaderCard> {

    ReaderCard findByCard(ReaderCard readerCard) {
		return dao.findByCard(readerCard);
	}

	void batchSave(List<ReaderCard> list){
		dao.batchSave(list);
	}
//	public ReaderCard findByReaderId(String readerId){return dao.findByReaderId(readerId);}
}