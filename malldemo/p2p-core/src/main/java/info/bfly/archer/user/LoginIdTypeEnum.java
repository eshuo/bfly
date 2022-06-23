package info.bfly.archer.user;

/**
 * @author zeminshao
 * @ClassName: LoginIdTypeEnum
 * @Description: TODO(登陆id判断的查询类型)
 * @date 2017年3月16日 下午1:52:49
 */
public enum LoginIdTypeEnum {

    USERNAME_TYPE("input.username", "用户id"), EMAIL_TYPE("input.email", "邮箱"), MOBILE_TYPE("input.mobile", "手机");

    private final String name;

    private final String description;

    LoginIdTypeEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
