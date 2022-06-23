package info.bfly.archer.picture.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.picture.model.AutcMtrType;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 认证材料查询List
 * 
 * @author Administrator
 *
 */
@Component
@Scope(ScopeType.VIEW)
public class AutcMtrTypeList extends EntityQuery<AutcMtrType> implements Serializable {
    @Log
    static Logger log;
    private static final String[] RESTRICTIONS      = {"autcMtrType.id like #{autcMtrTypeList.example.id}", "autcMtrType.name like #{autcMtrTypeList.example.name}"};
    private static final String   lazyModelCountHql = "select count(distinct autcMtrType) from AutcMtrType autcMtrType ";
    private static final String   lazyModelHql      = "select distinct autcMtrType from AutcMtrType autcMtrType ";

    public AutcMtrTypeList() {
        // TODO Auto-generated constructor stub
        setCountHql(AutcMtrTypeList.lazyModelCountHql);
        setHql(AutcMtrTypeList.lazyModelHql);
        setRestrictionExpressionStrings(Arrays.asList(AutcMtrTypeList.RESTRICTIONS));
    }

    /**
     * 通过id查找认证材料
     *
     * @param pid
     * @return
     */
    public AutcMtrType getAutcTypeById(String pid) {
        return getHt().get(AutcMtrType.class, pid);
    }
}
