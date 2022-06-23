package info.bfly.archer.term.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.term.TermConstants;
import info.bfly.archer.term.model.CategoryTerm;
import info.bfly.archer.term.model.CategoryTermType;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.StringManager;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Component
@Scope(ScopeType.REQUEST)
public class CategoryTermTypeHome extends EntityHome<CategoryTermType> implements Serializable {
    @Log
    static Logger log;
    private static final StringManager sm = StringManager.getManager(TermConstants.Package);

    public CategoryTermTypeHome() {
        setUpdateView(FacesUtil.redirect(TermConstants.View.TERM_TYPE_LIST));
    }

    @Override
    @Transactional(readOnly = false)
    public String delete() {
        if (CategoryTermTypeHome.log.isInfoEnabled()) {
            CategoryTermTypeHome.log.info(CategoryTermTypeHome.sm.getString("log.info.deleteTermType", FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
        }
        Set<CategoryTerm> termSets = getInstance().getCategoryTerms();
        if (termSets != null && termSets.size() > 0) {
            CategoryTermTypeHome.log.info(CategoryTermTypeHome.sm.getString("log.info.deleteTermTypeUnccessful", FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
            FacesUtil.addWarnMessage(CategoryTermTypeHome.sm.getString("deleteTermTypeUnccessful"));
            return null;
        } else {
            return super.delete();
        }
    }
}
