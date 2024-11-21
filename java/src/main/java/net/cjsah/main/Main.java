package net.cjsah.main;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws URISyntaxException {
        List<Integer> list = new ArrayList<>(365);
        for (int i = 0; i < 365; i++) {
            list.add(i);
        }
        System.out.println(list.stream().parallel().mapToInt(it -> it).sum());

    }

}
