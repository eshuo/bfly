package info.bfly.archer.urlmapping.service;

import com.ocpsoft.pretty.faces.config.PrettyConfig;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by XXSun on 2016/9/22.
 */
public interface UrlMappingService {
    LinkedList<UrlMapping> getUrlMappings(PrettyConfig pc, String themepath, List<UrlMapping> mappings);
}
