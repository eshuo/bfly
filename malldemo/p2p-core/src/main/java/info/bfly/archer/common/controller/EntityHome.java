package info.bfly.archer.common.controller;

import info.bfly.archer.common.CommonConstants;
import info.bfly.core.annotations.Log;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.StringManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class EntityHome<E> extends EntityBase<E> {
    private static final long serialVersionUID = -3688934777215129560L;
    protected static StringManager commonSM = StringManager.getManager(CommonConstants.Package);
    private final static String saveSuccessMessageKey = "saveSuccessMessage", deleteSuccessMessageKey = "deleteSuccessMessage", saveFailMessageKey = "saveFailMessage";
    @Log
    static  Logger log ;
    private String id;
    private String saveView;
    private String updateView;
    private String deleteView;
    private String saveSuccessMessage;
    private String updateSuccessMessage;
    private String deleteSuccessMessage;
    private String saveFailMessage;
    private String updateFailMessage;
    private String deleteFailMessage;
    private E instance;
    @Autowired
    HibernateTemplate baseService;

    public void clearInstance() {
        setInstance(null);
        setId(null);
    }

    /**
     * Create a new instance of the entity. <br />
     * Utility method called by {@link #initInstance()} to create a new instance
     * of the entity.
     */
    protected E createInstance() {
        if (getEntityClass() != null) {
            try {
                return getEntityClass().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    @Transactional(readOnly = false)
    public String delete() {
        baseService.delete(getInstance());
        FacesUtil.addInfoMessage(EntityHome.commonSM.getString(EntityHome.deleteSuccessMessageKey));
        return getDeleteView();
    }

    @Transactional(readOnly = false)
    public String delete(String id) {
        baseService.delete(baseService.get(getEntityName(), id));
        FacesUtil.addInfoMessage(EntityHome.commonSM.getString(EntityHome.deleteSuccessMessageKey));
        return getDeleteView();
    }

    public List<E> findAll() {
        return (List<E>) baseService.find("from " + getEntityClass().getSimpleName());
    }


    public HibernateTemplate getBaseService() {
        return baseService;
    }

    public String getDeleteFailMessage() {
        return deleteFailMessage;
    }

    public String getDeleteSuccessMessage() {
        return deleteSuccessMessage;
    }

    public String getDeleteView() {
        return deleteView;
    }

    private String getEntityName() {
        return getEntityClass().getName();
    }

    public String getId() {
        return id;
    }

    public E getInstance() {
        if (instance == null) {
            initInstance();
        }
        return instance;
    }

    public E getInstance(final String id) {
        setId(id);
        return getInstance();
    }

    public String getSaveFailMessage() {
        return saveFailMessage;
    }

    public String getSaveSuccessMessage() {
        if (StringUtils.isEmpty(saveSuccessMessage)) {
            saveSuccessMessage = EntityHome.commonSM.getString(EntityHome.saveSuccessMessageKey, id);
        }
        return saveSuccessMessage;
    }

    public String getSaveView() {
        if (saveView == null || "".equals(saveView)) {
            saveView = getUpdateView();
        }
        return saveView;
    }

    /**
     * The simple name of the managed entity
     */
    protected String getSimpleEntityName() {
        String name = getEntityName();
        if (name != null) {
            return name.lastIndexOf(".") > 0 && name.lastIndexOf(".") < name.length() ? name.substring(name.lastIndexOf(".") + 1, name.length()) : name;
        } else {
            return null;
        }
    }

    public String getUpdateFailMessage() {
        return updateFailMessage;
    }

    public String getUpdateSuccessMessage() {
        return updateSuccessMessage;
    }

    public String getUpdateView() {
        return updateView;
    }

    /**
     * Load the instance if the id is defined otherwise create a new instance <br />
     * Utility method called by {@link #getInstance()} to load the instance from
     * the Persistence Context if the id is defined. Otherwise a new instance is
     * created.
     *
     * @see #createInstance()
     */
    protected void initInstance() {
        if (isIdDefined()) {
            setInstance(baseService.get(getEntityClass(), getId()));
        } else {
            setInstance(createInstance());
        }
    }

    public boolean isIdDefined() {
        return getId() != null && !"".equals(getId());
    }

    public boolean isManaged() {
        return getInstance() != null && baseService.contains(getInstance());
    }

    @Transactional(readOnly = false)
    public String save() {
        return this.save(true);
    }

    /**
     * @param showFacesMessage 是否显示默认facesMessage
     * @return
     */
    @Transactional(readOnly = false)
    public String save(boolean showFacesMessage) {
        if (getEntityClass() == null) {
            throw new IllegalStateException("entityClass is null");
        }
        // message ~
        final String message;
        try {
            // log.debug("id=" + getId());
            if (!isIdDefined()) {// create instance
                if (StringUtils.isEmpty(id)) {
                    // id =
                    // (String)getInstance().getClass().getMethod("getId").invoke(getInstance());
                    id = (String) getAnnotadedWithId(getInstance(), getInstance().getClass());
                }
                if (!id.matches("^[a-zA-Z0-9_[-]]+$")) { // 编号只能为数字或者字母、下划线、中划线
                    FacesUtil.addErrorMessage(EntityHome.commonSM.getString("IDRuleMessage"));
                    return null;
                }
                if (null != baseService.get(getEntityClass(), getId())) {
                    message = EntityHome.commonSM.getString("entityIdHasExist", getId());
                    EntityHome.log.debug(message);
                    if (showFacesMessage) {
                        FacesUtil.addErrorMessage(message);
                    }
                    setId(null);
                    getInstance().getClass().getMethod("setId", String.class).invoke(getInstance(), getId());
                    return null;
                }
            }
            baseService.merge(getInstance());
            // baseService.saveOrUpdate(getInstance());
            message = getSaveSuccessMessage();
            if (showFacesMessage) {
                FacesUtil.addInfoMessage(message);
            }
            if (EntityHome.log.isInfoEnabled()) {
                EntityHome.log.info(message);
            }
        } catch (Exception e) {
            EntityHome.log.error(e.getMessage());
            FacesUtil.addErrorMessage(EntityHome.commonSM.getString(EntityHome.saveFailMessageKey));
        }
        return getSaveView();
    }

    public void setDeleteFailMessage(String deleteFailMessage) {
        this.deleteFailMessage = deleteFailMessage;
    }

    public void setDeleteSuccessMessage(String deleteSuccessMessage) {
        this.deleteSuccessMessage = deleteSuccessMessage;
    }

    public void setDeleteView(String deleteView) {
        this.deleteView = deleteView;
    }

    // ~ getter && setter
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Set/change the entity being managed.
     */
    public void setInstance(E instance) {
        this.instance = instance;
    }

    public void setSaveFailMessage(String saveFailMessage) {
        this.saveFailMessage = saveFailMessage;
    }

    public void setSaveSuccessMessage(String saveSuccessMessage) {
        this.saveSuccessMessage = saveSuccessMessage;
    }

    public void setSaveView(String saveView) {
        this.saveView = saveView;
    }

    public void setUpdateFailMessage(String updateFailMessage) {
        this.updateFailMessage = updateFailMessage;
    }

    public void setUpdateSuccessMessage(String updateSuccessMessage) {
        this.updateSuccessMessage = updateSuccessMessage;
    }

    public void setUpdateView(String updateView) {
        this.updateView = updateView;
    }

    @Transactional(readOnly = false)
    public String update() {
        baseService.update(getInstance());
        return getUpdateView();
    }
}
