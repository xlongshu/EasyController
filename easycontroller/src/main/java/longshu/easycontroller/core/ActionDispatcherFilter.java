package longshu.easycontroller.core;

import longshu.easycontroller.core.config.ActionConfig;
import longshu.easycontroller.core.config.ActionMapping;
import longshu.easycontroller.core.config.Constants;
import longshu.easycontroller.core.config.Interceptors;
import longshu.easycontroller.core.multipart.MultipartRequest;
import longshu.easycontroller.core.view.ViewManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * ActionDispatcherFilter 核心控制器
 *
 * @author LongShu 2017/05/09
 */
public class ActionDispatcherFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(ActionDispatcherFilter.class);

    private ActionHandler actionHandler;
    private ActionConfig actionConfig;
    private String encoding;
    private int contextPathLength;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.debug("init...");
        long start = System.currentTimeMillis();

        // 获取配置类
        String configClass = filterConfig.getInitParameter("configClass");
        createDispatcherConfig(configClass);

        ServletContext servletContext = filterConfig.getServletContext();
        String realPath = servletContext.getRealPath("/");
        logger.info("webRootPath:{}", realPath);
        Constants.me().setWebRootPath(new File(realPath));

        config();

        BeanFactory.getInject().injectMembers(ViewManager.me());
        ViewManager.me().init(servletContext);

        String contextPath = servletContext.getContextPath();
        logger.info("contextPath:{}", contextPath);
        contextPathLength = (contextPath == null || "/".equals(contextPath) ? 0 : contextPath.length());

        actionHandler = BeanFactory.getBean(ActionHandler.class);
        actionHandler.init(contextPath);
        actionConfig.afterStart();

        long end = System.currentTimeMillis();
        logger.info("init {} ms", (end - start));
    }

    private void config() {
        // configConstant
        actionConfig.configConstant(Constants.me());
        // configModule
        BeanFactory.me().addModule(new ActionModule());
        actionConfig.configModule(BeanFactory.me());
        BeanFactory.init();// 初始化后才可用
        // configInterceptor
        actionConfig.configInterceptor(Interceptors.me());
        // configMapping
        actionConfig.configMapping(BeanFactory.getBean(ActionMapping.class));

        encoding = Constants.me().getEncoding();

        // multipart config (after setWebRootPath!)
        MultipartRequest.init(Constants.me().getFormMaxSize(), encoding);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        request.setCharacterEncoding(encoding);
        response.setCharacterEncoding(encoding);

        String target = request.getRequestURI();
        if (contextPathLength != 0) {
            target = target.substring(contextPathLength);
        }

        boolean handle = actionHandler.handle(target, request, response);

        if (!handle) {// 没有处理则放行
            chain.doFilter(request, response);
        }
    }

    private void createDispatcherConfig(String configClass) {
        logger.debug("configClass:{}", configClass);
        if (null == configClass) {
            throw new RuntimeException("Please add configClass in web.xml.");
        }

        Object config;
        try {
            config = Class.forName(configClass).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Can not create instance of class: " + configClass, e);
        }

        if (config instanceof ActionConfig) {
            actionConfig = (ActionConfig) config;
        } else {
            throw new RuntimeException("Can not create instance of class: " + configClass);
        }
    }

    @Override
    public void destroy() {
        logger.debug("destroy...");
        actionConfig.beforeStop();
    }

}