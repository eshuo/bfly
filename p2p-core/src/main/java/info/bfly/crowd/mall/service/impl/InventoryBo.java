package info.bfly.crowd.mall.service.impl;

import info.bfly.core.util.IdGenerator;
import info.bfly.crowd.mall.model.Inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Description:
 */
@Service("inventoryBo")
public class InventoryBo {
    @Resource
    HibernateTemplate ht;
    @Autowired
    IdGenerator idGenerator;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String generateId() {
       return idGenerator.nextId(Inventory.class,"");
    }
}
