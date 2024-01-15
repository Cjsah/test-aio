package net.cjsah.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListUtil {

    public static <T, V> List<T> distinctBy(List<T> list, Function<T, V> key) {
        Map<V, Boolean> map = new ConcurrentHashMap<>();
        return list.stream().filter(it -> map.putIfAbsent(key.apply(it), Boolean.TRUE) == null).collect(Collectors.toList());
    }

    public static <T, V> List<V> map(List<T> list, Function<T, V> function) {
        return null == list ? new ArrayList<>() : list.stream().parallel().map(function).collect(Collectors.toList());
    }

}
