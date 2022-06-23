package info.bfly.app.protocol.service;

import info.bfly.p2p.trusteeship.exception.TrusteeshipReturnException;
import info.bfly.pay.controller.SinaUserController;
import info.bfly.pay.p2p.user.service.SinaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/4/12 0012.
 */
@Service
public class ApiPassWordService {


    @Resource
    SinaUserController sinaUserController;

    @Autowired
    private SinaUserService sinaUserService;

    /**
     * 查询是否设置交易密码
     *
     * @param userId
     * @return
     */
    public boolean queryUserSetPassWord(String userId) {
        return sinaUserService.queryIsSetPayPasswordSinaPay(userId);
    }

    public String payPassword(String userId) throws TrusteeshipReturnException {
        if (queryUserSetPassWord(userId)) {
            return modifyPayPassword(userId);
        } else {
            return setPayPassword(userId);
        }
    }


    public String setPayPassword(String userId) throws TrusteeshipReturnException {
        return sinaUserController.setPayPasswordSinaPay(userId);
    }

    public String modifyPayPassword(String userId) throws TrusteeshipReturnException {
        return sinaUserController.modifyPayPasswordSinaPay(userId);
    }


    public String findPayPassword(String userId) throws TrusteeshipReturnException {

        if (queryUserSetPassWord(userId)) {
            return sinaUserController.findPayPasswordSinaPay(userId);
        } else {
            return setPayPassword(userId);
        }
    }


}
