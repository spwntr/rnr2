package com.thoughtworks.rnr.service;

import com.thoughtworks.rnr.model.Constants;
import com.thoughtworks.rnr.model.Employee;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class EmployeeServiceTest {

    private static final LocalDate DATE = new LocalDate(2013, 10, 20);
    private static final String SOME_ROLLOVER_DAYS = "6";
    private static final String NO_ROLLOVER_DAYS = "";
    private static final String NO_INITIAL_ACCRUAL_RATE = "";
    private static final String CUSTOM_ACCRUAL_RATE = "17";
    private Map<LocalDate, Double> NO_PERSONAL_DAYS;
    private Map<LocalDate, Double> NO_VACATION;

    EmployeeService employeeService;

    @Before
    public void setUp() {
        employeeService = new EmployeeService();
        NO_PERSONAL_DAYS = new HashMap<LocalDate, Double>();
        NO_VACATION = new HashMap<LocalDate, Double>();
    }

    @Test
    public void shouldCreateNewEmployeeWithNoRolloverDaysIfNoRolloverDaysGiven() {
        assertEmployeeEquality(DATE, NO_ROLLOVER_DAYS, NO_VACATION, NO_PERSONAL_DAYS, NO_INITIAL_ACCRUAL_RATE);
    }

    @Test
    public void shouldCreateEmployeeWithCertainAmountOfRolloverDays() {
        assertEmployeeEquality(DATE, SOME_ROLLOVER_DAYS, NO_VACATION, NO_PERSONAL_DAYS, NO_INITIAL_ACCRUAL_RATE);
    }

    @Test
    public void shouldCreateEmployeeWithCustomAccrualRateIfGiven() {
        assertEmployeeEquality(DATE, NO_ROLLOVER_DAYS, NO_VACATION, NO_PERSONAL_DAYS, CUSTOM_ACCRUAL_RATE);
    }

    @Test
    public void shouldCreateEmployeeWithDefaultAccrualRateIfRateNotGiven(){
        assertEmployeeEquality(DATE, NO_ROLLOVER_DAYS, NO_VACATION, NO_PERSONAL_DAYS, NO_INITIAL_ACCRUAL_RATE);
    }

    private void assertEmployeeEquality(LocalDate date, String rolloverDays, Map<LocalDate, Double> daysOff, Map<LocalDate, Double> personalDaysTaken, String accrualRate) {
        Employee actualEmployee = employeeService.createEmployee(date, rolloverDays, daysOff, personalDaysTaken, accrualRate);

        double convertedRolloverDays = 0;
        double convertedAccrualRate = Constants.DEFAULT_ACCRUAL_RATE;

        if(!rolloverDays.equals("")){
            convertedRolloverDays = Double.parseDouble(rolloverDays);
        }

        if(!accrualRate.equals("")){
            convertedAccrualRate = Double.parseDouble(accrualRate);
        }

        assertThat(actualEmployee.getRolloverDays(), is(convertedRolloverDays));
        assertThat(actualEmployee.getInitialAccrualRate(), is(convertedAccrualRate));
    }
}
