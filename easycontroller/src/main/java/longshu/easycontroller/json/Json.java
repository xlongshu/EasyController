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
    protected static Json defaultJson = new UnrealizedJson();

    @Getter
    @Setter
    @NonNull
    protected static String defaultDatePattern;
    protected String datePattern = null;

    public abstract String toJson(Object object);

    public abstract <T> T parse(String json, Class<T> type);

    public String getDatePattern() {
        return datePattern;
    }

    public Json setDatePattern(String datePattern) {
        this.datePattern = datePattern;
        return this;
    }
}
