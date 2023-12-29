package net.cjsah.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RequestUtil {
    public static final String JSON = "application/json;charset=UTF-8";
    public static final String FORM = "application/x-www-form-urlencoded";

    public static <T> T post(String url, Map<String, String> header, Map<String, Object> params, String body, String contentType, Class<T> resultType) {
        HttpRequest request = HttpRequest.post(url)
                .header("Content-Type", contentType)
                .timeout(5000);
        if (null != header) request.headerMap(header, false);
        if (null != params) request.form(params);
        if (null != body) request.body(body);
        try (HttpResponse response = request.execute()) {
            String bodyStr = new String(response.bodyBytes(), StandardCharsets.UTF_8);
            return JsonUtil.str2Obj(bodyStr, resultType);
        }
    }

    public static <T> T get(String url, Map<String, String> header, Map<String, Object> params, String contentType, Class<T> resultType) {
        HttpRequest request = HttpRequest.get(url)
                .header("Content-Type", contentType)
                .timeout(5000);
        if (null != header) request.headerMap(header, false);
        if (null != params) request.form(params);
        try (HttpResponse response = request.execute()) {
            String bodyStr = new String(response.bodyBytes(), StandardCharsets.UTF_8);
            return JsonUtil.str2Obj(bodyStr, resultType);
        }
    }

}
