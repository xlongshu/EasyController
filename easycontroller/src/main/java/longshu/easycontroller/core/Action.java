package longshu.easycontroller.core;

import longshu.easycontroller.core.aop.Interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * Action
 *
 * @author LongShu 2017/05/09
 */
@lombok.Getter
@lombok.Setter
public class Action implements Serializable {
    private static final long serialVersionUID = 1L;

    private Class<? extends Controller> controllerClass;
    private HttpMethod[] methods;// 支持的请求方法
    private String controllerKey;
    private String actionKey;
    private Method method;
    private String methodName;
    private String viewPath;
    private Collection<Interceptor> interceptors = new HashSet<Interceptor>(2);
    private boolean index;// 是否为index页面

    public Action(Class<? extends Controller> controllerClass, HttpMethod[] methods, String controllerKey,
                  String actionKey, Method method, String methodName, String viewPath) {
        this.controllerClass = controllerClass;
        this.methods = methods;
        this.controllerKey = controllerKey;
        this.actionKey = actionKey;
        this.method = method;
        this.methodName = methodName;
        this.viewPath = viewPath;

        // "/" or "/test/"
        index = "/".equals(actionKey) || actionKey.length() - 1 == controllerKey.length();
    }

    public boolean isSupportedMethod(String method) {
        if (null == methods || methods.length == 0) {// 支持所有
            return true;
        }
        method = method.toUpperCase();
        for (HttpMethod m : methods) {
            if (m.toString().equals(method)) {
                return true;
            }
        }
        return false;
    }

    public void addInterceptors(Interceptor... inters) {
        if (inters == null || inters.length == 0) {
            throw new IllegalArgumentException("interceptors can not be null.");
        }

        for (Interceptor interceptor : inters) {
            interceptors.add(interceptor);
        }
    }

    public void addInterceptors(Collection<? extends Interceptor> interList) {
        if (null == interList) {
            throw new IllegalArgumentException("interceptors can be null.");
        }
        if (!interList.isEmpty()) {
            interceptors.addAll(interList);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Action{");
        sb.append("controllerClass=").append(controllerClass);
        sb.append(", methods=").append(Arrays.toString(methods));
        sb.append(", controllerKey='").append(controllerKey).append('\'');
        sb.append(", actionKey='").append(actionKey).append('\'');
        sb.append(", method=").append(method);
        sb.append(", methodName='").append(methodName).append('\'');
        sb.append(", viewPath='").append(viewPath).append('\'');
        sb.append(", index=").append(index);
        sb.append('}');
        return sb.toString();
    }

}