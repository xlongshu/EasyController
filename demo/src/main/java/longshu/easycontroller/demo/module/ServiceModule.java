package longshu.easycontroller.demo.module;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;

import longshu.easycontroller.demo.service.UserService;
import longshu.easycontroller.demo.service.impl.UserServiceImpl;

/**
 * ServiceModule
 * @author LongShu 2017年6月20日
 */
public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        binder().bind(UserService.class).to(UserServiceImpl.class).in(Singleton.class);
    }

}
