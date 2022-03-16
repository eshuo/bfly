package info.bfly.archer.urlmapping.service.impl;

import com.ocpsoft.pretty.faces.config.PrettyConfig;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import info.bfly.archer.urlmapping.service.UrlMappingService;
import info.bfly.core.annotations.Log;
import org.slf4j.Logger;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by XXSun on 2016/9/22.
 */
@Service
public class UrlMappingServiceImpl implements UrlMappingService {
    @Log
    static Logger log;
    @Resource
    HibernateTemplate ht;


    @Override
    public LinkedList<UrlMapping> getUrlMappings(PrettyConfig pc, String themepath, List<UrlMapping> mappings) {
        List<info.bfly.archer.urlmapping.model.UrlMapping> dbMappings = ht.loadAll(info.bfly.archer.urlmapping.model.UrlMapping.class);
        for (info.bfly.archer.urlmapping.model.UrlMapping urlMapping : dbMappings) {
            UrlMapping um = new UrlMapping();
            String viewId = urlMapping.getViewId();
            // 替换当前主题应有的字符串
            if (viewId.startsWith("themepath:")) {
                viewId = viewId.replaceFirst("themepath:", themepath);
            }
            um.setId(urlMapping.getId());
            um.setPattern(urlMapping.getPattern());
            um.setViewId(viewId);
            mappings.add(um);
        }
        LinkedList<UrlMapping> ums = new LinkedList<UrlMapping>();
        for (UrlMapping urlMapping : pc.getMappings()) {
            String viewId = urlMapping.getViewId();
            if (viewId.startsWith("themepath:")) {
                viewId = viewId.replaceFirst("themepath:", themepath);
                urlMapping.setViewId(viewId);
            }
        }
        return ums;
    }
}
