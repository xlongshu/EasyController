package longshu.easycontroller.core.view;

import longshu.easycontroller.core.Action;
import longshu.easycontroller.core.WebData;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.io.File;
import java.util.Collection;
import java.util.Map;

/**
 * DefaultViewResolver
 *
 * @author LongShu 2017/05/14
 */
@Slf4j
public class DefaultViewResolver implements ViewResolver {

    @Inject
    private ViewFactory viewFactory;

    @Override
    public View resolveView(Action action, Object returnValue) {
        log.debug("resolveView:{}", action.getActionKey());
        if (null == returnValue) { // 没有返回值,根据请求路径处理
            String view = action.getViewPath();
            if (action.isIndex()) {
                view += action.getMethodName();
            } else {
                view += action.getActionKey().substring(action.getControllerKey().length() + 1);
            }
            return viewFactory.getView(view);
        } else {
            return resolveReturnValue(returnValue);
        }
    }

    protected View resolveReturnValue(Object returnValue) {
        if (returnValue instanceof View) {
            return (View) returnValue;
        }

        View view = null;
        if (returnValue instanceof String) {
            String str = ((String) returnValue);
            if (str.startsWith(View.FORWARD_URL_PREFIX)) {// forward
                String url = str.substring(View.FORWARD_URL_PREFIX.length());
                view = viewFactory.getForwardView(url);

            } else if (str.startsWith(View.REDIRECT_URL_PREFIX)) {// redirect
                String url = str.substring(View.REDIRECT_URL_PREFIX.length());
                view = viewFactory.getRedirectView(url);

            } else if (str.startsWith(View.JSON_PREFIX)) {// json
                String json = str.substring(View.JSON_PREFIX.length());
                view = viewFactory.getJsonView(json);

            } else if (str.startsWith(View.JAVASCRIPT_PREFIX)) {// javascript
                String js = str.substring(View.JAVASCRIPT_PREFIX.length());
                view = viewFactory.getJavascriptView(js);

            } else {
                view = viewFactory.getTextView(str);
            }// String

        } else if (returnValue instanceof File) {// File
            view = viewFactory.getFileView((File) returnValue);
        } else if (returnValue instanceof Map || returnValue instanceof Collection || returnValue instanceof WebData) {// json
            view = viewFactory.getJsonView(returnValue);
        }
        return view;
    }

}
