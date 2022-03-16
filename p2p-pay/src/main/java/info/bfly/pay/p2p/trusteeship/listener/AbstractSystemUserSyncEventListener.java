package info.bfly.pay.p2p.trusteeship.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by XXSun on 6/8/2017.
 */
public abstract class AbstractSystemUserSyncEventListener<E extends ApplicationEvent> implements ApplicationListener<E> {
    @Override
    public void onApplicationEvent(E event) {
        Authentication authentication = new UsernamePasswordAuthenticationToken("system", "", null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        handleEvent(event);
        SecurityContextHolder.clearContext();
    }

    /**
     * 处理Event
     *
     * @param event
     */
    abstract void handleEvent(E event);
}
