package com.thoughtworks.rnr.unit.service;

import com.thoughtworks.rnr.service.DateParserService;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DateParserServiceTest {

    private DateParserService dateParserService;

    @Before
    public void setup(){
        dateParserService = new DateParserService();
    }

    @Test
    public void shouldReturnLocalDateForDayMonthYearsString() throws Exception {
        String stringDate = "10/11/2011";
        LocalDate expectedDate = new LocalDate(2011,10,11);
        assertThat(dateParserService.parse(stringDate), is(expectedDate));
    }

    @Test
    public void shouldReturnLocalDateForStringWithSingleDigitMonthOrDay(){
        String stringDateWithSingleDigits = "4/7/2012";
        LocalDate expectedDate = new LocalDate(2012,4,7);
        assertThat(dateParserService.parse(stringDateWithSingleDigits), is(expectedDate));
    }
}
