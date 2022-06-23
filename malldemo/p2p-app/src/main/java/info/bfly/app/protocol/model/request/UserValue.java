package info.bfly.app.protocol.model.request;

import info.bfly.api.exception.ParameterExection;

/**
 * Created by Administrator on 2017/2/23 0023.
 */
public class UserValue {

    private String userName;

    private String password;

    private String oldPassword;

    private String email;

    private String mobileNumber;

    private String veriCode;

    private String referrer;

    private String ticket;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getVeriCode() {
        return veriCode;
    }

    public void setVeriCode(String veriCode) {
        this.veriCode = veriCode;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public boolean getCheckData() {

        boolean b = true;
        if (password == null) {
            b = false;
            throw new ParameterExection("密码");
        }
        if (mobileNumber == null) {
            b = false;
            throw new ParameterExection("手机号");
        }
        if (veriCode == null) {
            b = false;
            throw new ParameterExection("手机验证码");
        }
        return b;
    }

}
