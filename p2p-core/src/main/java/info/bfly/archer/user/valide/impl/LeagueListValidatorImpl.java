/**
 *
 */
package info.bfly.archer.user.valide.impl;

import info.bfly.archer.user.valide.LeagueListValidator;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author yzc8866
 *
 */
@Service(value = "leagueListValidator")
public class LeagueListValidatorImpl implements LeagueListValidator {
    private static Boolean check_mid(Map<String, String> map, Boolean isMust) {
        Boolean result = false;
        if (null == map) {
            return false;
        }
        String mid = map.get("mid");
        if (StringUtils.isBlank(mid)) {
            if (isMust) {
                result = false;
                throw new RuntimeException("mid不能为空");
            } else {
                result = true;
            }
        } else {
            result = true;
            // 如果不为空可以做一些非法值的验证比如必须为数字或者字母，或者值只能在什么范围
        }
        return result;
    }

    private static Boolean check_uid(Map<String, String> map, Boolean isMust) {
        Boolean result = false;
        if (null == map) {
            return false;
        }
        String uid = map.get("uid");
        if (StringUtils.isBlank(uid)) {
            if (isMust) {
                result = false;
                throw new RuntimeException("uid不能为空");
            } else {
                result = true;
            }
        } else {
            result = true;
            // 如果不为空可以做一些非法值的验证比如必须为数字或者字母，或者值只能在什么范围
        }
        return result;
    }

    @Override
    public void getleagueByid(Map<String, String> map) throws RuntimeException {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see info.bfly.archer.user.valide.LeagueListValidator#leagueList(java.util
     * .Map)
     */
    @Override
    public void getleagueList(Map<String, String> map) throws RuntimeException {
        LeagueListValidatorImpl.check_uid(map, true);
        LeagueListValidatorImpl.check_mid(map, true);
    }

    @Override
    public void save(Map<String, String> map) throws RuntimeException {
        // TODO Auto-generated method stub
    }
}
