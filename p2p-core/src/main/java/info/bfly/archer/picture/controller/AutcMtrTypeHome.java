package info.bfly.archer.picture.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.picture.AutcMtrTypeConstants;
import info.bfly.archer.picture.model.AutcMtrPicture;
import info.bfly.archer.picture.model.AutcMtrType;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 认证材料查询Home
 *
 * @author Administrator
 *
 */
@Component
@Scope(ScopeType.VIEW)
public class AutcMtrTypeHome extends EntityHome<AutcMtrType> implements Serializable {
    List<AutcMtrPicture> pics;

    public AutcMtrTypeHome() {
        setUpdateView(FacesUtil.redirect(AutcMtrTypeConstants.View.AUCT_TYPE_LIST));
    }

    @Override
    protected AutcMtrType createInstance() {
        AutcMtrType type = new AutcMtrType();
        return type;
    }

    public List<AutcMtrPicture> getPics() {
        return pics;
    }

    @Override
    @Transactional(readOnly = false)
    public String save() {
        if (StringUtils.isEmpty(getInstance().getId())) {
            return null;
        } else {
            return super.save();
        }
    }

    public void setPics(List<AutcMtrPicture> pics) {
        this.pics = pics;
    }
}
