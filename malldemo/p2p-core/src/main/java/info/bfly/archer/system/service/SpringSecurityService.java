package info.bfly.archer.system.service;

import java.util.HashSet;

public interface SpringSecurityService {

    /**
     * 获取需要刷新权限的用户名HashSet
     *
     * @return
     */
    HashSet<String> getUsersNeedRefreshGrantedAuthorities();

    /**
     * 通过用户名，为已登录的用户重新赋予权限，并在通过RefreshGrantedAuthoritiesFilterImpl来刷新已登录用户的权限。
     *
     * @param userId
     */
    void refreshLoginUserAuthorities(String userId);

    /**
     * 清空当前用户的session
     * 
     * @Title: cleanSpringSecurityContext 设定文件
     * @return void 返回类型
     */
    void cleanSpringSecurityContext();
}
