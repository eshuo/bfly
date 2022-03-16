package info.bfly.app.protocol.model.response;

/**
 * Created by XXSun on 2016/12/27.
 */
public class ApiLoan {
    // Fields
    private String              id;
    // 借款名称
    private String              name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
