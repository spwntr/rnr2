package com.thoughtworks.rnr.unit.controller;

import com.thoughtworks.rnr.controller.HomeController;
import com.thoughtworks.rnr.model.AccrualRateCalculator;
import com.thoughtworks.rnr.model.Employee;
import com.thoughtworks.rnr.model.PersonalDaysCalculator;
import com.thoughtworks.rnr.service.DateParserService;
import com.thoughtworks.rnr.service.EmployeeService;
import com.thoughtworks.rnr.service.SalesForceParserService;
import com.thoughtworks.rnr.service.VacationCalculatorService;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.text.ParseException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class HomeControllerTest {
    private HomeController homeController;

    @Mock
    SalesForceParserService mockSalesForceParserService;
    @Mock
    EmployeeService mockEmployeeService;
    @Mock
    VacationCalculatorService mockVacationCalculatorService;
    @Mock
    AccrualRateCalculator mockAccrualRateCalculator;
    @Mock
    DateParserService mockDateParserService;
    @Mock
    PersonalDaysCalculator mockPersonalDaysCalculator;

    private String startDate;
    private String rolloverDays;
    private String accrualRate;
    private String salesForceText;
    private String endDate;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        homeController = new HomeController(mockEmployeeService, mockSalesForceParserService, mockVacationCalculatorService, mockAccrualRateCalculator, mockDateParserService, mockPersonalDaysCalculator);

        startDate = "10/22/2012";
        rolloverDays = "1";
        accrualRate = "10";
        salesForceText = "Test";
        endDate = "11/22/2013";
    }

    @Test
    public void post_shouldReturnHomeView() throws IOException, ParseException {
        assertThat(homeController.postDate(startDate, rolloverDays, accrualRate, salesForceText, endDate).getViewName(), is("home"));
    }

    @Test
    public void get_shouldReturnHomeView() {
        assertThat(homeController.get(), is("home"));
    }

    @Test
    public void shouldInteractWithSalesForceParserServiceAndEmployeeService() throws Exception {
        when(mockVacationCalculatorService.getVacationDays(any(Employee.class), any(AccrualRateCalculator.class), any(LocalDate.class))).thenReturn(20d);
        when(mockPersonalDaysCalculator.calculatePersonalDays(any(Employee.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(0d);

        assertThat(homeController.postDate(startDate, rolloverDays, accrualRate, salesForceText, endDate).getViewName(), is("home"));

        verify(mockSalesForceParserService, times(2)).extractDatesAndHoursFromSalesForceText(anyString(), anyList());
        verify(mockEmployeeService, times(1)).createEmployee(any(LocalDate.class), anyString(), anyMap(), anyMap(), anyString());
        verify(mockDateParserService, times(2)).parse(anyString());
    }
}
