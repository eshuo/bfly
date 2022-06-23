package info.bfly.archer.user.service;

import info.bfly.archer.user.model.AdverLeague;
import info.bfly.archer.user.model.AdverModel;

import java.util.List;

/**
 * 广告联盟接口
 * 
 * @author Administrator
 *
 */
@SuppressWarnings("unused")
public interface AdverService {
    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    AdverLeague getAdverByID(int id);

    /**
     * 反馈给联盟查询
     *
     * @param mid   联盟Id
     * @param uid   联盟用户Id
     * @param stime 开始日期
     * @param etime 结束日期
     * @return
     */
    List<AdverLeague> getAlList(String mid, String uid, String stime, String etime);

    /**
     * 查询某个时间段下，MID下的注册用户数
     *
     * @param d1
     * @param d2
     * @return
     */
    List<AdverModel> getCoungGroupMid(final String regStartDate, final String regEndDate);

    /**
     * 统计MID下的注册的用户数
     *
     * @param mid
     * @return
     */
    int getCountByMid(String mid);

    /**
     * 查询列表
     *
     * @return
     */
    List<AdverLeague> queryList();

    /**
     * 保存
     *
     * @param adverLeague
     */
    void save(AdverLeague adverLeague);
}
