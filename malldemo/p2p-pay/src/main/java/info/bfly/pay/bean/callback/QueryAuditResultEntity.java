package info.bfly.pay.bean.callback;

import info.bfly.pay.bean.SinaInEntity;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Service
public class QueryAuditResultEntity extends SinaInEntity {
    /**
     * 审核结果
     */
    @NotNull
    @Size(max = 32)
    public String audit_result;
    /**
     * 审核结果建议
     */
    @Size(max = 200)
    public String audit_mgs;
    /**
     * 扩展信息
     */
    @Size(max = 200)
    public String extend_param;
    public String getExtend_param() {
        return extend_param;
    }
    public void setExtend_param(String extend_param) {
        this.extend_param = extend_param;
    }
    public String getAudit_result() {
        return audit_result;
    }
    public void setAudit_result(String audit_result) {
        this.audit_result = audit_result;
    }
    public String getAudit_mgs() {
        return audit_mgs;
    }
    public void setAudit_mgs(String audit_mgs) {
        this.audit_mgs = audit_mgs;
    }

}
