package info.bfly.archer.user.service;

import info.bfly.archer.user.exception.MinPointLimitCannotMattchSeqNumException;
import info.bfly.archer.user.exception.SeqNumAlreadyExistException;
import info.bfly.archer.user.exception.UserExistInLevelException;
import info.bfly.archer.user.model.LevelForUser;

/**
 * Description: 等级service<br/>
 *
 */
public interface LevelService {
    /**
     * 删除
     *
     * @param levelForUserId
     * @throws UserExistInLevelException 有用户处于该等级，不能删除
     */
    void delete(String levelForUserId) throws UserExistInLevelException;

    /**
     * 新增或修改等级
     *
     * @param levelForUser 等级对象
     * @throws SeqNumAlreadyExistException              等级序号已存在
     * @throws MinPointLimitCannotMattchSeqNumException 等级积分下限的顺序，与等级序号的顺序，不相匹配
     */
    void saveOrUpdate(LevelForUser levelForUser) throws SeqNumAlreadyExistException, MinPointLimitCannotMattchSeqNumException;
}
