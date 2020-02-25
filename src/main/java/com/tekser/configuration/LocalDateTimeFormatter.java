package com.tekser.configuration;


import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Configuration
public class LocalDateTimeFormatter {

    public String dateTimeToString(LocalDateTime localDateTime) {
        return localDateTime.toString();
    }

    public LocalDateTime stringToDateTime (String string) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(string, dateTimeFormatter);
    }

    public LocalDate stringToDate (String string) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(string, dateTimeFormatter);
    }

    public static String neutralizer(String dateTimeString) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, dateTimeFormatter);
        Date date = Date.from( localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return date.toString();
    }
}
