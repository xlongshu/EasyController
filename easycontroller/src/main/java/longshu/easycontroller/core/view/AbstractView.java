package longshu.easycontroller.core.view;

/**
 * AbstractView
 *
 * @author LongShu 2017/05/11
 */
@lombok.Setter
public abstract class AbstractView implements View {
    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=UTF-8";
    public static final String DEFAULT_ENCODING = "UTF-8";

    protected String contentType = DEFAULT_CONTENT_TYPE;
    protected String encoding = DEFAULT_ENCODING;
    protected String view;

    public AbstractView() {
    }

    public AbstractView(String view) {
        this.view = view;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public String getEncoding() {
        return encoding;
    }

    @Override
    public String getView() {
        return view;
    }

    @Override
    public String toString() {
        return getClass().getName() + " -> " + getView();
    }

}
