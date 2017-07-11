package longshu.easycontroller.json;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Json
 *
 * @author LongShu 2017/06/20
 */
public abstract class Json {

    @Getter
    @Setter
    @NonNull
    protected static Json defaultJson;

    @Getter
    @Setter
    @NonNull
    protected static String defaultDatePattern = "yyyy-MM-dd HH:mm:ss";
    protected String datePattern = null;

    static {
        try {
            Class.forName("com.alibaba.fastjson.JSON");
            defaultJson = new FastJson();
        } catch (ClassNotFoundException ignored) {
            try {
                Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
                defaultJson = new Jackson();
            } catch (ClassNotFoundException e) {
                defaultJson = new UnrealizedJson();
            }
        }
    }

    public abstract String toJson(Object object, boolean pretty);

    public String toJson(Object object) {
        return toJson(object, false);
    }

    public abstract <T> T parse(String json, Class<T> type);

    public String getDatePattern() {
        return datePattern;
    }

    public Json setDatePattern(String datePattern) {
        this.datePattern = datePattern;
        return this;
    }

}
