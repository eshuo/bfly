package info.bfly.pay.bean.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class QueryCtrlResultEntity extends UserSinaEntity {
    private static final long serialVersionUID = -3971383022477203196L;
    /**
     * 冻结解冻订单号
     * 商户网站冻结或解冻订单号，商户内部保证唯一
     */
    @NotNull
    @Size(max = 32)
    private String out_ctrl_no;

    public String getOut_ctrl_no() {
        return out_ctrl_no;
    }

    public void setOut_ctrl_no(String out_ctrl_no) {
        this.out_ctrl_no = out_ctrl_no;
    }

}
