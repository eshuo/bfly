/**   
* @Title: MallService.java 
* @Package info.bfly.order.loan.service 
* @Description: TODO(用一句话描述该文件做什么) 
* @author zeminshao  
* @date 2017年4月14日 下午3:05:36 
* @version V1.0   
*/package info.bfly.crowd.mall.service;

import java.util.List;

import info.bfly.archer.common.exception.NoMatchingObjectsException;
import info.bfly.archer.user.model.Area;
import info.bfly.p2p.loan.model.Loan;



/** 
 * @author  zeminshao: 
 * @date 创建时间：2017年4月14日 下午3:05:36 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  */
/** 
 * @ClassName: MallService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zeminshao
 * @date 2017年4月14日 下午3:05:36 
 *  
 */
public interface MallService {
	
	
	/**
	 * 
	* @Title: update 
	* @Description:  修改众筹的配送范围
	* @param @param mall    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	void updateMall(Loan mall);
	/**
	 * 
	* @Title: removeMallArea 
	* @Description:  
	* @param @param mall
	* @param @param area    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	void removeMallArea(Loan mall,Area area);
	
	/**
	 * 
	* @Title: countMallSupportNum 
	* @Description:  统计项目的支持人数
	* @param @param loanId
	* @param @return    设定文件 
	* @return Integer    返回类型 
	* @throws
	 */
	Long countMallSupportNum(String loanId);
	
	/**
    * 
    *
    * 获取众筹的首页面推荐项目
    *
    * @Title: getRecommendCrowFundingList 
    * @return    设定文件 
    * @return List<Loan>    返回类型
     */
    List<Loan> getRecommendCrowFundingList();
    
    
    /**
     * 
    * @Title: calculateHavedMallMoney 
    * @Description:  计算众筹已经筹集的资金(已支付的订单)
    * @param @param loanId
    * @param @return
    * @param @throws NoMatchingObjectsException    设定文件 
    * @return Double    返回类型 
    * @throws
     */
    Double calculateHavedMallMoney(String loanId);
    
    /**
     * 
    * @Title: calculateHavedMallMoneyRate 
    * @Description:  计算众筹已经投入的资金比例
    * @param @param loanId
    * @param @return
    * @param @throws NoMatchingObjectsException    设定文件 
    * @return Double    返回类型 
    * @throws
     */
    Double calculateHavedMallMoneyRate(String loanId) throws NoMatchingObjectsException;

	
}
