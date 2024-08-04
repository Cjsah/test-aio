package net.cjsah.test;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import lombok.SneakyThrows;

import java.io.File;

public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        File input = new File("result.docx");
        File output = new File("result.pdf");
        Document doc = new Document(input.getAbsolutePath());
        doc.save(output.getAbsolutePath(), SaveFormat.PDF);
    }
}