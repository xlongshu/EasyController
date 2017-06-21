package longshu.easycontroller.core;

import longshu.easycontroller.core.aop.ActionInvocation;
import longshu.easycontroller.core.config.ActionMapping;
import longshu.easycontroller.core.config.Constants;
import longshu.easycontroller.core.view.AbstractUrlView;
import longshu.easycontroller.core.view.RenderException;
import longshu.easycontroller.core.view.View;
import longshu.easycontroller.core.view.ViewManager;
import longshu.easycontroller.core.view.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * ActionHandler
 *
 * @author LongShu 2017/05/09
 */
@lombok.Getter
@lombok.Setter
public class ActionHandler {
    private Logger logger = LoggerFactory.getLogger(ActionHandler.class);

    @Inject
    private ActionMapping actionMapping;
    @Inject
    private ViewResolver viewResolver;
    private String contextPath;

    public void init(String contextPath) {
        setContextPath(contextPath);

        if (logger.isDebugEnabled()) {
            List<String> actionKeys = actionMapping.getActionKeys();
            StringBuilder builder = new StringBuilder("ActionKeys:\n");
            for (String actionKey : actionKeys) {
                builder.append('[').append(actionKey).append("]\n");
            }
            logger.debug(builder.toString());
        }
    }

    /**
     * 处理请求
     *
     * @param target 请求目标
     * @return 是否已经处理
     */
    public boolean handle(String target, HttpServletRequest request, HttpServletResponse response) {
        long start = System.currentTimeMillis();
        // 不处理静态资源
        if (actionMapping.isStaticMapping(target)) {
            return false;
        }

        // ContextPath
//        request.setAttribute(Constants.me().getContextPathName(), contextPath);

        // 不处理带后缀的请求
        if (target.indexOf('.') != -1) {
            return false;
        }

        if (Constants.me().isDevMode()) {
            String qs = request.getQueryString();
            logger.debug("target:{}?{}", target, qs);
        }

        Action action = actionMapping.getAction(target);
        String method = request.getMethod();
        // 判断方法是否支持
        if (action == null || !action.isSupportedMethod(method)) {
            logger.info("404 Action Not Found: {} -> {}", method, target);
            ViewManager.me().getViewFactory().getErrorView(404).render(request, response);
            return true;
        }

        Controller controller = BeanFactory.getBean(action.getControllerClass());
        controller.init(request, response);
        ActionInvocation invocation = new ActionInvocation(action, controller);

        Object returnValue;
        try {
            invocation.invoke();
            returnValue = invocation.getReturnValue();
            logger.debug("returnValue:[{}]", returnValue);
        } catch (ActionException e) {
            logger.warn(e.getMessage(), e);
            ViewManager.me().getViewFactory().getErrorView(500).render(request, response);
            return true;
        }

        // resolveView
        View view = viewResolver.resolveView(action, returnValue);
        logger.debug("view:{}", view);

        // ContextPath
        if (view instanceof AbstractUrlView) {
            request.setAttribute(Constants.me().getContextPathName(), contextPath);
        }

        try {
            // render
            view.render(request, response);
        } catch (RenderException e) {
            logger.error(e.getMessage(), e);
            ViewManager.me().getViewFactory().getErrorView(500).render(request, response);
        }

        if (Constants.me().isDevMode()) {
            long end = System.currentTimeMillis();
            logger.info("handle {} ms", (end - start));
        }

        return true;
    }

}
