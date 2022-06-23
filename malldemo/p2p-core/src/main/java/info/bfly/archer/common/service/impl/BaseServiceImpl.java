package info.bfly.archer.common.service.impl;

import info.bfly.archer.common.service.BaseService;
import org.hibernate.LockMode;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service("baseService")
@SuppressWarnings("unchecked")
public class BaseServiceImpl<E> implements BaseService<E> {
    @Resource
    private HibernateTemplate ht;

    @Override
    public boolean contains(E e) {
        return ht.contains(e);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Class<E> c, Serializable id) {
        ht.delete(get(c, id));
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(E e) {
        ht.delete(e);
    }

    @Override
    public Object find(String hql) {
        return ht.find(hql);
    }

    @Override
    public List<E> findAll(Class<E> c) {
        List<E> result = (List<E>) ht.find("from " + c.getName());
        if (result == null) {
            return new ArrayList<E>();
        }
        return result;
    }

    @Override
    public List<E> findAll(Class<E> c, String orderBy) {
        return (List<E>) ht.find("from " + c.getName() + " order by " + orderBy);
    }

    @Override
    public List<E> findByExample(E e) {
        return ht.findByExample(e);
    }

    @Override
    public List findByNamedQuery(String queryName, Object... values) {
        return ht.findByNamedQuery(queryName, values);
    }

    @Override
    public E get(Class<E> c, Serializable id) {
        return ht.get(c, id);
    }

    @Override
    public E get(Class<E> c, Serializable id, LockMode lockMode) {
        return ht.get(c,id,lockMode);
    }

    public Class<E> getEntityClass() {
        Class<E> entityClass=null;

        return entityClass;
    }
    @Override
    @Transactional(readOnly = false)
    public void merge(E e) {
        ht.merge(e);
    }

    @Override
    @Transactional(readOnly = false)
    public void save(E e) {
        ht.save(e);
    }

    @Override
    @Transactional(readOnly = false)
    public void saveOrUpdate(E e) {
        ht.saveOrUpdate(e);
    }

    @Override
    @Transactional(readOnly = false)
    public void update(E e) {
        ht.update(e);
    }
}
