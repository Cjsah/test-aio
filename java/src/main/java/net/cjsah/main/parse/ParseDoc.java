package net.cjsah.main.parse;

import cn.hutool.core.io.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.main.parse.passages.Passage2;
import net.cjsah.util.StringUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ParseDoc {
    static int totalCount = 0;
    static int completeCount = 0;

    public static void main(String[] args) {
        File file = new File("分层周计划训练");

        try {
            travel(file);
        } catch (IOException e) {
            log.error("Err", e);
        }

        log.info("总数: {}, 成功: {}", totalCount, completeCount);

    }

    static final Pattern FOLDER_REGEX = Pattern.compile("^[.$].*");
    static final Pattern DOC_REGEX = Pattern.compile("\\d{5,20}-.*\\.docx");

    private static void travel(File directory) throws IOException {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                if (file.isDirectory()) {
                    if (!FOLDER_REGEX.matcher(name).find()) {
                        travel(file);
                    }
                } else if (name.startsWith("._") || name.startsWith("~$")) {
                    del(file);
                } else if (name.endsWith(".docx")) {
                    Matcher matcher = DOC_REGEX.matcher(name);
                    if (matcher.find()) {
                        totalCount++;
                        String match = matcher.group().split("-", 2)[1];
                        match = match.substring(0, match.length() - 5);
                        if (!StringUtil.contains(match, "完形", "分层周计划训练", "七选五")) {
                            System.out.println(file.getPath());
                        }
                        ParseType parseType = ParseType.NONE;
                        for (ParseType type : ParseType.values()) {
                            if (type.predicate.test(match)) {
                                parseType = type;
                                break;
                            }
                        }
                        boolean bl = false;
                        try (FileInputStream fis = new FileInputStream(file)) {

                            XWPFDocument document = new XWPFDocument(fis);
                            bl = parseType.parser.run(document, file.getPath());
                            if (!bl) {
                                log.error("[{}][{}]解析失败...", file.getPath(), parseType);
                            }
                        } catch (Exception e) {
                            log.error(file.getPath() + "解析失败...", e);
                        }
                        if (bl) {
                            completeCount++;
                            log.info("[{}]解析完成, 正在删除...", file.getPath());
                            del(file);
                        }
                    } else {
                        log.warn("未知的文档类型: {}", file.getPath());
                    }

                } else {
                    log.warn("[{}]跳过解析...", file.getPath());
                    del(file);
                }
            }
        }
    }

    private static void del(File file) {
        if (file.exists()) {
            FileUtil.del(file);
            File parent = file.getParentFile();
            File[] files = parent.listFiles();
            if (files == null || files.length == 0) {
                FileUtil.del(parent);
            }
        }
    }

    @RequiredArgsConstructor
    enum ParseType {
        NONE(name -> false, (list, file) -> false),
        P2(name -> StringUtil.starts(name, "分层周计划训练"), Passage2::parse),
        P3(name -> StringUtil.contains(name, "七选五"), (doc, file) -> false/*Passage3::parse*/),
        W1(name -> StringUtil.contains(name, "完形填空"), (doc, file) -> false);

//        P1(name -> StringUtil.contains(name, "阅读"), (doc, file) -> new Passage1().parse(doc, file));

        final Predicate<String> predicate;
        final BiFunction parser;

    }

    @FunctionalInterface
    public interface BiFunction {
        boolean run(XWPFDocument document, String filename) throws Exception;

    }

}
