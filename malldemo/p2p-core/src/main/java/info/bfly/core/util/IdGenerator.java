package info.bfly.core.util;

import info.bfly.archer.system.model.TableId;
import org.apache.commons.lang.StringUtils;
import org.hibernate.LockMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;
import javax.persistence.metamodel.EntityType;
import java.util.Date;
import java.util.UUID;

@Service
public class IdGenerator {
    @Autowired
    private HibernateTemplate ht;

    private final String MAX_ID = "999999";
    private final String MIN_ID = "000000";

    /**
     * 获取对应table的序列化id
     *
     * @param table
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String nextId(String table) {
        return nextId(table, table);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T> String nextId(Class<T> entity) {
        return nextId(getTableName(entity));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T> String nextId(Class<T> entity, String prefix) {

        return nextId(getTableName(entity), prefix);
    }

    private <T> String getTableName(Class<T> entity) {
        EntityType<T> entityType = ht.getSessionFactory().getMetamodel().entity(entity);
        Table t = entity.getAnnotation(Table.class);

        return (t == null)
                ? entityType.getName().toUpperCase()
                : t.name();
    }

    /**
     * 获取对应table的序列化id
     *
     * @param table
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String nextId(String table, String prefix) {
        synchronized (table) {

            String gid = DateUtil.DateToString(new Date(), DateStyle.YYYYMMDDHH);
            TableId tableId = ht.get(TableId.class, table, LockMode.PESSIMISTIC_READ);
            if (tableId == null) {
                tableId = new TableId();
                tableId.setTableName(table);
                tableId.setTableNextId(gid + idPlus(MIN_ID));

                gid = gid + MIN_ID;
                ht.save(tableId);
            } else {
                String tmp = tableId.getTableNextId();
                if (tmp.equals(gid + MAX_ID))
                    throw new IndexOutOfBoundsException(table + " 序列异常 当前值为" + tmp);
                if (tmp.startsWith(gid)) {
                    tableId.setTableNextId(gid + idPlus(tmp.substring(gid.length())));
                    gid = tmp;
                } else {
                    tableId.setTableNextId(gid + idPlus(MIN_ID));
                    gid = gid + MIN_ID;
                }
                ht.update(tableId);
            }
            //TODO 隐藏tablename!!

            return StringUtils.defaultIfEmpty(prefix, StringUtils.EMPTY) + gid;
        }
    }


    //JF010******
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String returnUserId(String table, String str) {

        TableId tableId = ht.get(TableId.class, table, LockMode.PESSIMISTIC_READ);

        synchronized (table) {
            if (tableId == null) {
                int num = (int) (Math.random() * 9000 + 1000);
                String gid = "" + num;
                tableId = new TableId();
                tableId.setTableName(table);
                tableId.setTableNextId("" + (num + 1));
                ht.save(tableId);
                return StringUtils.defaultIfEmpty(str, StringUtils.EMPTY) + gid;
            } else {
                String gid = tableId.getTableNextId();
                Integer integer = Integer.valueOf(gid);
                integer++;
                tableId.setTableNextId("" + integer);
                ht.update(tableId);
                return StringUtils.defaultIfEmpty(str, StringUtils.EMPTY) + gid;
            }


        }
    }


    /**
     * 对当前id进行增加 返回6为0补齐的字符串
     *
     * @param id
     * @return
     */
    private String idPlus(String id) {

        return String.format("%06d", Integer.parseInt(id) + 1);
    }


    public static String randomUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
