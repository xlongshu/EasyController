package longshu.easycontroller.core.view;

import longshu.easycontroller.core.config.Constants;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.VelocityException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * VelocityView
 *
 * @author LongShu 2017/05/11
 */
public class VelocityView extends AbstractTemplateView {

    private static final Properties properties = new Properties();

    public VelocityView(String view) {
        super(view);
    }

    static void init(ServletContext servletContext) {
        String webPath = servletContext.getRealPath("/");
        properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, webPath);

        String encoding = Constants.me().getEncoding();
        properties.setProperty(Velocity.INPUT_ENCODING, encoding);
        properties.setProperty(Velocity.OUTPUT_ENCODING, encoding);

        Velocity.init(properties);
    }

    public static void setProperties(Properties properties) {
        if (properties == null) {
            return;
        }
        Set<Map.Entry<Object, Object>> entries = properties.entrySet();
        for (Map.Entry<Object, Object> entry : entries) {
            VelocityView.properties.put(entry.getKey(), entry.getValue());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void render(HttpServletRequest request, HttpServletResponse response) throws RenderException {
        VelocityContext context = new VelocityContext();
        for (Enumeration<String> names = request.getAttributeNames(); names.hasMoreElements(); ) {
            String name = names.nextElement();
            context.put(name, request.getAttribute(name));
        }

        try {
            response.setContentType(getContentType());
            PrintWriter writer = response.getWriter();

            Template template = Velocity.getTemplate(getUrl());
            template.merge(context, writer);
            writer.flush();
        } catch (VelocityException e) {
            throw new RenderException(e);
        } catch (IOException e) {
            throw new RenderException(e);
        }
    }

}
