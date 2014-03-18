package com.thoughtworks.rnr.service;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class DateParserService {
    public LocalDate parse(String stringDate) {
        String[] dateFields = stringDate.split("/");
        LocalDate date = new LocalDate(Integer.parseInt(dateFields[2]), Integer.parseInt(dateFields[0]),
                Integer.parseInt(dateFields[1]));
        return date;
    }
}
