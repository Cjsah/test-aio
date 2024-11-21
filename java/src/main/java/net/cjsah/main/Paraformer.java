package net.cjsah.main;

import com.alibaba.dashscope.audio.asr.transcription.Transcription;
import com.alibaba.dashscope.audio.asr.transcription.TranscriptionParam;
import com.alibaba.dashscope.audio.asr.transcription.TranscriptionQueryParam;
import com.alibaba.dashscope.audio.asr.transcription.TranscriptionResult;
import com.alibaba.dashscope.audio.asr.transcription.TranscriptionTaskResult;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

@Slf4j
public class Paraformer {
    public static void main(String[] args) {
        TranscriptionParam param = TranscriptionParam.builder()
                .apiKey("sk-91889b0f1585465993fd189e8e48c430")
                .model("paraformer-v2")
                .fileUrls(Collections.singletonList("https://dev.shuhai777.com:1111/api/resource/261ff24c629246f28d2937cce4b34257"))
                .parameter("language_hints", new String[]{"en"})
                .build();
        try {
            Transcription transcription = new Transcription();
            // 提交转写请求
            TranscriptionResult result = transcription.asyncCall(param);
            // 打印TaskId
            System.out.println("TaskId: " + result.getTaskId());
            // 等待转写完成
            result = transcription.wait(
                            TranscriptionQueryParam.FromTranscriptionParam(param, result.getTaskId()));
            String status = result.getTaskStatus().getValue();
            if ("FAILED".equals(status)) {
                String code = result.getOutput().get("code").getAsString();
                String msg = result.getOutput().get("message").getAsString();
                System.out.println(code + " -> " + msg);
                return;
            }


            // 获取转写结果
            List<TranscriptionTaskResult> taskResultList = result.getResults();
            if (taskResultList != null && !taskResultList.isEmpty()) {
                TranscriptionTaskResult taskResult = taskResultList.get(0);
                // 获取转写结果的url
                String transcriptionUrl = taskResult.getTranscriptionUrl();
                System.out.println(transcriptionUrl);
                if (null == transcriptionUrl) {
                    System.out.println("transcriptionUrl is null");
                    return;
                }
                // 通过Http获取url内对应的结果
                HttpURLConnection connection = (HttpURLConnection) new URL(transcriptionUrl).openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                // 格式化输出json结果
                JSONObject json = JSON.parseObject(reader, JSONObject.class);
                JSONArray transcripts = json.getJSONArray("transcripts");
                for (int channelId = 0; channelId < transcripts.size(); channelId++) {
                    JSONObject channel = transcripts.getJSONObject(channelId);
                    System.out.println(channelId + "->" + channel.getString("text"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
