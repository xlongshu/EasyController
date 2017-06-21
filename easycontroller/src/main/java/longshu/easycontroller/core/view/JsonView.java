package longshu.easycontroller.core.view;

import longshu.easycontroller.json.Json;
import longshu.easycontroller.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * JsonView
 *
 * @author LongShu 2017/05/11
 */
public class JsonView extends TextView {

    private boolean forIE = false;

    public JsonView(String jsonText) {
        super(jsonText);
    }

    public JsonView(final Object obj) {
        super(Json.getDefaultJson().toJson(obj));
    }

    public JsonView(final String key, final Object value) {
        Map<String, Object> map = new HashMap<String, Object>(1);
        map.put(key, value);

        String jsonText = Json.getDefaultJson().toJson(map);
        setText(jsonText);
    }

    @Override
    public void render(HttpServletRequest request, HttpServletResponse response) throws RenderException {
        if (WebUtils.isIE(request)) {
            forIE();
        }
        super.render(request, response);
    }

    @Override
    public String getContentType() {
        if (isForIE()) {
            return contentType = "text/html; charset=" + getEncoding();
        }
        return contentType = "application/json; charset=" + getEncoding();
    }

    public JsonView forIE() {
        forIE = true;
        return this;
    }

    public boolean isForIE() {
        return forIE;
    }

    public void setForIE(boolean forIE) {
        this.forIE = forIE;
    }
}
