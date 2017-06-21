package longshu.easycontroller.demo.interceptor;

import lombok.extern.slf4j.Slf4j;
import longshu.easycontroller.core.aop.ActionInvocation;
import longshu.easycontroller.core.aop.Interceptor;

/**
 * GlobalInterceptor
 *
 * @author LongShu 2017/06/20
 */
@Slf4j
public class GlobalInterceptor implements Interceptor {
    @Override
    public void intercept(ActionInvocation invocation) {
        log.info("GlobalInterceptor: {}", invocation.getActionKey());
    }
}
