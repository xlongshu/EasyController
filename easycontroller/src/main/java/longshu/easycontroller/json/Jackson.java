package longshu.easycontroller.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Jackson
 *
 * @author LongShu 2017/06/20
 */
public class Jackson extends Json {

    // ThreadLocal<DateFormat> 使用
    private static String _datePattern;

    /**
     * SimpleDateFormat创建成本高,所以静态成员化,配合ThreadLocal解决线程安全问题
     */
    private static ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>() {
        @Override
        protected synchronized DateFormat initialValue() {
            return new SimpleDateFormat(_datePattern);
        }
    };

    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)// null属性值不映射
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);//有属性不能映射的时候不报错
        return objectMapper;
    }

    @Override
    public String toJson(Object object, boolean pretty) {
        ObjectMapper objectMapper = getObjectMapper();

        if (_datePattern == null) {
            _datePattern = datePattern == null ? defaultDatePattern : datePattern;
        }
        // setDatePattern()表示使用DateFormat
        if (datePattern != null) {
            DateFormat format = Jackson.dateFormat.get();
            objectMapper.setDateFormat(format);
        }

        try {
            if (pretty) {
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            } else {
                return objectMapper.writeValueAsString(object);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T parse(String json, Class<T> type) {
        ObjectMapper objectMapper = getObjectMapper();
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
