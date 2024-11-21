package net.cjsah.main;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class Main {

    public static void main(String[] args) throws URISyntaxException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.NOVEMBER, 28, 10, 10, 10);
        LocalDate localDate = LocalDate.ofInstant(calendar.toInstant(), ZoneId.of("GMT+8"));
        System.out.println(localDate);
        LocalDate now = LocalDate.now(ZoneId.of("GMT+8"));
        System.out.println(now);
        long between = ChronoUnit.DAYS.between(localDate, now);
        System.out.println(between);


    }

}
