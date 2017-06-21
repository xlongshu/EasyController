package longshu.easycontroller.core;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.google.inject.Binder;
import com.google.inject.Module;
import longshu.easycontroller.core.config.ActionConfig;
import longshu.easycontroller.core.config.ActionMapping;
import longshu.easycontroller.core.view.DefaultViewFactory;
import longshu.easycontroller.core.view.DefaultViewResolver;
import longshu.easycontroller.core.view.ViewFactory;
import longshu.easycontroller.core.view.ViewResolver;
import longshu.easycontroller.util.KaptchaExtend;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.Properties;

/**
 * ActionModule
 *
 * @author LongShu 2017/05/20
 */
@Slf4j
public class ActionModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(ActionMapping.class).toInstance(new ActionMapping());
        binder.bind(ActionHandler.class).toInstance(new ActionHandler());

        binder.bind(ViewFactory.class).to(DefaultViewFactory.class).in(Singleton.class);
        binder.bind(ViewResolver.class).to(DefaultViewResolver.class).in(Singleton.class);

        configureCaptcha(binder);
    }

    void configureCaptcha(Binder binder) {
        DefaultKaptcha producer = new DefaultKaptcha();

        Properties properties;
        try {
            properties = ActionConfig.loadPropertyFile("captcha.properties");
            Config config = new Config(properties);
            producer.setConfig(config);
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
        }

        binder.bind(Producer.class).toInstance(producer);
        binder.bind(KaptchaExtend.class).toInstance(new KaptchaExtend());
    }

}
