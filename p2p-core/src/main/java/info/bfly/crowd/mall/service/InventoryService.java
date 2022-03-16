package info.bfly.crowd.mall.service;

import info.bfly.crowd.mall.model.Inventory;


public interface InventoryService {

    public void save(Inventory inventory) ;
    
    public Inventory getInventory(String mallStageId);
}
