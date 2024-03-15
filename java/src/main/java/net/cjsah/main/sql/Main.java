package net.cjsah.main.sql;

import cn.hutool.core.io.FileUtil;
import com.google.gson.JsonArray;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.data.IdData;
import net.cjsah.sql.MybatisPlus;
import net.cjsah.sql.mapper.QuestionMapper;
import net.cjsah.sql.pojo.Question;
import net.cjsah.util.JsonUtil;
import org.apache.ibatis.session.SqlSession;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
public class Main {
    public static void main(String[] args) {
//        try (SqlSession session = MybatisPlus.session.openSession(true)) {
//            ArticleMapper mapper = session.getMapper(ArticleMapper.class);
//
//            log.info("读取文章列表中...");
//            List<Article> questions = mapper.selectList(new QueryWrapper<>() {{
//                this.eq("lexicon_id", 139);
//            }});
//
//            log.info("序列化文章列表中...");
//            String s = JsonUtil.obj2Str(questions);
//
//            log.info("保存文章列表中...");
//            FileUtil.writeUtf8String(s, new File("./test.json"));
//
//            log.info("完成");
//        }

//        File desc = new File("./test.json");
//        File out = new File("./test1.json");
//
//        String str = FileUtil.readUtf8String(desc);
//        List<Article> questions = JsonUtil.str2List(str, Article.class);
//        Map<String, List<Article>> collect = questions.stream().parallel().collect(Collectors.groupingBy(Main::merge));
//
//        List<List<Article>> list = collect.entrySet().stream().parallel().map(Map.Entry::getValue).filter(it -> it.size() > 1).toList();
//
//        str = JsonUtil.obj2PrettyStr(list);
//        FileUtil.writeUtf8String(str, out);

//        File desc = new File("./test1.json");
//        File out = new File("./test2.json");
//
//        String str = FileUtil.readUtf8String(desc);
//        List<JSONArray> jsonArrays = JsonUtil.str2List(str, JSONArray.class);
//        List<List<IdData>> questions = jsonArrays.stream().parallel()
//                .map(it -> it.toList(Article.class))
//                .sorted(comparingInt(List::size))
//                .map(it -> it.stream().parallel()
//                        .map(node -> new IdData(node.getId(), node.getQuestionId()))
//                        .toList())
//                .toList();
//
//        str = JsonUtil.obj2PrettyStr(questions);
//        FileUtil.writeUtf8String(str, out);

        File desc = new File("./test2.json");
        File out = new File("./test3.json");

        int [] lexicons = {76, 82, 120, 130, 139, 146};
        String str = FileUtil.readUtf8String(desc);
        JsonArray jsonArrays = JsonUtil.str2ObjGson(str, JsonArray.class);
        jsonArrays.remove(0);
        List<List<IdData>> questions = jsonArrays.asList().stream().parallel()
                .map(it -> (it.getAsJsonArray().asList()).stream().parallel()
                        .map(node -> JsonUtil.obj2Bean(node.getAsJsonObject(), IdData.class))
                        .toList())
                .toList();

        int size = questions.size();

        try (SqlSession session = MybatisPlus.session.openSession(true)) {
            QuestionMapper mapper = session.getMapper(QuestionMapper.class);
            List<Long> needDeletes = new ArrayList<>();

            for (int i = 0; i < questions.size(); i++) {
                log.info("进度: {}/{}", i + 1, size);
                List<IdData> question = questions.get(i);
                List<Long> qids = question.stream().parallel().map(IdData::getQid).toList();
                log.info("共 {} 篇: {}", question.size(), qids);
                List<Question> list = mapper.selectBatchIds(qids);

//                list.stream().parallel().map(it -> it.getId() + " -> " + it.getParse().length()).forEach(System.out::println);

                Optional<Question> max = list.stream().parallel().max(Comparator.comparingInt(node -> node.getParse().length()));
                if (max.isPresent()) {
                    Question reserve = max.get();
                    list.remove(reserve);
                    List<Long> removes = list.stream().parallel().map(Question::getId).toList();
                    log.info("保留 {}, 删除 {}", reserve.getId(), removes);
                    needDeletes.addAll(removes);
                }
            }

            log.info("全部处理完成, 将要删除 {} 篇: {}", needDeletes.size(), needDeletes);

            str = JsonUtil.obj2PrettyStr(needDeletes);
            FileUtil.writeUtf8String(str, out);

        }








//        int sum = questions.stream().parallel().mapToInt(List::size).sum();

//        int rows = questions.size() - 1;
//
//        try (HSSFWorkbook workbook = new HSSFWorkbook()) {
//            HSSFSheet sheet = workbook.createSheet();
//            workbook.setSheetName(0, "共 " + rows + " 组(行)");
//            for (int i = 0; i < rows; i++) {
//                HSSFRow row = sheet.createRow(i);
//                List<IdData> col = questions.get(i + 1);
//                HSSFCell cell = row.createCell(0);
//                cell.setCellValue("数量: " + col.size());
//                for (int j = 0; j < col.size(); j++) {
//                    IdData data = col.get(j);
//                    cell = row.createCell(j + 1);
//                    cell.setCellValue(data.getQid());
//                }
//            }
//
//            List<IdData> values = questions.get(0);
//            sheet = workbook.createSheet();
//            workbook.setSheetName(1, "1组共 " + values.size() + " 个");
//
//            int rowIndex = 0;
//            int colIndex = 0;
//            HSSFRow row = sheet.createRow(rowIndex++);
//            for (IdData data : values) {
//                HSSFCell cell = row.createCell(colIndex++);
//                cell.setCellValue(data.getQid());
//                if (colIndex == 20) {
//                    row = sheet.createRow(rowIndex++);
//                    colIndex = 0;
//                }
//            }
//
//            workbook.write(out);
//        } catch (IOException e) {
//            log.error("Error", e);
//        }


    }


//    private static <T> Comparator<T> comparingInt(ToIntFunction<? super T> keyExtractor) {
//        Objects.requireNonNull(keyExtractor);
//        return (Comparator<T> & Serializable)
//                (c1, c2) -> Integer.compare(keyExtractor.applyAsInt(c2), keyExtractor.applyAsInt(c1));
//    }
//
//    private static String merge(Article article) {
//        return merge(
//                article.getLevel1Word(),
//                article.getLevel1WordNumber(),
//                article.getLevel2Word(),
//                article.getLevel2WordNumber(),
//                article.getLevel3Word(),
//                article.getLevel3WordNumber(),
//                article.getLevel4Word(),
//                article.getLevel4WordNumber(),
//                article.getLevel5Word(),
//                article.getLevel5WordNumber(),
//                article.getLevel6Word(),
//                article.getLevel6WordNumber(),
//                article.getLevel7Word(),
//                article.getLevel7WordNumber(),
//                article.getLevel8Word(),
//                article.getLevel8WordNumber(),
//                article.getLevel9Word(),
//                article.getLevel9WordNumber(),
//                article.getLevel10Word(),
//                article.getLevel10WordNumber(),
//                article.getLevel11Word(),
//                article.getLevel11WordNumber(),
//                article.getLevel12Word(),
//                article.getLevel12WordNumber(),
//                article.getLevel13Word(),
//                article.getLevel13WordNumber(),
//                article.getLevel14Word(),
//                article.getLevel14WordNumber(),
//                article.getLevel15Word(),
//                article.getLevel15WordNumber(),
//                article.getLevel16Word(),
//                article.getLevel16WordNumber(),
//                article.getLevel17Word(),
//                article.getLevel17WordNumber(),
//                article.getLevel18Word(),
//                article.getLevel18WordNumber(),
//                article.getLevel19Word(),
//                article.getLevel19WordNumber(),
//                article.getLevel20Word(),
//                article.getLevel20WordNumber()
//        );
//    }
//
//    private static String merge(Object... values) {
//        StringBuilder builder = new StringBuilder();
//        for (Object value : values) {
//            builder.append(value);
//            builder.append('|');
//        }
//        builder.setLength(builder.length() - 1);
//        return builder.toString();
//    }


}
