package info.bfly.archer.language.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.language.model.Language;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Component
@Scope(ScopeType.REQUEST)
public class LanguageList extends EntityQuery<Language> implements Serializable {
    @Log
    static  Logger log;
    private List<Language>                 languages;

    public LanguageList() {
        final String[] RESTRICTIONS = { "id like #{languageList.example.id}", "name like #{languageList.example.name}", "country = #{languageList.example.country}" };
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }

    public List<Language> getLanguages() {
        if (languages == null) {
            languages = (List<Language>) getHt().findByNamedQuery("Language.findEnableLanguage");
        }
        return languages;
    }
}
