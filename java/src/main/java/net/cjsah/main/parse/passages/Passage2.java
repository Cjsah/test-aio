package net.cjsah.main.parse.passages;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.sql.HikariSql;
import net.cjsah.util.JsonUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Passage2 {
    static Stage stage = Stage.WAIT_START;
    static int difficulty = 0;
    static String answers = "";
    static Passage nowPassage = null;

    public static void main(String[] args) {
        File file = new File("passage/test2.docx");
        try (FileInputStream fis = new FileInputStream(file); XWPFDocument document = new XWPFDocument(fis)) {
            Passage2.parse(document, file.getPath());
        } catch (Exception e) {
            log.error("err", e);
        }

    }

    static final List<Passage> list = new ArrayList<>();

    private static Passage getOrCreate(int index) {
        for (Passage passage : list) {
            if (passage.index == index) return passage;
        }
        Passage passage = new Passage();
        passage.index = index;
        list.add(passage);
        return passage;
    }

    public static boolean parse(XWPFDocument document, String filename) throws Exception {
        stage = Stage.WAIT_START;
        difficulty = 0;
        answers = "";
        list.clear();
        nowPassage = null;
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (String s : paragraph.getParagraphText().split("\n")) {
                String trim = s.trim();
                if (trim.isEmpty()) continue;
                Line line = new Line(s, trim);
                stage = stage.next.apply(line, stage);
                stage.parse.accept(line);
            }
        }
        List<XWPFTable> tables = document.getTables();
        parseTable(getOrCreate(1), tables.get(0));
        parseTable(getOrCreate(2), tables.get(1));
        if (list.isEmpty()) {
            throw new Exception("Empty Passage");
        }
        for (Passage psg : list) {
            HikariSql.insert(filename, psg.id, psg.wordCount, difficulty, psg.content, JsonUtil.obj2Str(psg.questions), answers, psg.translate, JsonUtil.obj2Str(psg.words));
        }
        return true;
    }

    private static void parseTable(Passage passage, XWPFTable table) {
        XWPFTableRow row = table.getRow(0);
        XWPFTableCell content = row.getCell(0);
        XWPFTableCell words = row.getCell(1);

        TableStage stage = TableStage.CONTENT;

        for (XWPFParagraph paragraph : content.getParagraphs()) {
            String text = paragraph.getParagraphText();
            String trim = text.trim();
            Line line = new Line(text, trim);
            stage = stage.next.apply(line, stage);
            stage.parse.accept(passage, line);
        }
        for (XWPFParagraph paragraph : words.getParagraphs()) {
            passage.words.add(paragraph.getParagraphText());
        }
    }

    @RequiredArgsConstructor
    enum TableStage {

        QUESTION((next, now) -> now, (passage, line) -> passage.questions.add(line.origin)),

        CONTENT((next, now) -> next.trim.startsWith("郑重提示") ? QUESTION: now, (passage, line) -> passage.content += line.origin + "\n");

        final BiFunction<Line, TableStage, TableStage> next;
        final BiConsumer<Passage, Line> parse;
    }


    @Data
    static class Passage {
        int index;
        int id;
        int wordCount;
        String content = "";
        List<String> questions = new ArrayList<>();
        List<String> words = new ArrayList<>();
        String translate = "";
    }

    @Data
    static class Line {
        final String origin;
        final String trim;
    }

    static final Pattern PASSAGE_PATTERN = Pattern.compile("Passage \\d");
    static final Pattern COUNT_PATTERN = Pattern.compile("篇幅（含问题选项）：\\d+");
    static final Pattern ID_PATTERN = Pattern.compile("本篇编号：\\d+");
    static final Pattern DIFFICULTY_PATTERN = Pattern.compile("本次文章词汇难度值\\d+");

    @RequiredArgsConstructor
    enum Stage {
        COMPLETED((next, now) -> now, line -> {}),

        TRANSLATE((next, now) -> next.trim.contains("分层周计划训练") ? COMPLETED : now, line -> {
            if (line.trim.startsWith("Passage ")) {
                Matcher matcher = PASSAGE_PATTERN.matcher(line.trim);
                if (matcher.find()) {
                    int index = Integer.parseInt(matcher.group().split(" ")[1]);
                    nowPassage = getOrCreate(index);
                }
            } else if (nowPassage != null) {
                nowPassage.translate += line.origin + "\n";
            }

        }),

        WAIT_TRANSLATE((next, now) -> next.trim.startsWith("全文参考译文") ? TRANSLATE: now, line -> {}),
        ANSWER((next, now) -> next.trim.startsWith("难句翻译参考译文") ? WAIT_TRANSLATE: now, line -> answers += line.origin + "\n"),

        WAIT_ANSWER((next, now) -> next.trim.startsWith("答案和解析") ? ANSWER: now, line -> {}),

        CONTENT((next, now) -> next.trim.startsWith("请翻译并熟读下列句子") ? WAIT_ANSWER: now, line -> {
            if (line.trim.startsWith("Passage ")) {
                Matcher matcher = PASSAGE_PATTERN.matcher(line.trim);
                if (matcher.find()) {
                    int index = Integer.parseInt(matcher.group().split(" ")[1]);
                    Passage passage = getOrCreate(index);
                    matcher = COUNT_PATTERN.matcher(line.trim);
                    if (matcher.find()) {
                        passage.wordCount = Integer.parseInt(matcher.group().split("：")[1]);
                    }
                    matcher = ID_PATTERN.matcher(line.trim);
                    if (matcher.find()) {
                        passage.id = Integer.parseInt(matcher.group().split("：")[1]);
                    }

                }
            }
        }),

        WAIT_PASSAGE((next, now) -> next.trim.startsWith("阅读真题强化") ? CONTENT : now, line -> {}),

        WAIT_START((next, now) -> now, line -> {
            Matcher matcher = DIFFICULTY_PATTERN.matcher(line.trim);
            if (matcher.find()) {
                difficulty = Integer.parseInt(matcher.group().substring(9));
                stage = WAIT_PASSAGE;
            }
        });

        final BiFunction<Line, Stage, Stage> next;
        final Consumer<Line> parse;
    }

}
