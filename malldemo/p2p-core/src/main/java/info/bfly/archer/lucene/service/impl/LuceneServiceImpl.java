package info.bfly.archer.lucene.service.impl;

import info.bfly.archer.common.model.PageModel;
import info.bfly.archer.lucene.LuceneConstants;
import info.bfly.archer.lucene.model.Indexing;
import info.bfly.archer.lucene.model.ResultBean;
import info.bfly.archer.lucene.service.LuceneService;
import info.bfly.archer.node.model.Node;
import info.bfly.core.annotations.Log;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.FilterCharUtil;
import info.bfly.core.util.StringManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LuceneServiceImpl implements LuceneService {
    @Log
    static Logger log;
    private static final StringManager sm  = StringManager.getManager(LuceneConstants.Package);
    @Resource
    private HibernateTemplate          ht;

    /**
     * 创建索引
     */
    @Override
    public void createNewIndex(Indexing indexing, String indexPath, int weight) {
        /*
         * IndexWriter indexWriter = null; try { indexWriter =
         * getIndexWriter(indexPath); Document doc = getDocument(indexing,
         * weight); // 以索引的ID域为“主键”创建索引，若索引已经存在，则删除原有的索引再重新创建
         * indexWriter.updateDocument(new Term(LuceneConstants.Lucene.FIELDID,
         * indexing.getId()), doc); indexWriter.close(); } catch (IOException e)
         * { log.debug(e.getMessage());
         * log.error(sm.getString("log.createIndex.error", indexing.getId(),
         * e)); }
         */
    }

    /**
     * 封装查询结果
     */
    private ResultBean createResultBean(Document targetDoc, Query query, Analyzer analyzer) {
        ResultBean resultBean = new ResultBean();
        resultBean.setAuthor(targetDoc.get(LuceneConstants.Lucene.AUTHOR));
        String highLightStr = toHighlighter(query, targetDoc, LuceneConstants.Lucene.CONTENT, analyzer);
        if (StringUtils.isNotEmpty(highLightStr)) {
            resultBean.setContent(highLightStr + "...");
        }
        else {
            resultBean.setContent(targetDoc.get(LuceneConstants.Lucene.CONTENT));
        }
        resultBean.setId(targetDoc.get(LuceneConstants.Lucene.FIELDID));
        String highLighttitle = toHighlighter(query, targetDoc, LuceneConstants.Lucene.TITLE, analyzer);
        if (StringUtils.isNotEmpty(highLighttitle)) {
            resultBean.setTitle(highLighttitle);
        }
        else {
            resultBean.setTitle(targetDoc.get(LuceneConstants.Lucene.TITLE));
        }
        String date = targetDoc.get(LuceneConstants.Lucene.INDEXDATE);
        SimpleDateFormat sdf = getSimpleDateFormat();
        Date newDate = null;
        try {
            newDate = sdf.parse(date);
        }
        catch (java.text.ParseException e) {
            log.debug(e.getMessage());
            LuceneServiceImpl.log.error(LuceneServiceImpl.sm.getString("log.dateFormat.error", String.class, Date.class));
        }
        resultBean.setCreateTime(newDate);
        return resultBean;
    }

    @Override
    public void deleteIndex(String id, String indexPath) {
        IndexWriter indexWriter = null;
        try {
            indexWriter = getIndexWriter(indexPath);
            indexWriter.deleteDocuments(new Term(LuceneConstants.Lucene.FIELDID, id));
            indexWriter.close();
            if (LuceneServiceImpl.log.isInfoEnabled()) {
                LuceneServiceImpl.log.info(LuceneServiceImpl.sm.getString("log.info.deleteIndex", FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}"), id));
            }
        } catch (IOException e) {
            log.debug(e.getMessage());
            LuceneServiceImpl.log.error(LuceneServiceImpl.sm.getString("log.error.deleteIndex", id, e));
        }
    }

    private Document getDocument(Indexing indexing, int weight) {
        Document doc = new Document();
        // 创建索引的文本域
        Field contentField = new Field(LuceneConstants.Lucene.CONTENT, FilterCharUtil.Html2Text(indexing.getContent()), Store.YES, Index.ANALYZED);
        // contentField.setBoost(10.0f); //创建索引时设置关键字的权重，数值越大，排名越靠前
        // 创建索引的标题域
        Field titleField = new Field(LuceneConstants.Lucene.TITLE, indexing.getTitle(), Store.YES, Index.ANALYZED);
        // titleField.setBoost(10.0f);
        // 创建索引的作者域
        Field authorFiled = new Field(LuceneConstants.Lucene.AUTHOR, indexing.getAuthor(), Store.YES, Index.ANALYZED);
        // 创建索引的ID域
        Field idField = new Field(LuceneConstants.Lucene.FIELDID, indexing.getId(), Store.YES, Index.ANALYZED);
        /**
         * 创建索引的时间域
         */
        SimpleDateFormat sdf = getSimpleDateFormat();
        String now = sdf.format(indexing.getCreateTime());
        Field dateField = new Field(LuceneConstants.Lucene.INDEXDATE, now, Store.YES, Index.ANALYZED);
        doc.add(contentField);
        doc.add(titleField);
        doc.add(authorFiled);
        doc.add(idField);
        doc.add(dateField);
        return doc;
    }

    /**
     * 获得IndexWriter对象
     *
     * @param indexPath
     * @return
     */
    private IndexWriter getIndexWriter(String indexPath) {
        Directory indexDictory = null;
        IndexWriter indexWriter = null;
        try {
            indexDictory = FSDirectory.open(new File(indexPath));
            Analyzer analyzer = new IKAnalyzer(true);
            IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_40, analyzer);
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            iwc.setMaxBufferedDocs(1000);
            if (IndexWriter.isLocked(indexDictory)) {
                IndexWriter.unlock(indexDictory);
            }
            indexWriter = new IndexWriter(indexDictory, iwc);
        } catch (IOException e) {
            log.debug(e.getMessage());
            LuceneServiceImpl.log.error(LuceneServiceImpl.sm.getString("log.rebuildIndex.error", e));
        }
        return indexWriter;
    }

    private SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 搜索结果分页
     */
    @Override
    public PageModel<ResultBean> paginationResult(String key, String indexPath, int pageNo, int pageSize) {
        PageModel<ResultBean> pageModel = new PageModel<ResultBean>();
        if (StringUtils.isEmpty(key)) {
            return pageModel;
        }
        List<ResultBean> list = new ArrayList<ResultBean>();
        list = search(key, indexPath);
        int begin = pageSize * (pageNo - 1);// 本页显示的起始位置
        int end = Math.min(begin + pageSize, list.size());// 本页的结束位置
        if (begin == end) {
            pageModel.setCount(list.size());
            pageModel.setPage(pageNo);
            pageModel.setPageSize(pageSize);
            pageModel.setData(list);
            return pageModel;
        }
        List<ResultBean> result = new ArrayList<ResultBean>();
        for (int i = begin; i < end; i++) {
            result.add(list.get(i));
        }
        pageModel.setCount(list.size());
        pageModel.setPage(pageNo);
        pageModel.setPageSize(pageSize);
        pageModel.setData(result);
        return pageModel;
    }

    /**
     * 重置索引
     */
    @Override
    public void rebuildIndex(String indexPath) {
        try {
            IndexWriter indexWriter = getIndexWriter(indexPath);
            List<Node> nodeList = new ArrayList<Node>();
            int times = 0; // 用于记录每次循环次数
            do {
                final int limit = 1000 * times;
                nodeList = ht.execute(new HibernateCallback<List<Node>>() {
                    @Override
                    public List<Node> doInHibernate(Session session) throws HibernateException {
                        org.hibernate.query.Query query = session.getNamedQuery("Node.findAllNodeAndNodeBody");
                        query.setFirstResult(limit);
                        query.setMaxResults(1000);
                        return query.list();
                    }
                });
                if (nodeList != null && nodeList.size() > 0) {
                    for (int i = 0; i < nodeList.size(); i++) {
                        Indexing indexing = new Indexing();
                        indexing.setId(nodeList.get(i).getId());
                        indexing.setTitle(nodeList.get(i).getTitle());
                        indexing.setAuthor(nodeList.get(i).getUserByCreator().getUsername());
                        indexing.setContent(nodeList.get(i).getNodeBody().getBody());
                        indexing.setCreateTime(nodeList.get(i).getCreateTime());
                        Document doc = getDocument(indexing, 1);
                        // 以索引的ID域为“主键”创建索引，若索引已经存在，则删除原有的索引再重新创建
                        indexWriter.updateDocument(new Term(LuceneConstants.Lucene.FIELDID, indexing.getId()), doc);
                    }
                }
                times = times + 1;
            }
            while (nodeList != null && nodeList.size() > 0);
            indexWriter.close();
        }
        catch (Exception e) {
            log.debug(e.getMessage());
            LuceneServiceImpl.log.error(LuceneServiceImpl.sm.getString("log.rebuildIndex.error", e));
        }
    }

    /**
     * 搜索
     */
    @Override
    public List<ResultBean> search(String param, String indexPath) {
        List<ResultBean> list = new ArrayList<ResultBean>();
        File indexDir = new File(indexPath);
        Directory directory = null;
        IndexReader indexReader = null;
        try {
            directory = FSDirectory.open(indexDir);
            indexReader = IndexReader.open(directory);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            Analyzer analyzer = new IKAnalyzer(true);
            QueryParser qp = new QueryParser(Version.LUCENE_40, LuceneConstants.Lucene.CONTENT, analyzer);
            qp.setDefaultOperator(QueryParserBase.AND_OPERATOR);
            Sort sort = new Sort(new SortField(LuceneConstants.Lucene.CONTENT, SortField.Type.SCORE, false));
            Query query = qp.parse(param);
            TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE, sort);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            for (int i = 0; i < topDocs.totalHits; i++) {
                // 封装一个结果bean并放入list中
                Document targetDoc = indexSearcher.doc(scoreDocs[i].doc);
                ResultBean resultBean = createResultBean(targetDoc, query, analyzer);
                list.add(resultBean);
            }
            indexReader.close();
        }
        catch (IOException e) {
            log.debug(e.getMessage());
            LuceneServiceImpl.log.error(LuceneServiceImpl.sm.getString("log.search.error", param, e));
        } catch (ParseException e) {
            log.debug(e.getMessage());
            LuceneServiceImpl.log.error(LuceneServiceImpl.sm.getString("log.search.error", param, e));
        }
        return list;
    }

    /**
     * 设置文章的权重已确定其在搜索结果中显示的位置
     */
    @Override
    public void setWeight(int weight) {
        // TODO 设置文章权重的实现
    }

    /**
     * 高亮设置
     *
     * @param query
     * @param doc
     * @param field 指定显示高亮内容的域
     * @return
     */
    private String toHighlighter(Query query, Document doc, String field, Analyzer analyzer) {
        try {
            String str = doc.get(field);
            SimpleHTMLFormatter simpleHtmlFormatter = new SimpleHTMLFormatter("<font color=\"red\">", "</font>");
            Highlighter highlighter = new Highlighter(simpleHtmlFormatter, new QueryScorer(query));
            // 设置显示高亮文本的长度 str.length()指定长度为该文本的长度
            highlighter.setTextFragmenter(new SimpleFragmenter(100));
            String highlighterStr = highlighter.getBestFragment(analyzer, field, str);
            return highlighterStr;
        } catch (IOException e) {
            LuceneServiceImpl.log.error(e.getMessage());
        } catch (InvalidTokenOffsetsException e) {
            LuceneServiceImpl.log.error(e.getMessage());
        }
        return null;
    }
}
