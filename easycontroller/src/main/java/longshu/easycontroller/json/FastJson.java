package longshu.easycontroller.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * FastJson
 *
 * @author LongShu 2017/06/20
 */
public class FastJson extends Json {

    private static SerializerFeature[] defaultFeatures = {SerializerFeature.WriteEnumUsingToString};

    private static SerializerFeature[] prettyFeatures = {SerializerFeature.WriteEnumUsingToString, SerializerFeature.PrettyFormat};

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

    @Override
    public String toJson(Object object, boolean pretty) {
        String dp = datePattern == null ? defaultDatePattern : datePattern;
        if (pretty) {
            return JSON.toJSONStringWithDateFormat(object, dp, prettyFeatures);
        }
        return JSON.toJSONStringWithDateFormat(object, dp, defaultFeatures);
    }

    @Override
    public <T> T parse(String json, Class<T> type) {
        return JSON.parseObject(json, type);
    }

}
