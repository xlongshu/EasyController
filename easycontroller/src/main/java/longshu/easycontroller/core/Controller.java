package longshu.easycontroller.core;

import longshu.easycontroller.core.multipart.FilePart;
import longshu.easycontroller.core.multipart.MultipartRequest;
import longshu.easycontroller.core.view.ViewFactory;
import longshu.easycontroller.core.view.ViewManager;
import longshu.easycontroller.util.BeanUtil;
import longshu.easycontroller.util.ConvertUtils;
import longshu.easycontroller.util.KaptchaExtend;
import longshu.easycontroller.util.WebUtils;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author LongShu 2017/05/09
 */
public abstract class Controller {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Getter
    protected HttpServletRequest request;
    @Getter
    protected HttpServletResponse response;

    protected Cookie[] cookies;
    protected String userAgent;

    void init(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;

        if (isMultipartContent()) {
            this.request = new MultipartRequest(request);
        }
    }

    public <T> T getBean(Class<T> beanClass) {
        return BeanUtil.mapToBean(getParameterMap(), beanClass);
    }

    public String getHeader(String name) {
        return request.getHeader(name);
    }

    public String getParameter(String name) {
        return request.getParameter(name);
    }

    @SuppressWarnings("unchecked")
    public Enumeration<String> getParameterNames() {
        return request.getParameterNames();
    }

    public String[] getParameterValues(String name) {
        return request.getParameterValues(name);
    }

    @SuppressWarnings("unchecked")
    public Map<String, String[]> getParameterMap() {
        return request.getParameterMap();
    }

    @SuppressWarnings("unchecked")
    public <T> T getRequestAttribute(String name) {
        return (T) request.getAttribute(name);
    }

    public void setRequestAttribute(String name, Object value) {
        request.setAttribute(name, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getSessionAttribute(String name) {
        return (T) request.getSession().getAttribute(name);
    }

    public void setSessionAttribute(String name, Object value) {
        request.getSession().setAttribute(name, value);
    }

    /**
     * multipart
     */
    public boolean isMultipartContent() {
        return WebUtils.isMultipartContent(request);
    }

    /**
     * 获取所有上传的文件
     * <pre>
     * getParameterXXX或者不给会使用默认的上传路径,在给uploadPath将无效
     * </pre>
     *
     * @param uploadPath 文件上传的目录路径
     */
    public List<FilePart> getFiles(String uploadPath) {
        if (request instanceof MultipartRequest) {
            return ((MultipartRequest) request).getFiles(uploadPath);
        }
        return null;
    }

    public List<FilePart> getFiles(File uploadDir) {
        if (request instanceof MultipartRequest) {
            return ((MultipartRequest) request).getFiles(uploadDir.getAbsolutePath());
        }
        return null;
    }

    public List<FilePart> getFiles() {
        return getFiles((String) null);
    }

    /**
     * {@link #getFiles(String)}
     *
     * @see #getFiles(String)
     */
    public FilePart getFile(String name, String uploadPath) {
        if (request instanceof MultipartRequest) {
            return ((MultipartRequest) request).getFile(name, uploadPath);
        }
        return null;
    }

    public FilePart getFile(String name, File uploadDir) {
        if (request instanceof MultipartRequest) {
            return ((MultipartRequest) request).getFile(name, uploadDir.getAbsolutePath());
        }
        return null;
    }

    public FilePart getFile(String name) {
        return getFile(name, (String) null);
    }

    /**
     * Get cookie object by cookie name.
     */
    public Cookie getCookieObject(String name) {
        Cookie[] cookies = getCookieObjects();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return null;
    }

    /**
     * Get all cookie objects.
     */
    public Cookie[] getCookieObjects() {
        if (null == cookies) {
            cookies = request.getCookies();
        }
        return cookies != null ? cookies : new Cookie[0];
    }

    /**
     * Get cookie value by cookie name.
     */
    public String getCookie(String name, String defaultValue) {
        Cookie cookie = getCookieObject(name);
        return cookie != null ? cookie.getValue() : defaultValue;
    }

    /**
     * Get cookie value by cookie name.
     */
    public String getCookie(String name) {
        return getCookie(name, null);
    }

    /**
     * Set Cookie to response.
     *
     * @param name            cookie name
     * @param value           cookie value
     * @param maxAgeInSeconds -1: clear cookie when close browser. 0: clear cookie immediately.  n>0 : max age in n seconds.
     * @param path            see Cookie.setPath(String)
     * @param domain          the domain name within which this cookie is visible; form is according to RFC 2109
     * @param isHttpOnly      true if this cookie is to be marked as HttpOnly, false otherwise
     */
    public Controller setCookie(String name, String value, int maxAgeInSeconds, String path, String domain, boolean isHttpOnly) {
        return doSetCookie(name, value, maxAgeInSeconds, path, domain, isHttpOnly);
    }

    /**
     * {@link #setCookie(String, String, int, String, String, boolean)}
     */
    public Controller setCookie(String name, String value, int maxAgeInSeconds, String path, boolean isHttpOnly) {
        return doSetCookie(name, value, maxAgeInSeconds, path, null, isHttpOnly);
    }

    /**
     * {@link #setCookie(String, String, int, String, String, boolean)}
     */
    public Controller setCookie(String name, String value, int maxAgeInSeconds, String path) {
        return doSetCookie(name, value, maxAgeInSeconds, path, null, null);
    }

    /**
     * {@link #setCookie(String, String, int, String, String, boolean)}
     */
    public Controller setCookie(String name, String value, int maxAgeInSeconds, boolean isHttpOnly) {
        return doSetCookie(name, value, maxAgeInSeconds, null, null, isHttpOnly);
    }

    /**
     * {@link #setCookie(String, String, int, String, String, boolean)}
     */
    public Controller setCookie(String name, String value, int maxAgeInSeconds) {
        return doSetCookie(name, value, maxAgeInSeconds, null, null, null);
    }

    private Controller doSetCookie(String name, String value, int maxAgeInSeconds, String path, String domain, Boolean isHttpOnly) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAgeInSeconds);
        // set the default path value to "/"
        if (path == null) {
            path = "/";
        }
        cookie.setPath(path);

        if (domain != null) {
            cookie.setDomain(domain);
        }
//        if (isHttpOnly != null) {
//            cookie.setHttpOnly(isHttpOnly);
//        }
        response.addCookie(cookie);
        return this;
    }

    /**
     * 验证验证码
     *
     * @param paraName 验证码参数
     */
    public boolean validateCaptcha(String paraName) {
        String captcha = request.getParameter(paraName);
        String captchaKey = KaptchaExtend.getCaptcha(request);
        logger.debug("captcha:{}, captchaKey:{}", captcha, captchaKey);

        if (StringUtils.isAnyBlank(captcha, captchaKey)) {
            return false;
        }
        if (StringUtils.equalsIgnoreCase(captcha, captchaKey)) {
            KaptchaExtend.removeCaptcha(request.getSession());
            return true;
        }
        return false;
    }

    public ViewFactory getViewFactory() {
        return ViewManager.me().getViewFactory();
    }

    public String getUserAgent() {
        if (null == userAgent) {
            userAgent = WebUtils.getUserAgent(request);
            logger.debug("userAgent:{}", userAgent);
        }
        return userAgent;
    }

    public boolean isIE() {
        String userAgent = getUserAgent();
        return WebUtils.isIE(userAgent);
    }

    /**
     * 判断是否为Ajax异步请求
     */
    public boolean isAjax() {
        return WebUtils.isAjax(request);
    }

    /**
     * 是否为异步请求,默认为false <br />
     * 如果是异步请求时加字段async=1/async=true
     *
     * @return true if the value of the 'async' is "true" or "1", false if it is "false" or "0", default value if it is true
     */
    public boolean isAsync() {
        return isAjax() || toBoolean(getParameter("async"));
    }

    public Boolean toBoolean(String value) {
        try {
            return ConvertUtils.toBoolean(value);
        } catch (RuntimeException e) {
            throw new ActionException(e.getMessage());
        }
    }

    public Integer toInt(String value) {
        try {
            return ConvertUtils.toInt(value);
        } catch (RuntimeException e) {
            throw new ActionException("Can not parse [" + value + "] to Integer value.");
        }
    }

    public Long toLong(String value) {
        try {
            return ConvertUtils.toLong(value);
        } catch (RuntimeException e) {
            throw new ActionException("Can not parse [" + value + "] to Long value.");
        }
    }

}