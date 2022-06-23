package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.BaseCallBackEntity;
import info.bfly.pay.bean.enums.AUDIT_STATUS;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 4.8 企业会员审核通知
 * Created by XXSun on 2017/2/17.
 */
public class AuditCallBackEntity extends BaseCallBackEntity {
    /**
     * 请求审核订单号
     */
    @NotNull
    @Size(max = 32)
    private String       audit_order_no;
    /**
     * 内部交易凭证号
     */
    @NotNull
    @Size(max = 32)
    private String       inner_order_no;
    /**
     * 审核结果
     */
    @NotNull
    @Size(max = 32)
    private AUDIT_STATUS audit_status;
    /**
     * 审核结果建议
     */
    @Size(max = 200)
    private String       audit_message;

    public String getAudit_order_no() {
        return audit_order_no;
    }

    public void setAudit_order_no(String audit_order_no) {
        this.audit_order_no = audit_order_no;
    }

    public String getInner_order_no() {
        return inner_order_no;
    }

    public void setInner_order_no(String inner_order_no) {
        this.inner_order_no = inner_order_no;
    }

    public AUDIT_STATUS getAudit_status() {
        return audit_status;
    }

    public void setAudit_status(AUDIT_STATUS audit_status) {
        this.audit_status = audit_status;
    }

    public String getAudit_message() {
        return audit_message;
    }

    public void setAudit_message(String audit_message) {
        this.audit_message = audit_message;
    }

    @Override
    public String toString() {
        return "AuditCallBackEntity{" +
                "audit_order_no='" + audit_order_no + '\'' +
                ", inner_order_no='" + inner_order_no + '\'' +
                ", audit_status=" + audit_status +
                ", audit_message='" + audit_message + '\'' +
                '}';
    }
}
