package net.cjsah.main.parse;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.cjsah.sql.HikariSql;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Passages {
    public static boolean passage(List<XWPFParagraph> paragraphs, String filename) {
        List<Passage> passages = new ArrayList<>();
        Passage passage = new Passage();
        for (XWPFParagraph paragraph : paragraphs) {
            for (String s : paragraph.getParagraphText().split("\n")) {
                String trim = s.trim();
                if (trim.isEmpty()) continue;
                Line line = new Line(s, trim);
                passage.stage = passage.stage.next.apply(line, passage.stage);
                if (passage.stage == Stage.COMPLETE) {
                    passages.add(passage);
                    passage = new Passage();
                    passage.stage = Stage.COMPLETED;
                }
                passage.stage.parse.accept(passage, line);
            }
        }
        for (Passage psg : passages) {
            HikariSql.insert(filename, psg.id, psg.wordCount, psg.content, psg.questions, Passage.answers);
            return true;
        }
        return false;
    }

    @Data
    static class Passage {
        int id;
        int wordCount;
        String content = "";
        String questions = "";
        String words = "";
        static String answers = "";

        Stage stage = Stage.WAIT_START;
    }

    @Data
    static class Line {
        final String origin;
        final String trim;
    }

    static final Pattern COUNT_PATTERN = Pattern.compile("单词数：\\d+");
    static final Pattern ID_PATTERN = Pattern.compile("编号：\\d+");

    private static Stage parseFirstStage(Line line, Stage now) {
        if (line.trim.startsWith("答案")) {
            return Stage.ANSWER;
        } else if (now == Stage.COMPLETED && line.trim.startsWith("Passage")) {
            return Stage.MSG;
        } else {
            return now;
        }
    }

    @RequiredArgsConstructor
    enum Stage {
        COMPLETED(Passages::parseFirstStage, (passage, line) -> {}),

        COMPLETE((next, now) -> COMPLETED, (passage, line) -> {}),

        ANSWER((next, now) -> now, (passage, line) -> Passage.answers += line.origin + "\n"),

        QUESTION((next, now) -> next.trim.startsWith("完成时间") ? COMPLETE : now, (passage, line) -> passage.questions += line.origin + "\n"),

        WORD((next, now) -> next.trim.startsWith("郑重提示") ? QUESTION : now, (passage, line) -> passage.words += line.origin + "\n"),

        CONTENT((next, now) -> next.trim.startsWith("其他单词释义") ? WORD : now, (passage, line) -> passage.content += line.origin + "\n"),

        MSG((next, now) -> next.trim.startsWith("开始时间") ? CONTENT: now, (passage, line) -> {
            Matcher matcher = COUNT_PATTERN.matcher(line.trim);
            if (matcher.find()) {
                passage.wordCount = Integer.parseInt(matcher.group().split("：")[1]);
            }
            matcher = ID_PATTERN.matcher(line.trim);
            if (matcher.find()) {
                passage.id = Integer.parseInt(matcher.group().split("：")[1]);
            }
        }),

        WAIT_START((next, now) -> next.trim.startsWith("Passage") ? MSG : now, (passage, line) -> {});

        final BiFunction<Line, Stage, Stage> next;
        final BiConsumer<Passage, Line> parse;
    }

}
