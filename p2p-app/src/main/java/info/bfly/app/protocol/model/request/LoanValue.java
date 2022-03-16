package info.bfly.app.protocol.model.request;


import info.bfly.p2p.loan.LoanConstants;

/**
 * Created by Administrator on 2016/12/30 0030.
 */
public class LoanValue {

    private String id;

    private RequestPage page = new RequestPage();

    public String DEFALT_STATUS = "((loan.status='" + LoanConstants.LoanStatus.RAISING + "' or loan.status='" + LoanConstants.LoanStatus.COMPLETE + "' or loan.status='" + LoanConstants.LoanStatus.RECHECK + "' or loan.status='" + LoanConstants.LoanStatus.REPAYING + "') and loan.businessType='个人借款' or loan.businessType='企业借款')";

    public String DEFALT_STATUS1 = "((loan.status='" + LoanConstants.LoanStatus.RAISING + "' or loan.status='" + LoanConstants.LoanStatus.COMPLETE + "' or loan.status='" + LoanConstants.LoanStatus.RECHECK + "' or loan.status='" + LoanConstants.LoanStatus.REPAYING + "'))";
    
    public String DEFALT_STATUS2 = "loan.businessType='众筹'";
    
    private String status;

    private String riskLevel;

    private String minRate;

    private String maxRate;

    private String maxDeadline;

    private String minDeadline;

    private String commentsSize = "5";

    private String investSize = "5";
    
    private String mallStageSize = "1";

    private String type;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RequestPage getPage() {
        return page;
    }

    public void setPage(RequestPage page) {
        this.page = page;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getMinRate() {
        return minRate;
    }

    public void setMinRate(String minRate) {
        this.minRate = minRate;
    }

    public String getMaxRate() {
        return maxRate;
    }

    public void setMaxRate(String maxRate) {
        this.maxRate = maxRate;
    }

    public String getMaxDeadline() {
        return maxDeadline;
    }

    public void setMaxDeadline(String maxDeadline) {
        this.maxDeadline = maxDeadline;
    }

    public String getMinDeadline() {
        return minDeadline;
    }

    public void setMinDeadline(String minDeadline) {
        this.minDeadline = minDeadline;
    }

    public String getCommentsSize() {
        return commentsSize;
    }

    public void setCommentsSize(String commentsSize) {
        this.commentsSize = commentsSize;
    }

    public String getInvestSize() {
        return investSize;
    }

    public void setInvestSize(String investSize) {
        this.investSize = investSize;
    }

    public String getMallStageSize() {
        return mallStageSize;
    }

    public void setMallStageSize(String mallStageSize) {
        this.mallStageSize = mallStageSize;
    }
}
