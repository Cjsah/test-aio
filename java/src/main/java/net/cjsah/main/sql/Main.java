package net.cjsah.main.sql;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.sql.pojo.Article;
import net.cjsah.util.JsonUtil;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class Main {
    public static void main(String[] args) {
//        try (SqlSession session = MybatisPlus.session.openSession(true)) {
//            ArticleMapper mapper = session.getMapper(ArticleMapper.class);
//
//            log.info("读取文章列表中...");
//
//
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
//
//
//        }

//        File desc = new File("./test.json");
//        File out = new File("./test1.json");
//
//        String str = FileUtil.readUtf8String(desc);
//        List<Article> questions = JsonUtil.str2List(str, Article.class);
//        Map<String, List<Article>> collect = questions.stream().parallel().collect(Collectors.groupingBy(Main::merge));
//
//        List<List<Article>> list = collect.entrySet().stream().parallel().map(Map.Entry::getValue).toList();
//
//        str = JsonUtil.obj2PrettyStr(list);
//        FileUtil.writeUtf8String(str, out);

        File desc = new File("./test1.json");
        File out = new File("./test2.json");

        String str = FileUtil.readUtf8String(desc);
        List<JSONArray> jsonArrays = JsonUtil.str2List(str, JSONArray.class);
        List<List<Article>> questions = jsonArrays.stream().parallel().filter(it -> it.size() > 1).map(it -> it.toList(Article.class)).toList();

        str = JsonUtil.obj2PrettyStr(questions);
        FileUtil.writeUtf8String(str, out);


    }

    private static String merge(Article article) {
        return merge(
                article.getLevel1Word(),
                article.getLevel1WordNumber(),
                article.getLevel2Word(),
                article.getLevel2WordNumber(),
                article.getLevel3Word(),
                article.getLevel3WordNumber(),
                article.getLevel4Word(),
                article.getLevel4WordNumber(),
                article.getLevel5Word(),
                article.getLevel5WordNumber(),
                article.getLevel6Word(),
                article.getLevel6WordNumber(),
                article.getLevel7Word(),
                article.getLevel7WordNumber(),
                article.getLevel8Word(),
                article.getLevel8WordNumber(),
                article.getLevel9Word(),
                article.getLevel9WordNumber(),
                article.getLevel10Word(),
                article.getLevel10WordNumber(),
                article.getLevel11Word(),
                article.getLevel11WordNumber(),
                article.getLevel12Word(),
                article.getLevel12WordNumber(),
                article.getLevel13Word(),
                article.getLevel13WordNumber(),
                article.getLevel14Word(),
                article.getLevel14WordNumber(),
                article.getLevel15Word(),
                article.getLevel15WordNumber(),
                article.getLevel16Word(),
                article.getLevel16WordNumber(),
                article.getLevel17Word(),
                article.getLevel17WordNumber(),
                article.getLevel18Word(),
                article.getLevel18WordNumber(),
                article.getLevel19Word(),
                article.getLevel19WordNumber(),
                article.getLevel20Word(),
                article.getLevel20WordNumber()
        );
    }

    private static String merge(Object... values) {
        StringBuilder builder = new StringBuilder();
        for (Object value : values) {
            builder.append(value);
            builder.append('|');
        }
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }


}
