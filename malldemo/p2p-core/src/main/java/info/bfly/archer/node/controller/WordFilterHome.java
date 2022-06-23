package info.bfly.archer.node.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.node.NodeConstants;
import info.bfly.archer.node.model.WordFilter;
import info.bfly.archer.node.service.WordFilterService;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.StringManager;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;

@Component
@Scope(ScopeType.REQUEST)
public class WordFilterHome extends EntityHome<WordFilter> implements Serializable {
    private static final long serialVersionUID = 5877874185902208123L;
    @Log
    static Logger log;
    private final static StringManager sm = StringManager.getManager(NodeConstants.Package);
    @Resource
    private WordFilterService wfs;

    public WordFilterHome() {
        setUpdateView(FacesUtil.redirect("/admin/node/wordFilterList"));
        setDeleteView(FacesUtil.redirect("/admin/node/wordFilterList"));
    }

    @Override
    @Transactional(readOnly = false)
    public String delete() {
        if (WordFilterHome.log.isInfoEnabled()) {
            WordFilterHome.log.info(WordFilterHome.sm.getString("log.info.deleteWordFilter", FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
        }
        return super.delete();
    }

    @Override
    @Transactional(readOnly = false)
    public String save() {
        wfs.initPatterns();
        return super.save();
    }
}
