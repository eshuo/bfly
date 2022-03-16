package info.bfly.archer.term;

/**
 * Menu constants.
 * 
 * @author wanghm
 *
 */
public class TermConstants {
    /**
     * Term 模块所有的命名查询
     */
    public static class NamedQuery {
    }

    /**
     * 有关分类术语（term）页面
     *
     * @author wanghm
     */
    public static class View {
        public final static String VIEW_DIR              = "/admin/term";
        /**
         * 分类术语列表页面
         */
        public static final String TERM_LIST             = View.VIEW_DIR + "/categoryTermList";
        /**
         * 分类术语编辑页面
         */
        public static final String TERM_EDIT             = "/admin/menu/categoryTermEdit";
        /**
         * 分类术语类型列表页面
         */
        public static final String TERM_TYPE_LIST        = View.VIEW_DIR + "/categoryTermTypeList";
        /**
         * 分类术语类型编辑页面
         */
        public static final String TERM_TYPE_EDIT        = "/admin/menu/categoryTermTypeEdit";
        /**
         * TITLE的连接符
         */
        public static final String CONNECTOR_TITLE       = "_";
        /**
         * keywords连接符
         */
        public static final String CONNECTOR_KEYWORDS    = " ";
        /**
         * DESCRIPTION连接符
         */
        public static final String CONNECTOR_DESCRIPTION = "、";
    }

    /**
     * Package name. info.bfly.archer.term
     */
    public static final String Package = "info.bfly.archer.term";
}
