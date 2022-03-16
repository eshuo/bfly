package info.bfly.archer.system.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by XXSun on 3/5/2017.
 */
@Entity
@Table(name = "table_id")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class TableId implements Serializable {
    private static final long serialVersionUID = 8163869593745934241L;
    private String tableName;
    private String tableNextId;

    @Id
    @Column(name = "table_name")
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Basic
    @Column(name = "table_next_id")
    public String getTableNextId() {
        return tableNextId;
    }

    public void setTableNextId(String tableNextId) {
        this.tableNextId = tableNextId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TableId that = (TableId) o;

        return (tableName != null ? tableName.equals(that.tableName) : that.tableName == null) && (tableNextId != null ? tableNextId.equals(that.tableNextId) : that.tableNextId == null);
    }

    @Override
    public int hashCode() {
        int result = tableName != null ? tableName.hashCode() : 0;
        result = 31 * result + (tableNextId != null ? tableNextId.hashCode() : 0);
        return result;
    }
}
