package info.bfly.archer.oauth2.service;

import info.bfly.archer.common.service.BaseService;
import info.bfly.archer.oauth2.model.OAuth2AuthenticationAccessToken;

import java.util.List;


public interface OAuth2AccessTokenService extends BaseService<OAuth2AuthenticationAccessToken> {
    OAuth2AuthenticationAccessToken findByTokenId(String tokenId);

    OAuth2AuthenticationAccessToken findByRefreshToken(String refreshToken);

    OAuth2AuthenticationAccessToken findByAuthenticationId(String authenticationId);

    List<OAuth2AuthenticationAccessToken> findByClientIdAndUserName(String clientId, String userName);

    List<OAuth2AuthenticationAccessToken> findByClientId(String clientId);
}
