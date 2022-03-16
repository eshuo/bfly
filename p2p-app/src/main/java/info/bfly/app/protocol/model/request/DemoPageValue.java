package info.bfly.app.protocol.model.request;

/**
 * Created by XXSun on 2016/12/26.
 */
public class DemoPageValue  {
    private RequestPage page;
    private String      userId;

    public RequestPage getPage() {
        return page;
    }

    public void setPage(RequestPage page) {
        this.page = page;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
