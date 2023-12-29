package net.cjsah.data;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

public final class QuestionData {
    public static final Node EMPTY = new Node(false, "");

    @Data
    public static class Node {
        @SerializedName("is_image")
        private final boolean isImage;
        private final String value;

        public boolean isEmpty() {
            return this.value.isEmpty();
        }
    }

    @Data
    public static class Question {
        private final Node question;
        private final Node answer;
    }

    @Getter
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class TimedQuestion extends Question {
        private final int time;

        public TimedQuestion(Node question, Node answer, int time) {
            super(question, answer);
            this.time = time;
        }
    }

    @Data
    public static class LevelTree {
        private final String name;
        private final List<PointTree> list;
        public LevelTree(String name) {
            this.name = name;
            this.list = new ArrayList<>();
        }
    }

    @Data
    public static class PointTree {
        private final String name;
        private final List<TypeTree> list;
        public PointTree(String name) {
            this.name = name;
            this.list = new ArrayList<>();
        }
    }

    @Data
    public static class TypeTree {
        private final String name;
    }

    @Data
    public static class Correct {
        private final int id;
        private final boolean mark;
        private final String input;
    }

}
