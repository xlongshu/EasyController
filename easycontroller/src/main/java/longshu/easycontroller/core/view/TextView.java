package longshu.easycontroller.core.view;

import longshu.easycontroller.core.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * TextView
 *
 * @author LongShu 2017/05/11
 */
public class TextView extends AbstractView {

    protected String text;

    public TextView() {
    }

    public TextView(String text) {
        this.text = text;
        this.contentType = MediaType.TEXT_PLAIN;
    }

    public TextView(String text, String contentType) {
        this.text = text;
        this.contentType = contentType;
    }

    @Override
    public void render(HttpServletRequest request, HttpServletResponse response) throws RenderException {
        // HTTP/1.0 caches might not implement Cache-Control and might only implement Pragma: no-cache
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        response.setContentType(getContentType());
        response.setCharacterEncoding(getEncoding());

        // write
        try {
            PrintWriter writer = response.getWriter();
            writer.write(getText());
            writer.flush();
        } catch (IOException e) {
            throw new RenderException(e.getMessage(), e);
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
