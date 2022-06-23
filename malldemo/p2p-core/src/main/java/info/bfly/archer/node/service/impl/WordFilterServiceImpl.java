package info.bfly.archer.node.service.impl;

import info.bfly.archer.node.model.WordFilter;
import info.bfly.archer.node.service.WordFilterService;
import info.bfly.core.annotations.Log;
import org.slf4j.Logger;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service("wordFilterService")
@Transactional
public class WordFilterServiceImpl implements WordFilterService {
    @Log
    static Logger log;
    @Resource
    HibernateTemplate ht;
    private List<Pattern> patterns;

    private List<Pattern> getPatterns() {
        if (patterns == null) {
            patterns = initPatterns();
        }
        return patterns;
    }

    // FIXME:敏感词字与字之间用空格隔开
    @Override
    public List<Pattern> initPatterns() {
        List<WordFilter> wordFilters = (List<WordFilter>) ht.findByNamedQuery("WordFilter.findAllWordFilters");
        List<Pattern> ptns = new ArrayList<Pattern>();
        for (WordFilter wordFilter : wordFilters) {
            ptns.add(Pattern.compile("(?:" + wordFilter.getWord().replace(" ", "\\p{ASCII}{0,100}") + ")"));
        }
        return ptns;
    }

    public void setPatterns(List<Pattern> patterns) {
        this.patterns = patterns;
    }

    @Override
    public String wordFilter(String targetStr) {
        // long time = System.currentTimeMillis();
        for (Pattern pattern : getPatterns()) {
            // 替换词语
            targetStr = pattern.matcher(targetStr).replaceAll("***");
        }
        // System.out.println(System.currentTimeMillis() - time);
        return targetStr;
    }
}
