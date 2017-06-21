package longshu.easycontroller.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

/**
 * 服务端发送给客户端的统一数据(json)
 * 类似于SpringMVC的ResponseEntity
 * @author LongShu 2017/05/11
 */
@Getter
@EqualsAndHashCode
public class WebData<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /*
    * data 数据类型
    */
    public enum DataType {
        text, json, xml
    }

    /*
     * WebData的result类型
     */
    public static final int ERROR = -1; // 错误
    public static final int MSG = 1; // 文本信息/消息
    public static final int OBJ = 2; // json对象或数组

    private int result = MSG; // 数据结果类型
    private String msg; // 返回的信息
    private DataType type = DataType.text; // 数据内容类型
    private T data; // 数据内容,不一定有内容

    protected WebData() {
    }

    /**
     * @param result 结果类型
     * @param msg    信息
     * @param type   数据内容类型
     * @param data   数据
     */
    public WebData(int result, String msg, DataType type, T data) {
        this.result = result;
        this.msg = msg;
        this.type = type;
        this.data = data;
    }

    /**
     * 返回一个错误
     *
     * @param msg 错误信息
     */
    public static WebData<String> newError(String msg) {
        return new WebData<String>(ERROR, msg, DataType.text, null);
    }

    /**
     * 返回一个消息
     *
     * @param msg 消息
     */
    public static WebData<Object> newMsg(String msg) {
        return new WebData<Object>(MSG, msg, DataType.text, null);
    }

    /**
     * 返回含数据的消息
     *
     * @param msg  消息
     * @param data 数据
     */
    public static <T> WebData<T> newMsg(String msg, T data) {
        return new WebData<T>(MSG, msg, DataType.text, data);
    }

    /**
     * 返回一个json对象
     *
     * @param obj 对象/数组
     */
    public static <T> WebData<T> newObj(T obj) {
        return new WebData<T>(OBJ, null, DataType.json, obj);
    }

    /**
     * 结果类型判断
     * 使用isXXX会认为是set方法
     */
    public boolean error() {
        return ERROR == this.result;
    }

    public boolean msg() {
        return MSG == this.result;
    }

    public boolean obj() {
        return OBJ == this.result;
    }

    public int getResult() {
        return result;
    }

    public WebData<T> setResult(int result) {
        this.result = result;
        return this;
    }

    public WebData<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public WebData<T> setType(DataType type) {
        this.type = type;
        return this;
    }

    public WebData<T> setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WebData{");
        sb.append("result=").append(result);
        sb.append(", msg='").append(msg).append('\'');
        sb.append(", type=").append(type);
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }

}
