package net.cjsah.util;

import java.util.function.Function;

public class EnumUtil {

    public static <T extends Enum<T>> T fromInt(Class<T> clazz, String index, Function<T, Integer> getter) {
        return fromInt(clazz, Integer.parseInt(index), getter);
    }

    public static <T extends Enum<T>> T fromInt(Class<T> clazz, int index, Function<T, Integer> getter) {
        T[] values = clazz.getEnumConstants();
        for (T value : values) {
            if (getter.apply(value) == index) return value;
        }
        return null;
    }

    public static <T extends Enum<T>> T fromStr(Class<T> clazz, String name, Function<T, String> getter) {
        T[] values = clazz.getEnumConstants();
        for (T value : values) {
            if (getter.apply(value).equals(name)) return value;
        }
        return values[0];
    }

}
