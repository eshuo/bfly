package info.bfly.archer.node.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.common.model.PageModel;
import info.bfly.archer.node.NodeConstants;
import info.bfly.archer.node.model.Node;
import info.bfly.archer.node.model.NodeType;
import info.bfly.archer.term.model.CategoryTerm;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.util.StringManager;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 文章查询
 *
 * @author wanghm
 *
 */
@Component(value = "nodeList")
@Scope(ScopeType.APPLICATION)
public class NodeList extends EntityQuery<Node> implements Serializable {
    // private static final long serialVersionUID = 3446001352023091099L;
    static StringManager          sm                = StringManager.getManager(NodeConstants.Package);
    @Log
    static Logger log;
    private static final String[] RESTRICTIONS      = { "node.id like #{nodeList.example.id}", "node.title like #{nodeList.example.title}", "node.nodeType.id = #{nodeList.example.nodeType.id}",
            "node in elements(term.nodes) and term.id = #{nodeList.example.categoryTerms[0].id}" };
    private static final String   lazyModelCountHql = "select count(distinct node) from Node node left join node.categoryTerms term ";
    private static final String   lazyModelHql      = "select distinct node from Node node left join node.categoryTerms term";
    private              String   nodeTypeId        = NodeConstants.DEFAULT_TYPE;

    public NodeList() {
        init();
    }

    @Override
    public LazyDataModel<Node> getLazyModel() {
        getExample().setNodeType(new NodeType(getNodeTypeId()));
        return super.getLazyModel();
    }

    public Node getNodeById(String id) {
        return getHt().get(Node.class, id);
    }

    /**
     * 通过分类术语编号查询前创建时间后15篇文章
     *
     * @param termId
     * @return
     */
    public PageModel<Node> getNodes(String termId) {
        return getNodes(termId, NodeConstants.DEFAULT_RESULT_SIZE);
    }

    public PageModel<Node> getNodes(String termId, int maxResults) {
        return getNodes(termId, 0, maxResults);
    }

    /**
     * 通过分类id找到该分类底下的文章
     *
     * @param termId
     *            分类id
     * @param page
     *            文章数据库起始位置
     * @param maxResults
     *            要找出多少篇文章
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageModel<Node> getNodes(final String termId, final int page, final int maxResults) {

        final PageModel<Node> pageModel = new PageModel<Node>();
        pageModel.setPageSize(maxResults);
        pageModel.setPage(page);
        // data
        List<Node> data = getHt().execute(new HibernateCallback<List<Node>>() {
            @Override
            public List<Node> doInHibernate(Session session) throws HibernateException {
                Query query2 = null;
                Query query = null;
                if (StringUtils.isNotEmpty(termId)) {
                    // 如果分类编号不为空
                    query = session.getNamedQuery("Node.findNodeByTermOrderBySeqNumAndUpdateTime");
                    query.setParameter("nodeTypeId", getNodeTypeId());
                    query.setParameter("termId", termId);
                    query2 = session.getNamedQuery("Node.getNodeCountByTermId");
                    query2.setParameter("nodeTypeId", getNodeTypeId());
                    query2.setParameter("termId", termId);
                }
                else {
                    query = session.getNamedQuery("Node.findNodeOrderBySeqNumAndUpdateTime");
                    query.setParameter("nodeTypeId", getNodeTypeId());
                    query2 = session.getNamedQuery("Node.getNodeCount");
                    query2.setParameter("nodeTypeId", getNodeTypeId());
                }
                query.setCacheable(true);
                query.setFirstResult((page-1)*maxResults);
                query.setMaxResults(maxResults);
                query2.setCacheable(true);
                pageModel.setCount(Integer.valueOf(query2.uniqueResult().toString()));
                return query.list();
            }
        });
        pageModel.setData(data);
        return pageModel;
    }

    public String getNodeTypeId() {
        return nodeTypeId;
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<Node> getPingInfo() {
        // data
        List<Node> data = getHt().execute(new HibernateCallback<List<Node>>() {
            @Override
            public List<Node> doInHibernate(Session session) throws HibernateException {
                // Query query
                // =session.createSQLQuery(" select distinct node0_.id as id15_, node0_.create_time as create2_15_, node0_.description as descript3_15_, node0_.keywords as keywords15_, node0_.language as language15_, node0_.body as body15_, node0_.node_type as node14_15_, node0_.seq_num as seq6_15_, node0_.status as status15_, node0_.subtitle as subtitle15_, node0_.thumb as thumb15_, node0_.title as title15_, node0_.update_time as update11_15_, node0_.creator as creator15_, node0_.last_modify_user as last16_15_, node0_.version as version15_, node0_1_.characteristics as characte1_26_, case when node0_1_.id is not null then 1 when node0_.id is not null then 0 end as clazz_ from node node0_ left outer join product node0_1_ on node0_.id=node0_1_.id, category_term categoryte1_ where date(create_time)=date_sub(curdate(),interval 1 day)");
                Query query = session.getNamedQuery("Node.pingInfo");
                // query.setCacheable(true);
                return query.list();
            }
        });
        return data;
    }

    public void init() {
        setCountHql(NodeList.lazyModelCountHql);
        setHql(NodeList.lazyModelHql);
        setRestrictionExpressionStrings(Arrays.asList(NodeList.RESTRICTIONS));
        // addOrder("node.updateTime", DIR_DESC);
    }

    @Override
    protected void initExample() {
        Node node = new Node();
        List<CategoryTerm> terms = new ArrayList<CategoryTerm>(0);
        terms.add(new CategoryTerm());
        node.setCategoryTerms(terms);
        setExample(node);
    }

    public void setNodeTypeId(String nodeTypeId) {
        this.nodeTypeId = nodeTypeId;
    }
}
