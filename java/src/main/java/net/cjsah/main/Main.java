package net.cjsah.main;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class Main {
    public static void main(String[] args) {
        System.out.println(random(50));
        System.out.println(random(8));
    }

    private static int random(int range) {
        Random random = new Random();
        return random.nextInt(range) + 1;
    }

}
