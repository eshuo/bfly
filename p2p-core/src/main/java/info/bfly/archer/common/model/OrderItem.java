package info.bfly.archer.common.model;

/**
 * Created by XXSun on 2016/12/27.
 */
public class OrderItem {
    private String column;

    private String direction;

    public OrderItem() {
    }

    public OrderItem(String column, String direction) {
        this.column = column;
        this.direction = direction;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}