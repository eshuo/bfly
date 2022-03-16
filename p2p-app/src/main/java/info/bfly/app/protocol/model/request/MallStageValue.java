package info.bfly.app.protocol.model.request;

import info.bfly.crowd.mall.model.MallStageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/6 0006.
 */
public class MallStageValue {

    private String mallStageId;
    
    private String mallStageCacheId;
    
    private String userAddressId;
    
    private String orderId;

    private RequestPage page =  new RequestPage();

    private List<MallStageItem> stageItems = new ArrayList<MallStageItem>();

    public RequestPage getPage() {
        return page;
    }

    public void setPage(RequestPage page) {
        this.page = page;
    }

    public String getMallStageCacheId() {
        return mallStageCacheId;
    }

    public void setMallStageCacheId(String mallStageCacheId) {
        this.mallStageCacheId = mallStageCacheId;
    }

    public String getMallStageId() {
        return mallStageId;
    }

    public void setMallStageId(String mallStageId) {
        this.mallStageId = mallStageId;
    }

    public List<MallStageItem> getStageItems() {
        return stageItems;
    }

    public void setStageItems(List<MallStageItem> stageItems) {
        this.stageItems = stageItems;
    }

    public String getUserAddressId() {
        return userAddressId;
    }

    public void setUserAddressId(String userAddressId) {
        this.userAddressId = userAddressId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
