package info.bfly.archer.node.service;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 敏感词过滤 (评论和文章)
 * 
 * @author wangz
 *
 */
public interface WordFilterService {
    /**
     * 初始化patterns
     */
    List<Pattern> initPatterns();

    /**
     * 敏感词过滤
     *
     * @param targetStr
     * @return
     */
    String wordFilter(String targetStr);
}
