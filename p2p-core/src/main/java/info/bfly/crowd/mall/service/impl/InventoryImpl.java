package info.bfly.crowd.mall.service.impl;

import info.bfly.core.annotations.Log;


import info.bfly.crowd.mall.model.Inventory;
import info.bfly.crowd.mall.service.InventoryService;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

@Service("InventoryService")
public class InventoryImpl implements InventoryService {

    @Resource
    HibernateTemplate ht ;
    
    @Resource
    InventoryBo ib;
    
    @Log
    private static Logger log;

    @Override
    public void save(Inventory inventory) {
        if(inventory.getId()==null){
            inventory.setId(ib.generateId());
        }
        
        ht.saveOrUpdate(inventory);
        ht.flush();
        log.debug("保存成功，编号["+ inventory.getId() +"]");
    }

    @Override
    public Inventory getInventory(String mallStageId) {
        Inventory inventory = (Inventory)ht.find("from Inventory inventory where inventory.mallStage.id = ?", mallStageId);
        return inventory;
    }


}
