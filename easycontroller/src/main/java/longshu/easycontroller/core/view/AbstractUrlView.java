package longshu.easycontroller.core.view;

import longshu.easycontroller.core.config.Constants;

/**
 * AbstractUrlView
 *
 * @author LongShu 2017/05/11
 */
public abstract class AbstractUrlView extends AbstractView {

    protected String url;

    public AbstractUrlView(String view) {
        super(view);
    }

    public String getUrl() {
        if (null == url) {
            // jsp可以和其他模板引擎共存
            if (view.endsWith(".jsp") || view.endsWith(Constants.me().getViewExtension())) {
                url = view;
                return url;
            }
            if (JspView.class.equals(getClass())) {
                url = view + ".jsp";
                return url;
            }

            url = view + Constants.me().getViewExtension();
        }
        return url;
    }

    @Override
    public String toString() {
        return getClass().getName() + " -> " + getUrl();
    }

}
