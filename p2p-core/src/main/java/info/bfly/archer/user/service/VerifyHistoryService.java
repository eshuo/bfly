package info.bfly.archer.user.service;

import info.bfly.archer.user.model.VerifyHistory;

/**
 * Created by XXSun on 3/9/2017.
 * 审核
 */
public interface VerifyHistoryService {
    /**
     * 获取单条审核信息
     */
    VerifyHistory getVerifyHistory(String id);

    /**
     * 添加一条审核信息
     */

    void addVerifyHistory(VerifyHistory verifyHistory);

    /**
     * 根据targetId 获取某个项目的审核信息
     */
}
