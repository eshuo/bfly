package info.bfly.archer.system.controller;

import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.REQUEST)
public class WatchdogList {
    @Resource
    HibernateTemplate ht;
    Map<String, String> viewCountMap = new HashMap<String, String>();

    public String getViewCount(String nodeId) {
        final String url = FacesUtil.getCurrentAppUrl() + "/node/" + nodeId;
        if (viewCountMap.size() == 0) {
            ht.setCacheQueries(true);
            List<Object[]> views = (List<Object[]>) ht.find("Select url,count(url) from Watchdog group by url");
            for (Object[] result : views) {
                viewCountMap.put(result[0].toString(), result[1].toString());
            }
        }
        return viewCountMap.get(url) == null ? "0" : viewCountMap.get(url);
    }
}
