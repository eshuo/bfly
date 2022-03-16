package info.bfly.archer.oauth2.service.impl;

import info.bfly.archer.common.service.impl.BaseServiceImpl;
import info.bfly.archer.oauth2.model.OAuth2AuthenticationAccessToken;
import info.bfly.archer.oauth2.service.OAuth2AccessTokenService;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("oAuth2AccessTokenService")
public class OAuth2AccessTokenServiceImpl extends BaseServiceImpl<OAuth2AuthenticationAccessToken> implements OAuth2AccessTokenService {
    @Resource
    private HibernateTemplate ht;

    @Override
    public OAuth2AuthenticationAccessToken findByTokenId(String tokenId){
        OAuth2AuthenticationAccessToken oAuth2AuthenticationAccessToken = ht.get(OAuth2AuthenticationAccessToken.class, tokenId);
        return oAuth2AuthenticationAccessToken;
    };

    @Override
    public OAuth2AuthenticationAccessToken findByRefreshToken(String refreshToken){
        List<OAuth2AuthenticationAccessToken> result= (List<OAuth2AuthenticationAccessToken>) ht.findByNamedQuery("OAuth2AuthenticationAccessToken.findByRefreshToken",refreshToken);
        if(!result.isEmpty())
            return result.get(0);
        else
            return null;
    };


    @Override
    public OAuth2AuthenticationAccessToken findByAuthenticationId(String authenticationId){
        List<OAuth2AuthenticationAccessToken> result= (List<OAuth2AuthenticationAccessToken>) ht.findByNamedQuery("OAuth2AuthenticationAccessToken.findByAuthenticationId",authenticationId);
        if(!result.isEmpty())
            return result.get(0);
        else
            return null;
        };

    @Override
    public List<OAuth2AuthenticationAccessToken> findByClientIdAndUserName(String clientId, String userName){
        return (List<OAuth2AuthenticationAccessToken>) ht.findByNamedQuery("OAuth2AuthenticationAccessToken.findByClientIdAndUserName",clientId,userName);
    };

    @Override
    public List<OAuth2AuthenticationAccessToken> findByClientId(String clientId){
        return (List<OAuth2AuthenticationAccessToken>) ht.findByNamedQuery("OAuth2AuthenticationAccessToken.findByClientId",clientId);
    };
}
