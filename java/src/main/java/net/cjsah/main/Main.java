package net.cjsah.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class Main {
    public static void main(String[] args) {
        List<WordMeaning> words = new ArrayList<>();
        words.add(new WordMeaning("ago", "adv.以前"));


        List<String> total = new ArrayList<>();
        for (WordMeaning word : words) {
            List<String> translates = Arrays.stream(word.getMeaning().split("(<br/>|\\n)"))
                    .filter(meaning -> !meaning.trim().isEmpty() && !meaning.startsWith("*"))
                    .collect(Collectors.toList());

            int size = translates.size();

            for (int i = 0; i < size; i++) {
                String translate = translates.get(i);
                if (translate.contains("；")) {
                    String[] values = translate.split("；");
                    String[] first = values[0].split("\\.", 2);
                    if (first.length == 2) {
                        values[0] = first[1];
                        List<String> trans = Arrays.stream(values).parallel().map(it -> first[0] + "  " + it).collect(Collectors.toList());
                        translates.set(i, trans.remove(0));
                        translates.addAll(trans);
                    } else {
                        translates.addAll(Arrays.asList(values));
                    }
                }
            }

            total.addAll(translates);

            if (translates.size() > 8) {
                translates = translates.subList(0, 8);
            }

            List<TranslateOption> meanings = translates.stream().parallel().map(it -> new TranslateOption(it, true)).collect(Collectors.toList());

            word.setMeanings(meanings);
        }
        words.stream().parallel().forEach(word -> {
            List<TranslateOption> meanings = word.getMeanings();
            List<String> translates = meanings.stream().parallel().map(it -> it.translate).toList();
            List<String> notContains = total.stream().parallel().filter(it -> !translates.contains(it)).collect(Collectors.toList());
            Collections.shuffle(notContains);
            for (String translate : notContains) {
                if (meanings.size() >= 8) {
                    break;
                }
                meanings.add(new TranslateOption(translate, false));
            }
            Collections.shuffle(meanings);
        });
        System.out.println(words);
    }

    private static boolean containsNot(List<TranslateOption> meanings, String translate) {
        for (TranslateOption meaning : meanings) {
            if (meaning.getTranslate().equals(translate)) {
                return false;
            }
        }
        return true;
    }
    private static List<String> containsNot(List<String> totals, List<TranslateOption> meanings) {
        List<String> translates = meanings.stream().parallel().map(it -> it.translate).toList();
        return totals.stream().parallel().filter(it -> !translates.contains(it)).toList();
    }

    @Data
    static class WordMeaning {
        private final String word;
        private final String meaning;
        private List<TranslateOption> meanings;
    }

    @Data
    @AllArgsConstructor
    static class TranslateOption {
        private final String translate;
        private final boolean need;
        private boolean selected;

        public TranslateOption(String translate, boolean need) {
            this.translate = translate;
            this.need = need;
            this.selected = false;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            TranslateOption that = (TranslateOption) object;
            return Objects.equals(translate, that.translate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(translate);
        }
    }

}
