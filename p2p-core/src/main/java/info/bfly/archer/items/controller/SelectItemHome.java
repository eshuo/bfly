package info.bfly.archer.items.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.items.SelectItemConstant;
import info.bfly.archer.items.model.SelectItem;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.VIEW)
public class SelectItemHome extends EntityHome<SelectItem> implements Serializable {
    private static final long serialVersionUID = 437621400210746950L;
    @Resource
    HibernateTemplate ht;

    public SelectItemHome() {
        setUpdateView(FacesUtil.redirect(SelectItemConstant.View.SELECTITEM_LIST));
    }

    @SuppressWarnings("unchecked")
    public SelectItem getselectItemsbyId(String selectitemId) {
        List<SelectItem> itemList = (List<SelectItem>) ht.findByNamedQuery("SelectItem.findItemsById", selectitemId);
        if (itemList == null || itemList.size() == 0) {
            return null;
        }
        return itemList.get(0);
    }

    /**
     * 根据候选项id获取选项组
     *
     * @param selectitemId
     * @return
     */
    public List<Object> slectItemList(String selectitemId) {
        List<Object> itemlist = new ArrayList<Object>();
        SelectItem selectItem = getselectItemsbyId(selectitemId);
        if (selectItem == null) {
            return null;
        }
        String items = selectItem.getItems().replaceAll("，", ",");
        String[] itemArray = items.split(",");
        for (String item : itemArray) {
            itemlist.add(item);
        }
        return itemlist;
    }
}
