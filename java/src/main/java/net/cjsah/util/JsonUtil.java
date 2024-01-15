package net.cjsah.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class JsonUtil {
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    public static String obj2Str(Object obj) {
        return GSON.toJson(obj);
    }

    public static <T> T str2Obj(String json, Class<T> clazz) {
        return JSON.parseObject(json, new TypeReference<T>() {});
    }

    public static <T> T str2ObjOld(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    public static <T> List<T> str2List(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    public static <T> T jsonToObj(JSONObject json, Class<T> clazz) {
        return json.to(new TypeReference<T>() {});
    }
    public static <T> T jsonToObjOld(JSONObject json, Class<T> clazz) {
        return json.to(clazz);
    }

    public static <T> T defaultData(T data, String str, Class<T> trans, Consumer<T> setter) {
        if (null == data) {
            data = JsonUtil.str2Obj(str, trans);
            setter.accept(data);
        }
        return data;
    }

    public static <T> List<T> jsonGetList(JSONObject json, String key, Function<JSONObject, T> function) {
        return ListUtil.map(json.getJSONArray(key), it -> function.apply((JSONObject) it));
    }

}
