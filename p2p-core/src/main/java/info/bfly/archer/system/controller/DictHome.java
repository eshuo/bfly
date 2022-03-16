package info.bfly.archer.system.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.system.model.Dict;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.IdGenerator;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Scope(ScopeType.VIEW)
public class DictHome extends EntityHome<Dict> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -8926801296927239677L;

    public DictHome() {
        setUpdateView(FacesUtil.redirect("/admin/system/dictList"));
    }

    @Override
    @Transactional(readOnly = false)
    public String save() {
        if (StringUtils.isEmpty(getInstance().getId())) {// 新增
            getInstance().setId(IdGenerator.randomUUID());
        } else {// 编辑
            // 判断父编号不能为自己本身
            Dict parent = getInstance().getParent();
            String parentId = parent == null ? "" : parent.getId();
            if (StringUtils.equals(getInstance().getId(), parentId)) {
                FacesUtil.addErrorMessage("父节点编码不能为自己本身！");
                return null;
            }
        }
        return super.save();
    }
}
