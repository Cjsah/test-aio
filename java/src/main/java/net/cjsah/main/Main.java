package net.cjsah.main;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.cjsah.util.JsonUtil;
import net.cjsah.util.ListUtil;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println(String.format("%03d", 1));
        System.out.println(String.format("%03d", 12));
        System.out.println(String.format("%03d", 123));
        System.out.println(String.format("%03d", 1234));


    }

    @Data
    @RequiredArgsConstructor
    static class IdCls {
        private static int index = 0;

        private final int id;

        public IdCls() {
            this.id = index++;
        }
    }

    @Data
    static class Obj {
        public final List<Test> array;
        public final Test obj;
        public final int v;
    }

    @Data
    static class Test {
        private final int t;
    }
}