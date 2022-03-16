package info.bfly.archer.config.service.impl;

import info.bfly.archer.config.model.Config;
import info.bfly.archer.config.service.ConfigService;
import org.hibernate.ObjectNotFoundException;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Description:
 */
@Service("configService")
public class ConfigServiceImpl implements ConfigService {
    @Resource
    HibernateTemplate ht;

    @Override
    @Transactional
    public String getConfigValue(String configId) {
        Config config = ht.get(Config.class, configId);
        if (config != null) {
            return config.getValue();
        }
        throw new ObjectNotFoundException(Config.class, "config ID:" + configId + "对象为空！");
    }

    @Override
    @Transactional
    public String getConfigValue(String configId, String defaultVlaue) {
        Config config = ht.get(Config.class, configId);
        if (config != null) {
            return config.getValue();
        }
        else return defaultVlaue;
    }
}
