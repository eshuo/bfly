package info.bfly.archer.common.controller;

import info.bfly.core.annotations.ConverterId;

import javax.persistence.Id;
import java.io.Serializable;
import java.lang.reflect.*;

/**
 * Created by XXSun on 2016/12/27.
 */
public abstract class EntityBase<E> implements Serializable {

    protected Class<E> entityClass;

    private static final long serialVersionUID = 968380623857255773L;

    public Object getAnnotadedWithId(Object object, Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Method[] methods = clazz.getDeclaredMethods();
        try {
            for (Field field : fields) {
                if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(ConverterId.class)) {
                    field.setAccessible(true);
                    return field.get(object);
                }
            }
            for (Method method : methods) {
                if (method.isAnnotationPresent(Id.class) || method.isAnnotationPresent(ConverterId.class)) {
                    return method.invoke(object);
                }
            }
            if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Object.class)) {
                return getAnnotadedWithId(object, clazz.getSuperclass());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Class<E> getEntityClass() {
        if(entityClass != null){
            return entityClass;
        }
        if(getClass().isAssignableFrom(EntityBase.class)){

        }
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) type;
            if (paramType.getActualTypeArguments().length == 2) {
                // likely dealing with -> new
                // EntityHome<Person>().getEntityClass()
                if (paramType.getActualTypeArguments()[1] instanceof TypeVariable) {
                    throw new IllegalArgumentException("Could not guess entity class by reflection");
                }
                // likely dealing with -> new Home<EntityManager, Person>() {
                // ... }.getEntityClass()
                else {
                    entityClass = (Class<E>) paramType.getActualTypeArguments()[1];
                }
            } else {
                // likely dealing with -> new PersonHome().getEntityClass()
                // where PersonHome extends EntityHome<Person>
                entityClass = (Class<E>) paramType.getActualTypeArguments()[0];
            }
        } else {
            throw new IllegalArgumentException("Could not guess entity class by reflection");
        }
        return entityClass;
    }

    public void setEntityClass(Class<E> entityClass) {
        this.entityClass = entityClass;
    }
}
