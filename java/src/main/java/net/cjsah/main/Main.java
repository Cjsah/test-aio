package net.cjsah.main;

import antlr.StringUtils;
import cn.hutool.core.io.FileUtil;
import net.cjsah.data.StrategyData;
import net.cjsah.util.JsonUtil;
import net.cjsah.util.QuestionUtil;

import java.io.File;
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

        String str = FileUtil.readUtf8String(new File("./test.json"));

        List<StrategyData> list = JsonUtil.str2List(str, StrategyData.class);

        System.out.println(list);

        System.out.println("===");

        System.out.println(QuestionUtil.list2TreeNoMerge(list, (data, json) -> {
            json.put("hash", data.hashCode());
        }));

        System.out.println("===");

        System.out.println(QuestionUtil.list2Tree(list, (data, json) -> {
            json.put("hash", data.hashCode());
        }));

    }


}
