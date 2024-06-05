package net.cjsah.main;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import net.cjsah.util.JsonUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        String text = FileUtil.readUtf8String(new File("test.json"));
//        JSONObject json = JsonUtil.str2Obj(text, JSONObject.class);
//        List<JSONObject> wordList = json.getJSONObject("data").getList("middleWordList", JSONObject.class);
//        List<String> words = wordList.stream().map(it -> it.getString("word")).toList();
//        System.out.println(words);
//
//        int rows = words.size() / 4;
//        int remain = words.size() % 4;
//
//        List<Node[]> list = new ArrayList<>();
//        for (int row = 0; row < rows; row++) {
//            Node[] node = new Node[4];
//            for (int col = 0; col < 4; col++) {
//                int index = col * rows + row + Math.min(col, remain);
//                node[col] = new Node(words.get(index));
//                node[col].setIndex(index + 1);
//            }
//            list.add(node);
//        }
//        if (remain > 0) {
//            Node[] node = new Node[remain];
//            for (int i = 0; i < remain; i++) {
//                int index = (i + 1) * rows + i;
//                node[i] = new Node(words.get(index));
//                node[i].setIndex(index + 1);
//            }
//            list.add(node);
//        }
//
//        for (Node[] row : list) {
//            for (Node node : row) {
//                System.out.print(node);
//                System.out.print("\t");
//            }
//            System.out.println();
//        }





    }

    @Data
    static class Node {
        String word;
        int index;

        public Node(String word) {
            this.word = word;
            this.index = 0;
        }
    }

}
