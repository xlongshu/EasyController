package longshu.easycontroller.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * JavaBean工具类(beanutils实现)
 *
 * @author longshu 2017年1月21日
 */
public final class BeanUtil {

    public static Class<?> forName(String className) {
        if (StringUtils.isBlank(className)) {
            throw new IllegalArgumentException("className can not be blank.");
        }
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getBean(String className) {
        return getBean(forName(className));
    }

    public static <T> T getBean(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将javaBean对象转为Map
     *
     * @param bean
     * @return Map
     */
    public static Map<String, Object> beanToMap(Object bean) {
        try {
            return PropertyUtils.describe(bean);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void mapToBean(Map<String, ?> map, Object target) {
        try {
            BeanUtils.populate(target, map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 把Map转换成指定类型的javabean对象
     *
     * @param map
     * @param clazz
     * @return
     */
    public static <T> T mapToBean(Map<String, ?> map, Class<T> clazz) {
        T bean = getBean(clazz);
        mapToBean(map, bean);
        return bean;
    }

}
