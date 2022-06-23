package info.bfly.archer.items.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.items.SelectItemConstant;
import info.bfly.archer.items.model.SelectItemGroup;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.IdGenerator;
import info.bfly.core.util.PublicUtils;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

@Component
@Scope(ScopeType.REQUEST)
public class SelectItemGroupHome extends EntityHome<SelectItemGroup> implements Serializable {
    private static final long serialVersionUID = -3272530590049506040L;
    @Log
    static Logger log;
    @Resource
    HibernateTemplate ht;

    public SelectItemGroupHome() {
        setUpdateView(FacesUtil.redirect(SelectItemConstant.View.SELECTITEMGROUP_LIST));
    }

    @SuppressWarnings("unchecked")
    public boolean isExistName() {
        List<SelectItemGroup> itemgroup = (List<SelectItemGroup>) ht.findByNamedQuery("SelectItemGroup.findSelectItemGroupByname", getInstance().getName());
        if (itemgroup != null && itemgroup.size() > 1) {
            return true;
        }
        else if (itemgroup != null && itemgroup.size() == 1) {
            String group = itemgroup.get(0).getId();
            if (!group.equals(getInstance().getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional(readOnly = false)
    public String save() {
        if (PublicUtils.isEmpty(getId())) {
            if (PublicUtils.isEmpty(getInstance().getId())) {
                getInstance().setId(IdGenerator.randomUUID());
            }
            if (isExistName()) {
                SelectItemGroupHome.log.error("name has exist! please input again");
                FacesUtil.addErrorMessage(SelectItemConstant.ErrorMsg.NAMEHASEXIST);
                return null;
            }
        }
        return super.save();
    }
}
