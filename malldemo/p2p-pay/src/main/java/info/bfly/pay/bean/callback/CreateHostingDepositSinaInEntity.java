package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.SinaInEntity;
import info.bfly.pay.bean.enums.DEPOSIT_STATUS;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 3.10    托管充值响应 on 2017/2/20 0020.
 */
public class CreateHostingDepositSinaInEntity extends SinaInEntity implements Serializable {
    private static final long serialVersionUID = 7967498448447678600L;
    /**
     * 充值订单号
     */
    @NotNull
    private String         out_trade_no;
    /**
     * 充值状态
     */
    private DEPOSIT_STATUS deposit_status;
    /**
     * 后续推进需要的参数
     */
    private String         ticket;
    /**
     * 线下支付收款单位
     */
    private String         trans_account_name;
    /**
     * 线下支付收款账号
     */
    private String         trans_account_no;
    /**
     * 线下支付收款账号开户行
     */
    private String         trans_bank_brank;
    /**
     * 线下支付收款备注
     */
    private String         trans_trade_no;
    /**
     * 收银台重定向地址
     */
    private String         redirect_url;


    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public DEPOSIT_STATUS getDeposit_status() {
        return deposit_status;
    }

    public void setDeposit_status(DEPOSIT_STATUS deposit_status) {
        this.deposit_status = deposit_status;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getTrans_account_name() {
        return trans_account_name;
    }

    public void setTrans_account_name(String trans_account_name) {
        this.trans_account_name = trans_account_name;
    }

    public String getTrans_account_no() {
        return trans_account_no;
    }

    public void setTrans_account_no(String trans_account_no) {
        this.trans_account_no = trans_account_no;
    }

    public String getTrans_bank_brank() {
        return trans_bank_brank;
    }

    public void setTrans_bank_brank(String trans_bank_brank) {
        this.trans_bank_brank = trans_bank_brank;
    }

    public String getTrans_trade_no() {
        return trans_trade_no;
    }

    public void setTrans_trade_no(String trans_trade_no) {
        this.trans_trade_no = trans_trade_no;
    }

    public String getRedirect_url() {
        return redirect_url;
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }
}
