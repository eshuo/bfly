package info.bfly.archer.common.service;

import org.hibernate.LockMode;

import java.io.Serializable;
import java.util.List;

public interface BaseService<E> {
    boolean contains(E e);

    void delete(Class<E> c, Serializable id);

    void delete(E e);

    Object find(String hql);

    List<E> findAll(Class<E> c);

    List<E> findAll(Class<E> c, String orderBy);

    List<E> findByExample(E e);

    /**
     * 通过定义 @NamedQuery 查询
     *
     * @param queryName
     * @param values
     * @return 查询结果
     */
    List findByNamedQuery(String queryName, Object... values);

    E get(Class<E> c, Serializable id);

    E get(Class<E> c, Serializable id, LockMode lockMode);

    void merge(E e);

    void save(E e);

    void saveOrUpdate(E e);

    void update(E e);
}
