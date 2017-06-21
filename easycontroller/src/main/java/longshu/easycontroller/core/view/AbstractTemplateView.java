package longshu.easycontroller.core.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AbstractTemplateView
 * <p>
 * FreeMarkerView, VelocityView
 *
 * @author LongShu 2017/05/11
 */
public abstract class AbstractTemplateView extends AbstractUrlView {

    public AbstractTemplateView(String view) {
        super(view);
    }

    @Override
    public void render(HttpServletRequest request, HttpServletResponse response) throws RenderException {
        throw new RenderException("暂不支持该视图");
    }

    @Override
    public String getContentType() {
        return contentType = "text/html; charset=" + getEncoding();
    }

}
