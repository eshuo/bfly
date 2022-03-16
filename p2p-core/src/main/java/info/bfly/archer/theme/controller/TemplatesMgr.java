package info.bfly.archer.theme.controller;

import info.bfly.archer.theme.ThemeConstants;
import info.bfly.archer.theme.model.Component;
import info.bfly.archer.theme.model.ComponentParameter;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.StringManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@org.springframework.stereotype.Component
@Scope(ScopeType.VIEW)
public class TemplatesMgr implements java.io.Serializable {
    private static final long serialVersionUID = 6419385252367810112L;
    @Log
    private static Logger log;
    private final static String TEMPLATES_PATH = "/templates/";
    @Resource
    private HibernateTemplate ht;
    private StringManager sm = StringManager.getManager(ThemeConstants.Package);
    private List<String> templates;
    private String       selectTpl;
    private String       themeName;
    private String       content;
    private String       templateSrc;
    private Component    component;
    private String       insertContent;
    private String       editContent;
    private List<Component>          components                = new ArrayList<Component>();
    private List<ComponentParameter> componentParametersEdit   = new ArrayList<ComponentParameter>();
    private List<ComponentParameter> componentParametersInsert = new ArrayList<ComponentParameter>();

    public TemplatesMgr() {
        if (component == null) {
            component = new Component();
        }
    }

    public void editContent() {
        String str = initInclude(getComponentParametersEdit());
        setEditContent(str);
    }

    public void editInsertContent() {
        String str = initInclude(getComponentParametersInsert());
        setInsertContent(str);
    }

    public void findTemplate() {
        setComponent(new Component());
        String scriptSrc = getTemplateSrc();
        if (StringUtils.isEmpty(scriptSrc)) {
            return;
        } else {
            List<Component> componentList = (List<Component>) ht.findByNamedQuery("Component.findByUrl", scriptSrc);
            if (componentList != null && componentList.size() > 1) {
                // FacesUtil.addErrorMessage(sm.getString("componentUrlConflict"));
                return;
            } else if (componentList != null && componentList.size() == 1) {
                setComponent(componentList.get(0));
                setComponentParametersEdit((List<ComponentParameter>) ht.findByNamedQuery("ComponentParameter.findByCompanent", componentList.get(0).getId()));
            } else if (componentList == null || componentList.size() < 1) {
                // FacesUtil.addErrorMessage(sm.getString("componentUrlError"));
                return;
            }
        }
    }

    public Component getComponent() {
        return component;
    }

    public List<ComponentParameter> getComponentParametersEdit() {
        return componentParametersEdit;
    }

    public List<ComponentParameter> getComponentParametersInsert() {
        return componentParametersInsert;
    }

    public List<Component> getComponents() {
        components = (List<Component>) ht.findByNamedQuery("Component.findAll");
        return components;
    }

    public String getContent() {
        try {
            content = FileUtils.readFileToString(getFile(getThemePath(), getSelectTpl()), "utf-8");
        } catch (IOException e) {
            content = "Read File error.";
            log.debug(e.getMessage());
        }
        return content;
    }

    public String getEditContent() {
        return editContent;
    }

    private File getFile(String path) {
        return getFile(path, null);
    }

    private File getFile(String path, String file) {
        if (file == null) {
            return new File(path);
        }
        return new File(path + file);
    }

    public HibernateTemplate getHt() {
        return ht;
    }

    public String getInsertContent() {
        return insertContent;
    }

    public String getSelectTpl() {
        if (selectTpl == null && getTemplates().size() > 0) {
            selectTpl = (getTemplates().get(0));
        }
        return selectTpl;
    }

    public List<String> getTemplates() {
        if (templates == null) {
            // templates =
            // get really path .
            // System.out.println(FacesUtil.getAppRealPath()+ThemeConstants.THEME_PATH+"blog/");
            File tplsFile = getFile(getThemePath());
            if (tplsFile.exists()) {
                templates = Arrays.asList(tplsFile.list());
            }
            // filter .xhtml file .
        }
        return templates;
    }

    public String getTemplateSrc() {
        return templateSrc;
    }

    public String getThemeName() {
        return (String) FacesUtil.getSessionAttribute(ThemeConstants.SESSION_KEY_USER_THEME);
    }

    private String getThemePath() {
        return (FacesUtil.getAppRealPath() + ThemeConstants.THEME_PATH + getThemeName() + TemplatesMgr.TEMPLATES_PATH);
    }

    public void handleComponentChange() {
        String componentId = getComponent().getId();
        if (StringUtils.isEmpty(componentId)) {
            return;
        }
        setComponent(ht.get(Component.class, componentId));
        setComponentParametersInsert((List<ComponentParameter>) ht.findByNamedQuery("ComponentParameter.findByCompanent", componentId));
    }

    private String initInclude(List<ComponentParameter> componentParameters) {
        String str = "";
        if (StringUtils.isEmpty(getComponent().getScriptUrl())) {
            return str;
        }
        List<Component> componentList = (List<Component>) ht.findByNamedQuery("Component.findByUrl", getComponent().getScriptUrl());
        if (componentList != null && componentList.size() > 1) {
            // FacesUtil.addErrorMessage(sm.getString("componentUrlConflict"));
            return str;
        } else if (componentList != null && componentList.size() == 1) {
            setComponent(componentList.get(0));
        } else if (componentList == null || componentList.size() < 1) {
            // FacesUtil.addErrorMessage(sm.getString("componentUrlError"));
            return str;
        }
        str = "\n<ui:include src=\"" + getComponent().getScriptUrl() + "\">\n";
        if (componentParameters != null && componentParameters.size() > 0) {
            for (ComponentParameter c : componentParameters) {
                String cId = c.getId();
                ComponentParameter comNew = ht.get(ComponentParameter.class, cId);
                if (StringUtils.isNotEmpty(c.getValue()) && !c.getValue().equals(comNew.getValue())) {
                    str = str + "    <ui:param name=\"" + c.getName() + "\" value=\"" + c.getValue() + "\"></ui:param>\n";
                }
            }
        }
        str = str + "</ui:include>\n";
        return str;
    }

    public String saveTpl() {
        // save file .
        // System.out.println(getThemePath() + getSelectTpl() + "\n" + content);
        try {
            FileUtils.writeStringToFile(getFile(getThemePath(), selectTpl), content, "utf-8");
            FacesUtil.addInfoMessage(sm.getString("saveComponentSuccess"));
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
        // add message.
        return null;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public void setComponentParametersEdit(List<ComponentParameter> componentParametersEdit) {
        this.componentParametersEdit = componentParametersEdit;
    }

    public void setComponentParametersInsert(List<ComponentParameter> componentParametersInsert) {
        this.componentParametersInsert = componentParametersInsert;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEditContent(String editContent) {
        this.editContent = editContent;
    }

    public void setHt(HibernateTemplate ht) {
        this.ht = ht;
    }

    public void setInsertContent(String insertContent) {
        this.insertContent = insertContent;
    }

    public void setSelectTpl(String selectTpl) {
        this.selectTpl = selectTpl;
    }

    public void setTemplates(List<String> templates) {
        this.templates = templates;
    }

    public void setTemplateSrc(String templateSrc) {
        this.templateSrc = templateSrc;
    }
}
