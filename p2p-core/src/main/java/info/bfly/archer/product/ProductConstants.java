package info.bfly.archer.product;

/**
 * Product constants.
 *
 */
public class ProductConstants {
    /**
     * 产品模块（Product）一些前台展示页面
     */
    public static class View {
        public final static String VIEW_DIR     = "/admin/product";
        /**
         * 产品列表页面
         */
        public static final String PRODUCT_LIST = View.VIEW_DIR + "/productList";
        /**
         * 产品编辑页面
         */
        public static final String PRODUCT_EDIT = View.VIEW_DIR + "/productEdit";
    }

    /**
     * Package name. info.bfly.archer.product
     */
    public static final String Package  = "info.bfly.archer.product";
    public static final String NodeType = "PRODUCT";
}
