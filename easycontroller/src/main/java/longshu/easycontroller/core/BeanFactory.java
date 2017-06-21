package longshu.easycontroller.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import longshu.easycontroller.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * BeanFactory
 *
 * @author LongShu 2017/05/20
 */
@Slf4j
public class BeanFactory {
    private static final List<Module> modules = new ArrayList<Module>();
    private static Injector injector;

    private static final BeanFactory me = new BeanFactory();

    private BeanFactory() {
    }

    public static BeanFactory me() {
        return me;
    }

    /**
     * 添加模块
     */
    public synchronized void addModule(Module... modules) {
        if (null == modules || modules.length == 0) {
            return;
        }
        for (Module module : modules) {
            if (!BeanFactory.modules.contains(module)) {
                BeanFactory.modules.add(module);
            }
        }
    }

    /**
     * 初始化
     */
    public static synchronized void init() {
        if (null == injector) {
            injector = Guice.createInjector(modules);
        }
    }

    public static Injector getInject() {
        return injector;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String className) {
        Class<?> clazz = BeanUtil.forName(className);
        return (T) getBean(clazz);
    }

    public static <T> T getBean(Class<T> type) {
        log.debug("type:{}", type);
        if (null == type) {
            throw new IllegalArgumentException("type is null.");
        }
        return injector.getInstance(type);
    }

}
