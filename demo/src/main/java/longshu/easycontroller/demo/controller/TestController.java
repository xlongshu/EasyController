package longshu.easycontroller.demo.controller;

import longshu.easycontroller.core.ActionKey;
import longshu.easycontroller.core.Controller;
import longshu.easycontroller.core.HttpMethod;
import longshu.easycontroller.core.WebData;
import longshu.easycontroller.core.config.Constants;
import longshu.easycontroller.core.view.View;
import longshu.easycontroller.demo.entity.User;
import longshu.easycontroller.demo.service.ServiceException;
import longshu.easycontroller.demo.service.UserService;
import longshu.easycontroller.json.Json;

import javax.inject.Inject;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * TestController
 *
 * @author LongShu 2017/06/20
 */
public class TestController extends Controller {

    @Inject
    private UserService userService;

    public void index() {
    }

    public String loginView() {
        return View.FORWARD_URL_PREFIX + "/WEB-INF/jsps/login.jsp";
    }

    @ActionKey(methods = HttpMethod.POST)
    public Object login() {
        User user = getBean(User.class);
        logger.debug("user:{}", user);

        try {
            User loginUser = userService.login(user.getUsername(), user.getPassword());
            setSessionAttribute("loginUser", loginUser);
            return getViewFactory().getForwardView("/WEB-INF/jsps/test/index.jsp");
        } catch (ServiceException e) {
            logger.warn(e.getMessage());
            request.setAttribute("msg", e.getMessage());
            return getViewFactory().getForwardView("/WEB-INF/jsps/login.jsp");
        }
    }

    public Object userList() {
        List<User> userList = userService.findAll();
        request.setAttribute("userList", userList);

        return View.FORWARD_URL_PREFIX + "/WEB-INF/jsps/test/userList.jsp";
    }

    public String redirect() {
        return View.REDIRECT_URL_PREFIX + "/";
    }

    public String forward() {
        return View.FORWARD_URL_PREFIX + "/WEB-INF/jsps/index.jsp";
    }

    public Object text() {
        return "返回文本";
    }

    public String js() {
        return View.JAVASCRIPT_PREFIX + "alert('js代码')";
    }

    public String json() {
        return View.JSON_PREFIX + Json.getDefaultJson().toJson(new Date());
    }

    public Object json2() {
        return WebData.newObj(new Date());
    }

    public Object file() {
        return new File(Constants.me().getWebRootPath(), "index.jsp");
    }

    public Object file2() {
        return getViewFactory().getFileView(new File(Constants.me().getWebRootPath(), "index.jsp"));
    }

}
