package longshu.easycontroller.json;

/**
 * UnrealizedJson
 *
 * @author LongShu 2017/06/20
 */
public class UnrealizedJson extends Json {
    @Override
    public String toJson(Object object) {
        throw new RuntimeException("未实现不可用!");
    }

    @Override
    public <T> T parse(String json, Class<T> type) {
        throw new RuntimeException("未实现不可用!");
    }
}
