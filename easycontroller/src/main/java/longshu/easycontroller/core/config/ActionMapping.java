package longshu.easycontroller.core.config;

import longshu.easycontroller.core.Action;
import longshu.easycontroller.core.ActionKey;
import longshu.easycontroller.core.Controller;
import longshu.easycontroller.core.HttpMethod;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ActionMapping
 *
 * @author LongShu 2017/05/09
 */
public class ActionMapping implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ActionMapping.class);

    public static final String SLASH = "/";

    private String baseViewPath = null;
    private final Map<String, Action> mapping = new HashMap<String, Action>(64);
    private final List<String> staticMapping = new ArrayList<String>(2);

    // private final Set<String> controllerKeySet = new HashSet<>();
    private final Set<String> excludedMethodName = new HashSet<String>();

    public ActionMapping() {
        buildExcludedMethodName();
    }

    /**
     * 添加映射
     *
     * @param controllerKey   控制器的key
     * @param controllerClass 控制器
     * @param viewPath        视图路径
     */
    public ActionMapping addMapping(String controllerKey, Class<? extends Controller> controllerClass, String viewPath) {
        controllerKey = processControllerKey(controllerKey);
        viewPath = processViewPath(viewPath);

        // 是否为ControllerSupport的子类
        boolean sonOfController = (Controller.class.isAssignableFrom(controllerClass));
        Method[] methods = (sonOfController ? controllerClass.getDeclaredMethods() : controllerClass.getMethods());

        for (Method method : methods) {
            String methodName = method.getName();
            if (sonOfController && !Modifier.isPublic(method.getModifiers())) {
                continue;
            }
            if (excludedMethodName.contains(methodName) || method.getParameterTypes().length != 0) {
                continue;
            }

            String actionKey = "";
            HttpMethod[] httpMethods = {};
            // 注解自定义
            ActionKey ak = method.getAnnotation(ActionKey.class);
            if (ak != null) {
                actionKey = ak.value().trim();
                httpMethods = ak.methods();
                if (!"".equals(actionKey)) {
                    if (!actionKey.startsWith(SLASH)) {
                        actionKey = controllerKey.equals(SLASH) ? SLASH + actionKey : controllerKey + SLASH + actionKey;
                    }
                } else if ("index".equals(methodName)) {// index页
                    actionKey = controllerKey.equals(SLASH) ? SLASH : controllerKey + SLASH;
                }
            } else if ("index".equals(methodName)) {// index页
                actionKey = controllerKey.equals(SLASH) ? SLASH : controllerKey + SLASH;
            }

            if ("".equals(actionKey)) {
                actionKey = controllerKey.equals(SLASH) ? SLASH + methodName : controllerKey + SLASH + methodName;
            }

            if (mapping.containsKey(actionKey)) {
                throw new IllegalArgumentException("actionKey already exists: " + actionKey);
            }

            Action action = new Action(controllerClass, httpMethods, controllerKey, actionKey, method, methodName, viewPath);
            // 添加拦截器
            action.addInterceptors(Interceptors.me().createActionInterceptor(controllerClass));
            mapping.put(actionKey, action);
        }
        Action action = mapping.get("/");
        if (action != null) {
            mapping.put("", action);
        }
        return this;
    }

    /**
     * 添加映射
     * {@link #addMapping(String, Class, String)}
     */
    public ActionMapping addMapping(String controllerKey, Class<? extends Controller> controllerClass) {
        return addMapping(controllerKey, controllerClass, controllerKey);
    }

    /**
     * 添加静态资源映射
     */
    public void addStaticMapping(String path) {
        if (StringUtils.isBlank(path)) {
            throw new IllegalArgumentException("path is blank.");
        }
        staticMapping.add(path);
    }

    public boolean isStaticMapping(String path) {
        if (StringUtils.isBlank(path)) {
            return false;
        }
        for (String str : staticMapping) {
            if (path.startsWith(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param url 请求路径
     * @return 对应的Action
     */
    public Action getAction(String url) {
        Action action = mapping.get(url);
        if (null == action) {
            if (url.endsWith(SLASH)) {
                action = mapping.get(url.substring(0, url.length() - 1));
            }
        }
        return action;
    }

    /**
     * 所有ActionKey的映射
     */
    public List<String> getActionKeys() {
        List<String> allActionKeys = new ArrayList<String>(mapping.keySet());
        Collections.sort(allActionKeys);
        return allActionKeys;
    }

    /**
     * 排除的方法
     */
    private Set<String> buildExcludedMethodName() {
        Method[] methods = Controller.class.getMethods();
        for (Method m : methods) {
            if (Modifier.isPublic(m.getModifiers()) && m.getParameterTypes().length == 0) {
                excludedMethodName.add(m.getName());
            }
        }
        return excludedMethodName;
    }

    public ActionMapping setBaseViewPath(String baseViewPath) {
        logger.info("baseViewPath:{}", baseViewPath);
        if (StringUtils.isBlank(baseViewPath)) {
            throw new IllegalArgumentException("baseViewPath can not be blank.");
        }
        baseViewPath = baseViewPath.trim();
        // add prefix "/"
        if (!baseViewPath.startsWith(SLASH)) {
            baseViewPath = SLASH + baseViewPath;
        }
        // remove "/" in the end of baseViewPath
        if (baseViewPath.endsWith(SLASH)) {
            baseViewPath = baseViewPath.substring(0, baseViewPath.length() - 1);
        }

        this.baseViewPath = baseViewPath;
        return this;
    }

    public String getBaseViewPath() {
        return baseViewPath;
    }

    public String getFinalViewPath(String viewPath) {
        return baseViewPath != null ? baseViewPath + viewPath : viewPath;
    }

    private String processControllerKey(String controllerKey) {
        controllerKey = controllerKey.trim();
        if (!controllerKey.startsWith("/")) {
            controllerKey = "/" + controllerKey;
        }
//        if (controllerKeySet.contains(controllerKey)) {
//            throw new IllegalArgumentException("controllerKey already exists: " + controllerKey);
//        }
//        controllerKeySet.add(controllerKey);
        return controllerKey;
    }

    private String processViewPath(String viewPath) {
        viewPath = viewPath.trim();
        // add prefix "/"
        if (!viewPath.startsWith("/")) {
            viewPath = "/" + viewPath;
        }
        // add postfix "/"
        if (!viewPath.endsWith("/")) {
            viewPath = viewPath + "/";
        }
        return getFinalViewPath(viewPath);
    }

}