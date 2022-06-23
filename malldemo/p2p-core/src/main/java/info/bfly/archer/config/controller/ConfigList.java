package info.bfly.archer.config.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.config.model.Config;
import info.bfly.archer.config.model.ConfigType;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Component
@Scope(ScopeType.VIEW)
public class ConfigList extends EntityQuery<Config> implements Serializable {
    private static final long serialVersionUID = -6676637446365684885L;
    @Log
    static Logger log;

    public ConfigList() {
        final String[] RESTRICTIONS = {"id like #{configList.example.id}", "name like #{configList.example.name}", "configype.id = #{configList.example.configType.id}"};
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    @SuppressWarnings("unchecked")
    public List<Config> getConfigs() {
        getHt().setCacheQueries(true);
        List<Config> configs = (List<Config>) getHt().find("from " + Config.class);
        return configs;
    }

    public List<Config> getConfigsByType(String type) {
        return (List<Config>) getHt().find("from Config where configType.id = ? ", type);
    }

    @Override
    public void initExample() {
        Config config = new Config();
        config.setConfigType(new ConfigType());
        setExample(config);
    }
}
