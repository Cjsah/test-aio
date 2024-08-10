package net.cjsah.main;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class Main {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 4000; i++) {
            list.add(random.nextInt(1000) % 3);
        }
        System.out.println(list);
        int length = list.size();
        int size = (length - 1) / 200 + 1;
        List<Integer> parts = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int start = i * 200;
            List<Integer> value = list.subList(start, Math.min(start + 200, length));
            int count = (int) value.stream().filter(it -> it == 1).count();
            parts.add(count);
        }
        System.out.println(parts);
    }

}
