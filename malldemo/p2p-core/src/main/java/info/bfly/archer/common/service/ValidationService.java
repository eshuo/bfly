package info.bfly.archer.common.service;

import info.bfly.archer.common.exception.InputRuleMatchingException;
import info.bfly.archer.common.exception.NoMatchingObjectsException;

import java.util.List;

/**
 *
 * 
 * Description: 验证service，验证字符串规则，验证是否已存在等等。
 *
 * @version: 1.0
 */
public interface ValidationService {
    /**
     * 该实体的该字段的值，是否与数据库中相同。
     *
     * @param entityClass
     * @param fieldName
     * @param id          实体对象的id
     * @param value
     * @return
     * @throws ClassNotFoundException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    boolean equalsPersistenceValue(String entityClass, String fieldName, String id, String value) throws ClassNotFoundException, SecurityException, NoSuchMethodException;

    /**
     * 查找所有已存在的值
     *
     * @param entityClass
     * @param fieldName
     * @param value
     * @return
     * @throws ClassNotFoundException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    List findAlreadExists(String entityClass, String fieldName, Object value) throws ClassNotFoundException, SecurityException, NoSuchMethodException;

    /**
     * 根据输入匹配规则，验证value
     *
     * @param ruleId 匹配规则id
     * @param value  输入值
     * @return
     * @throws NoMatchingObjectsException 根据ruleId 未找到
     * @throws InputRuleMatchingException 验证失败，里面包含验证消息
     */
    boolean inputRuleValidation(String ruleId, String value) throws NoMatchingObjectsException, InputRuleMatchingException;

    /**
     * 该实体的该字段，在数据库记录中，是否已经存在某值。
     *
     * @param entityClass
     * @param fieldName
     * @param value
     * @return
     * @throws NoSuchFieldException
     * @throws ClassNotFoundException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    boolean isAlreadExist(String entityClass, String fieldName, Object value) throws SecurityException, ClassNotFoundException, NoSuchMethodException;

    /**
     * 该实体的该字段，在数据库记录中，是否已经存在某值。
     *
     * @param entityClass
     * @param fieldName
     * @param value
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    boolean isAlreadyExist(Class entityClass, String fieldName, Object value) throws SecurityException, NoSuchMethodException;
}
