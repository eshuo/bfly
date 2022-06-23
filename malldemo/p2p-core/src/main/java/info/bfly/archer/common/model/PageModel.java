package info.bfly.archer.common.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PageModel<E> {
    private int     count;
    private int     pageSize;
    private int     page;
    private List<E> data;
    private int     totalPage;

    public int getCount() {
        return count;
    }

    public List<E> getData() {
        return data;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPage() {
        if (pageSize > 0) {
            if (count % pageSize > 0) {
                totalPage = (count / pageSize) + 1;
            } else {
                totalPage = count / pageSize;
            }
        }
        return totalPage;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setData(List<E> data) {
        this.data = data;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
