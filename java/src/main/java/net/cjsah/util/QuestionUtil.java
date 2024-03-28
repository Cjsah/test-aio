package net.cjsah.util;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import net.cjsah.data.QuestionData;
import net.cjsah.data.StrategyData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class QuestionUtil {
    public static List<StrategyData> tree2List(List<QuestionData.LevelTree> levels) {
        List<StrategyData> list = new ArrayList<>();
        for (QuestionData.LevelTree level : levels) {
            for (QuestionData.PointTree point : level.getList()) {
                for (QuestionData.TypeTree type : point.getList()) {
                    list.add(new StrategyData(level.getName(), point.getName(), type.getName()));
                }
            }
        }
        return list;
    }

    public static <T extends StrategyData> JSONArray list2TreeNoMerge(List<T> list, BiConsumer<T, JSONObject> consumer) {
        JSONArray levels = new JSONArray();
        JSONObject lastLevel = null;
        JSONObject lastPoint = null;
        JSONObject lastType;
        for (T data : list) {
            if (lastLevel == null || !lastLevel.getString("name").equals(data.getLevel())) {
                lastLevel = new JSONObject();
                lastLevel.put("name", data.getLevel());
                lastPoint = new JSONObject();
                lastPoint.put("name", data.getPoint());
                lastType = new JSONObject();
                lastType.put("name", data.getType());
                consumer.accept(data, lastType);
                lastLevel.putArray("list").add(lastPoint);
                lastPoint.putArray("list").add(lastType);
                levels.add(lastLevel);
            } else if (!lastPoint.getString("name").equals(data.getPoint())){
                lastPoint = new JSONObject();
                lastPoint.put("name", data.getPoint());
                lastType = new JSONObject();
                lastType.put("name", data.getType());
                consumer.accept(data, lastType);
                lastLevel.getJSONArray("list").add(lastPoint);
                lastPoint.putArray("list").add(lastType);
            } else {
                lastType = new JSONObject();
                lastType.put("name", data.getType());
                consumer.accept(data, lastType);
                lastPoint.getJSONArray("list").add(lastType);
            }
        }
        return levels;
    }


    public static <T extends StrategyData> JSONArray list2Tree(List<T> list, BiConsumer<T, JSONObject> consumer) {
        JSONArray levels = new JSONArray();
        for (T data : list) {
            Optional<Object> levelOptional = levels.stream().parallel().filter(it -> ((JSONObject)it).getString("name").equals(data.getLevel())).findFirst();
            if (levelOptional.isPresent()) {
                JSONArray points = ((JSONObject)levelOptional.get()).getJSONArray("list");
                Optional<Object> pointOptional = points.stream().parallel().filter(it -> ((JSONObject)it).getString("name").equals(data.getPoint())).findFirst();
                if (pointOptional.isPresent()) {
                    JSONArray types = ((JSONObject)pointOptional.get()).getJSONArray("list");
                    if (types.stream().parallel().noneMatch(it -> ((JSONObject)it).getString("name").equals(data.getType()))) {
                        types.add(new JSONObject() {{
                            this.put("name", data.getType());
                            consumer.accept(data, this);
                        }});
                    }
                } else {
                    points.add(new JSONObject() {{
                        this.put("name", data.getPoint());
                        this.putArray("list").add(new JSONObject() {{
                            this.put("name", data.getType());
                            consumer.accept(data, this);
                        }});
                    }});
                }
            }else {
                levels.add(new JSONObject() {{
                    this.put("name", data.getLevel());
                    this.putArray("list").add(new JSONObject() {{
                        this.put("name", data.getPoint());
                        this.putArray("list").add(new JSONObject() {{
                            this.put("name", data.getType());
                            consumer.accept(data, this);
                        }});
                    }});
                }});
            }
        }
        return levels;
    }

    public static <T extends StrategyData> List<QuestionData.LevelTree> list2Tree(List<T> list) {
        List<QuestionData.LevelTree> levels = new ArrayList<>();
        for (StrategyData data : list) {
            Optional<QuestionData.LevelTree> levelOptional = levels.stream().parallel().filter(it -> it.getName().equals(data.getLevel())).findFirst();
            if (levelOptional.isPresent()) {
                List<QuestionData.PointTree> points = levelOptional.get().getList();
                Optional<QuestionData.PointTree> pointOptional = points.stream().parallel().filter(it -> it.getName().equals(data.getPoint())).findFirst();
                if (pointOptional.isPresent()) {
                    List<QuestionData.TypeTree> types = pointOptional.get().getList();
                    for (QuestionData.TypeTree type : types) {
                        if (type.getName().equals(data.getType())) break;
                    }
                    types.add(new QuestionData.TypeTree(data.getType()));
                } else {
                    points.add(new QuestionData.PointTree(data.getPoint()) {{
                        this.getList().add(new QuestionData.TypeTree(data.getType()));
                    }});
                }
            }else {
                levels.add(new QuestionData.LevelTree(data.getLevel()) {{
                    this.getList().add(new QuestionData.PointTree(data.getPoint()) {{
                        this.getList().add(new QuestionData.TypeTree(data.getType()));
                    }});
                }});
            }
        }
        return levels;
    }

    public static List<QuestionData.LevelTree> array2List(JSONArray array) {
        List<QuestionData.LevelTree> list = new ArrayList<>();
        if (null == array) return list;
        for (int i = 0; i < array.size(); i++) {
            JSONObject levelJson = array.getJSONObject(i);
            String levelName = levelJson.getString("name");
            QuestionData.LevelTree level = new QuestionData.LevelTree(levelName);
            JSONArray points = levelJson.getJSONArray("list");
            for (int j = 0; j < points.size(); j++) {
                JSONObject pointJson = points.getJSONObject(j);
                String pointName = pointJson.getString("name");
                JSONArray types = pointJson.getJSONArray("list");
                QuestionData.PointTree point = new QuestionData.PointTree(pointName);
                for (int k = 0; k < types.size(); k++) {
                    JSONObject type = types.getJSONObject(k);
                    String typeName = type.getString("name");
                    point.getList().add(new QuestionData.TypeTree(typeName));
                }
                level.getList().add(point);
            }
            list.add(level);
        }
        return list;
    }

}
