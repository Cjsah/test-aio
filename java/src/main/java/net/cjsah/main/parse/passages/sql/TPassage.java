package net.cjsah.main.parse.passages.sql;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
public class TPassage {

    public static void main(String[] args) {
//        try (SqlSession session = MybatisPlus.session.openSession(true)) {
//            Passage2AMapper tPsgMapper = session.getMapper(Passage2AMapper.class);
//            PassageWord passage = tPsgMapper.selectOne(new QueryWrapper<>() {{
//                this.last("limit 1");
//            }});
//            TPassage.parse(passage);
//        }
//        log.info("解析完成");
    }

    private static final Pattern pattern = Pattern.compile("^\\d+\\..+");

//    public static void parse(PassageWord passage) {
//        String content = passage.getContent();
//        String questions = passage.getQuestions();
//        String answers = passage.getAnswers();
//
//        List<String> questionList = JsonUtil.str2List(questions, String.class);
//
//        content = content.trim().replaceAll("(\\(\\d+\\)|【\\d+】)", "");
//
//        List<Question> resultQuestions = new ArrayList<>();
//        for (String node : questionList) {
//            String[] splits = node.split("\n");
//            Matcher matcher = pattern.matcher(splits[0].trim());
//            if (matcher.find()) {
//                String group = matcher.group();
//                String[] split = group.split("\\.", 2);
//                Question question = new Question(Integer.parseInt(split[0]), split[1]);
//                for (int i = 1; i < splits.length; i++) {
//                    question.options.add(splits[i].trim());
//                }
//                resultQuestions.add(question);
//            }
//        }
//        for (String answer : answers.split("\n")) {
//            answer = answer.trim();
//            Matcher matcher = pattern.matcher(answer);
//            if (matcher.find()) {
//                String[] split = matcher.group().split("\\.", 2);
//                int num = Integer.parseInt(split[0]);
//                String result = split[1].trim();
//                for (Question question : resultQuestions) {
//                    if (question.num == num) {
//                        question.answer = result;
//                        break;
//                    }
//                }
//            }
//
//        }
//
//        questions = JsonUtil.obj2Str(resultQuestions);
//
//        passage.setContent(content);
//        passage.setQuestions(questions);
//        passage.setAnswers("");
//    }

    @Data
    static class Question {
        public final int num;
        public final String question;
        public final List<String> options= new ArrayList<>();
        public String answer = "";
    }
}
