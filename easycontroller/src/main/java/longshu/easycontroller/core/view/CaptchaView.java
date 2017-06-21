package longshu.easycontroller.core.view;

import longshu.easycontroller.util.KaptchaExtend;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * CaptchaView
 *
 * @author LongShu 2017/05/11
 */
@Singleton
public class CaptchaView extends AbstractView {

    @Inject
    private KaptchaExtend kaptchaExtend;

    @Override
    public void render(HttpServletRequest request, HttpServletResponse response) throws RenderException {
        try {
            kaptchaExtend.captcha(request, response);
        } catch (ServletException e) {
            throw new RenderException(e);
        } catch (IOException e) {
            throw new RenderException(e);
        }
    }

}
