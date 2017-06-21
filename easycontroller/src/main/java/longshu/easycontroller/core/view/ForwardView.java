package longshu.easycontroller.core.view;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ForwardView
 *
 * @author LongShu 2017/05/11
 */
public class ForwardView extends AbstractView {

    public ForwardView(String view) {
        super(view);
    }

    @Override
    public void render(HttpServletRequest request, HttpServletResponse response) throws RenderException {
        try {
            request.getRequestDispatcher(getView()).forward(request, response);
        } catch (ServletException e) {
            throw new RenderException(e);
        } catch (IOException e) {
            throw new RenderException(e);
        }
    }

}
