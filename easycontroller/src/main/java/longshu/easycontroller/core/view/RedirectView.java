package longshu.easycontroller.core.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * RedirectView
 *
 * @author LongShu 2017/05/11
 */
public class RedirectView extends AbstractView {

    private static String contextPath;
    private boolean withQueryString;

    public RedirectView(String url) {
        super(url);
    }

    public RedirectView(String url, boolean withQueryString) {
        super(url);
        this.withQueryString = withQueryString;
    }

    @Override
    public void render(HttpServletRequest request, HttpServletResponse response) throws RenderException {
        getContxtPath(request);
        String finalUrl = buildFinalUrl(request);
        try {
            response.sendRedirect(finalUrl);
        } catch (IOException e) {
            throw new RenderException(e.getMessage(), e);
        }
    }

    protected String getContxtPath(HttpServletRequest request) {
        if (contextPath == null) {
            String cp = request.getContextPath();
            contextPath = ("".equals(cp) || "/".equals(cp)) ? null : cp;
        }
        return contextPath;
    }

    public String buildFinalUrl(HttpServletRequest request) {
        String result;
        // 如果一个url为/login/connect?goto=http://www.xxx.com，则有错误
        // ^((https|http|ftp|rtsp|mms)?://)$   ==> indexOf 取值为 (3, 5)
        if (contextPath != null && (view.indexOf("://") == -1 || view.indexOf("://") > 5)) {
            result = contextPath + getView();
        } else {
            result = view;
        }

        if (withQueryString) {
            String queryString = request.getQueryString();
            if (queryString != null) {
                if (result.indexOf("?") == -1) {
                    result = result + "?" + queryString;
                } else {
                    result = result + "&" + queryString;
                }
            }
        }

        return result;
    }

}
