package net.cjsah.main.sql;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class FileMain {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            System.out.print("> ");
            input = scanner.nextLine();
            if ("exit".equals(input)) break;

            List<String> nodes = Arrays.asList(input.split(" "));
            if (nodes.isEmpty()) continue;

            switch (nodes.get(0)) {
                case "create" -> {
                    String path = getOrThrow(nodes, 1);
                    if (path == null) {
                        log.warn("参数不全");
                    } else {
                        create(path);
                    }
                }
                case "update" -> {
                    String name = getOrThrow(nodes, 1);
                    String path = getOrThrow(nodes, 2);
                    if (name == null || path == null) {
                        log.warn("参数不全");
                    } else {
                        update(name, path);
                    }
                }
                case "remove" -> {
                    String id = getOrThrow(nodes, 1);
                    if (id == null) {
                        log.warn("参数不全");
                    } else {
                        delete(Long.parseLong(id));
                    }
                }
                case "query" -> {
                    String param = getOrThrow(nodes, 1);
                    if (param == null) {
                        log.warn("参数不全");
                    } else {
                        List<KV> params = Arrays.stream(param.split(",")).map(it -> {
                            String[] kv = it.split("=");
                            if (kv.length != 2) return null;
                            return new KV(kv[0], kv[1]);
                        }).toList();
                        if (params.contains(null)) {
                            log.warn("参数有误");
                        } else {
                            retrieve(params);
                        }
                    }
                }
            }
        }


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

    }

    private static String getOrThrow(List<String> nodes, int index) {
        if (index >= 0 && nodes.size() > index) return nodes.get(index);
        else return null;
    }

    private static void create(String path) {
        System.out.println("正在读取文件信息: path");
    }

    private static void update(String name, String path) {

    }

    private static void delete(long id) {

    }

    private static void retrieve(List<KV> params) {

    }

    @Data
    static class KV {
        private final String key;
        private final String value;
    }

}
