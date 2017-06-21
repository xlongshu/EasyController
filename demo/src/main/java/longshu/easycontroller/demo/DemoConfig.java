package longshu.easycontroller.demo;

import longshu.easycontroller.core.BeanFactory;
import longshu.easycontroller.core.config.ActionConfig;
import longshu.easycontroller.core.config.ActionMapping;
import longshu.easycontroller.core.config.Constants;
import longshu.easycontroller.core.config.Interceptors;
import longshu.easycontroller.core.view.ViewType;
import longshu.easycontroller.demo.controller.IndexController;
import longshu.easycontroller.demo.controller.TestController;
import longshu.easycontroller.demo.interceptor.GlobalInterceptor;
import longshu.easycontroller.demo.interceptor.TestInterceptor;
import longshu.easycontroller.demo.module.MyBatisMapperModule;
import longshu.easycontroller.demo.module.ServiceModule;
import longshu.easycontroller.json.FastJson;

import java.io.Serializable;

/**
 * DemoConfig
 *
 * @author LongShu 2017/06/20
 */
public class DemoConfig extends ActionConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public void configConstant(Constants me) {
        me.setDevMode(true);
        me.setViewType(ViewType.JSP);
        me.setViewExtension(".jsp");

        me.setDefaultJson(new FastJson());

        me.setErrorView(404, "/WEB-INF/jsps/error/404.jsp");
        me.setErrorView(500, "/WEB-INF/jsps/error/500.jsp");

    }

    @Override
    public void configModule(BeanFactory me) {
        me.addModule(new MyBatisMapperModule(), new ServiceModule());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void configInterceptor(Interceptors me) {
        // 全局拦截器
        me.addGlobalInterceptor(GlobalInterceptor.class);

        me.addActionInterceptor(TestController.class, TestInterceptor.class);
    }

    @Override
    public void configMapping(ActionMapping me) {
        me.setBaseViewPath("/WEB-INF/jsps/");// 视图路径

        me.addStaticMapping("/assets/");

        me.addMapping("/", IndexController.class);
        me.addMapping("/test", TestController.class);
    }

}
