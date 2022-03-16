package info.bfly.archer.system.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 实体统计
 */
public class EntityGroupBy {
    private Class  entityClazz;
    private String groupByField;
    private String groupByValue;
    private int    amount;
    private List<Object> tempValues = new ArrayList<Object>(0);

    public EntityGroupBy(Class entityClazz, String groupByField, String groupByValue, int amount, List<Object> tempValues) {
        super();
        this.entityClazz = entityClazz;
        this.groupByField = groupByField;
        this.groupByValue = groupByValue;
        this.amount = amount;
        this.tempValues = tempValues;
    }

    public EntityGroupBy(String entityClazz, String groupByField, String groupByValue, int amount, List<Object> tempValues) throws ClassNotFoundException {
        super();
        this.entityClazz = Class.forName(entityClazz);
        this.groupByField = groupByField;
        this.groupByValue = groupByValue;
        this.amount = amount;
        this.tempValues = tempValues;
    }

    public int getAmount() {
        return amount;
    }

    public Class getEntityClazz() {
        return entityClazz;
    }

    public String getGroupByField() {
        return groupByField;
    }

    public String getGroupByValue() {
        return groupByValue;
    }

    public List<Object> getTempValues() {
        return tempValues;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setEntityClazz(Class entityClazz) {
        this.entityClazz = entityClazz;
    }

    public void setGroupByField(String groupByField) {
        this.groupByField = groupByField;
    }

    public void setGroupByValue(String groupByValue) {
        this.groupByValue = groupByValue;
    }

    public void setTempValues(List<Object> tempValues) {
        this.tempValues = tempValues;
    }
}
