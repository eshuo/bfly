package info.bfly.app.protocol.model.response;

import info.bfly.archer.user.model.User;

import javax.validation.constraints.NotNull;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * Created by XXSun on 2016/12/13.
 */

public class ApiUser {

    @NotNull
    private String emailAddress;

    private Integer age;
    private String id;

    public ApiUser() {
    }

    public ApiUser(User user) {
        this.emailAddress = user.getEmail();
        this.id = user.getId();
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(final String emailAddress) {
        notNull(emailAddress, "Mandatory argument 'emailAddress missing.'");
        this.emailAddress = emailAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        notNull(id, "Mandatory argument 'id missing.'");
        this.id = id;
    }



    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }


}
