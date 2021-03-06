package com.zaijiadd.app.store.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import com.zaijiadd.app.store.dao.SupplyGoodsInfoDao;
import com.zaijiadd.app.store.entity.GoodsInfoEntity;
import com.zaijiadd.app.store.entity.SupplyGoodsInfoEntity;
import com.zaijiadd.app.store.service.SupplyGoodsInfoService;
import com.zaijiadd.app.utils.constants.ConstantsForPage;

public class SupplyGoodsInfoServiceImpl implements SupplyGoodsInfoService{

	@Autowired
	private SupplyGoodsInfoDao supplyGoodsInfoDao;
	
	@Override
	public List<SupplyGoodsInfoEntity> queryGoodsInfo(Map<String, Object> params) {
		return supplyGoodsInfoDao.queryGoodsInfo(params);
	}

	/**
	 * 根据supply_goods_info中的id查询某产品的详细情况
	 * @param id
	 * @return
	 */
	public SupplyGoodsInfoEntity getGoodsInfoById(Integer id){
		return supplyGoodsInfoDao.getGoodsInfoById(id);
	}
	
	@Override
	public List<GoodsInfoEntity> queryGoodsInfoByGoodsName(Map<String, Object> params) {
		return supplyGoodsInfoDao.queryGoodsInfoByGoodsName(params);
	}

	@Override
	public void addSupplyGoods(SupplyGoodsInfoEntity supplyGoodsInfo) {
		supplyGoodsInfoDao.addSupplyGoods(supplyGoodsInfo);
	}

	/**
	 * 返回货物id
	 */
	@Override
	public SupplyGoodsInfoEntity isHasGoodsInSupplyGoodsInfo(SupplyGoodsInfoEntity supplyGoodsInfo) {
		return supplyGoodsInfoDao.isHasGoodsInSupplyGoodsInfo(supplyGoodsInfo);
	}

	@Override
	public void updateSupplyGoods(SupplyGoodsInfoEntity supplyGoodsInfo) {
		supplyGoodsInfoDao.updateSupplyGoods(supplyGoodsInfo);		
	}
	
	/**
	 * 批量下架
	 * @param ids
	 */
	public void batchUndercarriage(@Param("ids")List<Integer> ids){
		supplyGoodsInfoDao.batchUndercarriage(ids);
	}
	
	/**
	 * 售出物品后修改已经售出件数
	 * @param params
	 */
	public void saleOutGoods(int id, int saleCount){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("saleCount", saleCount);
		supplyGoodsInfoDao.saleOutGoods(params);
	}

	/**
	 * 
	 * @param pageNo
	 * @return
	 */
	@Override
	public List<SupplyGoodsInfoEntity> queryGoodsInfoInStore(int storeId, String categoryCode, int pageNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(ConstantsForPage.PAGE_SIZE, ConstantsForPage.PER_PAGE_SIZE);
		params.put(ConstantsForPage.START, ConstantsForPage.PER_PAGE_SIZE * (pageNo-1));
		params.put("categoryCode", categoryCode);
		params.put("storeId", storeId);
		return supplyGoodsInfoDao.queryGoodsInfoInStore(params);
	}

}
