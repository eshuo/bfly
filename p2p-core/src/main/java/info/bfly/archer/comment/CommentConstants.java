package info.bfly.archer.comment;

public class CommentConstants {
    /**
     * 评论返回视图
     *
     * @author Administrator
     */
    public static class View {
        public final static String VIEW_DIR     = "/admin/comment";
        /**
         * 文章列表页面
         */
        public static final String COMMENT_LIST = View.VIEW_DIR + "/commentList";
        /**
         * 文章编辑页面
         */
        public static final String COMMENT_EDIT = View.VIEW_DIR + "/commentEdit";
    }

    /**
     * Package name. info.bfly.archer.comment .
     */
    public static final String Package                = "info.bfly.archer.comment";
    /**
     * 评论状态，值为1，当前状态为可用状态
     */
    public static final String COMMENT_STATUS_ENABLE  = "1";
    /**
     * 评论状态，值为0，当前状态为不可用状态
     */
    public static final String COMMENT_STATUS_DISABLE = "0";
    /**
     * 评论状态，值为2，当前状态为正在审核状态
     */
    public static final String COMMENT_STATUS_CHECK   = "2";
}
