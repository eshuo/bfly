package info.bfly.archer.node;

/**
 * Node constants.
 *
 * @author wanghm
 *
 */
public class NodeConstants {
    /**
     * 内容模块（Node）一些前台展示页面
     *
     * @author wanghm
     */
    public static class View {
        public final static String VIEW_DIR       = "/admin/node";
        /**
         * 文章列表页面
         */
        public static final String NODE_LIST      = View.VIEW_DIR + "/nodeList";
        /**
         * 文章编辑页面
         */
        public static final String NODE_EDIT      = View.VIEW_DIR + "/nodeEdit";
        /**
         * 文章类型列表页面
         */
        public static final String NODE_TYPE_LIST = View.VIEW_DIR + "/nodeTypeList";
        /**
         * 文章类型编辑页面
         */
        public static final String NODE_TYPE_EDIT = View.VIEW_DIR + "/nodeTypeEdit";
        /**
         * 文章属性列表页面
         */
        public static final String NODE_ATTR_LIST = View.VIEW_DIR + "/nodeAttrList";
    }

    /**
     * Package name.
     */
    public final static String Package             = "info.bfly.archer.node";
    public final static int    DEFAULT_RESULT_SIZE = 15;
    /**
     * 默认node type为文章
     */
    public final static String DEFAULT_TYPE        = "article";
}
