package info.bfly.archer.statistics.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.statistics.model.UserExt;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.util.ArithUtil;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Component
@Scope(ScopeType.VIEW)
public class UserExtList extends EntityQuery<UserExt> implements Serializable {

    @Log
    private static Logger log;
    private static final String lazyModelCountHql = "select count(distinct userExt.id) from UserExt userExt";
    private static final String lazyModelHql      = "select distinct userExt from UserExt userExt";

    public UserExtList() {
        setCountHql(UserExtList.lazyModelCountHql);
        setHql(UserExtList.lazyModelHql);
        final String[] RESTRICTIONS = {};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    public String getAgeStatistics() {
        StringBuilder sb = new StringBuilder("{'result':[");
        String hql = "select count(ue.id), ue.age from UserExt ue group by ue.age";
        List ues = getHt().find(hql);
        for (int i = 0; i < ues.size(); i++) {
            Object[] o = (Object[]) ues.get(i);
            double total = Double.parseDouble((o[0].toString()));
            sb.append("{'age':'").append(o[1].toString()).append("','total':").append(total).append("}");
            if (i != ues.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]}");
        return sb.toString();
    }

    /**
     * 会员地区统计数据 TODO:台湾省 页面不能选，此处也查不到。
     *
     * @return 统计数据的json形式
     */
    public String getAreaStatistics() {
        StringBuilder sb = new StringBuilder("{'investmentSection':[");
        String hql = "select count(user.id), p.id, p.name from Area p, Area a left join a.users user where a.parent = p and p.parent is null group by p.id";
        List ues = getHt().find(hql);
        long count = 0;
        for (int i = 0; i < ues.size(); i++) {
            Object[] o = (Object[]) ues.get(i);
            count += Long.parseLong(o[0].toString());
        }
        for (int i = 0; i < ues.size(); i++) {
            Object[] o = (Object[]) ues.get(i);
            double total = Double.parseDouble((o[0].toString()));
            double per = ArithUtil.round(total / count * 100, 2);
            sb.append("{'cha':'").append(o[1].toString()).append("','city':'").append(o[2].toString()).append("','amount':").append(total).append(",'ratio':'").append(per).append("%'},");
        }
        sb.append("{'cha':'710000','city':'台湾','amount':0.0,'ratio':'0.0%'}");
        sb.append("]}");
        log.info(sb.toString());
        return sb.toString();
    }
}
