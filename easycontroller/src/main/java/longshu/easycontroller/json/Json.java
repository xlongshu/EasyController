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

    @Setter
    @NonNull
    private static Json defaultJson;

    @Getter
    @Setter
    @NonNull
    private static String defaultDatePattern;

    @Getter
    @Setter
    @NonNull
    protected String datePattern = null;

    public static Json getDefaultJson() {
        if (defaultJson == null) {
            defaultJson = new UnrealizedJson();
        }
        return defaultJson;
    }

    public abstract String toJson(Object object);

    public abstract <T> T parse(String json, Class<T> type);

}
