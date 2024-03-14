package net.cjsah.main.parse.passages;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.sql.HikariSql;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Passage2Word {
    final List<Passage> passages = new ArrayList<>();
    Passage current = null;
    Stage stage = Stage.WAIT_START;
    int wordCount = 0;
    int difficulty = 0;

    public static void main(String[] args) {
        File file = new File("passage/test2.docx");
        try (FileInputStream fis = new FileInputStream(file); XWPFDocument document = new XWPFDocument(fis)) {
            new Passage2Word().parse(document, file.getPath());
        } catch (Exception e) {
            log.error("err", e);
        }

    }
    public boolean parse(XWPFDocument document, String filename) throws Exception {
        this.current = new Passage();
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (String s : paragraph.getParagraphText().split("\n")) {
                String trim = s.trim();
                if (trim.isEmpty()) continue;
                Line line = new Line(s, trim);
                stage = stage.next.apply(line, stage);
                if (stage == Stage.COMPLETE) {
                    passages.add(current);
                    current = new Passage();
                    stage = Stage.COMPLETED;
                }
                stage.parse.accept(this, line);
            }
        }

        if (passages.isEmpty()) {
            throw new Exception("Empty Passage");
        }
        System.out.println(wordCount);
        System.out.println(passages);
        for (Passage psg : passages) {
//            if (!HikariSql.update(psg.id, wordCount, difficulty)) {
//                log.error("更新失败, {}不存在", psg.id);
//                return false;
//            }
        }
        return true;
    }

    @Data
    static class Passage {
        int id;
    }

    @Data
    static class Line {
        final String origin;
        final String trim;
    }

    static final Pattern ID_PATTERN = Pattern.compile("本篇编号：\\d+");
    static final Pattern WORD_COUNT_PATTERN = Pattern.compile("你当前词汇量约为\\d+");
    static final Pattern DIFFICULTY_PATTERN = Pattern.compile("本次文章词汇难度值\\d+");

    private static Stage parseFirstStage(Line line, Stage now) {
        if (now == Stage.COMPLETED && line.trim.startsWith("Passage")) {
            return Stage.MSG;
        } else {
            return now;
        }
    }

    @RequiredArgsConstructor
    enum Stage {
        COMPLETED(Passage2Word::parseFirstStage, (passage, line) -> {}),

        COMPLETE((next, now) -> COMPLETED, (passage, line) -> {}),

        MSG((next, now) -> next.trim.startsWith("完成时间") ? COMPLETE : now, (passage, line) -> {
            Matcher matcher = ID_PATTERN.matcher(line.trim);
            if (matcher.find()) {
                passage.current.id = Integer.parseInt(matcher.group().split("：")[1]);
            }
        }),

        WAIT_PASSAGE((next, now) -> next.trim.startsWith("Passage") ? MSG : now, (passage, line) -> {}),

        WAIT_START((next, now) -> now, (passage, line) -> {
            Matcher matcher = WORD_COUNT_PATTERN.matcher(line.trim);
            if (matcher.find()) {
                passage.wordCount = Integer.parseInt(matcher.group().substring(8));
            }
            matcher = DIFFICULTY_PATTERN.matcher(line.trim);
            if (matcher.find()) {
                passage.difficulty = Integer.parseInt(matcher.group().substring(9));
                passage.stage = WAIT_PASSAGE;
            }
        });

        final BiFunction<Line, Stage, Stage> next;
        final BiConsumer<Passage2Word, Line> parse;
    }

}
