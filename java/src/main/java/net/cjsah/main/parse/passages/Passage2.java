package net.cjsah.main.parse.passages;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.sql.HikariSql;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        } catch (IOException e) {
            log.error("err", e);
        }

    }

    static final List<Passage> list = new ArrayList<>();

    private static Passage getOrCreate(int index) {
        for (Passage passage : list) {
            if (passage.index == index) return passage;
        }
        Passage passage = new Passage();
        list.add(passage);
        return passage;
    }

    public static boolean parse(XWPFDocument document, String filename) {
        stage = Stage.WAIT_START;
        difficulty = 0;
        answers = "";
        for (XWPFTable table : document.getTables()) {
            System.out.println(table);

        }
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (String s : paragraph.getParagraphText().split("\n")) {
                String trim = s.trim();
                if (trim.isEmpty()) continue;
                Line line = new Line(s, trim);
                System.out.println(line.origin);

//                passage.stage = passage.stage.next.apply(line, passage.stage);
//                if (passage.stage == Stage.COMPLETE) {
//                    passages.add(passage);
//                    passage = new Passage();
//                    passage.stage = Stage.COMPLETED;
//                }
//                passage.stage.parse.accept(passage, line);
            }
        }
        System.out.println(list);
        for (Passage psg : list) {
            HikariSql.insert(filename, psg.id, psg.wordCount, difficulty, psg.content, psg.questions, answers, psg.translate);
            return true;
        }
        return false;
    }

    @Data
    static class Passage {
        int index;
        int id;
        int wordCount;
        String content = "";
        String questions = "";
        String words = "";
        String translate = "";

        Stage stage = Stage.WAIT_START;
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

    private static Stage parseFirstStage(Line line, Stage now) {
        if (line.trim.startsWith("答案")) {
            return Stage.ANSWER;
        } else if (now == Stage.COMPLETED && line.trim.startsWith("Passage")) {
            return Stage.CONTENT;
        } else {
            return now;
        }
    }

    @RequiredArgsConstructor
    enum Stage {
        COMPLETED(Passage2::parseFirstStage, line -> {}),

        TRANSLATE((next, now) -> now, line -> {
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

        ANSWER((next, now) -> next.trim.startsWith("答案和解析") ? TRANSLATE: now, line -> answers += line.origin + "\n"),

        CONTENT((next, now) -> next.trim.startsWith("答案和解析") ? ANSWER: now, line -> {
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
