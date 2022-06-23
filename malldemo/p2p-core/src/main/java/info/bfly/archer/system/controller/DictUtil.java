package info.bfly.archer.system.controller;

import info.bfly.archer.system.model.Dict;
import info.bfly.core.annotations.Log;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class DictUtil implements Serializable {
    private static final long serialVersionUID = -7586918172774417104L;
    @Log
    private Logger            log;
    @Autowired
    private HibernateTemplate ht;

    public Dict getDict(String id) {
        return ht.get(Dict.class, id);
    }

    public List<Dict> getDictByParentKey(String parentKey) {
        return (List<Dict>) ht.find("from Dict where parent.key = ? order by seqNum", parentKey);
    }

    /**
     * 通过编号获取该编号对应的名称，此处是父key为空的情况下
     *
     * @param
     * @return
     */
    public String getValue(String key) {
        return getValue(null, key);
    }

    /**
     * 通过数据字典的 父编码和子编码获取对应的名称
     *
     * @param parentKey
     * @param key
     * @return
     */
    public String getValue(String parentKey, String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        if (parentKey == null) {
            parentKey = "COMMON";
        }
        String hql = "from Dict where parent.key = ? and key = ?";
        List<Dict> dicts = (List<Dict>) ht.find(hql, parentKey, key);
        if (dicts.size() > 1) {
            log.error("有多个相同的key存在数据库中");
            return "ERROR[多个相同的KEY]";
        } else if (dicts.size() < 1) {
            return parentKey + "." + key + " not found";
        } else {
            return dicts.get(0).getValue();
        }
    }
}
