package info.bfly.app.protocol.model.request;

import info.bfly.archer.common.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XXSun on 2016/12/26.
 */
public class RequestPage {
    /**
     * 当前页面
     */
    private int currentPage = 0;

    /**
     * 页面大小
     */
    private int size = 20;

    private List<OrderItem> order = new ArrayList<OrderItem>();

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<OrderItem> getOrder() {
        return order;
    }

    public void setOrder(List<OrderItem> order) {
        this.order = order;
    }
}
