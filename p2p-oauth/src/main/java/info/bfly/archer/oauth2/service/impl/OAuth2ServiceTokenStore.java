package info.bfly.archer.oauth2.service.impl;

import info.bfly.archer.oauth2.model.OAuth2AuthenticationAccessToken;
import info.bfly.archer.oauth2.model.OAuth2AuthenticationRefreshToken;
import info.bfly.archer.oauth2.service.OAuth2AccessTokenService;
import info.bfly.archer.oauth2.service.OAuth2RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class OAuth2ServiceTokenStore implements TokenStore {

    private final OAuth2AccessTokenService oAuth2AccessTokenService;

    private final OAuth2RefreshTokenService oAuth2RefreshTokenService;

    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

    @Autowired
    public OAuth2ServiceTokenStore(final OAuth2AccessTokenServiceImpl oAuth2AccessTokenService,
                                   final OAuth2RefreshTokenServiceImpl oAuth2RefreshTokenService) {
        this.oAuth2AccessTokenService = oAuth2AccessTokenService;
        this.oAuth2RefreshTokenService = oAuth2RefreshTokenService;
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return readAuthentication(token.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String tokenId) {
        return oAuth2AccessTokenService.findByTokenId(tokenId).getAuthentication();
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        OAuth2AuthenticationAccessToken oAuth2AuthenticationAccessToken;
        oAuth2AuthenticationAccessToken = oAuth2AccessTokenService.findByTokenId(token.getValue());
        if (oAuth2AuthenticationAccessToken == null)
            oAuth2AuthenticationAccessToken = new OAuth2AuthenticationAccessToken(token,
                    authentication, authenticationKeyGenerator.extractKey(authentication));
        else
            oAuth2AuthenticationAccessToken.megara(token, authentication, authenticationKeyGenerator.extractKey(authentication));
        oAuth2AccessTokenService.saveOrUpdate(oAuth2AuthenticationAccessToken);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        OAuth2AuthenticationAccessToken token = oAuth2AccessTokenService.findByTokenId(tokenValue);
        if (token == null) {
            return null;
        }
        OAuth2AccessToken accessToken = token.getoAuth2AccessToken();
        return accessToken;
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        OAuth2AuthenticationAccessToken accessToken = oAuth2AccessTokenService.findByTokenId(token.getValue());
        if (accessToken != null) {
            oAuth2AccessTokenService.delete(accessToken);
        }
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        OAuth2AuthenticationRefreshToken  oAuth2AuthenticationRefreshToken;
        oAuth2AuthenticationRefreshToken = oAuth2RefreshTokenService.findByTokenId(refreshToken.getValue());
        if(oAuth2AuthenticationRefreshToken == null)
            oAuth2AuthenticationRefreshToken = new OAuth2AuthenticationRefreshToken(refreshToken,authentication);
        else
            oAuth2AuthenticationRefreshToken.megara(refreshToken,authentication);
        oAuth2RefreshTokenService.saveOrUpdate(oAuth2AuthenticationRefreshToken);
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        return oAuth2RefreshTokenService.findByTokenId(tokenValue).getoAuth2RefreshToken();
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        return oAuth2RefreshTokenService.findByTokenId(token.getValue()).getAuthentication();
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
        oAuth2RefreshTokenService.delete(oAuth2RefreshTokenService.findByTokenId(token.getValue()));
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        oAuth2AccessTokenService.delete(oAuth2AccessTokenService.findByRefreshToken(refreshToken.getValue()));
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        OAuth2AuthenticationAccessToken token = oAuth2AccessTokenService.findByAuthenticationId(authenticationKeyGenerator.extractKey(authentication));
        return token == null ? null : token.getoAuth2AccessToken();
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        List<OAuth2AuthenticationAccessToken> tokens = oAuth2AccessTokenService.findByClientId(clientId);
        return extractAccessTokens(tokens);
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        List<OAuth2AuthenticationAccessToken> tokens = oAuth2AccessTokenService.findByClientIdAndUserName(clientId, userName);
        return extractAccessTokens(tokens);
    }

    private Collection<OAuth2AccessToken> extractAccessTokens(List<OAuth2AuthenticationAccessToken> tokens) {
        List<OAuth2AccessToken> accessTokens = new ArrayList<OAuth2AccessToken>();
        for (OAuth2AuthenticationAccessToken token : tokens) {
            accessTokens.add(token.getoAuth2AccessToken());
        }
        return accessTokens;
    }

}
