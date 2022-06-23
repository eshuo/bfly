package info.bfly.p2p.loan.controller;

import info.bfly.archer.banner.controller.BannerPictureHome;
import info.bfly.core.annotations.ScopeType;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.VIEW)
public class LoanGuaranteeInfoPictureHome extends BannerPictureHome {
    private static final long serialVersionUID = 2631626583094488757L;
}
