package net.cjsah.main.vosk;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSONObject;
import net.cjsah.util.JsonUtil;
import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Vosk {
    private static final String model_small = "./vosk-model-small-en-us-0.15";
    private static final String model_all = "./vosk-model-small-en-us-0.15";
    private static final String model_lgraph = "./vosk-model-small-en-us-0.15";
    private static final String model_gigaspeech = "./vosk-model-small-en-us-0.15";
    private static final String input = "./清晰.wav";

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {
        LibVosk.setLogLevel(LogLevel.DEBUG);
        try (
                Model model = new Model(model_small);
                Recognizer recognizer = new Recognizer(model, 32000)
        ) {
            byte[] bytes = FileUtil.readBytes(new File(input));
            if (recognizer.acceptWaveForm(bytes, bytes.length)) {
                // 返回语音识别结果
                JSONObject json = JsonUtil.str2Json(recognizer.getResult());
                System.out.println("text: " + json.getString("text"));
            } else {
                JSONObject json = JsonUtil.str2Json(recognizer.getPartialResult());
                System.out.println("partial: " + json.getString("partial"));
            }
            String finalResult = recognizer.getFinalResult();
            System.out.println(finalResult);
            JSONObject json = JsonUtil.str2Json(finalResult);
            System.out.println("result: " + json.getString("text"));
        }
    }
}
