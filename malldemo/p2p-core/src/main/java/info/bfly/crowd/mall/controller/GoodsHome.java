/**   
 * @Title: GoodsHome.java 
 * @Package info.bfly.order.mall 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author zeminshao  
 * @date 2017年3月31日 下午2:12:12 
 * @version V1.0   
 */
package info.bfly.crowd.mall.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;

import info.bfly.crowd.mall.GoodsContants;
import info.bfly.crowd.mall.model.Goods;
import info.bfly.crowd.mall.service.GoodsService;

import java.io.Serializable;
import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/** 
 * @author  zeminshao: 
 * @date 创建时间：2017年3月31日 下午2:12:12 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  */
/**
 * @ClassName: GoodsHome
 * @Description: TODO(回报物管理类)
 * @author zeminshao
 * @date 2017年3月31日 下午2:12:12
 * 
 */
@Component
@Scope(ScopeType.VIEW)
public class GoodsHome extends EntityHome<Goods> implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 4285589170779655937L;

	@Resource
	private GoodsService goodsService;

	public GoodsHome() {
		this.setUpdateView(FacesUtil.redirect(GoodsContants.View.GOODS_LIST));
		this.setDeleteView(FacesUtil.redirect(GoodsContants.View.GOODS_LIST));
	}

	/**
	 * 
	* @Title: deleteGoods 
	* @Description:  删除回报物，判断是否已经管理模板
	* @param @param id
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public String deleteGoods(String id) {
		goodsService.deleteGoods(id);
		return FacesUtil.redirect(this.getUpdateView());
	}

	/**
	 * 
	 * @Title: createGoods
	 * @Description: 新建回报物
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String createGoods() {
		Goods goods = this.getInstance();
		goodsService.createGoods(goods);
		return FacesUtil.redirect(this.getUpdateView());
	}

	/**
	 * 
	 * @Title: updateGoods
	 * @Description: 修改回报物
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String updateGoods() {
		goodsService.updateGoods(this.getInstance());
		return FacesUtil.redirect(this.getUpdateView());
	}

	/**
	 * 
	 * @Title: getTraceTemplateByGoods
	 * @Description: 根据回报物id查询可追溯模板
	 * @param @param goodsId
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getTraceTemplateByGoods(String goodsId) {
		Goods goods = new Goods();
		goods.setId(goodsId);
		return goodsService.getTraceTemplateByGoods(goods).getTemplateName();
	}

}
