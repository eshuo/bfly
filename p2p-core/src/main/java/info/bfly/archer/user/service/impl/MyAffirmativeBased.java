package info.bfly.archer.user.service.impl;

import info.bfly.archer.user.exception.MyAccessDeniedException;

import java.util.Collection;
import java.util.List;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.core.Authentication;

public class MyAffirmativeBased extends AffirmativeBased {
    /**
     * @param decisionVoters
     */
    public MyAffirmativeBased(List<AccessDecisionVoter<? extends Object>> decisionVoters) {
        super(decisionVoters);
    }

    @Override
    public void decide(Authentication authentication, Object object, Collection configAttributes) throws AccessDeniedException {
        try {
            super.decide(authentication, object, configAttributes);
        } catch (AccessDeniedException ade) {
            throw new MyAccessDeniedException(ade.getMessage(), authentication, object, configAttributes);
        }
    }
}
