package net.cjsah.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class StreamUtil {

    public static <T, R> List<R> map(Collection<T> list, Function<T, R> function) {
        return list.stream().parallel().map(function).collect(Collectors.toList());
    }

    public static <T> int sum(Collection<T> list, ToIntFunction<T> function) {
        return list.stream().parallel().mapToInt(function).sum();
    }

    public static <T> List<T> filter(Collection<T> list, Predicate<T> predicate) {
        return list.stream().parallel().filter(predicate).collect(Collectors.toList());
    }

    public static <T, R> Map<R ,List<T>> group(Collection<T> list, Function<T, R> groupBy) {
        return list.stream().parallel().collect(Collectors.groupingBy(groupBy));
    }

    public static <T> T find(Collection<T> list, Predicate<T> predicate) {
        return list.stream().parallel().filter(predicate).findFirst().orElse(null);
    }

}
