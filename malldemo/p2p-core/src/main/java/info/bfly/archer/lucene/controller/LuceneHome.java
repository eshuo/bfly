package info.bfly.archer.lucene.controller;

import info.bfly.archer.common.model.PageModel;
import info.bfly.archer.lucene.LuceneConstants;
import info.bfly.archer.lucene.model.ResultBean;
import info.bfly.archer.lucene.service.LuceneService;
import info.bfly.core.annotations.Log;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.StringManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Scope(ScopeType.REQUEST)
public class LuceneHome {
    @Log
    static Logger log;
    private final static StringManager sm = StringManager.getManager(LuceneConstants.Package);
    @Resource
    private LuceneService         luceneService;
    private PageModel<ResultBean> pageModel;
    private String                key;
    private String                isEmpty;
    private Integer               pageNo;
    private Integer               pageSize;

    public String getIsEmpty() {
        return isEmpty;
    }

    public String getKey() {
        return key;
    }

    public LuceneService getLuceneService() {
        return luceneService;
    }

    public PageModel<ResultBean> getPageModel() {
        return pageModel;
    }

    public Integer getPageNo() {
        if (pageNo == null || pageNo == 0) {
            pageNo = 1;
        }
        return pageNo;
    }

    public Integer getPageSize() {
        if (pageSize == null || pageSize == 0) {
            // TODO 可在数据库中配置
            pageSize = 15;
        }
        return pageSize;
    }

    public PageModel<ResultBean> getResults(String value, Integer pageNo) {
        if (StringUtils.isEmpty(value.trim())) return pageModel;
        if (pageNo == null || pageNo == 0) {
            pageNo = 1;
        }
        if (pageModel == null) pageModel = luceneService.paginationResult(value, LuceneConstants.LUCENE_INDEX_PATH, pageNo, getPageSize());
        return pageModel;
    }

    public String rebuildIndex() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
            String start = sdf.format(new Date());
            luceneService.rebuildIndex(LuceneConstants.LUCENE_INDEX_PATH);
            String end = sdf.format(new Date());
            FacesUtil.addInfoMessage(LuceneHome.sm.getString("rebuildIndexSuccessful"));
            if (LuceneHome.log.isInfoEnabled()) {
                LuceneHome.log.info(LuceneHome.sm.getString("log.info.rebuildIndexSuccessful", start, end));
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
            LuceneHome.log.error(LuceneHome.sm.getString("log.error.rebuildIndexSuccessful", e));
        }
        return null;
    }

    public void setIsEmpty(String isEmpty) {
        this.isEmpty = isEmpty;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setLuceneService(LuceneService luceneService) {
        this.luceneService = luceneService;
    }

    public void setPageModel(PageModel<ResultBean> pageModel) {
        this.pageModel = pageModel;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    // /**
    // * 获取索引所在目录
    // *
    // * @param request
    // * @return
    // */
    // private String getAbsulotlyPath(HttpServletRequest request) {
    // String absulotlyPath = "";
    // absulotlyPath = request.getSession().getServletContext()
    // .getRealPath("");
    // // TODO /lucene/index 路径可从数据库中读取
    // absulotlyPath = absulotlyPath + "/lucene/index";
    // return absulotlyPath;
    // }
}
