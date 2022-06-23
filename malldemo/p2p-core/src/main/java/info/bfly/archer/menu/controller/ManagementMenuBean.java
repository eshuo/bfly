package info.bfly.archer.menu.controller;

import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import org.primefaces.event.TabChangeEvent;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ScopeType.SESSION)
public class ManagementMenuBean implements java.io.Serializable {
    private static final long serialVersionUID = -1958765048831627777L;
    @Log
    static Logger log;
    private String activeIndex = "0";

    public String getActiveIndex() {
        return activeIndex;
    }

    public void onTabChange(TabChangeEvent event) {
        int index = event.getTab().getParent().getChildren().indexOf(event.getTab());
        ManagementMenuBean.log.debug("Tab changeed,active index is " + index);
        // if( activeIndex.equals("0") ){
        activeIndex = String.valueOf(index);
        // }
    }

    public void setActiveIndex(String activeIndex) {
        this.activeIndex = activeIndex;
    }
}
