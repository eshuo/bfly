package info.bfly.p2p.bankcode.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.core.annotations.ScopeType;
import info.bfly.p2p.bankcode.BankCodeConstant.Status;
import info.bfly.p2p.bankcode.model.BankCode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Component
@Scope(ScopeType.VIEW)
public class BankCodeList extends EntityQuery<BankCode> implements Serializable {
    private static final String[] RESTRICTIONS = { "bankCode.name like #{bankCodeList.example.name}", "bankCode.payCode like #{bankCodeList.example.payCode}",
            "bankCode.status like #{bankCodeList.example.status}", "bankCode.type = #{bankCodeList.example.type}" };

    public BankCodeList() {
        setRestrictionExpressionStrings(Arrays.asList(BankCodeList.RESTRICTIONS));
    }

    /** 获取某类型且可用的bankCode */
    public List<BankCode> getBankCodesByType(String type) {
        String hql = "from BankCode bc where bc.type=? and bc.status=?";
        return (List<BankCode>) getHt().find(hql,  type, Status.ENABLE );
    }
}
