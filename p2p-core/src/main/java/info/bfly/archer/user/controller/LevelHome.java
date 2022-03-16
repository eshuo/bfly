package info.bfly.archer.user.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.user.exception.MinPointLimitCannotMattchSeqNumException;
import info.bfly.archer.user.exception.SeqNumAlreadyExistException;
import info.bfly.archer.user.model.LevelForUser;
import info.bfly.archer.user.service.LevelService;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.IdGenerator;

import java.io.Serializable;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.VIEW)
public class LevelHome extends EntityHome<LevelForUser> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 3385074649231209727L;
    @Resource
    private LevelService levelService;

    /**
     * 增加或修改等级方法
     */
    @Override
    public String save() {
        if (StringUtils.isEmpty(getInstance().getId())) {
            getInstance().setId(IdGenerator.randomUUID());
        }
        try {
            levelService.saveOrUpdate(getInstance());
        } catch (SeqNumAlreadyExistException e) {
            FacesUtil.addErrorMessage("等级序号已存在！");
        } catch (MinPointLimitCannotMattchSeqNumException e) {
            FacesUtil.addErrorMessage(" 等级积分下限的顺序，与等级序号的顺序不匹配！");
        }
        FacesUtil.addInfoMessage("保存成功！");
        // TODO:return 等级list
        return "等级list";
    }
}
