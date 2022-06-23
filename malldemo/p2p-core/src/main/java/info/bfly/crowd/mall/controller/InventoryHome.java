package info.bfly.crowd.mall.controller;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.core.annotations.ScopeType;
import info.bfly.crowd.mall.model.Inventory;

@Component
@Scope(ScopeType.VIEW)
public class InventoryHome extends EntityHome<Inventory> implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public InventoryHome(){
    }

}
