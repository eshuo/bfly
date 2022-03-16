package info.bfly.archer.oauth2.service;

import info.bfly.archer.common.service.BaseService;
import info.bfly.archer.oauth2.model.OAuth2AuthenticationRefreshToken;


public interface OAuth2RefreshTokenService extends BaseService<OAuth2AuthenticationRefreshToken> {
    OAuth2AuthenticationRefreshToken findByTokenId(String tokenId);
}
