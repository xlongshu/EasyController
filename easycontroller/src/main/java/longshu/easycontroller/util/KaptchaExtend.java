package longshu.easycontroller.util;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * KaptchaExtend
 *
 * @author LongShu 2017/04/29
 * @see com.google.code.kaptcha.servlet.KaptchaExtend
 */
@Slf4j
public class KaptchaExtend {

    @Inject
    private com.google.code.kaptcha.Producer kaptchaProducer;
    private String imgType = "jpeg";// jpeg,png,gif
    private static long expire = 300000;// 过期时间间隔,单位为毫秒,0表示不过期

    public void setKaptchaProducer(com.google.code.kaptcha.Producer kaptchaProducer) {
        this.kaptchaProducer = kaptchaProducer;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    /**
     * 间隔时间,单位为秒
     */
    public static void setExpire(int expire) {
        KaptchaExtend.expire = expire * 1000;
        log.info("expire:{}", expire);
    }

    /**
     * map it to the /url/captcha.jpg
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public String captcha(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setDateHeader("Expires", 0);
        // Set standard HTTP/1.0 no-cache header.
        resp.setHeader("Pragma", "no-cache");
        // Set standard HTTP/1.1 no-cache headers.
        resp.setHeader("Cache-Control", "no-store, no-cache");

        // return a image
        resp.setContentType("image/" + imgType);

        // create the text for the image
        String capText = this.kaptchaProducer.createText();
        log.debug("capText:{}", capText);

        // store the text in the session
        req.getSession().setAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY, capText);

        // store the date in the session so that it can be compared
        // against to make sure someone hasn't taken too long to enter
        // their kaptcha
        req.getSession().setAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_DATE, System.currentTimeMillis());

        // create the image with the text
        BufferedImage bi = this.kaptchaProducer.createImage(capText);

        ServletOutputStream out = resp.getOutputStream();

        // write the data out
        ImageIO.write(bi, imgType, out);

        // fixes issue #69: set the attributes after we write the image in case
        // the image writing fails.

        // store the text in the session
        req.getSession().setAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY, capText);

        // store the date in the session so that it can be compared
        // against to make sure someone hasn't taken too long to enter
        // their kaptcha
        req.getSession().setAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_DATE, System.currentTimeMillis());

        return capText;
    }

    public static boolean isExpired(HttpSession session) {
        if (0 == expire) {
            return false;
        }
        Long saveTime = (Long) session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_DATE);
        if (null == saveTime) {
            return true;
        }
        if ((saveTime + expire) < System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    public static String getCaptcha(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return getCaptcha(session);
    }

    public static String getCaptcha(HttpSession session) {
        if (isExpired(session)) {
            removeCaptcha(session);
            return null;
        }
        return (String) session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
    }

    public static void removeCaptcha(HttpSession session) {
        session.removeAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        session.removeAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_DATE);
    }

}

