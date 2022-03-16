package info.bfly.archer.user.aop;

import info.bfly.archer.user.model.Role;
import info.bfly.archer.user.model.User;
import info.bfly.p2p.message.service.impl.MessageBO;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 积分监听器，给用户加积分
 */
//@Component
//@Aspect
public class UserPointMonitor {
    @Resource
    private MessageBO messageBO;

    /**
     * 用户角色添加
     *
     * @param user
     * @param role
     */
    //@AfterReturning(argNames = "user, role", value = "execution(public void info.bfly.archer.user.service.impl.addRole(..)) && args(info.bfly.archer.user.model.User, info.bfly.archer.user.model.Role)")
    public void addRole(User user, Role role) {
        if (role.getId().equals("INVESTOR")) {
            // TODO:实名认证
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", user.getUsername());
            // messageBO.sendMsg(user, userMessageNodeId, params);
        }
    }

    /**
     * 用户角色移除
     *
     * @param user
     * @param role
     */
   // @AfterReturning(argNames = "user, role", value = "execution(public void info.bfly.archer.user.service.impl.removeRole(..)) && args(info.bfly.archer.user.model.User, info.bfly.archer.user.model.Role)")
    public void removeRole(User user, Role role) {
        if (role.getId().equals("INACTIVE")) {
            // TODO:用户激活
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", user.getUsername());
            // messageBO.sendMsg(user, userMessageNodeId, params);
        }
    }
}
