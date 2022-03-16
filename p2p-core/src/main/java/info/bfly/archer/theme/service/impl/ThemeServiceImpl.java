package info.bfly.archer.theme.service.impl;

import info.bfly.archer.theme.ThemeConstants;
import info.bfly.archer.theme.model.Theme;
import info.bfly.archer.theme.service.ThemeService;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ThemeServiceImpl implements ThemeService {
    @Resource
    private HibernateTemplate ht;

    @Override
    @Transactional(readOnly = false)
    public boolean setDefaultTheme(String themeId) {
        if (StringUtils.isEmpty(themeId)) {
            return false;
        }
        ht.bulkUpdate("update Theme set status = ? where status = ?", ThemeConstants.ThemeStatus.ENABLE, ThemeConstants.ThemeStatus.DEFAULT);
        Theme theme = ht.get(Theme.class, themeId);
        theme.setStatus(ThemeConstants.ThemeStatus.DEFAULT);
        ht.update(theme);
        return true;
    }
}
