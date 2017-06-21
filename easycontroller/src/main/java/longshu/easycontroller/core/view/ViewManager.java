package longshu.easycontroller.core.view;

import longshu.easycontroller.core.config.Constants;
import longshu.easycontroller.util.FileUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.io.File;

/**
 * ViewManager
 *
 * @author LongShu 2017/05/14
 */
@Slf4j
public class ViewManager {

    private static ViewManager me = new ViewManager();

    @Setter
    @lombok.NonNull
    private ServletContext servletContext;
    @Inject
    @Getter
    private ViewFactory viewFactory;

    private ViewManager() {
    }

    public static ViewManager me() {
        return me;
    }

    public void init(ServletContext servletContext) {
        setServletContext(servletContext);

        initJspView();
        initFileView();

        initFreeMarkerView();
        initVelocityView();
    }

    private void initJspView() {
        try {
            Class.forName("javax.el.ELResolver");
            Class.forName("javax.servlet.jsp.JspFactory");
        } catch (ClassNotFoundException e) {
            log.info("Jsp or JSTL can not be supported!");
        }
    }

    private void initFileView() {
        String downloadPath = Constants.me().getBaseDownloadPath().trim();
        // 如果为绝对路径则直接使用，否则把 downloadPath 参数作为项目根路径的相对路径
        if (!FileUtil.isAbsolutelyPath(downloadPath)) {
            downloadPath = new File(Constants.me().getWebRootPath(), downloadPath).getPath();
        }
        log.info("downloadPath:{}", downloadPath);
        FileView.initContext(downloadPath, servletContext);
    }

    private void initFreeMarkerView() {
        try {
            Class.forName("freemarker.template.Template");  // detect freemarker
            // TODO
        } catch (ClassNotFoundException e) {
            log.info("freemarker can not be supported!");
        }
    }

    private void initVelocityView() {
        try {
            Class.forName("org.apache.velocity.VelocityContext");
            // TODO
        } catch (ClassNotFoundException e) {
            log.info("Velocity can not be supported!");
        }
    }

}
