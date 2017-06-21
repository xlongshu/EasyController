package longshu.easycontroller.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * FastJson
 *
 * @author LongShu 2017/06/20
 */
public class FastJson extends Json {

    private static SerializerFeature[] defaultFeatures = {SerializerFeature.WriteEnumUsingToString,
            SerializerFeature.WriteDateUseDateFormat};

    private static SerializerFeature[] prettyFeatures = {SerializerFeature.WriteEnumUsingToString,
            SerializerFeature.WriteDateUseDateFormat, SerializerFeature.PrettyFormat};

    public static SerializerFeature[] getDefaultFeatures() {
        return defaultFeatures;
    }

    public static synchronized void setDefaultFeatures(SerializerFeature[] defaultFeatures) {
        FastJson.defaultFeatures = defaultFeatures;
    }

    public static SerializerFeature[] getPrettyFeatures() {
        return prettyFeatures;
    }

    public static synchronized void setPrettyFeatures(SerializerFeature[] prettyFeatures) {
        FastJson.prettyFeatures = prettyFeatures;
    }

    public static String toJson(Object object, boolean pretty) {
        if (pretty) {
            return JSON.toJSONString(object, prettyFeatures);
        }
        return JSON.toJSONString(object, defaultFeatures);
    }

    @Override
    public String toJson(Object object) {
        return toJson(object, false);
    }

    @Override
    public <T> T parse(String json, Class<T> type) {
        return JSON.parseObject(json, type);
    }

}
