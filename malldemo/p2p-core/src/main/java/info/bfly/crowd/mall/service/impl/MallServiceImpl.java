/**   
 * @Title: MallServiceImpl.java 
 * @Package info.bfly.order.loan.service.impl 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author zeminshao  
 * @date 2017年4月14日 下午3:06:17 
 * @version V1.0   
 */
package info.bfly.crowd.mall.service.impl;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import info.bfly.archer.common.exception.NoMatchingObjectsException;
import info.bfly.archer.user.model.Area;
import info.bfly.core.util.ArithUtil;
import info.bfly.crowd.mall.service.MallService;
import info.bfly.p2p.loan.model.Loan;

/** 
 * @author  zeminshao: 
 * @date 创建时间：2017年4月14日 下午3:06:17 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  */
/**
 * @ClassName: MallServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zeminshao
 * @date 2017年4月14日 下午3:06:17
 * 
 */
@Service("mallService")
public class MallServiceImpl implements MallService {

    @Resource
    HibernateTemplate ht;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void updateMall(Loan mall) {
        // TODO Auto-generated method stub
        ht.update(mall);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void removeMallArea(Loan mall, Area area) {
        // TODO Auto-generated method stub
        List<Area> mallAreas = mall.getAreas();
        for (Iterator iterator = mallAreas.iterator(); iterator.hasNext();) {
            Area role2 = (Area) iterator.next();
            if (role2.getId().equals(area.getId())) {
                iterator.remove();
            }
        }
        ht.update(mall);
    }

    @Override
    public Long countMallSupportNum(String loanId) {
        List<Object> supportNum = (List<Object>) ht
                .find("select count(order1.user) from Order order1 left join order1.orderCaches caches where caches.mallStageCache.mallStage.loan.id = ? and ( order1.orderStatus = 'payed' or order1.orderStatus = 'finished') ",
                        loanId);
        Long sumNum = supportNum.get(0) == null ? 0 : (Long) supportNum.get(0);
        return sumNum;
    }

    @Override
    public List<Loan> getRecommendCrowFundingList() {
        List<Loan> recommendList = (List<Loan>) ht
                .find("Select distinct loan from Loan loan left join loan.loanAttrs attr where attr.id =? and loan.status='raising' order by loan.commitTime desc",
                        "recommend");
        if(null != recommendList && recommendList.size() !=0){
            if(recommendList.size() >= 4){
                List<Loan> tempList = recommendList.subList(0, 4);
                return tempList;
            }
        }
        return recommendList;
    }
    
    @Override
    public Double calculateHavedMallMoney(String loanId){
        List<Object> havedMallMoney = (List<Object>) ht
                .find("select sum(order1.invest.money) from Order order1 left join order1.orderCaches caches where caches.mallStageCache.mallStage.loan.id = ? and ( order1.orderStatus = 'payed' or order1.orderStatus = 'finished') ",
                        loanId);
        double sumMoney = havedMallMoney.get(0) == null ? 0D : (Double) havedMallMoney.get(0);
        return sumMoney;
    }

    @Override
    public Double calculateHavedMallMoneyRate(String loanId)
            throws NoMatchingObjectsException {
        Loan loan = ht.get(Loan.class, loanId);
        if (loan == null) {
            throw new NoMatchingObjectsException(Loan.class, "cannot find loan by loanId:" + loanId);
        }
        Double havedMallMoney = calculateHavedMallMoney(loanId);
        return ArithUtil.round(havedMallMoney / loan.getLoanMoney() * 100, 2);
    }

}
