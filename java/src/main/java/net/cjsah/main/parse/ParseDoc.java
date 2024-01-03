package net.cjsah.main.parse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.main.parse.passages.Passage1;
import net.cjsah.main.parse.passages.Passage2;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ParseDoc {
    public static void main(String[] args) {
        File file = new File("/Volumes/数据");

        try {
            travel(file);
        } catch (IOException e) {
            log.error("Err", e);
        }
    }

    static final Pattern FOLDER_REGEX = Pattern.compile("[.$].*");
    static final Pattern DOC_REGEX = Pattern.compile("\\d{5,20}-.*\\.docx");

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void travel(File directory) throws IOException {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                if (file.isDirectory()) {
                    if (!FOLDER_REGEX.matcher(name).find()) {
                        travel(file);
                    }
                } else if (name.endsWith(".docx")){
                    Matcher matcher = DOC_REGEX.matcher(name);
                    if (matcher.find()) {
                        String match = matcher.group().split("-", 2)[1];
                        match = match.substring(0, match.length() - 5);
                        ParseType parseType = ParseType.NONE;
                        for (ParseType type : ParseType.values()) {
                            if (type.predicate.test(match)) {
                                parseType = type;
                            }
                        }
                        if (parseType == ParseType.NONE) {
                            log.warn("[{}]未配置解析器", file.getPath());
                            throw new RuntimeException();
                        }
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


                    } else {
                        log.warn("未知的文档类型: {}", file.getPath());
                    }

                }
            }
        }
    }


    static final Pattern PASSAGE1_REGEX = Pattern.compile("入学摸底.*");
    static final Pattern NUMBER_REGEX = Pattern.compile("\\d+");
    static final Pattern PASSAGE9_REGEX = Pattern.compile("入学摸底.*");
    static final Pattern PASSAGE10_REGEX = Pattern.compile("入学摸底.*");
    static final Pattern PASSAGE11_REGEX = Pattern.compile("入学摸底.*");
    static final Pattern PASSAGE12_REGEX = Pattern.compile("入学摸底.*");
    static final Pattern PASSAGE13_REGEX = Pattern.compile("入学摸底.*");
    static final Pattern PASSAGE14_REGEX = Pattern.compile("入学摸底.*");
    static final Pattern PASSAGE15_REGEX = Pattern.compile("入学摸底.*");
    static final Pattern PASSAGE16_REGEX = Pattern.compile("入学摸底.*");
    static final Pattern PASSAGE17_REGEX = Pattern.compile("入学摸底.*");
    static final Pattern PASSAGE18_REGEX = Pattern.compile("入学摸底.*");
    static final Pattern PASSAGE19_REGEX = Pattern.compile("入学摸底.*");


    @RequiredArgsConstructor
    enum ParseType {
        NONE(name -> false, (list, file) -> false, false),
        PASS(name -> contains(name, "反馈表", "对照表", "筛查表", "作文") ||
                starts(name,
                        "词汇作业",
                        "书法训练",
                        "语感训练",
                        "词汇训练",
                        "题型特训",
                        "语法训练",
                        "句型转换",
                        "语法作业",
                        "高频生词训练",
                        "临考词汇突击",
                        "单元全词汇训练",
                        "选项答题表",
                        "专题特训-音标"
                ) ||
                match(name, PASSAGE1_REGEX, NUMBER_REGEX), (doc, file) -> false, false),
        P1(name -> contains(name, "阅读"), Passage1::parse, false),
        P2(name -> starts(name, "分层周计划训练"), Passage2::parse, false),
        W1(name -> contains(name, "完形填空"), (doc, file) -> false, false);

        final Predicate<String> predicate;
        final BiFunction parser;
        final boolean delete;

    }

    private static boolean match(String value, Pattern... patterns) {
        return Arrays.stream(patterns).parallel().anyMatch(it -> it.matcher(value).find());
    }

    private static boolean contains(String value, String... patterns) {
        return Arrays.stream(patterns).anyMatch(value::contains);
    }

    private static boolean starts(String value, String... patterns) {
        return Arrays.stream(patterns).anyMatch(value::startsWith);
    }

    @FunctionalInterface
    public interface BiFunction {
        boolean run(XWPFDocument document, String filename) throws Exception;

    }

}
