package info.bfly.p2p.loan.model;

// default package

import info.bfly.archer.banner.model.BannerPicture;
import info.bfly.archer.comment.model.Comment;
import info.bfly.archer.node.model.NodeAttr;
import info.bfly.archer.user.model.Area;
import info.bfly.archer.user.model.User;
import info.bfly.core.util.ArithUtil;
import info.bfly.crowd.mall.model.MallStage;
import info.bfly.p2p.invest.model.Invest;
import info.bfly.p2p.loan.LoanConstants.RepayStatus;
import info.bfly.p2p.repay.model.LoanRepay;
import info.bfly.p2p.repay.model.RepayRoadmap;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Loan entity. // TODO:竞标借款， 担保借款之类
 */
@Entity
@Table(name = "loan")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class Loan implements Serializable, Cloneable {
    private static final long serialVersionUID = -2939112013867572718L;
    // Fields
    private String   id;
    
    


    /**
     * sina标号
     */
    private String   sina_bid_no;
    /**
     * 标的凭证号（Sina返回）
     */
    private String   inner_bid_no;
    // 借款名称
    private String name;
    // 借款说明
    private String description;
    /**
     * 自定义图片
     */
    private String customPicture;
    // 借款用途
    private String loanPurpose;
    // 借款人
    private User user;
    // 审核人
    private User verifyUser;
    // 是否有抵押物
    private String hasPawn;
    // 审核是否通过
    private String verified;
    // 抵押物名称
    private String pawnName;
    // 抵押物
    private String pawn;
    /**
     * 实际借到的金额（放款金额），放款后才有值
     */
    private Double money;

    /**
     * 项目初始借款金额
     */
    private Double initMoney;
    /**
     * 预计的借款金额
     */
    private Double loanMoney;
    // 企业注册号
    private String companyNo;
    /**
     */
    private String companyName;                                   // 企业名称
    /**
     * 最小投资金额
     */
    private Double minInvestMoney;
    /**
     * 最大投资金额
     */
    private Double maxInvestMoney;
    /**
     * 投资递增金额
     */
    private Double cardinalNumber;
    /**
     * 借款保证金
     */
    private Double deposit;
    // 风险等级
    private String riskLevel;
    // 借款类型
    private LoanType type;
    // 利率(如果是竞标借款，则为最高能接受利率)，注意，此处存储的不是百分比利率
    private Double rate;
    // 项目执行利率，仅限于竞标借款。注意，此处存储的不是百分比利率
    private Double actualRate;
    // 项目发起时间
    private Date commitTime;
    private String status;
    /**
     * 逾期信息：未催帐、已催帐、催帐成功、催帐失败
     */
    private String overdueInfo;
    // 审核信息
    private String verifyMessage;
    /**
     * 审核时间
     */
    private Date verifyTime;
    // 放款时间
    private Date giveMoneyTime;
    // 预计开始执行时间
    private Date expectTime;
    /**
     * 完成时间
     */
    private Date completeTime;
    /**
     * 流标时间
     */
    private Date cancelTime;
    // 借款期限
    private Integer deadline;
    // 还款周期
    private String repayPeriod;
    // 还款方式
    private String repayType;
    /**
     * 地点，区域
     */
    private String location;
    /**
     * 合同类型
     */
    private String contractType;
    // 商业类型（企业、个人）
    private String businessType;
    /**
     * 企业借款的首期还款日
     */
    private Date repayDay;
    /**
     * 开始计息日（还款日）
     */
    private Date interestBeginTime;
    /**
     * 借款管理费
     */
    private Double loanGuranteeFee;
    private Integer seqNum;
    /**
     * 视频id
     */
    private String videoId;
    /**
     * 借款手续费，还款时候收取，平均到每笔还款中。
     */
    private Double feeOnRepay;
    /**
     * 投资人手续费，收取的是还款时候所得利息的比例。
     */
    private Double investorFeeRate;
    private List<Invest> invests = new ArrayList<Invest>(0);
    private List<LoanRepay> loanRepays = new ArrayList<LoanRepay>(0);
    private List<Comment> conmments = new ArrayList<Comment>(0);
    private List<NodeAttr> loanAttrs = new ArrayList<NodeAttr>(0);
    //众筹配送范围
    private List<Area> areas = new ArrayList<Area>(0);
    /**
     * 档位
     */
    private List<MallStage> mallstages;
    /********************************************** 企业借款信息 **************************************************/
    /**
     * 担保公司名称
     */
    private String guaranteeCompanyName;
    /**
     * 企业详述
     */
    private String companyDescription;
    /**
     * 资金运转
     */
    private String fundDescription;
    /**
     * 风险控制措施
     */
    private String riskDescription;
    /**
     * 政策及市场分析
     */
    private String policyDescription;
    /**
     * 项目材料
     */
    private List<BannerPicture> loanInfoPics;
    /**
     * 产品说明书(pdf路径)
     */
    private String loanInstruction;
    /**
     * 担保公司详述
     */
    private String guaranteeCompanyDescription;
    /**
     * 担保信息详述
     */
    private String guaranteeInfoDescription;
    /**
     * 担保材料
     */
    private List<BannerPicture> guaranteeInfoPics;
    /**
     * 风险提示函(pdf路径)
     */
    private String riskInstruction;
    /**
     * 还款路标（待还、已还金额之类）
     */
    private RepayRoadmap repayRoadmap;
    /**
     * 项目期限日期（天）
     */
    private Integer projectDuration;

    /**
     * 标的类型
     */
    private String target_type;

    /**
     * 入账账户类型
     */
    private String accountType;
    // Constructors



    /**
     * default constructor
     */
    public Loan() {
    }
    public Loan(String id) {
    	this.id =id;
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "sina_bid_no",length = 64,unique = true)
    public String getSina_bid_no() {
        return sina_bid_no;
    }

    @Column(name = "inner_bid_no",length = 64)
    public String getInner_bid_no() {
        return inner_bid_no;
    }

    /**
     * 如果项目为竞标借款，则返回actualRate，否则返回rate
     *
     * @return
     */
    @Column(name = "actual_rate", precision = 22, scale = 0)
    public Double getActualRate() {
        return actualRate;
    }

    @Column(name = "business_type", length = 20)
    public String getBusinessType() {
        return businessType;
    }

    @Column(name = "cancel_time")
    public Date getCancelTime() {
        return cancelTime;
    }

    @Column(name = "cardinal_number")
    public Double getCardinalNumber() {
        if (cardinalNumber == null) {
            cardinalNumber = 1D;
        }
        return cardinalNumber;
    }

    @Column(name = "commit_time", nullable = false, length = 19)
    public Date getCommitTime() {
        return commitTime;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "company_description", columnDefinition = "longtext")
    public String getCompanyDescription() {
        return companyDescription;
    }

    @Lob
    @Column(name = "company_name", columnDefinition = "CLOB")
    public String getCompanyName() {
        return companyName;
    }

    @Column(name = "companyno")
    public String getCompanyNo() {
        return companyNo;
    }

    @Column(name = "complete_time")
    public Date getCompleteTime() {
        return completeTime;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "loan", orphanRemoval = true)
    @OrderBy("createTime desc")
    public List<Comment> getConmments() {
        return conmments;
    }

    @Lob
    @Column(name = "contract_type", columnDefinition = "CLOB")
    public String getContractType() {
        return contractType;
    }

    @Lob
    @Column(name = "custom_picture", columnDefinition = "CLOB")
    public String getCustomPicture() {
        return customPicture;
    }

    @Column(name = "deadline")
    public Integer getDeadline() {
        return deadline;
    }

    @Column(name = "deposit", precision = 22, scale = 0)
    public Double getDeposit() {
        return deposit;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "description", columnDefinition = "longtext")
    public String getDescription() {
        return description;
    }

    @Column(name = "expect_time")
    public Date getExpectTime() {
        return expectTime;
    }

    @Column(name = "fee_on_repay")
    public Double getFeeOnRepay() {
        return feeOnRepay;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "fund_description", columnDefinition = "longtext")
    public String getFundDescription() {
        return fundDescription;
    }

    @Column(name = "give_money_time")
    public Date getGiveMoneyTime() {
        return giveMoneyTime;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "guarantee_company_description", columnDefinition = "longtext")
    public String getGuaranteeCompanyDescription() {
        return guaranteeCompanyDescription;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "guarantee_company_name", columnDefinition = "longtext")
    public String getGuaranteeCompanyName() {
        return guaranteeCompanyName;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "guarantee_info_description", columnDefinition = "longtext")
    public String getGuaranteeInfoDescription() {
        return guaranteeInfoDescription;
    }

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "loan_guarantee_pics", joinColumns = {@JoinColumn(name = "loan_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "pic_id", nullable = false, updatable = false)})
    public List<BannerPicture> getGuaranteeInfoPics() {
        return guaranteeInfoPics;
    }

    @Column(name = "has_pawn", length = 10)
    public String getHasPawn() {
        return hasPawn;
    }


    @Column(name = "interest_begin_time")
    public Date getInterestBeginTime() {
        return interestBeginTime;
    }

    @Column(name = "investor_fee_rate")
    public Double getInvestorFeeRate() {
        return investorFeeRate;
    }

    @Transient
    public Double getInvestorFeeRatePercent() {
        if (getInvestorFeeRate() != null) {
            return ArithUtil.round(getInvestorFeeRate() * 100, 2);
        }
        return null;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "loan")
    @OrderBy(value = "time desc")
    public List<Invest> getInvests() {
        return invests;
    }

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "loan_node_attr", joinColumns = {@JoinColumn(name = "loan_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "node_attr_id", nullable = false, updatable = false)})
    public List<NodeAttr> getLoanAttrs() {
        return loanAttrs;
    }

    @Column(name = "loan_gurantee_fee")
    public Double getLoanGuranteeFee() {
        return loanGuranteeFee;
    }

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "loan_info_pics", joinColumns = {@JoinColumn(name = "loan_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "pic_id", nullable = false, updatable = false)})
    public List<BannerPicture> getLoanInfoPics() {
        return loanInfoPics;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "loan_instruction", columnDefinition = "longtext")
    public String getLoanInstruction() {
        return loanInstruction;
    }

    @Column(name = "loan_money", nullable = false)
    public Double getLoanMoney() {
        return loanMoney;
    }

    @Column(name = "loan_purpose", length = 500)
    public String getLoanPurpose() {
        return loanPurpose;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "loan")
    @OrderBy(value = "period")
    public List<LoanRepay> getLoanRepays() {
        return loanRepays;
    }

    @Lob
    @Column(name = "location", columnDefinition = "CLOB")
    public String getLocation() {
        return location;
    }

    @Column(name = "max_invest_money")
    public Double getMaxInvestMoney() {
        if (maxInvestMoney == null) {
            maxInvestMoney = 99999999D;
        }
        return maxInvestMoney;
    }

    @Column(name = "min_invest_money")
    public Double getMinInvestMoney() {
        if (minInvestMoney == null) {
            minInvestMoney = 50D;
        }
        return minInvestMoney;
    }

    @Column(name = "money", precision = 22, scale = 0)
    public Double getMoney() {
        return money;
    }

    @Column(name = "name", length = 100, nullable = false)
    public String getName() {
        return name;
    }

    @Column(name = "overdue_info", length = 100)
    public String getOverdueInfo() {
        return overdueInfo;
    }

    @Column(name = "pawn", length = 200)
    public String getPawn() {
        return pawn;
    }

    @Column(name = "pawn_name", length = 200)
    public String getPawnName() {
        return pawnName;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "policy_description", columnDefinition = "longtext")
    public String getPolicyDescription() {
        return policyDescription;
    }

    // scale 整数长度 precision 小数精度
    @Column(name = "rate", nullable = false, precision = 22, scale = 0)
    public Double getRate() {
        return rate;
    }

    @Transient
    public Double getRatePercent() {
        if (getRate() != null) {
            return ArithUtil.round(getRate() * 100, 2);
        }
        return null;
    }

    @Column(name = "repay_day")
    public Date getRepayDay() {
        return repayDay;
    }

    @Column(name = "repay_period", length = 50)
    public String getRepayPeriod() {
        return repayPeriod;
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
            for (int i = 0; i < getLoanRepays().size(); i++) {
                LoanRepay ir = getLoanRepays().get(i);
                if (ir.getStatus().equals(RepayStatus.BAD_DEBT) || ir.getStatus().equals(RepayStatus.OVERDUE) || ir.getStatus().equals(RepayStatus.REPAYING)) {
                    unPaidCorpus = ArithUtil.add(unPaidCorpus, ir.getCorpus());
                    unPaidInterest = ArithUtil.add(unPaidInterest, ir.getInterest());
                    unPaidFee = ArithUtil.add(unPaidFee, ir.getFee());
                    unPaidDefaultInterest = ArithUtil.add(unPaidDefaultInterest, ir.getDefaultInterest());
                    if (i == 0 || getLoanRepays().get(i - 1).getStatus().equals(RepayStatus.COMPLETE)) {
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
                    nextRepayInterest, nextRepayFee, nextRepayDefaultInterest, paidPeriod, getLoanRepays().size() - paidPeriod, true);
        }
        return repayRoadmap;
    }

    @Column(name = "repay_type", length = 100)
    public String getRepayType() {
        return repayType;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "risk_description", columnDefinition = "longtext")
    public String getRiskDescription() {
        return riskDescription;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "risk_instruction", columnDefinition = "longtext")
    public String getRiskInstruction() {
        return riskInstruction;
    }

    @Column(name = "risk_level", length = 50)
    public String getRiskLevel() {
        return riskLevel;
    }

    @Column(name = "seq_num")
    public Integer getSeqNum() {
        if (seqNum == null) {
            return 0;
        }
        return seqNum;
    }

    @Column(name = "status", nullable = false, length = 50)
    public String getStatus() {
        return status;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type", nullable = false)
    public LoanType getType() {
        return type;
    }

    /**
     * 计算该笔借款的待还金额
     *
     * @return
     */
    @Transient
    public double getUnpaidMoney() {
        double sum = 0D;
        for (LoanRepay lr : getLoanRepays()) {
            if (lr.getStatus().equals(RepayStatus.BAD_DEBT) || lr.getStatus().equals(RepayStatus.OVERDUE) || lr.getStatus().equals(RepayStatus.REPAYING)) {
                sum = ArithUtil.add(sum, lr.getCorpus());
                sum = ArithUtil.add(sum, lr.getInterest());
                sum = ArithUtil.add(sum, lr.getDefaultInterest());
            }
        }
        return sum;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    @Column(name = "verified", length = 32)
    public String getVerified() {
        return verified;
    }

    @Column(name = "verify_message", length = 500)
    public String getVerifyMessage() {
        return verifyMessage;
    }

    @Column(name = "verify_time")
    public Date getVerifyTime() {
        return verifyTime;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verify_user_id")
    public User getVerifyUser() {
        return verifyUser;
    }

    @Column(name = "video_id")
    public String getVideoId() {
        return videoId;
    }

    @Column(name = "project_duration")
    public Integer getProjectDuration() {
        return projectDuration;
    }

    @Column(name = "target_type")
    public String getTarget_type() {
        return target_type;
    }
    
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "loan_area", joinColumns = {@JoinColumn(name = "loan_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "area_id", nullable = false, updatable = false)})
	public List<Area> getAreas() {
		return areas;
	}

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "loan")
    public List<MallStage> getMallstages() {
        return mallstages;
    }

    @Column(name = "init_money")
    public Double getInitMoney() {
        return initMoney;
    }
    @Column(name = "account_type",length = 32)
    public String getAccountType() {
        return accountType;
    }

    public void setInitMoney(Double initMoney) {
        this.initMoney = initMoney;
    }

    public void setMallstages(List<MallStage> mallstages) {
        this.mallstages = mallstages;
    }
    
	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}

    public void setSina_bid_no(String sina_bid_no) {
        this.sina_bid_no = sina_bid_no;
    }

    public void setInner_bid_no(String inner_bid_no) {
        this.inner_bid_no = inner_bid_no;
    }

    public void setTarget_type(String target_type) {
        this.target_type = target_type;
    }



    public void setActualRate(Double actualRate) {
        this.actualRate = actualRate;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    public void setCardinalNumber(Double cardinalNumber) {
        this.cardinalNumber = cardinalNumber;
    }

    public void setCommitTime(Date commitTime) {
        this.commitTime = commitTime;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public void setConmments(List<Comment> conmments) {
        this.conmments = conmments;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public void setCustomPicture(String customPicture) {
        this.customPicture = customPicture;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExpectTime(Date expectTime) {
        this.expectTime = expectTime;
    }

    public void setFeeOnRepay(Double feeOnRepay) {
        this.feeOnRepay = feeOnRepay;
    }

    public void setFundDescription(String fundDescription) {
        this.fundDescription = fundDescription;
    }

    public void setGiveMoneyTime(Date giveMoneyTime) {
        this.giveMoneyTime = giveMoneyTime;
    }

    public void setGuaranteeCompanyDescription(String guaranteeCompanyDescription) {
        this.guaranteeCompanyDescription = guaranteeCompanyDescription;
    }

    public void setGuaranteeCompanyName(String guaranteeCompanyName) {
        this.guaranteeCompanyName = guaranteeCompanyName;
    }

    public void setGuaranteeInfoDescription(String guaranteeInfoDescription) {
        this.guaranteeInfoDescription = guaranteeInfoDescription;
    }

    public void setGuaranteeInfoPics(List<BannerPicture> guaranteeInfoPics) {
        this.guaranteeInfoPics = guaranteeInfoPics;
    }

    public void setHasPawn(String hasPawn) {
        this.hasPawn = hasPawn;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setInterestBeginTime(Date interestBeginTime) {
        this.interestBeginTime = interestBeginTime;
    }

    public void setInvestorFeeRate(Double investorFeeRate) {
        this.investorFeeRate = investorFeeRate;
    }

    public void setInvestorFeeRatePercent(Double investorFeeRatePercent) {
        if (investorFeeRatePercent != null) {
            investorFeeRate = ArithUtil.div(investorFeeRatePercent, 100, 4);
        }
    }

    public void setInvests(List<Invest> invests) {
        this.invests = invests;
    }

    public void setLoanAttrs(List<NodeAttr> loanAttrs) {
        this.loanAttrs = loanAttrs;
    }

    public void setLoanGuranteeFee(Double loanGuranteeFee) {
        this.loanGuranteeFee = loanGuranteeFee;
    }

    public void setLoanInfoPics(List<BannerPicture> loanInfoPics) {
        this.loanInfoPics = loanInfoPics;
    }

    public void setLoanInstruction(String loanInstruction) {
        this.loanInstruction = loanInstruction;
    }

    public void setLoanMoney(Double loanMoney) {
        this.loanMoney = loanMoney;
    }

    public void setLoanPurpose(String loanPurpose) {
        this.loanPurpose = loanPurpose;
    }

    public void setLoanRepays(List<LoanRepay> loanRepays) {
        this.loanRepays = loanRepays;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMaxInvestMoney(Double maxInvestMoney) {
        this.maxInvestMoney = maxInvestMoney;
    }

    public void setMinInvestMoney(Double minInvestMoney) {
        this.minInvestMoney = minInvestMoney;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOverdueInfo(String overdueInfo) {
        this.overdueInfo = overdueInfo;
    }

    public void setPawn(String pawn) {
        this.pawn = pawn;
    }

    public void setPawnName(String pawnName) {
        this.pawnName = pawnName;
    }

    public void setPolicyDescription(String policyDescription) {
        this.policyDescription = policyDescription;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public void setRatePercent(Double ratePercent) {
        rate = ArithUtil.div(ratePercent, 100, 4);
    }

    public void setRepayDay(Date repayDay) {
        this.repayDay = repayDay;
    }

    public void setRepayPeriod(String repayPeriod) {
        this.repayPeriod = repayPeriod;
    }

    public void setRepayRoadmap(RepayRoadmap repayRoadmap) {
        this.repayRoadmap = repayRoadmap;
    }

    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }

    public void setRiskDescription(String riskDescription) {
        this.riskDescription = riskDescription;
    }

    public void setRiskInstruction(String riskInstruction) {
        this.riskInstruction = riskInstruction;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(LoanType type) {
        this.type = type;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public void setVerifyMessage(String verifyMessage) {
        this.verifyMessage = verifyMessage;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }

    public void setVerifyUser(User verifyUser) {
        this.verifyUser = verifyUser;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public void setProjectDuration(Integer projectDuration) {
        this.projectDuration = projectDuration;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
