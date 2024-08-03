package net.cjsah.main.template;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import lombok.SneakyThrows;

import java.io.File;

public class Word2Pdf {
    @SneakyThrows
    public static void main(String[] args) {
        File input = new File("result.docx");
        File output = new File("result.pdf");
        Document doc = new Document(input.getAbsolutePath());
        doc.save(output.getAbsolutePath(), SaveFormat.PDF);

    }
}
