package info.bfly.archer.theme;

import com.ocpsoft.pretty.PrettyContext;

public class ThemeConstants {
    /**
     * 主题状态 ENABLE = 1 , DISABLE = 0 , DEFAULT = 2
     *
     * @author wanghm
     *
     */
    public final static class ThemeStatus {
        public static final String ENABLE  = "1";
        public static final String DISABLE = "0";
        public static final String DEFAULT = "2";
    }
    /**
     * 主题模块（Theme）一些前台展示页面
     *
     * @author wanghm
     *
     */
    public static class View {
        public final static String VIEW_DIR                 = "/themes/themeadmin";
        /**
         * 主题列表页面
         */
        public static final String THEME_LIST               = View.VIEW_DIR + "/themeList";
        /**
         * 主题编辑页面
         */
        public static final String THEME_EDIT               = View.VIEW_DIR + "/themeEdit";
        /**
         * 模板列表页面
         */
        public static final String TEMPLATE_LIST            = View.VIEW_DIR + "/templateList";
        /**
         * 模板编辑页面
         */
        public static final String TEMPLATE_EDIT            = View.VIEW_DIR + "/templateEdit";
        /**
         * 模板部位列表页面
         */
        public static final String REGION_LIST              = View.VIEW_DIR + "/regionList";
        /**
         * 元件列表页面
         */
        public static final String COMPONENT_LIST           = View.VIEW_DIR + "/componentList";
        /**
         * 元件编辑页面
         */
        public static final String COMPONENT_EDIT           = View.VIEW_DIR + "/componentEdit";
        /**
         * 元件参数编辑页面
         */
        public static final String COMPONENT_PARAMETER_EDIT = View.VIEW_DIR + "/componentParameterEdit";
        /**
         * 主题设计页面
         */
        public static final String THEME_DESIGN             = View.VIEW_DIR + "/themeDesign";
    }

    /**
     * Package name.
     */
    public static final String Package                   = "info.bfly.archer.theme";
    /**
     * Session中存放pretty 配置对象的key
     */
    public final static String SESSION_KEY_PRETTY_CONFIG = PrettyContext.CONFIG_KEY;
    /**
     * Session中存放用户主题编号的key
     */
    public final static String SESSION_KEY_USER_THEME    = "user_theme";
    /**
     * 默认主题
     */
    public final static String DEFAULT_USER_THEME        = "default";
    /**
     * 存放主题文件的路径
     */
    public final static String THEME_PATH                = "/themes";
}
