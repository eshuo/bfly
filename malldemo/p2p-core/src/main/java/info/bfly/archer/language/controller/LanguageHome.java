package info.bfly.archer.language.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.language.LanguageConstants;
import info.bfly.archer.language.model.Language;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.StringManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

@Component
@Scope(ScopeType.REQUEST)
public class LanguageHome extends EntityHome<Language> implements Serializable {
    private static final long serialVersionUID = 6618951975751189888L;
    @Log
    static Logger log;
    private final static StringManager sm = StringManager.getManager(LanguageConstants.Package);
    @Resource
    HibernateTemplate ht;
    private List<String> localeList = new ArrayList<String>();
    private String                     str;

    public LanguageHome() {
        setUpdateView(FacesUtil.redirect(LanguageConstants.View.CONFIG_LIST));
        setDeleteView(FacesUtil.redirect(LanguageConstants.View.CONFIG_LIST));
    }

    @Override
    @Transactional(readOnly = false)
    public String delete() {
        if (LanguageHome.log.isInfoEnabled()) {
            LanguageHome.log.info(LanguageHome.sm.getString("log.info.deleteLanguage", FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}"), new Date(), getId()));
        }
        return super.delete();
    }

    public List<String> getLocaleList() {
        if (localeList == null || localeList.size() <= 0) {
            List<Language> listLanguage = (List<Language>) ht.findByNamedQuery("Language.findAllLanguage");
            Locale[] list = Locale.getAvailableLocales();
            Locale locale = new Locale("Enlish");
            String first = LanguageHome.sm.getString("chooseYourLanguage");
            localeList.add(first);
            for (int i = 0; i < list.length; i++) {
                String str = list[i].getDisplayCountry();
                if (StringUtils.isNotEmpty(str)) {
                    str = list[i].getDisplayCountry(locale) + "  " + list[i].getDisplayCountry() + "(" + list[i].getDisplayLanguage() + ")  " + list[i].getLanguage() + "_" + list[i].getCountry();
                    if (localeList.contains(str)) {
                        continue;
                    }
                    else {
                        if (listLanguage != null && listLanguage.size() > 0) {
                            boolean isNotExist = true;
                            String countryStr = list[i].getLanguage() + "_" + list[i].getCountry();
                            for (Language l : listLanguage) {
                                if (countryStr.equals(l.getId())) {
                                    isNotExist = false;
                                }
                            }
                            if (isNotExist) {
                                localeList.add(str);
                            }
                        }
                        else {
                            localeList.add(str);
                        }
                    }
                }
            }
        }
        Collections.sort(localeList);
        return localeList;
    }

    public String getStr() {
        return str;
    }

    public void initLanguage() {
        String language = getStr();
        if (StringUtils.isNotEmpty(language)) {
            String[] arrStr = language.split("  ");
            if (arrStr.length > 1) {
                getInstance().setId(arrStr[2]);
                getInstance().setCountry(arrStr[1]);
                getInstance().setName(arrStr[0]);
            }
        }
    }

    @Override
    @Transactional
    public String save() {
        return super.save();
    }

    public void setLocaleList(List<String> localeList) {
        this.localeList = localeList;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
