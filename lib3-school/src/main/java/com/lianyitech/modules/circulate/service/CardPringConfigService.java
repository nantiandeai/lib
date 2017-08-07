package com.lianyitech.modules.circulate.service;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.utils.SystemUtils;
import com.lianyitech.modules.catalog.dao.CopyDao;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.circulate.dao.BillDao;
import com.lianyitech.modules.circulate.dao.CardPrintConfigDao;
import com.lianyitech.modules.circulate.dao.RuleDao;
import com.lianyitech.modules.circulate.entity.*;
import com.lianyitech.modules.peri.dao.BindingDao;
import com.lianyitech.modules.sys.utils.UserUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.lianyitech.core.utils.CommonUtils.parseDouble;

/**
 * 操作单据管理Service
 * @author zengzy
 * @version 2016-09-09
 */
@Service

public class CardPringConfigService extends CrudService<CardPrintConfigDao, CardPrintConfig> {

	@Autowired
	CardPrintConfigDao dao;


	@Override
	public CardPrintConfig get(CardPrintConfig entity) {
		entity.setOrgId(UserUtils.getOrgId());
		CardPrintConfig cardPrintConfig = super.get(entity);
		if(cardPrintConfig==null) {
			entity.setOrgId("");
			cardPrintConfig = super.get(entity);
			cardPrintConfig.setId("");
			cardPrintConfig.setCompName(UserUtils.getUser().getOrgName());
		}
		return cardPrintConfig;
	}
}