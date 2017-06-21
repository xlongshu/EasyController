package longshu.easycontroller.core.aop;

import longshu.easycontroller.core.Action;
import longshu.easycontroller.core.ActionException;
import longshu.easycontroller.core.Controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

/**
 * ActionInvocation
 *
 * @author LongShu 2017/05/09
 */
@lombok.Getter
@lombok.Setter
public class ActionInvocation {

    private Action action;
    private Controller controller;

    Iterator<Interceptor> interceptors;

    private Object returnValue = null;
    private boolean invoked;// 是否已经invoke

    public ActionInvocation(Action action, Controller controller) {
        this.action = action;
        this.controller = controller;

        interceptors = action.getInterceptors().iterator();
    }

    public void invoke() {
        if (interceptors.hasNext()) {
            Interceptor interceptor = interceptors.next();
            interceptor.intercept(this);
        }

        try {
            if (!invoked) {
                returnValue = action.getMethod().invoke(controller);
                invoked = true;
            }
        } catch (InvocationTargetException e) {
            Throwable throwable = e.getTargetException();
            throw throwable instanceof RuntimeException ? (RuntimeException) throwable : new ActionException(e);
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable t) {
            throw new ActionException(t);
        }
    }

    /**
     * Return the controller key.
     */
    public String getControllerKey() {
        return action.getControllerKey();
    }

    /**
     * Return the method name of this action's method.
     */
    public String getMethodName() {
        return action.getMethodName();
    }

    /**
     * Return the action key.
     * actionKey = controllerKey + methodName
     */
    public String getActionKey() {
        return action.getActionKey();
    }

}
