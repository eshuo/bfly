package info.bfly.p2p.loan.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.loan.LoanConstants;
import info.bfly.p2p.loan.model.Loan;
import info.bfly.p2p.loan.service.LoanService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Description: 借款查询相关
 */
@Component
@Scope(ScopeType.VIEW)
public class LoanList extends EntityQuery<Loan> implements Serializable {
    private static final long   serialVersionUID  = 7820000598089306972L;
    private static final String lazyModelCountHql = "select count(distinct loan) from Loan loan left join loan.loanAttrs attr ";
    private static final String lazyModelHql      = "select distinct loan from Loan loan left join loan.loanAttrs attr";
    private Double              loanMoney;
    private String              loanPurpose;
    private String              riskLevel;
    private Double              indexLoanRate;
    // 还款时间
    private Integer             minDeadline;
    private Integer             maxDeadline;
    // 实际借款金额
    private Double              minMoney;
    private Double              maxMoney;
    // 借款金额
    private Double              minLoanMoney;
    private Double              maxLoanMoney;
    // 利率
    private Double              minRate;
    private Double              maxRate;
    private Date                searchCommitMinTime;
    private Date                searchCommitMaxTime;
    @Resource
    private LoanService         loanService;

    public LoanList() {
        setCountHql(LoanList.lazyModelCountHql);
        setHql(LoanList.lazyModelHql);
        final String[] RESTRICTIONS = {"loan.id like #{loanList.example.id}", "loan.repayType like #{loanList.example.repayType}",
                "loan.name like #{loanList.example.name}", "loan.rate >=#{loanList.minRate}", "loan.rate <=#{loanList.maxRate}", "loan.status like #{loanList.example.status}",
                "loan.riskLevel like #{loanList.example.riskLevel}", "loan.type like #{loanList.example.type}", "loan.user.id = #{loanList.example.user.id}",
                "loan.user.username like #{loanList.example.user.username}", "loan.businessType like #{loanList.example.businessType}", "loan.projectDuration >= #{loanList.minDeadline}",
                "loan.projectDuration <= #{loanList.maxDeadline}", "loan.money >= #{loanList.minMoney}", "loan.money <= #{loanList.maxMoney}", "loan.loanMoney >= #{loanList.minLoanMoney}",
                "loan.loanMoney <= #{loanList.maxLoanMoney}", "loan.commitTime >= #{loanList.searchCommitMinTime}", "loan.commitTime <= #{loanList.searchCommitMaxTime}",
                "loan.loanPurpose like #{loanList.example.loanPurpose}" };
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    /**
     * 计算所有的企业融资金额
     *
     * @return
     */
    public double getAllLoansMoney() {
        String hql = "select sum(loan.money) from Loan loan where loan.status = ? or loan.status = ? or loan.status = ? or loan.status = ? or loan.status = ? or loan.status = ?";
        List<Object> oos = (List<Object>) getHt().find(hql, LoanConstants.LoanStatus.RAISING, LoanConstants.LoanStatus.RECHECK, LoanConstants.LoanStatus.REPAYING,
                LoanConstants.LoanStatus.OVERDUE, LoanConstants.LoanStatus.COMPLETE, LoanConstants.LoanStatus.BAD_DEBT);
        return (Double) oos.get(0);
    }

    public Double getIndexLoanRate() {
        return indexLoanRate;
    }


    public Double getLoanMoney() {
        return loanMoney;
    }

    public String getLoanPurpose() {
        return loanPurpose;
    }

    public Integer getMaxDeadline() {
        return maxDeadline;
    }

    public Double getMaxLoanMoney() {
        return maxLoanMoney;
    }

    public Double getMaxMoney() {
        return maxMoney;
    }

    public Double getMaxRate() {
        return maxRate;
    }

    public Integer getMinDeadline() {
        return minDeadline;
    }

    public Double getMinLoanMoney() {
        return minLoanMoney;
    }

    public Double getMinMoney() {
        return minMoney;
    }

    public Double getMinRate() {
        return minRate;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public Date getSearchCommitMaxTime() {
        return searchCommitMaxTime;
    }

    public Date getSearchCommitMinTime() {
        return searchCommitMinTime;
    }

    @Override
    protected void initExample() {
        Loan loan = new Loan();
        loan.setUser(new User());
        setExample(loan);
    }


    public void setIndexLoanRate(Double indexLoanRate) {
        this.indexLoanRate = indexLoanRate;
    }
    public void setIndexLoanRateAndPage(Double indexLoanRate) {
        this.indexLoanRate = indexLoanRate;
    }

    public void setLoanMoney(Double loanMoney) {
        this.loanMoney = loanMoney;
    }

    public void setLoanPurpose(String loanPurpose) {
        this.loanPurpose = loanPurpose;
    }


    public void setMaxDeadline(Integer maxDeadline) {

        this.maxDeadline = maxDeadline;
    }

    public void setMaxLoanMoney(Double maxLoanMoney) {
        this.maxLoanMoney = maxLoanMoney;
    }

    public void setMaxMoney(Double maxMoney) {
        this.maxMoney = maxMoney;
    }

    public void setMaxRate(Double maxRate) {
        this.maxRate = maxRate;
    }

    /**
     * 筛选借款期限
     *
     * @param minDeadline
     * @param maxDeadline
     */
    public void setMinAndMaxDeadline(Integer minDeadline, Integer maxDeadline) {
        setMinDeadline(minDeadline);
        setMaxDeadline(maxDeadline);
    }

    public void setMinAndMaxLoanMoney(Double min, Double max) {
        setMinLoanMoney(min);
        setMaxLoanMoney(max);
    }

    /**
     * 查询，金额范围
     *
     * @param min
     *            最新金额
     * @param max
     *            最大金额
     */
    public void setMinAndMaxMoney(Double min, Double max) {
        setMinMoney(min);
        setMaxMoney(max);
    }

    public void setMinAndMaxRate(Double min, Double max) {
        setMinRate(min);
        setMaxRate(max);
    }

    public void setMinDeadline(Integer minDeadline) {
        this.minDeadline = minDeadline;
    }

    public void setMinLoanMoney(Double minLoanMoney) {
        this.minLoanMoney = minLoanMoney;
    }

    public void setMinMoney(Double minMoney) {
        this.minMoney = minMoney;
    }

    public void setMinRate(Double minRate) {
        this.minRate = minRate;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public void setSearchCommitMaxTime(Date searchCommitMaxTime) {
        this.searchCommitMaxTime = searchCommitMaxTime;
    }

    public void setSearchCommitMinTime(Date searchCommitMinTime) {
        this.searchCommitMinTime = searchCommitMinTime;
    }
    
    
    /**
     * 设置查询参数并初始化当前页数
     */
    
    public void setStatusInitPage(String str){
    	this.getExample().setStatus(str);
    	this.setCurrentPage(1);
    }
    
    public void setMinAndMaxDeadlineInitPage(Integer minDeadline,Integer maxDeadline){
        setMinDeadline(minDeadline);
        setMaxDeadline(maxDeadline);
        this.setCurrentPage(1);
    }
    
    public void setMinAndMaxRateInitPage(Double min, Double max) {
        setMinRate(min);
        setMaxRate(max);
        this.setCurrentPage(1);
    }
    public void setRiskLevelInitPage(String riskLevel){
    	this.getExample().setRiskLevel(riskLevel);
    	this.setCurrentPage(1);
    }
    
    
}
