package me.zhengjie.utils;

/**
 * 处理Double的工具类
 */
public class DoubleUtils {

    public static Double parseDouble(Object object){
        return (Double)(object == null ? 0D: object);
    }
}
