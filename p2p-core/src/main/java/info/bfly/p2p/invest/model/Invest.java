package info.bfly.p2p.invest.model;

// default package

import info.bfly.archer.user.model.User;
import info.bfly.core.util.ArithUtil;
import info.bfly.crowd.orders.model.Order;
import info.bfly.p2p.coupon.model.UserCoupon;
import info.bfly.p2p.loan.LoanConstants.RepayStatus;
import info.bfly.p2p.loan.model.Loan;
import info.bfly.p2p.repay.model.InvestRepay;
import info.bfly.p2p.repay.model.RepayRoadmap;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Investment entity. 投资实体
 */
@Entity
@Table(name = "invest")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class Invest implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1771986926931233207L;
    // Fields
    private String  id;
    private User    user;
    private Loan    loan;
    private Date    time;
    private Boolean isAutoInvest;
    private String  status;
    // 注意，此处存储的不是百分比利率
    private Double  rate;
    // 利率百分比形式
    private Double  ratePercent;
    // 投资类型(本金保障计划之类)
    private String  type;
    /**
     * 持有本金，依据此金额计算还款时应得金额。
     */
    private Double  money;
    /**
     * 投资的金额
     */
    private Double  investMoney;

    /**
     * 投资记录id 如果有2份，就是“001,002”类似这样
     */
    private String        investId;
    // 支付帐号类型
    private String        accountType;
    //支付方式
    private String        payMethod;
    /**
     * 优惠券
     */
    private UserCoupon    userCoupon;
    /**
     * 如果该投资是购买的债权，则此字段记录申请债权转让的信息。
     */
    private TransferApply transferApply;
    private List<TransferApply> transferApplies = new ArrayList<TransferApply>(0);
    private List<InvestRepay>   investRepays    = new ArrayList<InvestRepay>(0);
    // Constructors
    /**
     * 还款路标（待还、已还金额之类）
     */
    private RepayRoadmap repayRoadmap;

    /**
     * 订单编号
     */
    private String operationOrderNo;

    private Order order;


    /**
     * default constructor
     */
    public Invest() {
    }

    /**
     * 深度克隆对象
     *
     * @return
     */
    public Object copy() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
        return ois.readObject();
    }

    @Column(name = "accountType", length = 50)
    public String getAccountType() {
        return accountType;
    }
    @Column(name = "pay_method", length = 50)
    public String getPayMethod(){
        return payMethod;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "invest_id", length = 100)
    public String getInvestId() {
        return investId;
    }

    @Column(name = "invest_money", nullable = false, precision = 22, scale = 0)
    public Double getInvestMoney() {
        return investMoney;
    }

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "invest")
    @OrderBy(value = "period")
    public List<InvestRepay> getInvestRepays() {
        return investRepays;
    }


    @Column(name = "is_auto_invest", columnDefinition = "BOOLEAN")
    public Boolean getIsAutoInvest() {
        return isAutoInvest;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    public Loan getLoan() {
        return loan;
    }

    @Column(name = "money", nullable = false, precision = 22, scale = 0)
    public Double getMoney() {
        return money;
    }

    @Column(name = "rate", precision = 22, scale = 0)
    public Double getRate() {
        return rate;
    }

    @Column(name = "order_no", unique = true, length = 64)
    public String getOperationOrderNo() {
        return operationOrderNo;
    }


    @Transient
    public Double getRatePercent() {
        if (ratePercent == null && getRate() != null) {
            return getRate() * 100;
        }
        return ratePercent;
    }

    @Transient
    public RepayRoadmap getRepayRoadmap() {
        if (repayRoadmap == null) {
            // 未还本金
            Double unPaidCorpus = 0D;
            // 未还利息
            Double unPaidInterest = 0D;
            // 未还手续费
            Double unPaidFee = 0D;
            // 未还罚息
            Double unPaidDefaultInterest = 0D;
            // 已还本金
            Double paidCorpus = 0D;
            // 已还利息
            Double paidInterest = 0D;
            // 已还手续费
            Double paidFee = 0D;
            // 已还罚息
            Double paidDefaultInterest = 0D;
            // 下个还款日
            Date nextRepayDate = null;
            // 下个还款本金
            Double nextRepayCorpus = null;
            // 下个还款利息
            Double nextRepayInterest = null;
            // 下个还款手续费
            Double nextRepayFee = null;
            // 下个还款罚息
            Double nextRepayDefaultInterest = null;
            // 已还期数
            int paidPeriod = 0;
            for (int i = 0; i < getInvestRepays().size(); i++) {
                InvestRepay ir = getInvestRepays().get(i);
                if (ir.getStatus().equals(RepayStatus.BAD_DEBT) || ir.getStatus().equals(RepayStatus.OVERDUE) || ir.getStatus().equals(RepayStatus.REPAYING)) {
                    unPaidCorpus = ArithUtil.add(unPaidCorpus, ir.getCorpus());
                    unPaidInterest = ArithUtil.add(unPaidInterest, ir.getInterest());
                    unPaidFee = ArithUtil.add(unPaidFee, ir.getFee());
                    unPaidDefaultInterest = ArithUtil.add(unPaidDefaultInterest, ir.getDefaultInterest());
                    if (i == 0 || getInvestRepays().get(i - 1).getStatus().equals(RepayStatus.COMPLETE)) {
                        // 下一期待还款
                        nextRepayDate = ir.getRepayDay();
                        nextRepayCorpus = ir.getCorpus();
                        nextRepayInterest = ir.getInterest();
                        nextRepayFee = ir.getFee();
                        nextRepayDefaultInterest = ir.getDefaultInterest();
                    }
                } else if (ir.getStatus().equals(RepayStatus.COMPLETE)) {
                    paidCorpus = ArithUtil.add(paidCorpus, ir.getCorpus());
                    paidInterest = ArithUtil.add(paidInterest, ir.getInterest());
                    paidFee = ArithUtil.add(paidFee, ir.getFee());
                    paidDefaultInterest = ArithUtil.add(paidDefaultInterest, ir.getDefaultInterest());
                    paidPeriod++;
                }
            }
            repayRoadmap = new RepayRoadmap(unPaidCorpus, unPaidInterest, unPaidFee, unPaidDefaultInterest, paidCorpus, paidInterest, paidFee, paidDefaultInterest, nextRepayDate, nextRepayCorpus,
                    nextRepayInterest, nextRepayFee, nextRepayDefaultInterest, paidPeriod, getInvestRepays().size() - paidPeriod, false);
        }
        return repayRoadmap;
    }

    @Column(name = "status", nullable = false, length = 50)
    public String getStatus() {
        return status;
    }

    @Column(name = "time", nullable = false, length = 19)
    public Date getTime() {
        return time;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "invest")
    public List<TransferApply> getTransferApplies() {
        return transferApplies;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_apply")
    public TransferApply getTransferApply() {
        return transferApply;
    }

    @Column(name = "type", length = 100)
    public String getType() {
        return type;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_coupon")
    public UserCoupon getUserCoupon() {
        return userCoupon;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setInvestId(String investId) {
        this.investId = investId;
    }

    public void setInvestMoney(Double investMoney) {
        this.investMoney = investMoney;
    }

    public void setInvestRepays(List<InvestRepay> investRepays) {
        this.investRepays = investRepays;
    }

    public void setIsAutoInvest(Boolean isAutoInvest) {
        this.isAutoInvest = isAutoInvest;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public void setRatePercent(Double ratePercent) {
        if (ratePercent != null) {
            rate = ArithUtil.div(ratePercent, 100, 4);
        }
        this.ratePercent = ratePercent;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setTransferApplies(List<TransferApply> transferApplies) {
        this.transferApplies = transferApplies;
    }

    public void setTransferApply(TransferApply transferApply) {
        this.transferApply = transferApply;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserCoupon(UserCoupon userCoupon) {
        this.userCoupon = userCoupon;
    }

    public void setOperationOrderNo(String operationOrderNo) {
        this.operationOrderNo = operationOrderNo;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }
}
