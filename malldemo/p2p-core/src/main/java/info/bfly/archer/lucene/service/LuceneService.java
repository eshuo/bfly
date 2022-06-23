package info.bfly.archer.lucene.service;

import info.bfly.archer.common.model.PageModel;
import info.bfly.archer.lucene.model.Indexing;
import info.bfly.archer.lucene.model.ResultBean;

import java.util.List;

public interface LuceneService {
    /**
     * 创建索引
     *
     * @param index
     */
    void createNewIndex(Indexing indexing, String indexPath, int weight);

    /**
     * 根据Id删除相应的的索引
     *
     * @param id
     * @param indexPath
     */
    void deleteIndex(String id, String indexPath);

    /**
     * 对搜索结果分页
     *
     * @param param     查询的关键字
     * @param pageNo    页码号
     * @param indexPath 索引文件所在的路径
     * @param pageSize  每页显示的数据条数
     * @return
     */
    PageModel<ResultBean> paginationResult(String param, String indexPath, int pageNo, int pageSize);

    /**
     * 重建索引
     */
    void rebuildIndex(String indexPath);

    /**
     * 查询
     *
     * @param param 查询的关键字
     * @return
     */
    List<ResultBean> search(String param, String indexPath);

    /**
     * 设置文章的索引权重
     *
     * @param weight
     */
    void setWeight(int weight);
}
