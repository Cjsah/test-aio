package net.cjsah.main;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSONObject;
import net.cjsah.util.JsonUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String text = FileUtil.readUtf8String(new File("test.json"));
        JSONObject json = JsonUtil.str2Obj(text, JSONObject.class);
        List<JSONObject> wordList = json.getList("middleWordList", JSONObject.class);
        List<String> words = wordList.stream().map(it -> it.getString("word")).toList();

        System.out.println(words);



    }



}
