package longshu.easycontroller.demo.controller;

import longshu.easycontroller.core.ActionKey;
import longshu.easycontroller.core.Controller;
import longshu.easycontroller.core.HttpMethod;
import longshu.easycontroller.core.view.View;
import org.apache.commons.lang3.StringUtils;

/**
 * IndexController
 *
 * @author LongShu 2017/05/14
 */
public class IndexController extends Controller {

    /**
     * 首页
     */
    @ActionKey(methods = HttpMethod.GET)
    public void index() {
    }

    /**
     * 验证码
     */
    @ActionKey(value = "captcha", methods = HttpMethod.GET)
    public View captcha() {
        return getViewFactory().getCaptchaView();
    }

    public Object error() {
        String code = getParameter("code");
        int errorCode = 404;
        if (!StringUtils.isBlank(code)) {
            errorCode = toInt(code);
        }
        return getViewFactory().getErrorView(errorCode);
    }

}
