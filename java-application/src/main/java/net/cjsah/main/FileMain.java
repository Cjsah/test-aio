package net.cjsah.main;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.cjsah.sql.MybatisPlus;
import net.cjsah.sql.mapper.VideoAudioMapper;
import net.cjsah.sql.mapper.VideoFileMapper;
import net.cjsah.sql.pojo.VideoAudio;
import net.cjsah.sql.pojo.VideoFile;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

@Slf4j
public class FileMain {

    private static final Random random = new Random();

    public static void main(String[] args) throws IOException {
        Runtime.getRuntime().exec("powershell -Command cls");

//        System.out.print("\033[H\033[2J");
//        System.out.flush();

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
                    String id = getOrThrow(nodes, 1);
                    String path = getOrThrow(nodes, 2);
                    if (id == null || path == null) {
                        log.warn("参数不全");
                    } else {
                        update(Long.parseLong(id), path);
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
                        retrieve(Collections.emptyList());
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

    }

    private static String getOrThrow(List<String> nodes, int index) {
        if (index >= 0 && nodes.size() > index) return nodes.get(index);
        else return null;
    }

    private static void create(String path) {
        System.out.println("正在读取文件信息......");
        String[] paths = path.split("[/\\\\]");
        String filename = paths[paths.length - 1];
        int index = filename.lastIndexOf(".");
        String name = filename.substring(0, index);
        String extra = filename.substring(index + 1);
        System.out.println("正在读取视频信息......");
        int size = random.nextInt(200);
        long duration = random.nextInt(3600) + 600;
        int days = random.nextInt(9);
        System.out.println("正在读取音频信息......");
        System.out.println("正在存入数据库......");

//        String sql = "insert into video_file (path, title, size, time, duration, type) values ('', '', '', '', '', '');";
        try (SqlSession session = MybatisPlus.session.openSession(true)) {
            VideoFileMapper videoFileMapper = session.getMapper(VideoFileMapper.class);
            VideoAudioMapper videoAudioMapper = session.getMapper(VideoAudioMapper.class);

            VideoFile videoFile = new VideoFile(path, name, extra, 1048576L * size, duration, DateUtil.afterDays(-days));
            videoFileMapper.insert(videoFile);
            long vid = videoFile.getId();

            VideoAudio channel1 = new VideoAudio(vid, 1, "aac", "");
            VideoAudio channel2 = new VideoAudio(vid, 2, "aac", "");
            videoAudioMapper.insert(channel1);
            videoAudioMapper.insert(channel2);

            log.info("操作完成");
        }


    }

    private static void update(long id, String path) {
        System.out.println("正在读取文件信息......");
        String[] paths = path.split("[/\\\\]");
        String filename = paths[paths.length - 1];
        int index = filename.lastIndexOf(".");
        String name = filename.substring(0, index);
        String extra = filename.substring(index + 1);
        System.out.println("正在读取视频信息......");
        int size = random.nextInt(200);
        long duration = random.nextInt(3600) + 600;
        int days = random.nextInt(9);
        System.out.println("正在读取音频信息......");
        System.out.println("正在更新数据库......");

        try (SqlSession session = MybatisPlus.session.openSession(true)) {
            VideoFileMapper videoFileMapper = session.getMapper(VideoFileMapper.class);

            VideoFile videoFile = new VideoFile(path, name, extra, 1048576L * size, duration, DateUtil.afterDays(-days));
            videoFile.setId(id);
            videoFileMapper.updateById(videoFile);

            log.info("操作完成");
        }

    }

    private static void delete(long id) {
        System.out.println("正在删除内容......");
        try (SqlSession session = MybatisPlus.session.openSession(true)) {
            VideoFileMapper videoFileMapper = session.getMapper(VideoFileMapper.class);
            VideoAudioMapper videoAudioMapper = session.getMapper(VideoAudioMapper.class);

            videoFileMapper.deleteById(id);

            videoAudioMapper.delete(new QueryWrapper<>() {{
                this.eq("vid", id);
            }});

            log.info("操作完成");
        }

    }

    private static final List<String> change = new ArrayList<>();

    static {
        change.add("size");
        change.add("duration");
    }

    private static void retrieve(List<KV> params) {
        try (SqlSession session = MybatisPlus.session.openSession(true)) {
            VideoFileMapper videoFileMapper = session.getMapper(VideoFileMapper.class);

            List<VideoFile> videoFiles = videoFileMapper.selectList(new QueryWrapper<>() {{
                for (KV param : params) {
                    Object value = param.value;
                    if (change.contains(param.key)) {
                        value = Long.parseLong(param.value);
                    }
                    this.eq(param.key, value);
                }
            }});

            System.out.println(videoFiles);

            log.info("操作完成");
        }

    }

    @Data
    static class KV {
        private final String key;
        private final String value;
    }

}
