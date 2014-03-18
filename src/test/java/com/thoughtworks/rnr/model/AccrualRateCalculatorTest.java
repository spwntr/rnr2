package com.thoughtworks.rnr.model;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AccrualRateCalculatorTest {

    public static final double MAXIMUM_VACATION_DAYS = 30d;
    public static final double NO_ROLLOVER_DAYS = 0d;

    private final LocalDate TODAY = new LocalDate();
    private final LocalDate TOMORROW = new LocalDate().plusDays(1);
    private final LocalDate ONE_YEAR_FROM_NOW = new LocalDate().plusYears(1).plusDays(1);
    private final LocalDate THREE_YEARS_FROM_NOW = new LocalDate().plusYears(3).plusDays(1);
    private final LocalDate SIX_YEARS_FROM_NOW = new LocalDate().plusYears(6).plusDays(1);

    private final double CUSTOM_INITIAL_ACCRUAL_RATE = 17d;

    private final Map<LocalDate, Double> NO_VACATION = new HashMap<LocalDate, Double>();
    private final Map<LocalDate, Double> NO_PERSONAL_DAYS = new HashMap<LocalDate, Double>();

    AccrualRateCalculator accrualRateCalculator;
    Employee employee;

    @Before
    public void setUp() throws Exception {
        accrualRateCalculator = new AccrualRateCalculator();
        employee = new Employee(TODAY, NO_ROLLOVER_DAYS, NO_VACATION, NO_PERSONAL_DAYS, Constants.DEFAULT_ACCRUAL_RATE);
    }

    @Test
    public void shouldHaveAccrualRateOfTenDaysBeforeOneYearElapses() {
        assertThat(accrualRateCalculator.calculateDailyAccrualRate(employee, TOMORROW), is(Constants.DEFAULT_ACCRUAL_RATE / Constants.YEAR_IN_DAYS));
    }

    @Test
    public void shouldHaveAccrualRateOfFifteenDaysAfterOneYear() {
        assertThat(accrualRateCalculator.calculateDailyAccrualRate(employee, ONE_YEAR_FROM_NOW), is(AccrualRate.LESS_THAN_THREE_YEARS.getAccrualRate() / Constants.YEAR_IN_DAYS));
    }

    @Test
    public void shouldHaveAccrualRateOfTwentyDaysAfterThreeYears() {
        assertThat(accrualRateCalculator.calculateDailyAccrualRate(employee, THREE_YEARS_FROM_NOW), is(AccrualRate.LESS_THAN_SIX_YEARS.getAccrualRate() / Constants.YEAR_IN_DAYS));
    }

    @Test
    public void shouldHaveAccrualRateOfTwentyFiveDaysAfterSixYears() {
        assertThat(accrualRateCalculator.calculateDailyAccrualRate(employee, SIX_YEARS_FROM_NOW), is(AccrualRate.MORE_THAN_SIX_YEARS.getAccrualRate() / Constants.YEAR_IN_DAYS));
    }

    @Test
    public void shouldHaveCustomAccrualRateIfSpecified() {
        Employee employeeWithCustomAccrualRate = new Employee(TODAY, NO_ROLLOVER_DAYS, NO_VACATION, NO_PERSONAL_DAYS, CUSTOM_INITIAL_ACCRUAL_RATE);

        assertThat(accrualRateCalculator.calculateDailyAccrualRate(employeeWithCustomAccrualRate, ONE_YEAR_FROM_NOW), is(CUSTOM_INITIAL_ACCRUAL_RATE / Constants.YEAR_IN_DAYS));
        assertThat(accrualRateCalculator.calculateDailyAccrualRate(employeeWithCustomAccrualRate, TOMORROW), is(CUSTOM_INITIAL_ACCRUAL_RATE / Constants.YEAR_IN_DAYS));
        assertThat(accrualRateCalculator.calculateDailyAccrualRate(employeeWithCustomAccrualRate, THREE_YEARS_FROM_NOW), is(AccrualRate.LESS_THAN_SIX_YEARS.getAccrualRate() / Constants.YEAR_IN_DAYS));

    }

    @Test
    public void shouldReturnVacationDayCap() {
        assertThat(accrualRateCalculator.calculateVacationDayCap(employee, TOMORROW), is(Constants.DEFAULT_ACCRUAL_RATE * 1.5));
        assertThat(accrualRateCalculator.calculateVacationDayCap(employee, ONE_YEAR_FROM_NOW), is(AccrualRate.LESS_THAN_THREE_YEARS.getAccrualRate() * 1.5));
        assertThat(accrualRateCalculator.calculateVacationDayCap(employee, SIX_YEARS_FROM_NOW), is(MAXIMUM_VACATION_DAYS));
    }
}
