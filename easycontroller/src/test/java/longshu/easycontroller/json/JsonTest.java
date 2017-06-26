package longshu.easycontroller.json;

import longshu.easycontroller.util.DateUtil;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JsonTest
 *
 * @author LongShu 2017/06/26
 */
public class JsonTest {

    private static Json json;
    private static Object object;
    private static String jsonText;

    @BeforeClass
    public static void init() {
        Json.setDefaultJson(new FastJson().setDatePattern(DateUtil.DATE_PATTERN));
        json = Json.getDefaultJson();
    }

    @Test
    public void toJson() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", 1);
        map.put("name", Json.getDefaultJson().getClass().getName());
        map.put("date", new Date());

        object = map;
        jsonText = json.toJson(object);
        System.out.println("jsonText = " + jsonText);
    }

    @Test
    public void parse() throws Exception {
        object = json.parse(jsonText, Map.class);
        System.out.println(object.getClass());
        System.out.println("object = " + object);
    }

}