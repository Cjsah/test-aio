package net.cjsah.main;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class Main {
    public static void main(String[] args) {
        String [] texts = new String[] {
               "video", "at", "rps", "dice", "shake", "poke", "anonymous", "share", "contact", "location", "music", "reply", "forward", "node"
        };

        List<String> names = Arrays.stream(texts).map(it -> it.substring(0, 1).toUpperCase() + it.substring(1) + "MessageNode").toList();

        System.out.println(names);


    }

    interface S {

    }

    static class A implements S {

    }

    static class B extends A implements S {

    }

}
