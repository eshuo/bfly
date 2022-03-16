package info.bfly.archer.oauth2.service.impl;

import info.bfly.archer.common.service.impl.BaseServiceImpl;
import info.bfly.archer.oauth2.model.OAuth2AuthenticationRefreshToken;
import info.bfly.archer.oauth2.service.OAuth2RefreshTokenService;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service("oAuth2RefreshTokenService")
public class OAuth2RefreshTokenServiceImpl extends BaseServiceImpl<OAuth2AuthenticationRefreshToken> implements OAuth2RefreshTokenService {
    @Resource
    private HibernateTemplate ht;

    @Override
    public OAuth2AuthenticationRefreshToken findByTokenId(String tokenId) {
        return ht.get(OAuth2AuthenticationRefreshToken.class, tokenId);
    }
}
