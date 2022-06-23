/**
 *
 */
package info.bfly.core.util;

import info.bfly.archer.user.model.Permission;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author XXSun
 *
 */
@Component
public class PermissionUtil {
    public static String getPermissionStr(List<Permission> permissions) {
        StringBuffer sbf = new StringBuffer();
        for (Permission permission : permissions) {
            sbf.append(permission.getId()).append(",");
        }
        if (sbf.length() != 0) sbf.deleteCharAt(sbf.length() - 1);
        return sbf.toString();
    }
}
