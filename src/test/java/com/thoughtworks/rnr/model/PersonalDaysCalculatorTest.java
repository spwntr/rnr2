package com.thoughtworks.rnr.model;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PersonalDaysCalculatorTest {

    PersonalDaysCalculator personalDaysCalculator;
    Employee mockEmployee;
    LocalDate beginningOf2013;
    LocalDate middleOf2013;
    LocalDate endOf2013;

    Map<LocalDate, Double> NO_PERSONAL_DAYS;
    Map<LocalDate, Double> SOME_PERSONAL_DAYS;

    @Before
    public void setUp() {
        personalDaysCalculator = new PersonalDaysCalculator();

        endOf2013 = new LocalDate(2013, 12, 31);
        middleOf2013 = new LocalDate(2013, 7, 1);
        beginningOf2013 = new LocalDate(2013, 1, 1);

        NO_PERSONAL_DAYS = new HashMap<LocalDate, Double>();
        SOME_PERSONAL_DAYS = new HashMap<LocalDate, Double>();

        SOME_PERSONAL_DAYS.put(new LocalDate(2013, 2, 1), 24.0);
        SOME_PERSONAL_DAYS.put(new LocalDate(2013, 9, 1), 24.0);

        mockEmployee = mock(Employee.class);
        when(mockEmployee.getPersonalDaysTaken()).thenReturn(NO_PERSONAL_DAYS);
    }

    @Test
    public void shouldReturnSevenDaysIfStartDateBeforeMayFirst() {
        assertExpectedPersonalDays(beginningOf2013, endOf2013, 7.0);
    }

    @Test
    public void shouldReturnFourDaysForStartDateBetweenMayAndAugust() {
        assertExpectedPersonalDays(middleOf2013, endOf2013, 4.0);
    }

    @Test
    public void shouldReturnThreeDaysForStartDateAfterSeptemberFirst() {
        assertExpectedPersonalDays(endOf2013, endOf2013, 3.0);
    }

    @Test
    public void shouldReturnPersonalDaysAllottedMinusPersonalDaysTaken() {
        double personalDaysLeft = 1.0;

        when(mockEmployee.getPersonalDaysTaken()).thenReturn(SOME_PERSONAL_DAYS);

        assertExpectedPersonalDays(beginningOf2013, endOf2013, personalDaysLeft);
    }

    @Test
    public void shouldReturnSevenDaysIfEndDateInDifferentYearThanStartDate() {
        LocalDate endOf2012 = new LocalDate(2012, 12, 31);
        assertExpectedPersonalDays(endOf2012, endOf2013, 7.0);
    }

    @Test
    public void shouldNotTakeOffPersonalDaysAfterEndDate() {
        when(mockEmployee.getPersonalDaysTaken()).thenReturn(SOME_PERSONAL_DAYS);

        assertExpectedPersonalDays(beginningOf2013, middleOf2013, 4.0);
    }

    private void assertExpectedPersonalDays(LocalDate startDate, LocalDate endDate, double expectedPersonalDays) {
        assertThat(personalDaysCalculator.calculatePersonalDays(mockEmployee, startDate, endDate), is(expectedPersonalDays));
    }

}
