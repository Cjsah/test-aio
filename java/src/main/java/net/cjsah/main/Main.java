package net.cjsah.main;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(3);
        System.out.println(list);
        list.add(0, 1);
        System.out.println(list);
    }
}
