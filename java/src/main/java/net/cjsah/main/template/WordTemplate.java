package net.cjsah.main.template;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.P;
import org.docx4j.wml.R;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class WordTemplate {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static void main(String[] args) {
        WordTemplate template = new WordTemplate();
//        template.generate("study-all.docx");
        template.generate("study-template.docx");
    }

    private void generate(String file) {
        String path = "./";

        try {

            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new File(path, file));

            MainDocumentPart document = wordMLPackage.getMainDocumentPart();

            List<Object> content = document.getContent();

            System.out.println(content);

            for (Object o : content) {
                String value = o.toString();
                System.out.println(value);
                if ()


            }


//            writeContent("tip", content, 6, 9);
//            writeContent("word", content, 10, 18);
//            writeContent("article", content, 20, 28);
//            writeContent("answer", content, 6, 9);




            try (FileOutputStream fos = new FileOutputStream(path + "result.docx")) {
                wordMLPackage.save(fos);
            }

        }catch (Exception e) {
            log.error("Error", e);
        }
    }

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
