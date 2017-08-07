package com.lianyitech.modules.circulate.service;

import com.lianyitech.common.persistence.Page;
import com.lianyitech.common.service.CrudService;
import com.lianyitech.common.utils.StringUtils;
import com.lianyitech.core.utils.SystemUtils;
import com.lianyitech.modules.catalog.dao.CopyDao;
import com.lianyitech.modules.catalog.entity.Copy;
import com.lianyitech.modules.circulate.dao.BillDao;
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

public class BillService extends CrudService<BillDao, Bill> {

	@Autowired
	private RuleDao ruleDao;
	@Autowired
	private CopyDao copyDao;
	@Autowired
	private BindingDao bindingDao;

	public Bill get(String id) {
		return super.get(id);
	}

	public List<Bill> findList(Bill bill) {
		bill.setOrgId(UserUtils.getOrgId());
		return super.findList(bill);
	}

	public Page<Bill> findPage(Page<Bill> page, Bill bill) {
		bill.setOrgId(UserUtils.getOrgId());
		return super.findPage(page, bill);
	}

	public ReaderCirculateInfo findReaderCirculateInfo(ReaderCard readerCard,Page<Reader> page,Page<ReaderUnionBook> pageBook){
		readerCard.setRuleId(ruleDao.findRuleIdByCard(readerCard.getOrgId(),readerCard.getCard()));
		ReaderCirculateInfo rcInfo = dao.findReaderCirculateInfo(readerCard);
		if(rcInfo!=null){
			Reader reader = new Reader();
			reader.setId(rcInfo.getReaderId());
			reader.setOrgId(readerCard.getOrgId());
			reader.setKeyWords(readerCard.getKeyWords());
			if (StringUtils.isBlank(readerCard.getAppId())) {
				List<ReaderUnionBook> list = dao.findBorrowByBill(reader);
				rcInfo.setCountBorrow(list!=null?list.size():0);
				rcInfo.setList(list);
			} else {
				reader.setPage(page);
				List<ReaderUnionBook> list = dao.findBorrowByBill(reader);
				pageBook.setCount(page.getCount());
 				pageBook.setList(list);
				rcInfo.setPage(pageBook);
				rcInfo.setCountBorrow(new Long(pageBook.getCount()).intValue());
			}
		}
		return rcInfo;
	}

	public Page<CirculateLogDTO> findAllCirculate(CirculateLogDTO clDto,Page<CirculateLogDTO> page){
		clDto.setOrgId(UserUtils.getOrgId());
		clDto.setPage(page);
		if("0".equals(clDto.getDirType())){
			//查询图书流通记录
			page.setList(dao.findBookCirculates(clDto));
		}else if("1".equals(clDto.getDirType())){
			//查询期刊流通记录
			page.setList(dao.findPeriCirculates(clDto));
		}
		return page;
	}

	public Page<CirculateLogDTO> findOrderBorrow(CirculateLogDTO clDto,Page<CirculateLogDTO> page){
		clDto.setPage(page);
		clDto.setOrgId(UserUtils.getOrgId());
		page.setList(dao.findOrderBorrow(clDto));
		return page;
	}


	/**
	 * 返回没有分页的数据，用来导出
	 * @param clDto DTO模型数据
	 * @return DTO数据集合
     */
	public List<CirculateLogDTO> findAllCirculate(CirculateLogDTO clDto){
		clDto.setOrgId(UserUtils.getOrgId());
		List<CirculateLogDTO> list = new ArrayList<>();
		if("0".equals(clDto.getDirType())){
			//查询图书流通记录
			list = dao.findBookCirculates(clDto);
		}else if("1".equals(clDto.getDirType())){
			//查询期刊流通记录
			list = dao.findPeriCirculates(clDto);
		}
		if(list.size()!=0){
			for(CirculateLogDTO cLogDto : list){
				if(cLogDto.getReturnDate()!=null){
					cLogDto.setActionDate(cLogDto.getReturnDate());
				}else{
					cLogDto.setActionDate(cLogDto.getBorrowDate());
				}
			}
		}
		return list;
	}

	public Map<String, Object> findBookByBarCode(Copy copy) {
		Map<String, Object> data = null;
		copy.setOrgId(UserUtils.getOrgId());
		Map<String, Object> map = copyDao.findBookByBarCode(copy.getOrgId(), copy.getBarcode());
		//查询期刊
		if (null == map) {
			map = bindingDao.findPeriByBarCode(copy.getOrgId(), copy.getBarcode());
		}
		if (map != null) {
			data = new HashedMap();
			data.put("indexNum", SystemUtils.getIndexNum((String) map.get("librarsortCode"), (String) map.get("tanejiNo"), (String) map.get("assNo")));
			data.put("title", map.get("title"));
			data.put("subTitle", map.get("subTitle"));
			data.put("author", map.get("author"));
			data.put("publishingName", map.get("publishingName"));
			if(map.get("price")!=null) {
				data.put("price",parseDouble(map.get("price").toString()));
			} else {
				data.put("price",map.get("price"));
			}
			data.put("status", map.get("status"));
			Map<String, Object> readerInfoMap = dao.findReaderInfo((String) map.get("copyId"), copy.getOrgId());
			if (null != readerInfoMap) {
				data.put("readerId", readerInfoMap.get("readerId"));
				data.put("readerCard", readerInfoMap.get("readerCard"));
			}
		}
		return data;
	}

	public Map<String,Object> findPeriCopyByBarcode(Copy copy){
		copy.setOrgId(UserUtils.getOrgId());
		return dao.findPeriCopyByBarcode(copy);
	}


	/**
	 * 创建单据
     * param 输入参数 billId type userID remarks orgID readerId borrowDays renewDays bespeakingDays bespokeDays
     */
	void create(CirculateDTO dto) {
		dto.setRuleId(ruleDao.findRuleIdByCard(dto.getOrgId(),dto.getCard()));
		if(StringUtils.isNotEmpty(dto.getDirType()) && "1".equals(dto.getDirType())){
			//期刊操作
			dao.createPeri(dto);
		}else{
			//图书操作
			dao.create(dto);
		}
	}

	/**
	 * 创建操作日志 @param param 输入参数 billId type userID remarks orgID readerId borrowDays renewDays bespeakingDays bespokeDays
	 */
	void createlog(CirculateLogDTO logDTO) {
		dao.createlog(logDTO);
	}

	public void modify(CirculateDTO dto) {
		dto.setRuleId(ruleDao.findRuleIdByCard(dto.getOrgId(),dto.getCard()));
		if(StringUtils.isNotEmpty(dto.getDirType()) && "1".equals(dto.getDirType())){
			//期刊操作
			dao.modifyPeri(dto);
		}else{
			//图书操作
			dao.modify(dto);
		}
	}

	public void modifyBill(Bill bill){
		dao.modifyBill(bill);
	}
	/**
	 *  根据单据类型、条形码查询最新的单据
	 * @param type 单据类型
	 * @param barcode 条形码
	 * @return 单据
	 */
    /*Bill findLastByTypeAndBarCode(String type, String barcode) {
        Map<String, String> param = new HashMap<>();
        param.put("type",type);
        param.put("barcode",barcode);
        return dao.findLastByTypeAndBarCode(param);
    }*/
	/**
	 * 根据条件查询最新单据
	 */
     Bill findLastByCon(CirculateDTO dto){
     	dto.setOrgId(UserUtils.getOrgId());
		 return dao.findLastByCon(dto);
	 }
	/**
	 * 根据单据类型、读者Id查询该读者对于该操作的可操作次数
	 */
	/*Integer operateAllowCount(String type, String readerId) {
        Map<String, String> param = new HashMap<>();
        param.put("type",type);
        param.put("readerId",readerId);
        return dao.operateAllowCount(param);
    }
*/

	public List<CirculateDTO> findOverduePreBorrow(){
		return dao.findOverduePreBorrow();
	}

	public List<BillOverdueRecall> findRecallorOverdue(){
		return dao.findRecallorOverdue();
	}

	public int updateRecallDate(BillOverdueRecall billOverdueRecall){
		return dao.updateRecallDate(billOverdueRecall);
	}

	//超期条数
	public int countExceed(Bill bill){
		return dao.countExceed(bill);
	}

	/**
	 * 已借未还
	 * @param clDto clDto
	 * @param page page
	 * @return page
	 */
	public Page<CirculateLogDTO> listBorrowing(CirculateLogDTO clDto, Page<CirculateLogDTO> page) {
		clDto.setOrgId(UserUtils.getOrgId());
		clDto.setPage(page);
		page.setList(dao.listBorrowing(clDto));
		return page;
	}

	public List<CirculateLogDTO> exportListBorrowing(CirculateLogDTO clDto) {
		clDto.setOrgId(UserUtils.getOrgId());
		return dao.listBorrowing(clDto);
	}

	/**
	 * 取消合订期刊时条码是否流通过
	 * @param barcode barcode
	 * @return map
	 */
	public List<String> periBarcode(String barcode){
		String orgId = UserUtils.getOrgId();
		String[] idarr = barcode.split(",");
		List idList = Arrays.asList(idarr);
		Map<String,Object> map = new HashMap<>();
		map.put("orgId", orgId);
		map.put("idList", idList);
		return dao.periBarcode(map);
	}

}