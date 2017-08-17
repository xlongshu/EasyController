package longshu.easycontroller.core.view;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.Getter;
import longshu.easycontroller.core.config.Constants;
import longshu.easycontroller.util.DateUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * FreeMarkerView
 *
 * @author LongShu 2017/05/11
 */
public class FreeMarkerView extends AbstractTemplateView {

    @Getter
    private static final Configuration config = new Configuration(Configuration.VERSION_2_3_20);

    public FreeMarkerView(String view) {
        super(view);
    }

    static void init(ServletContext servletContext) {
        config.setServletContextForTemplateLoading(servletContext, "/");
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        String encoding = Constants.me().getEncoding();
        config.setDefaultEncoding(encoding);
        config.setOutputEncoding(encoding);

        config.setNumberFormat("#0.######");
        config.setDateFormat("yyyy-MM-dd");
        config.setTimeFormat("HH:mm:ss");
        config.setDateTimeFormat(DateUtil.DATE_PATTERN);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void render(HttpServletRequest request, HttpServletResponse response) throws RenderException {
        response.setContentType(getContentType());
        // 获取数据
        Map<String, Object> data = new HashMap<String, Object>();
        for (Enumeration<String> names = request.getAttributeNames(); names.hasMoreElements(); ) {
            String name = names.nextElement();
            data.put(name, request.getAttribute(name));
        }

        try {
            Template template = config.getTemplate(getUrl());
            PrintWriter writer = response.getWriter();
            template.process(data, writer);
            writer.flush();
        } catch (TemplateException e) {
            throw new RenderException(e);
        } catch (IOException e) {
            throw new RenderException(e);
        }
    }

}
