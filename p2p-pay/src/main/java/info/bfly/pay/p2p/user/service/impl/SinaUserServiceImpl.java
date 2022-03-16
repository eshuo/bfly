package info.bfly.pay.p2p.user.service.impl;

import info.bfly.archer.user.service.UserService;
import info.bfly.core.util.IdGenerator;
import info.bfly.pay.controller.SinaUserController;
import info.bfly.pay.p2p.user.service.SinaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/5/17 0017.
 */
@Service("sinaUserService")
public class SinaUserServiceImpl implements SinaUserService {



    @Autowired
    private UserService userService;

    @Resource
    SinaUserController sinaUserController;


    @Override
    public boolean queryIsSetPayPasswordSinaPay(String userId) {

        if (userService.isSetCashPassword(userId)) {
            return true;
        } else {
            if (sinaUserController.queryIsSetPayPasswordSinaPay(userId)) {
                userService.modifyCashPassword(userId, IdGenerator.randomUUID().substring(10));
                return true;
            } else {
                return false;
            }
        }
    }

}
