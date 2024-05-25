package net.cjsah.main.sql;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.cjsah.sql.MybatisPlus;
import net.cjsah.sql.mapper.FileMappingMapper;
import net.cjsah.sql.pojo.FileMapping;
import net.cjsah.util.JsonUtil;
import org.apache.ibatis.session.SqlSession;

import java.io.File;
import java.util.List;
import java.util.Set;

public class FileUUID {
    public static void main(String[] args) {
//        try (SqlSession session = MybatisPlus.session.openSession(true)) {
//            QuestionMapper mapper = session.getMapper(QuestionMapper.class);
//
//            List<Question> questions = mapper.selectList(Wrappers.emptyWrapper());
//
//            Set<String> uuids = new HashSet<>();
//
//            for (Question question : questions) {
//                JSONObject data = question.getData();
//                switch (question.getQuestionType()) {
//                    case FORMULA_SHOW -> appendUUID(uuids, data);
//                    case FORMULA_MEM, EXAMPLE_QUESTIONS -> {
//                        appendUUID(uuids, data.getJSONObject("question"));
//                        appendUUID(uuids, data.getJSONObject("answer"));
//                    }
//                    case EXERCISES -> {
//                        appendUUID(uuids, data.getJSONObject("question"));
//                        appendUUID(uuids, data.getJSONObject("answer"));
//                        appendUUID(uuids, data.getJSONObject("tip"));
//                    }
//                }
//            }
//
//            FileUtil.writeUtf8String(JsonUtil.obj2Str(uuids), new File("uuid.json"));
//        }

        String json = FileUtil.readUtf8String(new File("uuid.json"));
        List<String> uuids = JsonUtil.str2List(json, String.class);

        System.out.println(uuids.size());

//        try (SqlSession session = MybatisPlus.session.openSession(true)) {
//            FileMappingMapper mapper = session.getMapper(FileMappingMapper.class);
//
//            QueryWrapper<FileMapping> wrapper = new QueryWrapper<>();
//
//            List<FileMapping> questions = mapper.selectList(wrapper);
//
//            Set<String> uuids = new HashSet<>();
//
//            for (Question question : questions) {
//                JSONObject data = question.getData();
//                switch (question.getQuestionType()) {
//                    case FORMULA_SHOW -> appendUUID(uuids, data);
//                    case FORMULA_MEM, EXAMPLE_QUESTIONS -> {
//                        appendUUID(uuids, data.getJSONObject("question"));
//                        appendUUID(uuids, data.getJSONObject("answer"));
//                    }
//                    case EXERCISES -> {
//                        appendUUID(uuids, data.getJSONObject("question"));
//                        appendUUID(uuids, data.getJSONObject("answer"));
//                        appendUUID(uuids, data.getJSONObject("tip"));
//                    }
//                }
//            }
//
//            FileUtil.writeUtf8String(JsonUtil.obj2Str(uuids), new File("uuid.json"));
//
//        }

    }

    public static void appendUUID(Set<String> uuids, JSONObject node) {
        if (node == null) return;
        if (node.getBoolean("is_image")) {
            uuids.add(node.getString("value"));
        }
    }
}
