package info.bfly.pay.util;

import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.inject.Singleton;
import java.util.concurrent.Executor;

/**
 * Created by XXSun on 2017/1/10.
 */
@Service
@Singleton
public class SinaSecurityUtils {
    private Executor executor;

    public SinaSecurityUtils() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken("sinaPay", "doesnotmatter", AuthorityUtils.createAuthorityList("ROLE_SINA"));
        context.setAuthentication(authentication);
        SimpleAsyncTaskExecutor delegateExecutor = new SimpleAsyncTaskExecutor();
        executor = new DelegatingSecurityContextExecutor(delegateExecutor, context);
    }

    void doInSinaContext(Runnable command) {
        executor.execute(command);
    }
}
