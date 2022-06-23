/**   
 * @Title: GoodsCacheList.java 
 * @Package info.bfly.order.mall.controller 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author zeminshao  
 * @date 2017年4月1日 下午2:57:10 
 * @version V1.0   
 */
package info.bfly.crowd.orders.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.crowd.mall.service.MallStageService;
import info.bfly.crowd.orders.GoodsCacheContants;
import info.bfly.crowd.orders.model.GoodsCache;
import info.bfly.crowd.orders.service.GoodsCacheService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/** 
 * @author  zeminshao: 
 * @date 创建时间：2017年4月1日 下午2:57:10 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  */
/**
 * @ClassName: GoodsCacheList
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zeminshao
 * @date 2017年4月1日 下午2:57:10
 * 
 */
@Component
@Scope(ScopeType.VIEW)
public class GoodsCacheList extends EntityQuery<GoodsCache> implements
		Serializable {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 8627641614871731881L;

	private final String countHql = "select count(distinct goodsCache) from GoodsCache goodsCache";

	private final String hql = "select distinct goodsCache from GoodsCache goodsCache";
	
	@Resource
	private GoodsCacheService goodsCacheService;
	
	@Resource 
	private MallStageService mallStageService;

	private Double searchMinMoney;

	private Double searchMaxMoney;

	private String goodsId;
	
	private List<GoodsCache> showGoodsCacheList =new ArrayList<GoodsCache>();
	
	private List<GoodsCache> chooseGoodsCacheList;
	
	private String mallStageId;
	
	public void chooseGoodsCacheList(){
	    chooseGoodsCacheList = new ArrayList<GoodsCache>();
	    List<GoodsCache> chooseList = goodsCacheService.QueryGoodsCacheByMallStageId(mallStageId);
	    if(null != chooseList&&chooseList.size()!=0){
	        chooseGoodsCacheList.addAll(chooseList);
	    }
	}
	
	
	public List<GoodsCache> getShowGoodsCacheList() {
	    List<GoodsCache> notChooseList = goodsCacheService.getNotChooseGoodsCacheList();
	    if(null != notChooseList && notChooseList.size() != 0)
            showGoodsCacheList.addAll(notChooseList);
	    
	    List<GoodsCache> chooseList = goodsCacheService.QueryGoodsCacheByMallStageId(mallStageId);
        if(null != chooseList&&chooseList.size()!=0){
            chooseGoodsCacheList.addAll(chooseList);
        }

        return showGoodsCacheList;
    }
	
	public String updateCache(){
	    goodsCacheService.cleanAllGoodsCacheByMallStageId(mallStageId);
	    goodsCacheService.updateGoodsCacheAll(chooseGoodsCacheList, mallStageId);
        return FacesUtil.redirect(GoodsCacheContants.View.GOODS_CACHE_LIST);
	    
	}

    public void setShowGoodsCacheList(List<GoodsCache> showGoodsCacheList) {
        this.showGoodsCacheList = showGoodsCacheList;
    }

    public List<GoodsCache> getChooseGoodsCacheList() {
        return chooseGoodsCacheList;
    }

    public void setChooseGoodsCacheList(List<GoodsCache> chooseGoodsCacheList) {
        this.chooseGoodsCacheList = chooseGoodsCacheList;
    }

    public String getMallStageId() {
        return mallStageId;
    }

    public void setMallStageId(String mallStageId) {
        this.mallStageId = mallStageId;
    }

    public GoodsCacheList() {
		this.setCountHql(countHql);
		this.setHql(hql);
		final String[] RESTRICTIONS = {
				"goodsCache.cache is null",
				"goodsCache.id like #{goodsCacheList.example.id}",
				"goodsCache.num = #{goodsCacheList.example.num}",
				"goodsCache.money >= #{goodsCacheList.searchMinMoney}",
				"goodsCache.money <= #{goodsCacheList.searchMaxMoney}",
				"goodsCache.good.id = #{goodsCacheList.goodsId}" };
		this.setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public Double getSearchMinMoney() {
		return searchMinMoney;
	}

	public void setSearchMinMoney(Double searchMinMoney) {
		this.searchMinMoney = searchMinMoney;
	}

	public Double getSearchMaxMoney() {
		return searchMaxMoney;
	}

	public void setSearchMaxMoney(Double searchMaxMoney) {
		this.searchMaxMoney = searchMaxMoney;
	}

}
