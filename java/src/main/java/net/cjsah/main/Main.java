package net.cjsah.main;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {

        long time = 1712302697000L;

        LocalDateTime dateTime = Instant.ofEpochMilli(time)
                .atZone(ZoneId.of("GMT+8"))
                .toLocalDateTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);

        System.out.println(formattedDateTime);

    }



}
