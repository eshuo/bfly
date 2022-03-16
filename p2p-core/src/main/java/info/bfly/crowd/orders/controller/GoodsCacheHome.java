/**   
 * @Title: GoodsCacheHome.java 
 * @Package info.bfly.order.mall.controller 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author zeminshao  
 * @date 2017年4月1日 下午2:55:36 
 * @version V1.0   
 */
package info.bfly.crowd.orders.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;




import info.bfly.crowd.mall.model.Goods;
import info.bfly.crowd.mall.service.GoodsService;
import info.bfly.crowd.orders.GoodsCacheContants;
import info.bfly.crowd.orders.model.GoodsCache;
import info.bfly.crowd.orders.service.GoodsCacheService;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/** 
 * @author  zeminshao: 
 * @date 创建时间：2017年4月1日 下午2:55:36 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  */
/**
 * @ClassName: GoodsCacheHome
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zeminshao
 * @date 2017年4月1日 下午2:55:36
 * 
 */
@Component
@Scope(ScopeType.VIEW)
public class GoodsCacheHome extends EntityHome<GoodsCache> implements
		Serializable {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = -5676979247812990708L;
	
	@Resource
	private GoodsCacheService goodsCacheService;
	@Resource
	private GoodsService goodsService;
	
	List<Goods>  chooseGoods;

	
	public GoodsCacheHome() {
		setUpdateView(FacesUtil
				.redirect(GoodsCacheContants.View.GOODS_CACHE_LIST));
		setDeleteView(FacesUtil
				.redirect(GoodsCacheContants.View.GOODS_CACHE_LIST));
	}
	
	
	public String createGoodsCache() {
		GoodsCache goodsCache = this.getInstance();
		goodsCacheService.createGoodsCache(goodsCache);
		return FacesUtil.redirect(this.getUpdateView());
	}
	
	public String updateGoodsCache(){
		goodsCacheService.updateGoodsCache(this.getInstance());
		return FacesUtil.redirect(this.getUpdateView());
	}

    public List<Goods> getChooseGoods() {
        return goodsService.chooseGoods(this.getId());
    }


    public void setChooseGoods(List<Goods> chooseGoods) {
        this.chooseGoods = chooseGoods;
    }

}
