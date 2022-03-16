package info.bfly.app.protocol.model.response;

import info.bfly.archer.common.controller.EntityQuery;

import java.util.List;

public class ResponsePage {
    private int currentPage;//当前页数
    private int maxPage;//最大页数
    private int size = 20;//每页显示条数
    private int maxSize = 100;//最大条数
    private int start;//当前页数在整体数据中的位置
    private int end;//当前页数截止在整体数据中的位置
    private List<?> data;//所对应的json数据

    public ResponsePage() {
    }

    public ResponsePage(List<?> data) {
        this.data = data;
    }

    public ResponsePage(EntityQuery query) {
        this.data = query.getLazyModelData();
        this.currentPage = query.getCurrentPage();
        this.size = query.getPageSize();
        this.maxSize = query.getLazyModel().getRowCount();
        this.maxPage = this.maxSize % this.size == 0 ? this.maxSize / this.size : (this.maxSize / this.size) + 1;
        init();
    }

    /**
     * 初始化方法
     */
    public void init() {
        if (currentPage <= maxPage) {
            int cPage = 0;
            if (currentPage > 0) {
                cPage = currentPage - 1;
            }
            this.start = cPage * size;
            this.end = start + size;
        }


    }


    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public List getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
