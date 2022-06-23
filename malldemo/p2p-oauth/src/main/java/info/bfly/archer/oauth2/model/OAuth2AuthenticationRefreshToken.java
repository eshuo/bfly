package info.bfly.archer.oauth2.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "oauth_authentication_refresh_token")
@NamedQueries({@NamedQuery(name = "OAuth2AuthenticationRefreshToken.findByTokenId", query = "from  OAuth2AuthenticationRefreshToken v where v.tokenId = ?")})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "entityCache")
public class OAuth2AuthenticationRefreshToken implements Serializable {

    private static final long serialVersionUID = -8964718788597475493L;

    private String               tokenId;
    private OAuth2RefreshToken   oAuth2RefreshToken;
    private OAuth2Authentication authentication;

    public OAuth2AuthenticationRefreshToken() {

    }

    public OAuth2AuthenticationRefreshToken(final OAuth2RefreshToken oAuth2RefreshToken,final OAuth2Authentication authentication) {
        this.setoAuth2RefreshToken(oAuth2RefreshToken);
        this.setAuthentication(authentication);
        this.setTokenId(oAuth2RefreshToken.getValue());
    }

    public void megara(final OAuth2RefreshToken oAuth2RefreshToken,final OAuth2Authentication authentication){
        this.setoAuth2RefreshToken(oAuth2RefreshToken);
        this.setAuthentication(authentication);
    }

    @Id
    @Column
    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }


    @Type(type = "org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken")
    @Column(columnDefinition = "blob")
    public OAuth2RefreshToken getoAuth2RefreshToken() {
        return oAuth2RefreshToken;
    }

    public void setoAuth2RefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {
        this.oAuth2RefreshToken = oAuth2RefreshToken;
    }

    @Type(type = "org.springframework.security.oauth2.provider.OAuth2Authentication")
    @Column(columnDefinition = "blob")
    public OAuth2Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(OAuth2Authentication authentication) {
        this.authentication = authentication;
    }
}