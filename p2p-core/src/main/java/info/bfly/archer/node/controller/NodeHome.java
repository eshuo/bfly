package info.bfly.archer.node.controller;

import info.bfly.archer.common.CommonConstants;
import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.lucene.LuceneConstants;
import info.bfly.archer.lucene.model.Indexing;
import info.bfly.archer.lucene.service.LuceneService;
import info.bfly.archer.menu.controller.MenuHome;
import info.bfly.archer.menu.model.Menu;
import info.bfly.archer.node.NodeConstants;
import info.bfly.archer.node.model.Node;
import info.bfly.archer.node.model.NodeBody;
import info.bfly.archer.node.model.NodeType;
import info.bfly.archer.node.service.NodeService;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.archer.term.model.CategoryTerm;
import info.bfly.archer.theme.controller.TplVars;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.ImageUploadUtil;
import info.bfly.core.util.StringManager;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Component
@Scope(ScopeType.VIEW)
public class NodeHome extends EntityHome<Node> implements Serializable {
    private static final long          serialVersionUID = 1736551447868451250L;
    @Log
    static Logger log;
    private static final StringManager sm       = StringManager.getManager(NodeConstants.Package);
    private static final StringManager smCommon = StringManager.getManager(CommonConstants.Package);
    @Resource
    private LuceneService              luceneService;
    @Resource
    private LoginUserInfo              loginUserInfo;
    @Resource
    private HibernateTemplate          ht;
    @Resource
    private MenuHome                   menuHome;
    @Resource
    private TplVars                    tplVars;
    @Resource
    private NodeService                nodeService;
    private Integer                    weight;
    public  StringBuffer navStringAll = new StringBuffer();
    private String       navTitle     = "";
    private String       navId        = "";
    private Menu         menu         = null;
    private String       nodeTypeId   = NodeConstants.DEFAULT_TYPE;

    public NodeHome() {
        setUpdateView(FacesUtil.redirect(NodeConstants.View.NODE_LIST));
        setDeleteView(FacesUtil.redirect(NodeConstants.View.NODE_LIST));
    }

    private void addIndex(Node node) {
        // FIXME:建立一个关于索引的service,搞个索引的模块。
        // 封装一个索引
        Indexing indexing = new Indexing();
        indexing.setId(node.getId());
        indexing.setTitle(node.getTitle());
        indexing.setAuthor(node.getUserByCreator().getUsername());
        indexing.setCreateTime(node.getCreateTime());
        indexing.setContent(node.getNodeBody().getBody());
        // FIXME:getWeight() 用途？？？
        // 添加索引
        luceneService.createNewIndex(indexing, LuceneConstants.LUCENE_INDEX_PATH, getWeight());
        // FacesUtil.addInfoMessage(commonSM.getString("saveSuccessMessage",
        // this
        // .getInstance().getId()));
    }

    /**
     * 给当前instance添加term
     *
     * @param termId
     */
    public void addTerm(String termId) {
        if (StringUtils.isNotEmpty(termId)) {
            CategoryTerm term = getBaseService().get(CategoryTerm.class, termId);
            if (term != null) {
                this.getInstance().getCategoryTerms().add(term);
            }
        }
    }

    @Override
    protected Node createInstance() {
        Node node = new Node();
        node.setNodeType(new NodeType());
        node.setNodeBody(new NodeBody());
        node.setSeqNum(0);
        // node.setCategoryTerms(new HashSet<CategoryTerm>());
        return node;
    }

    @Override
    @Transactional(readOnly = false)
    public String delete() {
        if (NodeHome.log.isInfoEnabled()) {
            NodeHome.log.info(NodeHome.sm.getString("log.info.deleteNode", (FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}")), new Date(), getId()));
        }
        String view = super.delete();
        // 删除文章的索引, 先删除文章
        luceneService.deleteIndex(getId(), LuceneConstants.LUCENE_INDEX_PATH);
        return view;
    }

    private void fillHref(StringBuffer navStringAll, CategoryTerm cat) {
        String url = "";
        if (null != cat && null != navStringAll) {
            Menu menu = menuHome.getMenuById(cat.getId());
            if (null != menu) {
                url = menu.getUrl();
                if (!StringUtils.isBlank(url)) {
                    navStringAll.insert(0, "<a class='color57 fontsize14' href='" + getComponentsPath() + url + "'>" + cat.getName() + "</a>>>");
                }
            }
        }
    }

    private String getComponentsPath() {
        return tplVars.getContextPath();
    }

    public LuceneService getLuceneService() {
        return luceneService;
    }

    public String getMenuParentId() {
        String menuParent = "";
        CategoryTerm cat = getInstance().getCategoryTerms().get(0);
        if (null != cat) {
            Menu menu = menuHome.getMenuById(cat.getId());
            if (null != menu) {
                Menu parent = menu.getParent();
                if (null != parent) {
                    menuParent = parent.getId();
                }
                else {
                    menuParent = menu.getId();
                }
            }
        }
        log.info("menuParent:" + menuParent);
        return menuParent;
    }

    /**
     * 获取相同类型的下一个内容。按照序号、时间排序
     *
     * @param nodeId
     * @param termId
     * @return
     *
     */
    public Node getNextNode(final String nodeId, final String termId) {
        Node nodeOri = getBaseService().get(Node.class, nodeId);
        if (nodeOri == null) {
            return null;
        }
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Node.class);
        detachedCriteria.add(Restrictions.eq("nodeType.id", getNodeTypeId()));
        detachedCriteria.createAlias("categoryTerms", "term").add(Restrictions.eq("term.id", termId));
        if (nodeOri.getSeqNum() != null) {
            detachedCriteria.add(Restrictions.or(Restrictions.lt("seqNum", nodeOri.getSeqNum()), Restrictions.and(Restrictions.eq("seqNum", nodeOri.getSeqNum()), Restrictions.lt("updateTime", nodeOri
                    .getUpdateTime()))));
        }
        else {
            detachedCriteria.add(Restrictions.lt("updateTime", nodeOri.getUpdateTime()));
        }
        detachedCriteria.addOrder(Order.desc("seqNum")).addOrder(Order.desc("updateTime"));
        List<Node> nodes = (List<Node>) ht.findByCriteria(detachedCriteria, 0, 1);
        if (nodes.size() > 0) {
            return nodes.get(0);
        }
        return null;
    }

    public String getNodeTypeId() {
        return nodeTypeId;
    }

    public String getNowTitle(CategoryTerm cat) {
        if (null != cat) {
            if (null != cat.getParent()) {
                fillHref(navStringAll, cat);
                getNowTitle(cat.getParent());
            } else {
                fillHref(navStringAll, cat);
            }
        }
        return navStringAll.toString();
    }

    public String getParentTile() {
        String parentTile = "";
        CategoryTerm cat = getInstance().getCategoryTerms().get(0);
        if (null != cat) {
            if (null != cat.getParent()) {
                parentTile = cat.getParent().getName();
            } else {
                parentTile = cat.getName();
            }
        }
        log.info("parentTile:" + parentTile);
        return parentTile;
    }

    /**
     * 获取相同类型的上一个内容。按照序号、时间排序
     *
     * @param nodeId
     * @param termId
     * @return
     */
    public Node getPreviousNode(final String nodeId, final String termId) {
        Node nodeOri = getBaseService().get(Node.class, nodeId);
        if (nodeOri == null) {
            return null;
        }
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Node.class);
        detachedCriteria.add(Restrictions.eq("nodeType.id", getNodeTypeId()));
        detachedCriteria.createAlias("categoryTerms", "term").add(Restrictions.eq("term.id", termId));
        if (nodeOri.getSeqNum() != null) {
            detachedCriteria.add(Restrictions.or(Restrictions.gt("seqNum", nodeOri.getSeqNum()), Restrictions.and(Restrictions.eq("seqNum", nodeOri.getSeqNum()), Restrictions.gt("updateTime", nodeOri
                    .getUpdateTime()))));
        }
        else {
            detachedCriteria.add(Restrictions.gt("updateTime", nodeOri.getUpdateTime()));
        }
        detachedCriteria.addOrder(Order.asc("seqNum")).addOrder(Order.asc("updateTime"));
        List<Node> nodes = (List<Node>) ht.findByCriteria(detachedCriteria, 0, 1);
        if (nodes.size() > 0) {
            return nodes.get(0);
        }
        return null;
    }

    public String getRootTile() {
        String rootTile = "";
        CategoryTerm cat = getInstance().getCategoryTerms().get(0);
        if (null != cat) {
            rootTile = getNowTitle(cat);
            log.info(rootTile);
        }
        getMenuParentId();
        return rootTile;
    }

    public Integer getWeight() {
        if (weight == null) {
            weight = 1;
        }
        return weight;
    }

    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        InputStream is = null;
        try {
            is = file.getInputstream();
            this.getInstance().setThumb(ImageUploadUtil.upload(is, file.getFileName()));
        } catch (IOException e) {
            log.debug(e.getMessage());
            FacesUtil.addErrorMessage(NodeHome.sm.getString("imageUploadFail"));
            return;
        }
    }

    // FIXME: 这是一个死循环
    @Override
    protected void initInstance() {
        super.initInstance();
        /*
         * if (isIdDefined()) {
         * //setInstance(getBaseService().get(getEntityClass(), getId()));
         * //getInstance().getNodeBodyHistories(); } else {
         * setInstance(createInstance()); }
         */
    }

    @Override
    @Transactional(readOnly = false)
    public String save() {
        Node node = getInstance();
        String loginUserId = loginUserInfo.getLoginUserId();
        User user = getBaseService().get(User.class, loginUserId);
        if (user == null) {
            NodeHome.log.warn("save node: " + NodeHome.smCommon.getString("loginUserIsNull"));
            FacesUtil.addErrorMessage(NodeHome.smCommon.getString("loginUserIsNull"));
            return null;
        }
        node.setUserByLastModifyUser(user);
        node.setUpdateTime(new Date());
        // addIndex(node);
        // 判断id重复
        if (!isIdDefined()) {// create instance
            if (StringUtils.isEmpty(getId())) {
                setId(getInstance().getId());
            }
            node.setUserByCreator(user);
            node.setCreateTime(new Date());
            if (null != getBaseService().get(getEntityClass(), getId())) {
                FacesUtil.addErrorMessage(EntityHome.commonSM.getString("entityIdHasExist", getId()));
                setId(null);
                getInstance().setId(null);
                return null;
            }
        }
        nodeService.save(getInstance());
        FacesUtil.addInfoMessage(getSaveSuccessMessage());
        return getSaveView();
    }

    public void setLuceneService(LuceneService luceneService) {
        this.luceneService = luceneService;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
