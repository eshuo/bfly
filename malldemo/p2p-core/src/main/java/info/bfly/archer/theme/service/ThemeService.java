package info.bfly.archer.theme.service;

public interface ThemeService {
    /**
     * 设置系统的默认主题
     *
     * @param themeId 主题编号
     * @return
     */
    boolean setDefaultTheme(String themeId);
}
