/**   
 * @Title: GoodsList.java 
 * @Package info.bfly.order.mall.controller 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author zeminshao  
 * @date 2017年3月31日 下午2:14:05 
 * @version V1.0   
 */
package info.bfly.crowd.mall.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.core.annotations.ScopeType;
import info.bfly.crowd.mall.model.Goods;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/** 
 * @author  zeminshao: 
 * @date 创建时间：2017年3月31日 下午2:14:05 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  */
/**
 * @ClassName: GoodsList
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zeminshao
 * @date 2017年3月31日 下午2:14:05
 * 
 */
@Component
@Scope(ScopeType.VIEW)
public class GoodsList extends EntityQuery<Goods> implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = -1366996775564774619L;

	private static final String lazyModelCountHql = "select count(distinct goods) from Goods goods ";

	private static final String lazyModelHql = "select distinct goods from Goods goods ";

	private Date searchCommitMinTime;

	private Date searchCommitMaxTime;
	
	private String traceTemplateId;

	public GoodsList() {
		setCountHql(lazyModelCountHql);
		setHql(lazyModelHql);
		final String[] RESTRICTIONS = {
				"goods.id like #{goodsList.example.id}",
				"goods.goodsName like #{goodsList.example.goodsName}",
				"goods.traceTemplate.id = #{goodsList.traceTemplateId}",
				"goods.commitTime >= #{goodsList.searchCommitMinTime}",
				"goods.commitTime <= #{goodsList.searchCommitMaxTime}" };
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	}

	public Date getSearchCommitMinTime() {
		return searchCommitMinTime;
	}

	public void setSearchCommitMinTime(Date searchCommitMinTime) {
		this.searchCommitMinTime = searchCommitMinTime;
	}

	public Date getSearchCommitMaxTime() {
		return searchCommitMaxTime;
	}

	public void setSearchCommitMaxTime(Date searchCommitMaxTime) {
		this.searchCommitMaxTime = searchCommitMaxTime;
	}

	public String getTraceTemplateId() {
		return traceTemplateId;
	}

	public void setTraceTemplateId(String traceTemplateId) {
		this.traceTemplateId = traceTemplateId;
	}
}
