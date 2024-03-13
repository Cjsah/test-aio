package net.cjsah.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();

        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);

        list = list.subList(0, 2);

        System.out.println(list);

//        Supplier<Integer> supplier = () -> Integer.valueOf(1);
//        fun1(supplier);


    }

    /**
     * 假如此时这个方法是由事件调用
     */

    private static void fun1(Supplier<Integer> supplier) {
        fun2(() -> supplier.get()); // 你的方式
        fun2(supplier);             // 我的方式
    }

    private static void fun2(Supplier<Integer> supplier) {

    }


}
