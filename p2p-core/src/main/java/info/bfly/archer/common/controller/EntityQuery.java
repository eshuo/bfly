package info.bfly.archer.common.controller;

import info.bfly.archer.common.CommonConstants;
import info.bfly.archer.common.model.OrderItem;
import info.bfly.core.annotations.Log;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.StringManager;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.web.context.ContextLoader.getCurrentWebApplicationContext;

@Component
public class EntityQuery<E> extends EntityBase<E> {
    private static final long serialVersionUID = 2441609552501565935L;
    @Log
    static Logger log;
    protected static       StringManager commonSM                  = StringManager.getManager(CommonConstants.Package);
    private final static   String        HQL_TEMPLATE_SELECT       = "Select {0} from {1} {0} ";
    private final static   String        HQL_TEMPLATE_SELECT_COUNT = "Select count({0}) from {1} {0} ";
    private final static   String        EL_PATTERN                = "[#{](.*?)[}]";
    // private static final Pattern SUBJECT_PATTERN =
    // Pattern.compile("^select (\\w+(\\.\\w+)*)\\s+from",
    // Pattern.CASE_INSENSITIVE);
    // private static final Pattern FROM_PATTERN =
    // Pattern.compile("(^|\\s)(from)\\s", Pattern.CASE_INSENSITIVE);
    private static final   Pattern       WHERE_PATTERN             = Pattern.compile("\\s(where)\\s", Pattern.CASE_INSENSITIVE);
    private static final   Pattern       ORDER_PATTERN             = Pattern.compile("\\s(order)(\\s)+by\\s", Pattern.CASE_INSENSITIVE);
    protected static final String        DIR_ASC                   = "asc";
    protected static final String        DIR_DESC                  = "desc";
    /**
     * 自定义每页数量
     */
    private                int           pageSize                  = 1;
    /**
     * 自定义当前页
     */
    private                int           currentPage               = 1;
    private String           hql;
    private String           countHql;
    private List<String>     orderColumn;
    private List<String>     orderDirection;
    private Object[]         parameterValues;
    private List<String>     restrictionExpressionStrings;
    private E                example;
    public  List<E>          allResultList;
    private LazyDataModel<E> lazyModel;
    @Resource
    HibernateTemplate ht;

    private String entityAlias;

    /**
     * 添加一个排序
     *
     * @param orderColumn    排序字段
     * @param orderDirection 排序方向 DIR_DESC DIR_ASC
     */
    public void addOrder(String orderColumn, String orderDirection) {
        if (this.orderColumn == null || this.orderDirection == null) {
            this.orderColumn = new ArrayList<String>();
            this.orderColumn.add(orderColumn);
            this.orderDirection = new ArrayList<String>();
            this.orderDirection.add(orderDirection);
        } else {
            // 如果排序字段已经包含要添加的字段
            if (this.getOrderColumn().contains(orderColumn)) {
                int index = this.getOrderColumn().indexOf(orderColumn);
                // 判断该字段对应的排序方向是否和需要添加的一样
                if (!this.getOrderDirection().get(index).equals(orderDirection)) {
                    // 如果不同，则改变排序方向
                    this.getOrderDirection().remove(index);
                    this.getOrderDirection().add(index, orderDirection);
                }
            } else {
                this.getOrderColumn().add(orderColumn);
                this.getOrderDirection().add(orderDirection);
            }
        }
    }

    public void addOrder(List<OrderItem> orders) {
        for (OrderItem order : orders) {
            addOrder(order.getColumn(), order.getDirection());
        }
    }

    /**
     * 添加lazyModel的一个查询条件。
     *
     * @param restirction
     */
    public void addRestriction(String restirction) {
        if (this.restrictionExpressionStrings == null) {
            this.restrictionExpressionStrings = new ArrayList<String>();
            this.restrictionExpressionStrings.add(restirction);
        } else if (!this.restrictionExpressionStrings.contains(restirction)) {
            if (this.restrictionExpressionStrings instanceof AbstractList) {
                this.restrictionExpressionStrings = new ArrayList<String>(this.restrictionExpressionStrings);
            }
            this.restrictionExpressionStrings.add(restirction);
        }
    }

    public void dealResultList(List<E> resultList) {
    }

    public List<E> getAllResultList() {
        if (allResultList == null) {
            initAllResultList();
        }
        return allResultList;
    }

    public String getCountHql() {
        if (countHql == null || countHql.equals("")) {
            countHql = parserHql(EntityQuery.HQL_TEMPLATE_SELECT_COUNT);
        }
        return countHql;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    String getEntityAlias() {
        if (entityAlias == null) {
            String entityClassName = getEntityClass().getSimpleName();
            entityAlias = StringUtils.uncapitalize(entityClassName);
        }
        return entityAlias;
    }

    public E getEntityById(String id) {
        return getHt().get(getEntityClass(), id);
    }


    public E getExample() {
        if (example == null) {
            initExample();
        }
        return example;
    }

    public String getHql() {
        if (hql == null || hql.length() == 0) {
            hql = parserHql(EntityQuery.HQL_TEMPLATE_SELECT);
        }
        return hql;
    }

    public HibernateTemplate getHt() {
        return ht;
    }

    public LazyDataModel<E> getLazyModel() {
        if (lazyModel == null) {
            initLazyModel();
        }
        return lazyModel;
    }

    /**
     * 手动取LazyModel的值，用自定义当前页和自定义数量
     *
     * @return
     */
    public List<E> getLazyModelData() {
        return this.getLazyModel().load(pageSize * (currentPage - 1), pageSize, null, null, null);
    }

    public E getLazyModelRowData(String rowKey) {
        // 如果dataTable要多选，则必须重新该方法。
        throw new UnsupportedOperationException("getLazyModelRowData(String rowKey) must be implemented when basic rowKey algorithm is not used.");
    }

    public Object getLazyModelRowKey(E object) {
        // 如果dataTable要多选，则必须重新该方法。
        throw new UnsupportedOperationException("getLazyModelRowKey(T object) must be implemented when basic rowKey algorithm is not used.");
    }

    public String getOrder() {
        if (getOrderColumn() == null) {
            return null;
        }
        StringBuffer sortStr = new StringBuffer();
        for (int i = 0; i < getOrderColumn().size(); i++) {
            if (i > 0) {
                sortStr.append(",");
            }
            sortStr.append(getOrderColumn().get(i));
            sortStr.append("  ");
            sortStr.append(getOrderDirection().get(i));
        }
        return sortStr.toString();
    }

    /**
     * 获取一个排序
     *
     * @param orderColumn 排序字段
     *                    排序方向 DIR_DESC DIR_ASC
     */
    public String getOrderByOrderColumn(String orderColumn) {
        if (this.orderColumn != null && this.orderDirection != null && this.getOrderColumn().contains(orderColumn)) {
            int index = this.getOrderColumn().indexOf(orderColumn);
            return this.getOrderDirection().get(index);
        }
        return null;
    }

    public List<String> getOrderColumn() {
        return orderColumn;
    }

    public List<String> getOrderDirection() {
        return orderDirection;
    }

    public int getPageSize() {
        return pageSize;
    }

    public Object[] getParameterValues() {
        return parameterValues;
    }

    protected String getRenderedCountHql() {
        return parseHql(getCountHql());
    }

    public String getRenderedHql() {
        StringBuilder renderedHql = new StringBuilder(parseHql(getHql()));
        // parser order .
        if (!EntityQuery.ORDER_PATTERN.matcher(renderedHql).find() && StringUtils.isNotEmpty(getOrder())) {
            renderedHql.append(" order by ").append(getOrder());
        }
        return renderedHql.toString();
    }

    public List<String> getRestrictionExpressionStrings() {
        return restrictionExpressionStrings;
    }

    public void initAllResultList() {
        try {
            allResultList = (List<E>) ht.find("from " + getEntityClass().getSimpleName());
        } catch (Exception e) {
            log.debug(e.getMessage());
            EntityQuery.log.error(e.getMessage());
        }
    }

    protected void initExample() {
        try {
            setExample(getEntityClass().newInstance());
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }

    protected void initLazyModel() {
        lazyModel = new LazyDataModel<E>() {
            private static final long serialVersionUID = -4605725783065773722L;

            @Override
            public E getRowData(String rowKey) {
                return getLazyModelRowData(rowKey);
            }

            @Override
            public Object getRowKey(E object) {
                return getLazyModelRowKey(object);
            }

            /**
             * 多行排序
             */
            @Override
            public List<E> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, String> filters) {
                List<String> orderColumns = null;
                List<String> orderDirections = null;
                if (multiSortMeta != null) {
                    orderColumns = new ArrayList<String>();
                    orderDirections = new ArrayList<String>();
                    for (SortMeta sortMeta : multiSortMeta) {
                        orderColumns.add(sortMeta.getSortField());
                        orderDirections.add(sortMeta.getSortOrder().compareTo(SortOrder.DESCENDING) == 0 ? EntityQuery.DIR_DESC : EntityQuery.DIR_ASC);
                    }
                }
                return load(first, pageSize, orderColumns, orderDirections);
            }

            private List<E> load(final int first, final int pageSize, List<String> orderColumns, List<String> orderDirections) {

                if (getOrderColumn() != null && orderColumns != null) {
                    // 确保用户在表格中进行排序的，一定要首先生效。
                    getOrderColumn().addAll(0, orderColumns);
                } else if (orderColumns != null) {
                    setOrderColumn(orderColumns);
                }
                if (getOrderDirection() != null && orderDirections != null) {
                    getOrderDirection().addAll(0, orderDirections);
                } else if (orderDirections != null) {
                    setOrderDirection(orderDirections);
                }
                if (getParameterValues() == null) {
                    setParameterValues(new Object[]{});
                }
                if (EntityQuery.log.isDebugEnabled()) {
                    EntityQuery.log.debug("Rendered Hql = " + getRenderedHql());
                    EntityQuery.log.debug("Rendered Count Hql = " + getRenderedCountHql());
                }
                List<Object> objs = (List<Object>) ht.find(getRenderedCountHql(), getParameterValues());
                Long count = 0L;
                if (objs.size() != 0) {
                    count = (Long) objs.get(0);
                }
                setRowCount(count.intValue());
                if (first > count.intValue()) {
                    return new ArrayList<E>();
                }
                List<E> resultList = ht.execute(new HibernateCallback<List<E>>() {
                    @Override
                    public List<E> doInHibernate(Session session) throws HibernateException {
                        Query query = session.createQuery(getRenderedHql());
                        // 从第0行开始
                        query.setFirstResult(first);
                        query.setMaxResults(pageSize);
                        for (int i = 0; i < getParameterValues().length; i++) {
                            query.setParameter(i, getParameterValues()[i]);
                        }
                        return query.list();
                    }
                });
                setWrappedData(resultList);
                dealResultList(resultList);
                return resultList;
            }

            @Override
            public List<E> load(final int first, final int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
                List<String> orderColumns = null;
                List<String> orderDirections = null;
                if (StringUtils.isNotEmpty(sortField)) {
                    orderColumns = new ArrayList<String>();
                    orderDirections = new ArrayList<String>();
                    orderColumns.add(sortField);
                    orderDirections.add(sortOrder.compareTo(SortOrder.DESCENDING) == 0 ? EntityQuery.DIR_DESC : EntityQuery.DIR_ASC);
                }
                return load(first, pageSize, orderColumns, orderDirections);
            }

            @Override
            public void setRowIndex(int rowIndex) {
                if (getPageSize() == 0) {
                    setPageSize(15);
                }
                super.setRowIndex(rowIndex);
            }
        };
    }

    protected String parseHql(final String hql) {
        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();
        standardEvaluationContext.setBeanResolver(new BeanFactoryResolver(getCurrentWebApplicationContext()));
        ExpressionParser parser = new SpelExpressionParser();
        StringBuilder builder = new StringBuilder(hql);
        Pattern p = Pattern.compile(EntityQuery.EL_PATTERN);
        List<String> restrictions = getRestrictionExpressionStrings();
        // parser where
        if (restrictions != null && restrictions.size() >= 1) {
            List<Object> parameterValues = new ArrayList<Object>();
            for (String restriction : restrictions) {
                boolean isFindExpression = false;
                Matcher m = p.matcher(restriction);
                while (m.find()) {
                    isFindExpression = true;
                    final String el = m.group();
                    Object elValue;
                    if (FacesUtil.getCurrentInstance() != null) {
                        //like语句中增加%符号
                        if (restriction.contains("like") && null != FacesUtil.getExpressionValue(el) && !"".equals(FacesUtil.getExpressionValue(el))) {
                            elValue = "%" + FacesUtil.getExpressionValue(el) + "%";
                        } else {
                            elValue = FacesUtil.getExpressionValue(el);
                        }
                    } else {
                        Expression exp = parser.parseExpression("@".concat(el.replaceAll(" |^#\\{|\\}$", "")));
                        elValue = exp.getValue(standardEvaluationContext, Object.class);
                    }
                    // log.debug("el = " + el + ",elValue=" + elValue);
                    if (elValue != null && !elValue.toString().equals("")) {
                        if (EntityQuery.WHERE_PATTERN.matcher(builder).find()) {
                            builder.append(" and ");
                        } else {
                            builder.append(" where ");
                        }
                        restriction = m.replaceAll("?");
                        parameterValues.add(elValue);
                        builder.append(restriction);
                    }
                }
                if (!isFindExpression) {
                    if (EntityQuery.WHERE_PATTERN.matcher(builder).find()) {
                        builder.append(" and ");
                    } else {
                        builder.append(" where ");
                    }
                    builder.append(restriction);
                }
            }
            setParameterValues(parameterValues.toArray());
        }
        return builder.toString();
    }

    private String parserHql(String hql) {
        String entityAlias = getEntityAlias();
        String entityClassName = getEntityClass().getSimpleName();
        return MessageFormat.format(hql, entityAlias, entityClassName);
    }

    /**
     * 移除一个排序
     *
     * @param orderColumn
     */
    public void removeOrder(String orderColumn) {
        int index = this.getOrderColumn().indexOf(orderColumn);
        if (index != -1) {
            this.getOrderColumn().remove(index);
            this.getOrderDirection().remove(index);
        }
    }

    /**
     * 移除lazyModel的一个查询条件。
     *
     * @param restirction
     */
    public void removeRestriction(String restirction) {
        if (this.restrictionExpressionStrings != null && this.restrictionExpressionStrings.contains(restirction)) {
            if (this.restrictionExpressionStrings instanceof AbstractList) {
                this.restrictionExpressionStrings = new ArrayList<String>(this.restrictionExpressionStrings);
            }
            this.restrictionExpressionStrings.remove(restirction);
        }
    }

    public void setAllResultList(List<E> allResultList) {
        this.allResultList = allResultList;
    }

    public void setCountHql(String countHql) {
        this.countHql = countHql;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setExample(E example) {
        this.example = example;
    }

    public void setHql(String hql) {
        this.hql = hql;
    }

    public void setLazyModel(LazyDataModel<E> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public void setOrderColumn(List<String> orderColumn) {
        this.orderColumn = orderColumn;
    }

    public void setOrderDirection(List<String> orderDirection) {
        this.orderDirection = orderDirection;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setParameterValues(Object[] parameterValues) {
        this.parameterValues = parameterValues;
    }

    public void setRestrictionExpressionStrings(List<String> restrictionExpressionStrings) {
        this.restrictionExpressionStrings = restrictionExpressionStrings;
    }
}
