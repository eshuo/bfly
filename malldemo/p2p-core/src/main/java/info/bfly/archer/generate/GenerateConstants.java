package info.bfly.archer.generate;

public class GenerateConstants {
    public static final class MenuType {
        public static final String MAIN_MENU  = "MainMenu";
        public static final String NAVIGATION = "Navigation";
        public static final String MANAGEMENT = "Management";
    }

    /**
     * 文章类型
     *
     * @author Administrator
     */
    public static final class nodeType {
        public static final String ARTICLE = "";
    }

    public static final class TermType {
        /**
         * 文章
         */
        public static final String ARTICLE   = "ARTICLE";
        /**
         * 产品
         */
        public static final String PRODUCT   = "PRODUCT";
        /**
         * 商品
         */
        public static final String GOODS     = "GOODS";
        /**
         * 案例
         */
        public static final String CASE      = "case";
        /**
         * 文章（有图片）
         */
        public static final String ARTICLE_P = "ARTICLE_P";
    }

    public static final class UrlType {
        /**
         * 列表页（分类）
         */
        public static final String TERM = "term";
        /**
         * 文章详情页
         */
        public static final String NODE = "node";
        /**
         * 单页
         */
        public static final String PAGE = "spage";
    }

    /**
     * Package name. info.bfly.archer.common .
     */
    public static final String Package = "info.bfly.archer.generate";
}
