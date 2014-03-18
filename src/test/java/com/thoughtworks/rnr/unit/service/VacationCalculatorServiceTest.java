package com.thoughtworks.rnr.unit.service;

import com.thoughtworks.rnr.model.AccrualRateCalculator;
import com.thoughtworks.rnr.model.Employee;
import com.thoughtworks.rnr.model.VacationCalculator;
import com.thoughtworks.rnr.service.VacationCalculatorService;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class VacationCalculatorServiceTest {

    Employee mockEmployee;
    AccrualRateCalculator mockAccrualRateCalculator;
    VacationCalculator mockVacationCalculator;
    VacationCalculatorService vacationCalculatorService;

    LocalDate endDate = new LocalDate();

    @Before
    public void setUp(){
        mockVacationCalculator = mock(VacationCalculator.class);
        mockEmployee = mock(Employee.class);
        mockAccrualRateCalculator = mock(AccrualRateCalculator.class);
        vacationCalculatorService = new VacationCalculatorService(mockVacationCalculator);
    }

    @Test
    public void shouldReturnCapReachedNoticeWhenCapIsReached(){
        when(mockVacationCalculator.getDaysUntilCapIsReached(any(Employee.class), any(AccrualRateCalculator.class), any(LocalDate.class))).thenReturn(0.0);

        String actualCapNotice = vacationCalculatorService.getVacationCapNotice(mockEmployee, mockAccrualRateCalculator, endDate);
        String expectedCapNotice = "You have reached your vacation day cap.";

        assertThat(actualCapNotice, is(expectedCapNotice));
    }

    @Test
    public void shouldReturnTimeUntilCapIsReachedIfUnderOneMonth(){
        when(mockVacationCalculator.getDaysUntilCapIsReached(any(Employee.class), any(AccrualRateCalculator.class), any(LocalDate.class))).thenReturn(10.0);

        String actualCapNotice = vacationCalculatorService.getVacationCapNotice(mockEmployee, mockAccrualRateCalculator, endDate);
        String expectedCapNotice = "You are 10 days away from reaching your vacation day cap.";

        assertThat(actualCapNotice, is(expectedCapNotice));
    }

    @Test
    public void shouldCorrectPluralityOfDaysWhenOnlyOneDayLeft(){
        when(mockVacationCalculator.getDaysUntilCapIsReached(any(Employee.class), any(AccrualRateCalculator.class), any(LocalDate.class))).thenReturn(1.2);

        String actualCapNotice = vacationCalculatorService.getVacationCapNotice(mockEmployee, mockAccrualRateCalculator, endDate);
        String expectedCapNotice = "You are 1 day away from reaching your vacation day cap.";

        assertThat(actualCapNotice, is(expectedCapNotice));
    }

    @Test
    public void shouldReturnBlankStringIfMoreThanThirtyDaysAwayFromCap(){
        when(mockVacationCalculator.getDaysUntilCapIsReached(any(Employee.class), any(AccrualRateCalculator.class), any(LocalDate.class))).thenReturn(31.0);

        String actualCapNotice = vacationCalculatorService.getVacationCapNotice(mockEmployee, mockAccrualRateCalculator, endDate);
        String expectedCapNotice = "";

        assertThat(actualCapNotice, is(expectedCapNotice));
    }

    @Test
    public void shouldInteractWithVacationCalculator(){
        vacationCalculatorService.getVacationDays(mockEmployee, mockAccrualRateCalculator, endDate);

        verify(mockVacationCalculator).getVacationDays(mockEmployee, mockAccrualRateCalculator, endDate);
    }
}
