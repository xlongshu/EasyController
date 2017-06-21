package longshu.easycontroller.core.view;

import longshu.easycontroller.core.BeanFactory;
import longshu.easycontroller.core.config.Constants;

import java.io.File;

/**
 * DefaultViewFactory
 *
 * @author LongShu 2017/05/14
 */
public class DefaultViewFactory implements ViewFactory {

    @Override
    public View getView(String viewName) {
        ViewType viewType = Constants.me().getViewType();
        View view = null;
        switch (viewType) {
            case JSP:
                view = new JspView(viewName);
                break;
            case FREE_MARKER:
                view = new FreeMarkerView(viewName);
                break;
            case VELOCITY:
                view = new VelocityView(viewName);
                break;
        }
        return view;
    }

    @Override
    public JspView getJspView(String viewName) {
        return new JspView(viewName);
    }

    @Override
    public FreeMarkerView getFreeMarkerView(String viewName) {
        return new FreeMarkerView(viewName);
    }

    @Override
    public VelocityView getVelocityView(String viewName) {
        return new VelocityView(viewName);
    }

    @Override
    public ForwardView getForwardView(String url) {
        return new ForwardView(url);
    }

    @Override
    public RedirectView getRedirectView(String url) {
        return new RedirectView(url);
    }

    @Override
    public RedirectView getRedirectView(String url, boolean withQueryString) {
        return new RedirectView(url, withQueryString);
    }

    @Override
    public ErrorView getErrorView(int errorCode, String view) {
        return new ErrorView(errorCode, view);
    }

    @Override
    public ErrorView getErrorView(int errorCode) {
        return new ErrorView(errorCode, Constants.me().getErrorView(errorCode));
    }

    @Override
    public TextView getTextView(String text) {
        return new TextView(text);
    }

    @Override
    public JsonView getJsonView(String key, Object value) {
        return new JsonView(key, value);
    }

    @Override
    public JsonView getJsonView(String jsonText) {
        return new JsonView(jsonText);
    }

    @Override
    public JsonView getJsonView(Object object) {
        return new JsonView(object);
    }

    @Override
    public JavascriptView getJavascriptView(String jsText) {
        return new JavascriptView(jsText);
    }

    @Override
    public CaptchaView getCaptchaView() {
        return BeanFactory.getBean(CaptchaView.class);
    }

    @Override
    public FileView getFileView(String fileName) {
        return new FileView(fileName);
    }

    @Override
    public FileView getFileView(File file) {
        return new FileView(file);
    }

}
