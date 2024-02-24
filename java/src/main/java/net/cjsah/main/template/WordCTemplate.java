package net.cjsah.main.template;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
public class WordCTemplate {
    private static final Pattern pattern = Pattern.compile("\\$\\{\\S+}");

    public static void main(String[] args) {
        WordCTemplate template = new WordCTemplate();
//        template.generate("study-all.docx");
        template.generate("study-answer.docx");
    }

    private void generate(String file) {
        String path = "./";

        try {

            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new File(path, file));

            MainDocumentPart document = wordMLPackage.getMainDocumentPart();

            List<Object> content = document.getContent();

//            writeContent("tip", content, 6, 8);
//            writeContent("word", content, 10, 17);
//            writeContent("article", content, 21, 28);
            writeContent("answer", content, 6, 10);

        }catch (Exception e) {
            log.error("Error", e);
        }
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static void writeContent(String name, List<Object> content, int from, int to) {
        final String path = "template.json";
        List<String> templates = new ArrayList<>();
        for (int i = from; i <= to; i++) {
            templates.add(XmlUtils.marshaltoString(content.get(i)));
        }

        JsonObject json;
        try (FileReader reader = new FileReader(path)) {
            json = GSON.fromJson(reader, JsonObject.class);
            json.add(name, GSON.toJsonTree(templates));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (FileWriter writer = new FileWriter(path)) {
            writer.write(GSON.toJson(json));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
