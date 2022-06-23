package info.bfly.pay.p2p.user.service;

/**
 * Created by Administrator on 2017/5/17 0017.
 */
public interface SinaUserService {


    /**
     * 查询交易密码是否设置
     * @param userId
     * @return
     */
    boolean queryIsSetPayPasswordSinaPay(String userId);
}
