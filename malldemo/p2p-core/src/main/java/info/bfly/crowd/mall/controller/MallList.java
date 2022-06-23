/**   
* @Title: MallList.java 
* @Package info.bfly.order.loan.controller 
* @Description: TODO(用一句话描述该文件做什么) 
* @author zeminshao  
* @date 2017年4月13日 下午2:58:11 
* @version V1.0   
*/package info.bfly.crowd.mall.controller;

import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.loan.controller.LoanList;

import java.io.Serializable;
import java.util.Arrays;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/** 
 * @author  zeminshao: 
 * @date 创建时间：2017年4月13日 下午2:58:11 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  */
/** 
 * @ClassName: MallList 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zeminshao
 * @date 2017年4月13日 下午2:58:11 
 *  
 */

@Component
@Scope(ScopeType.VIEW)
public class MallList extends LoanList implements Serializable {

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = -5044431324254623557L;
	
	private static final String lazyModelCountHql = "select count(distinct loan) from Loan loan ";

	private static final String lazyModelHql = "select distinct loan from Loan loan ";
	
	
    public MallList() {
        setCountHql(lazyModelCountHql);
        setHql(lazyModelHql);
        final String[] RESTRICTIONS = {"loan.businessType = '众筹'"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
}
