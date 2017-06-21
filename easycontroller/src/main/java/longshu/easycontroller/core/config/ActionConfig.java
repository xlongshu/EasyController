package longshu.easycontroller.core.config;

import longshu.easycontroller.core.BeanFactory;
import longshu.easycontroller.util.ConvertUtils;
import lombok.Cleanup;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.util.Properties;

/**
 * ActionConfig
 *
 * @author LongShu 2017/05/09
 */
public abstract class ActionConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    protected static Properties prop;

    /**
     * Config constant
     */
    public abstract void configConstant(Constants me);

    /**
     * Config Module
     */
    public abstract void configModule(BeanFactory me);

    /**
     * Config Interceptors
     */
    public abstract void configInterceptor(Interceptors me);

    /**
     * Config mapping
     */
    public abstract void configMapping(ActionMapping me);

    public void afterStart() {
    }

    public void beforeStop() {
        unloadPropertyFile();
    }

    /**
     * Load property file.
     *
     * @see #loadPropertyFile(String, String)
     */
    public static Properties loadPropertyFile(String fileName) {
        return loadPropertyFile(fileName, Constants.DEFAULT_ENCODING);
    }

    /**
     * Load property file.
     * Example:<br>
     * loadPropertyFile("db_username_pass.txt", "UTF-8");
     *
     * @param fileName the file in CLASSPATH or the sub directory of the CLASSPATH
     * @param encoding the encoding
     */
    public static Properties loadPropertyFile(String fileName, String encoding) {
        Properties properties;
        try {

            @Cleanup InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            @Cleanup Reader reader = new InputStreamReader(inputStream, encoding);

            properties = new Properties();
            properties.load(reader);
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file.", e);
        }
        return properties;
    }

    protected void unloadPropertyFile() {
        prop = null;
    }

    private Properties getProp() {
        if (prop == null) {
            throw new IllegalStateException("Load propties file by invoking loadPropertyFile(String fileName) method first.");
        }
        return prop;
    }

    public String getProp(String key) {
        return getProp().getProperty(key);
    }

    public String getProp(String key, String defaultValue) {
        return getProp().getProperty(key, defaultValue);
    }

    public Integer getPropToInt(String key) {
        return getPropToInt(key, null);
    }

    public Integer getPropToInt(String key, Integer defaultValue) {
        String value = getProp().getProperty(key);
        return ConvertUtils.toInt(value, defaultValue);
    }

    public Boolean getPropToBoolean(String key) {
        return getPropToBoolean(key, null);
    }

    public Boolean getPropToBoolean(String key, Boolean defaultValue) {
        String value = getProp().getProperty(key);
        return ConvertUtils.toBoolean(value, defaultValue);
    }

}
