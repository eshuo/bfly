package info.bfly.pay.bean.order.param;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 3.2.1分账信息列表 on 2017/2/17 0017.
 */
public class SplitParam implements Serializable {
    private static final long serialVersionUID = -7080320417234156767L;
    /**
     * 付款人标识
     */
    @NotNull
    private String payer_identity;
    /**
     * 付款人标识类型
     */
    @NotNull
    private String payer_type;

    /**
     * 付款人账户类型
     */
    private String payer_account_type;

    /**
     * 收款人标识
     */
    @NotNull
    private String payee_identity;
    /**
     * 收款人标识类型
     */
    @NotNull
    private String payee_type;

    /**
     * 收款人账户类型
     */
    private String payee_account_type;

    /**
     * 金额
     */
    @NotNull
    private BigDecimal amount;

    /**
     * 备注
     */
    private String remarks;


    @Override
    public String toString() {
        return payer_identity + "^" + payer_type + "^" + payer_account_type + "^" + payee_identity + "^" + payee_type + "^" + payee_account_type + "^" + amount + "^" + remarks;
    }

    public SplitParam(String payer_no, String payer_type, String payer_account_type, String payee_no, String payee_type, String payee_account_type, BigDecimal amount, String remarks) {
        this.payer_identity = payer_no;
        this.payer_type = payer_type;
        this.payer_account_type = payer_account_type;
        this.payee_identity = payee_no;
        this.payee_type = payee_type;
        this.payee_account_type = payee_account_type;
        this.amount = amount;
        this.remarks = remarks;
    }

    public String getPayer_identity() {
        return payer_identity;
    }

    public void setPayer_identity(String payer_identity) {
        this.payer_identity = payer_identity;
    }

    public String getPayer_type() {
        return payer_type;
    }

    public void setPayer_type(String payer_type) {
        this.payer_type = payer_type;
    }

    public String getPayer_account_type() {
        return payer_account_type;
    }

    public void setPayer_account_type(String payer_account_type) {
        this.payer_account_type = payer_account_type;
    }

    public String getPayee_identity() {
        return payee_identity;
    }

    public void setPayee_identity(String payee_identity) {
        this.payee_identity = payee_identity;
    }

    public String getPayee_type() {
        return payee_type;
    }

    public void setPayee_type(String payee_type) {
        this.payee_type = payee_type;
    }

    public String getPayee_account_type() {
        return payee_account_type;
    }

    public void setPayee_account_type(String payee_account_type) {
        this.payee_account_type = payee_account_type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
