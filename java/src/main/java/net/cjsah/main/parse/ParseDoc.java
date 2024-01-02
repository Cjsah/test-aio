package net.cjsah.main.parse;

import cn.hutool.core.io.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;

@Slf4j
public class ParseDoc {
    public static void main(String[] args) {
        File file = new File("/Volumes/数据");

        try (FileOutputStream fos = new FileOutputStream("./file.txt")) {
            travel(file, fos);
        } catch (IOException e) {
            log.error("Err", e);
        }
    }

    static final Pattern PATTERN = Pattern.compile("[.$].*");

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void travel(File directory, OutputStream os) throws IOException {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                if (file.isDirectory() && !PATTERN.matcher(name).find()) {
                    if (!PATTERN.matcher(name).find()) {
                        travel(file, os);
                    }
                } else if (name.endsWith(".docx")){







                    ParseType parseType = ParseType.NONE;
                    for (ParseType type : ParseType.values()) {
                        if (type.predicate.test(name)) {
                            parseType = type;
                        }
                    }

                    System.out.println(parseType);

//                    try (FileInputStream fis = new FileInputStream(file)) {
//
//                        XWPFDocument document = new XWPFDocument(fis);
//                        boolean bl = parseType.parser.run(document.getParagraphs(), file.getPath());
//                        if (bl) {
//                            File backup = new File("backup/" + file.getPath());
//                            File parent = backup.getParentFile();
//                            if (!parent.exists()) {
//                                parent.mkdirs();
//                            }
//                            FileUtil.copy(file, backup, true);
//                        }
//                    } catch (Exception e) {
//                        log.error("Error:" + file.getPath(), e);
//                    }

                }
            }
        }
    }

    @RequiredArgsConstructor
    enum ParseType {
        NONE(name -> false, (list, file) -> false),
        A(name -> false, (list, file) -> false);

        final Predicate<String> predicate;
        final BiFunction parser;

    }

    @FunctionalInterface
    public interface BiFunction {
        boolean run(List<XWPFParagraph> paragraphs, String filename) throws Exception;

    }

}
