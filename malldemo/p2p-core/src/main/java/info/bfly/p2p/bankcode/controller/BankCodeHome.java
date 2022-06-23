package info.bfly.p2p.bankcode.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.p2p.bankcode.model.BankCode;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.VIEW)
public class BankCodeHome extends EntityHome<BankCode> implements Serializable {
    public BankCodeHome() {
        setUpdateView(FacesUtil.redirect("/admin/bankCode/bankCodeList"));
    }
}
