package net.cjsah.util;

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
}
