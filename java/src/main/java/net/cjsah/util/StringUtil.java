package net.cjsah.util;

import cn.hutool.core.stream.CollectorUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collector;

public class StringUtil {

    public static Collector<CharSequence, StringBuilder, String> join() {
        return new Collector<CharSequence, StringBuilder, String>() {
            @Override
            public Supplier<StringBuilder> supplier() {
                return () -> {
                    StringBuilder builder = new StringBuilder();
                    builder.append("[");
                    return builder;
                };
            }

            @Override
            public BiConsumer<StringBuilder, CharSequence> accumulator() {
                return (builder, value) -> {
                    builder.append(value);
                    builder.append(",");
                };
            }

            @Override
            public BinaryOperator<StringBuilder> combiner() {
                return (r1, r2) -> r1;
            }

            @Override
            public Function<StringBuilder, String> finisher() {
                return builder -> {
                    builder.setCharAt(builder.length() - 1, ']');
                    return builder.toString();
                };
            }

            @Override
            public Set<Characteristics> characteristics() {
                return CollectorUtil.CH_NOID;
            }
        };
    }

    private static final List<Byte[]> fuzzy = new ArrayList<>();

    static {
        fuzzy.add(new Byte[] {
                48,  // 0
                111  // o
        });
        fuzzy.add(new Byte[] {
                49,  // 1
                105, // i
                108  // l
        });
        fuzzy.add(new Byte[]{
                53, // 5
                115 // s
        });
        fuzzy.add(new Byte[]{
                50, // 2
                122 // z
        });
    }

    public static boolean fuzzyMatch(String str1, String str2) {
        int str1Len = str1.length();
        if (str1Len != str2.length()) return false;
        if (str1Len == 0) return true;
        byte[] str1Bytes = str1.toLowerCase().getBytes();
        byte[] str2Bytes = str2.toLowerCase().getBytes();
        match:
        for (int i = 0; i < str1Len; i++) {
            byte b1 = str1Bytes[i];
            byte b2 = str2Bytes[i];
            if (b1 == b2) continue;
            for (Byte[] bytes : fuzzy) {
                boolean m1 = false;
                boolean m2 = false;
                for (byte b : bytes) {
                    if (b == b1) m1 = true;
                    else if (b == b2) m2 = true;
                }
                if (m1 && m2) continue match;
            }
            return false;
        }
        return true;

    }

    public static boolean match(String value, Pattern... patterns) {
        return Arrays.stream(patterns).parallel().anyMatch(it -> it.matcher(value).find());
    }

    public static boolean contains(String value, String... patterns) {
        return Arrays.stream(patterns).anyMatch(value::contains);
    }

    public static boolean starts(String value, String... patterns) {
        return Arrays.stream(patterns).anyMatch(value::startsWith);
    }

}
