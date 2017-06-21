package longshu.easycontroller.core.view;

import java.io.File;

/**
 * ViewFactory
 *
 * @author LongShu 2017/05/14
 * @see DefaultViewFactory
 */
public interface ViewFactory {

    View getView(String viewName);

    JspView getJspView(String viewName);

    FreeMarkerView getFreeMarkerView(String viewName);

    VelocityView getVelocityView(String viewName);

    ForwardView getForwardView(String url);

    RedirectView getRedirectView(String url);

    RedirectView getRedirectView(String url, boolean withQueryString);

    ErrorView getErrorView(int errorCode, String view);

    ErrorView getErrorView(int errorCode);

    TextView getTextView(String text);

    JsonView getJsonView(String key, Object value);

    JsonView getJsonView(String jsonText);

    JsonView getJsonView(Object object);

    JavascriptView getJavascriptView(String jsText);

    CaptchaView getCaptchaView();

    FileView getFileView(String fileName);

    FileView getFileView(File file);

}
