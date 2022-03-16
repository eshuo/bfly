package info.bfly.p2p.message.service.impl;

import java.io.Serializable;

/**
 * @author XXSun
 */
public class ApiResultBase implements Serializable {
    private static final long serialVersionUID = -4541692901907015405L;
    private int               code;
    private String            msg;
    private String            detail;

    public int getCode() {
        return code;
    }

    public String getDetail() {
        return detail;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isSuccess() {
        return code == 0;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Result{ code=" + code + ",msg='" + msg + "',detail='" + detail + "'} \r\n";
    }
}
