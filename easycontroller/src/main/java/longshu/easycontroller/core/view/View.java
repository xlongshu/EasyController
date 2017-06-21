package longshu.easycontroller.core.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * View
 *
 * @author LongShu 2017/05/11
 */
public interface View {

    String REDIRECT_URL_PREFIX = "redirect:";
    String FORWARD_URL_PREFIX = "forward:";
    String JAVASCRIPT_PREFIX = "javascript:";
    String JSON_PREFIX = "json:";

    String getContentType();

    String getEncoding();

    String getView();

    /**
     * Render to client
     */
    void render(HttpServletRequest request, HttpServletResponse response) throws RenderException;

}
