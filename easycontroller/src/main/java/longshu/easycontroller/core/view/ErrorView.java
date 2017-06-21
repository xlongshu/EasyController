package longshu.easycontroller.core.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * ErrorView
 *
 * @author LongShu 2017/05/11
 */
@lombok.Getter
@lombok.Setter
public class ErrorView extends AbstractUrlView {

    /**
     * @see HttpServletResponse SC_XXX
     */
    protected int errorCode;

    public ErrorView(int errorCode, String view) {
        super(view);
        this.errorCode = errorCode;
    }

    @Override
    public void render(HttpServletRequest request, HttpServletResponse response) throws RenderException {
        response.setStatus(getErrorCode());

        if (null != view) {
            ViewManager.me().getViewFactory().getView(getUrl()).render(request, response);
            return;
        }

        response.setContentType(getContentType());
        try {
            PrintWriter writer = response.getWriter();
            writer.write("出错了...");
            writer.flush();
        } catch (IOException e) {
            throw new RenderException(e.getMessage(), e);
        }
    }

}
