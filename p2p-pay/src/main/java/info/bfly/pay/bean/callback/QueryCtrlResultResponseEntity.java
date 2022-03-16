package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.SinaInEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class QueryCtrlResultResponseEntity extends SinaInEntity{
    /**
     * 冻结解冻订单号
     * 返回入参的冻结解冻订单号
     */
    @NotNull
    @Size(max = 32)
    public String out_ctrl_no;
    /**
     * 订单状态
     * 冻结解冻订单状态，详见附录“支付状态”
     */
    @Size(max = 16)
    public String ctrl_status;
    /**
     * 错误信息
     * 冻结解冻失败原因
     */
    @Size(max = 256)
    public String error_msg;


    public String getOut_ctrl_no() {
        return out_ctrl_no;
    }

    public void setOut_ctrl_no(String out_ctrl_no) {
        this.out_ctrl_no = out_ctrl_no;
    }

    public String getCtrl_status() {
        return ctrl_status;
    }

    public void setCtrl_status(String ctrl_status) {
        this.ctrl_status = ctrl_status;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

}
