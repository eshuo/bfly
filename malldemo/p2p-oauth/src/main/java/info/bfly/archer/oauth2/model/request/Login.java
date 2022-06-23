package info.bfly.archer.oauth2.model.request;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Created by XXSun on 2016/12/21.
 */
public class Login {
    private String username =StringUtils.EMPTY;
    private String password = StringUtils.EMPTY;
    private String refresh_token = StringUtils.EMPTY;
    private String grant_type = "password";

    public Login() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    @Override
    public String toString() {
        try {
            return "username=" + URLEncoder.encode(username,StandardCharsets.UTF_8.name()) + "&password=" + URLEncoder.encode(password,StandardCharsets.UTF_8.name()) +  "&refresh_token=" + URLEncoder.encode(refresh_token,StandardCharsets.UTF_8.name()) + "&grant_type=" + URLEncoder.encode(grant_type,StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return  "username=" + username + "&password=" + password +  "&refresh_token=" + refresh_token + "&grant_type=" + grant_type;
        }
    }
}
