package com.zaijiadd.app.store.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zaijiadd.app.store.entity.GoodsInfoEntity;
import com.zaijiadd.app.store.entity.SupplyGoodsInfoEntity;

/**
 * 查找所有的商品信息
 * @author chensl
 *
 */
public interface SupplyGoodsInfoDao {
	
	public List<SupplyGoodsInfoEntity> queryGoodsInfo(Map<String, Object> params);
	
	/**
	 * 根据商品名模糊查询商品信息
	 * @param params
	 * @return
	 */
	public List<GoodsInfoEntity> queryGoodsInfoByGoodsName(Map<String, Object> params);
	
	/**
	 * 查找某商店下的产品,可按类型查找
	 * @param params
	 * @return
	 */
	public List<SupplyGoodsInfoEntity> queryGoodsInfoInStore(Map<String, Object> params);
	
	/**
	 * 为店铺新增商品
	 * @param supplyGoodsInfo
	 * @return
	 */
	public void addSupplyGoods(SupplyGoodsInfoEntity supplyGoodsInfo);
	
	/**
	 * 在店铺中是否存在某货物
	 * @param goodsInfoId
	 * @return
	 */
	public SupplyGoodsInfoEntity isHasGoodsInSupplyGoodsInfo(SupplyGoodsInfoEntity supplyGoodsInfo);
	
	/**
	 * 修改产品信息
	 * @param id
	 */
	public void updateSupplyGoods(SupplyGoodsInfoEntity supplyGoodsInfo);
	
	/**
	 * 批量下架
	 * @param ids
	 */
	public void batchUndercarriage(@Param("ids")List<Integer> ids);
	
	/**
	 * 售出物品后修改已经售出件数
	 * @param params
	 */
	public void saleOutGoods(Map<String, Object> params);
	
	/**
	 * 根据supply_goods_info中的id查询某产品的详细情况
	 * @param id
	 * @return
	 */
	public SupplyGoodsInfoEntity getGoodsInfoById(Integer id);
}
