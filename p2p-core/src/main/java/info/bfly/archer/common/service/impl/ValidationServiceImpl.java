package info.bfly.archer.common.service.impl;

import info.bfly.archer.common.exception.InputRuleMatchingException;
import info.bfly.archer.common.exception.NoMatchingObjectsException;
import info.bfly.archer.common.service.ValidationService;
import info.bfly.archer.config.model.Config;
import info.bfly.core.annotations.ConverterId;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

/**
 */
@Service("validationService")
public class ValidationServiceImpl implements ValidationService {
    private static final String FIND_EXISTS_HQL              = "select {0} from {1} {0} where {0}.{2}=?";
    private static final String EQUALS_PERSISTENCE_VALUE_HQL = "select {0} from {1} {0} where {0}.{2}=? and {0}.{3}=?";
    @Resource
    HibernateTemplate ht;

    @Override
    public boolean equalsPersistenceValue(String entityClass, String fieldName, String id, String value) throws ClassNotFoundException, SecurityException, NoSuchMethodException {
        Class clazz = Class.forName(entityClass);
        // 判断field是否正确
        clazz.getMethod("get" + StringUtils.capitalize(fieldName));
        String entityClassName = clazz.getSimpleName();
        String entityAlias = StringUtils.uncapitalize(entityClassName);
        String idFieldName = getAnnotadedWithId(clazz);
        String hql = MessageFormat.format(ValidationServiceImpl.EQUALS_PERSISTENCE_VALUE_HQL, entityAlias, entityClassName, idFieldName, fieldName);
        List objs = ht.find(hql, new String[]{id, value});
        return objs.size() > 0;
    }

    @Override
    public List findAlreadExists(String entityClass, String fieldName, Object value) throws ClassNotFoundException, SecurityException, NoSuchMethodException {
        Class clazz = Class.forName(entityClass);
        // 判断field是否正确
        clazz.getMethod("get" + StringUtils.capitalize(fieldName));
        String entityClassName = clazz.getSimpleName();
        String entityAlias = StringUtils.uncapitalize(entityClassName);
        String hql = MessageFormat.format(ValidationServiceImpl.FIND_EXISTS_HQL, entityAlias, entityClassName, fieldName);
        return ht.find(hql, value);
    }

    private String getAnnotadedWithId(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Method[] methods = clazz.getDeclaredMethods();
        try {
            for (Field field : fields) {
                if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(ConverterId.class)) {
                    field.setAccessible(true);
                    return field.getName();
                }
            }
            for (Method method : methods) {
                if (method.isAnnotationPresent(Id.class) || method.isAnnotationPresent(ConverterId.class)) {
                    String name = method.getName();
                    name = StringUtils.uncapitalize(name.substring(3, name.length()));
                    return name;
                }
            }
            if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Object.class)) {
                return getAnnotadedWithId(clazz.getSuperclass());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean inputRuleValidation(String ruleId, String value) throws NoMatchingObjectsException, InputRuleMatchingException {
        // ruleId = "input_"+ruleId;
        if (StringUtils.isEmpty(value)) {
            // 输入框是否为空，有requried="true"来判断，所以此处一律返回true
            return true;
        }
        Config rule = ht.get(Config.class, ruleId);
        if (rule == null) {
            // ruleId未找到
            throw new NoMatchingObjectsException(Config.class, "ruleId:" + ruleId);
        }
        String regex = rule.getValue();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        if (!matcher.matches()) {
            // 验证失败
            throw new InputRuleMatchingException(rule.getDescription());
        }
        return true;
    }

    @Override
    public boolean isAlreadExist(String entityClass, String fieldName, Object value) throws SecurityException, ClassNotFoundException, NoSuchMethodException {
        List objs = findAlreadExists(entityClass, fieldName, value);
        return objs.size() > 0;
    }

    @Override
    public boolean isAlreadyExist(Class entityClass, String fieldName, Object value) throws SecurityException, NoSuchMethodException {
        // 判断field是否正确
        entityClass.getMethod("get" + StringUtils.capitalize(fieldName));
        String entityClassName = entityClass.getSimpleName();
        String entityAlias = StringUtils.uncapitalize(entityClassName);
        String hql = MessageFormat.format(ValidationServiceImpl.FIND_EXISTS_HQL, entityAlias, entityClassName, fieldName);
        List objs = ht.find(hql, value);
        return objs.size() > 0;
    }
}
