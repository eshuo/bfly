package info.bfly.archer.oauth2.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "oauth_authentication_access_token")
@NamedQueries({@NamedQuery(name = "OAuth2AuthenticationAccessToken.findByTokenId", query = "from  OAuth2AuthenticationAccessToken v where v.tokenId = ?"),
@NamedQuery(name="OAuth2AuthenticationAccessToken.findByRefreshToken",query = "from OAuth2AuthenticationAccessToken v where v.refreshToken = ?"),
@NamedQuery(name="OAuth2AuthenticationAccessToken.findByAuthenticationId",query ="from OAuth2AuthenticationAccessToken v where v.authenticationId = ?"),
@NamedQuery(name="OAuth2AuthenticationAccessToken.findByClientIdAndUserName",query = "from OAuth2AuthenticationAccessToken v where v.clientId=? and v.userName=?"),
@NamedQuery(name="OAuth2AuthenticationAccessToken.findByClientId",query = "from OAuth2AuthenticationAccessToken v where v.clientId = ?")})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "entityCache")
public class OAuth2AuthenticationAccessToken implements Serializable {


    private static final long serialVersionUID = 4992017361741148156L;

    private String               tokenId;
    private OAuth2AccessToken    oAuth2AccessToken;
    private String               authenticationId;
    private String               userName;
    private String               clientId;
    private OAuth2Authentication authentication;
    private String               refreshToken;

    public OAuth2AuthenticationAccessToken() {
    }

    public OAuth2AuthenticationAccessToken(final OAuth2AccessToken oAuth2AccessToken, final OAuth2Authentication authentication, final String authenticationId) {
        this.setTokenId(oAuth2AccessToken.getValue());
        this.setoAuth2AccessToken(oAuth2AccessToken);
        this.setAuthenticationId(authenticationId);
        this.setUserName(authentication.getName());
        this.setClientId(authentication.getOAuth2Request().getClientId());
        this.setAuthentication(authentication);
        this.setRefreshToken(oAuth2AccessToken.getRefreshToken().getValue());
    }

    public void megara(final OAuth2AccessToken oAuth2AccessToken, final OAuth2Authentication authentication, final String authenticationId){
        this.setoAuth2AccessToken(oAuth2AccessToken);
        this.setAuthenticationId(authenticationId);
        this.setUserName(authentication.getName());
        this.setClientId(authentication.getOAuth2Request().getClientId());
        this.setAuthentication(authentication);
        this.setRefreshToken(oAuth2AccessToken.getRefreshToken().getValue());
    }

    @Id
    @Column
    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    @Type(type = "org.springframework.security.oauth2.common.DefaultOAuth2AccessToken")
    @Column(columnDefinition = "blob")
    public OAuth2AccessToken getoAuth2AccessToken() {
        return oAuth2AccessToken;
    }

    public void setoAuth2AccessToken(OAuth2AccessToken oAuth2AccessToken) {
        this.oAuth2AccessToken = oAuth2AccessToken;
    }


    @Column
    public String getAuthenticationId() {
        return authenticationId;
    }

    public void setAuthenticationId(String authenticationId) {
        this.authenticationId = authenticationId;
    }


    @Column
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    @Column
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Type(type = "org.springframework.security.oauth2.provider.OAuth2Authentication")
    @Column(columnDefinition = "blob")
    public OAuth2Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(OAuth2Authentication authentication) {
        this.authentication = authentication;
    }

    @Column
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
