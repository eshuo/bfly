package info.bfly.p2p.statistics.controller;

import info.bfly.archer.user.model.Area;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.loan.LoanConstants;
import info.bfly.p2p.loan.LoanConstants.LoanStatus;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

/**
 * 借款统计
 *
 * @author Administrator
 *
 */
@Component
@Scope(ScopeType.REQUEST)
public class LoanStatistics {
    @Resource
    HibernateTemplate ht;

    /**
     * 发起借款的数量
     *
     * @return
     */
    public long getLoanAmount(String userId) {
        String hql = "select count(*) from Loan loan where loan.user.id=?";
        return (Long) ht.iterate(hql, userId).next();
    }

    /**
     * 获取融资笔数
     *
     * @return
     */
    public Long getLoanCount() {
        String hql = "select count(loan) from Loan loan";
        List<Object> oos = (List<Object>) ht.find(hql);
        Object o = oos.get(0);
        if (o == null) {
            return 0L;
        }
        return (Long) o;
    }

    /**
     * 获取某种状态中的融资笔数
     *
     * @return
     */
    public Long getLoanCount(String loanStatus) {
        String hql = "select count(loan) from Loan loan where loan.status=?";
        List<Object> oos = (List<Object>) ht.find(hql, loanStatus);
        Object o = oos.get(0);
        if (o == null) {
            return 0L;
        }
        return (Long) o;
    }

    /**
     * 获取某种状态中的融资笔数
     *
     * @return
     */
    public Long getLoanCount(String loanStatus, String businessType) {
        String hql = "select count(loan) from Loan loan where loan.status=? and loan.businessType=?";
        List<Object> oos = (List<Object>) ht.find(hql, new String[]{loanStatus, businessType});
        Object o = oos.get(0);
        if (o == null) {
            return 0L;
        }
        return (Long) o;
    }

    /**
     * 获取融资笔数(募集中和成功的) raising and success
     *
     * @return
     */
    public Long getLoanRSCount() {
        String hql = "select count(loan) from Loan loan where loan.status in(?,?,?,?,?,?,?)";
        List<Object> oos = (List<Object>) ht.find(hql, new String[] { LoanStatus.BAD_DEBT, LoanStatus.COMPLETE, LoanStatus.OVERDUE, LoanStatus.RAISING, LoanStatus.RECHECK, LoanStatus.REPAYING,
                LoanStatus.WAITING_RECHECK_VERIFY });
        Object o = oos.get(0);
        if (o == null) {
            return 0L;
        }
        return (Long) o;
    }

    /**
     * 获取融资笔数(募集中和成功的) raising and success
     *
     * @return
     */
    public Long getLoanRSCount(String businessType) {
        String hql = "select count(loan) from Loan loan where loan.status in(?,?,?,?,?,?,?) and loan.businessType=?";
        List<Object> oos = (List<Object>) ht.find(hql, new String[]{LoanStatus.BAD_DEBT, LoanStatus.COMPLETE, LoanStatus.OVERDUE, LoanStatus.RAISING, LoanStatus.RECHECK, LoanStatus.REPAYING,
                LoanStatus.WAITING_RECHECK_VERIFY, businessType});
        Object o = oos.get(0);
        if (o == null) {
            return 0L;
        }
        return (Long) o;
    }

    /**
     * 逾期金额
     */
    public double getOverdueMoney(String userId) {
        String hql = "select sum(lr.corpus+lr.interest+lr.defaultInterest+lr.fee) from LoanRepay lr " + "where lr.repayDay<? and lr.loan.user.id=?";
        List<Object> oos = (List<Object>) ht.find(hql, new Date(), userId);
        Object o = oos.get(0);
        if (o == null) {
            return 0;
        }
        return (Double) o;
    }

    /**
     * 获取某用户总的借款金额（不管借款成功与否）
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    public double getSumLoan(String userId) {
        List sumLoanMoney = ht.find("select sum(loan.money) from Loan loan where loan.user.id=?", userId);
        if (sumLoanMoney == null || sumLoanMoney.size() == 0 || sumLoanMoney.contains(null)) {
            return 0.0;
        }
        else {
            return (Double) sumLoanMoney.get(0);
        }
    }

    /**
     * 获取用户总的借款金额(成功的借款)
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    public double getSumLoanMoney(String userId) {
        List sumLoanMoney = ht.find("select sum(loan.money) from Loan loan where loan.user.id=? and loan.status in (?,?,?)", new String[] { userId, LoanConstants.LoanStatus.RAISING,
                LoanConstants.LoanStatus.RECHECK, LoanConstants.LoanStatus.REPAYING, });
        if (sumLoanMoney == null || sumLoanMoney.size() == 0 || sumLoanMoney.contains(null)) {
            return 0.0;
        }
        else {
            return (Double) sumLoanMoney.get(0);
        }
    }

    /**
     * 待还金额笔数，还款日小于某个date的
     */
    public Long getWaitRepayCount(String userId, Date date) {
        String hql = "select count(lr) from LoanRepay lr " + "where lr.time is null and lr.loan.user.id=? and lr.repayDay<?";
        List<Object> oos = (List<Object>) ht.find(hql, userId, date);
        Object o = oos.get(0);
        if (o == null) {
            return 0L;
        }
        return (Long) o;
    }

    /**
     * 待还金额
     */
    public double getWaitRepayMoney(String userId) {
        String hql = "select sum(lr.corpus+lr.interest+lr.defaultInterest+lr.fee) from LoanRepay lr " + "where lr.time is null and lr.loan.user.id=?";
        List<Object> oos = (List<Object>) ht.find(hql, userId);
        Object o = oos.get(0);
        if (o == null) {
            return 0;
        }
        return (Double) o;
    }

    /**
     * 待还金额，还款日小于某个date的
     */
    public double getWaitRepayMoney(String userId, Date date) {
        String hql = "select sum(lr.corpus+lr.interest+lr.defaultInterest+lr.fee) from LoanRepay lr " + "where lr.time is null and lr.loan.user.id=? and lr.repayDay<?";
        List<Object> oos = (List<Object>) ht.find(hql, userId, date);
        Object o = oos.get(0);
        if (o == null) {
            return 0;
        }
        return (Double) o;
    }
    
    /**
     * 
    * @Title: getALLAreaName 
    * @Description:  根据区域id查询全部中文名
    * @param @param areaId
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    public String getALLAreaName(String areaId){
        Area area = ht.get(Area.class, areaId);
        if(null != area)
            return returnAllName(area);
       return "";
    }
    
    /**
     * 
    * @Title: getMobileAreaId 
    * @Description:  
    * @param @param areaId
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    public String getMobileAreaId(String areaId){
        Area area = ht.get(Area.class, areaId);
        if(null != area)
            return  "/" + returnAllId(area);
       return "";
    }
    
    
    /**
     * 
    * @Title: returnAllId 
    * @Description: 获取手机端地区数据
    * /410000/411600/411623
    * 空值用*替代
    * @param @param area
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    private  String returnAllId(Area area){
        if(null ==area)
            return "*";
        if (null == area.getParent()) {
            return area.getId();
        } else{
            return returnAllId(area.getParent()) +"/" + area.getId();
        }
        
    }
    
    /**
     * 
    * @Title: returnAllName 
    * @Description:  显示所有配送范围中文名,从大到小
    * @param @param mallArea
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    private  String returnAllName(Area area){
        if(null ==area)
            return "";
        if (null == area.getParent()) {
            return area.getName();
        } else{
            return returnAllName(area.getParent()) +"-" + area.getName();
        }
        
    }
}
