package info.bfly.archer.user.model;

import java.util.Date;

/**
 * 广告模型
 * 
 * @author Administrator
 *
 */
public class AdverModel implements java.io.Serializable {
    private static final long serialVersionUID = -5143925802779672191L;
    private Date   regDate;                                  // 注册日期
    private String fromWhere;                                // 来源==MID
    private long   regCount;                                 // 注册数
    private int    authCount;                                // 认证数

    public AdverModel(Date regDate, String fromWhere, long regCount, int authCount) {
        super();
        this.regDate = regDate;
        this.fromWhere = fromWhere;
        this.regCount = regCount;
        this.authCount = authCount;
    }

    public int getAuthCount() {
        return authCount;
    }

    public String getFromWhere() {
        return fromWhere;
    }

    public long getRegCount() {
        return regCount;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setAuthCount(int authCount) {
        this.authCount = authCount;
    }

    public void setFromWhere(String fromWhere) {
        this.fromWhere = fromWhere;
    }

    public void setRegCount(long regCount) {
        this.regCount = regCount;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }
}
