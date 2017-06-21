package longshu.easycontroller.core.config;

import longshu.easycontroller.core.BeanFactory;
import longshu.easycontroller.core.Controller;
import longshu.easycontroller.core.aop.Interceptor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Interceptors
 *
 * @author LongShu 2017/05/26
 */
public class Interceptors {

    /**
     * 全局拦截器
     */
    @Getter
    private Collection<Class<? extends Interceptor>> globalInterceptor;
    /**
     * 控制器的拦截器
     */
    private Map<Class<? extends Controller>, Collection<Class<? extends Interceptor>>> actionInterceptors;

    // 单例拦截器
    private Map<Class<? extends Interceptor>, Interceptor> singletonInterceptor;

    private static Interceptors me = new Interceptors();

    private Interceptors() {
        globalInterceptor = new ArrayList<Class<? extends Interceptor>>(2);
        actionInterceptors = new HashMap<Class<? extends Controller>, Collection<Class<? extends Interceptor>>>();
        singletonInterceptor = new HashMap<Class<? extends Interceptor>, Interceptor>();
    }

    public static Interceptors me() {
        return me;
    }

    @SuppressWarnings("all")
    public void addGlobalInterceptor(Class<? extends Interceptor>... inters) {
        if (inters == null || inters.length == 0) {
            throw new IllegalArgumentException("interceptors can not be null.");
        }
        for (Class<? extends Interceptor> inter : inters) {
            if (!globalInterceptor.contains(inter)) {
                globalInterceptor.add(inter);
            }
        }
    }

    @SuppressWarnings("all")
    public void addActionInterceptor(Class<? extends Controller> controllerClass,
                                     Class<? extends Interceptor>... inters) {
        if (inters == null || inters.length == 0) {
            throw new IllegalArgumentException("interceptors can not be null.");
        }
        actionInterceptors.put(controllerClass, Arrays.asList(inters));
    }

    @SuppressWarnings("unchecked")
    public Collection<Interceptor> createActionInterceptor(Class<? extends Controller> controllerClasses) {
        // 全局
        Collection<Class<? extends Interceptor>> interceptorClasses = new HashSet<Class<? extends Interceptor>>();
        interceptorClasses.addAll(Interceptors.me().getGlobalInterceptor());

        // 控制器的拦截器
        Collection<Class<? extends Interceptor>> actionInterceptorClasses = me.actionInterceptors.get(controllerClasses);
        if (null != actionInterceptorClasses && !actionInterceptorClasses.isEmpty()) {
            interceptorClasses.addAll(actionInterceptorClasses);
        }

        if (interceptorClasses.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        Collection<Interceptor> interceptors = new ArrayList<Interceptor>(interceptorClasses.size());
        for (Class<? extends Interceptor> interceptorClass : interceptorClasses) {
            Interceptor interceptor = createInterceptor(interceptorClass);
            interceptors.add(interceptor);
        }
        return interceptors;
    }

    public Interceptor createInterceptor(Class<? extends Interceptor> interceptorClass) {
        Interceptor interceptor = singletonInterceptor.get(interceptorClass);
        if (null == interceptor) {
            interceptor = BeanFactory.getBean(interceptorClass);
            singletonInterceptor.put(interceptorClass, interceptor);
        }
        return interceptor;
    }

}
