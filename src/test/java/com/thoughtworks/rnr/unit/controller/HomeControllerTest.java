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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class HomeControllerTest {
    HttpServletRequest mockHttpServletRequest;
    SalesForceParserService mockSalesForceParserService;
    EmployeeService mockEmployeeService;
    VacationCalculatorService mockVacationCalculatorService;
    AccrualRateCalculator mockAccrualRateCalculator;
    DateParserService mockDateParserService;
    PersonalDaysCalculator mockPersonalDaysCalculator;

    @Before
    public void setUp() throws Exception {
        mockHttpServletRequest = mock(HttpServletRequest.class);
        mockSalesForceParserService = mock(SalesForceParserService.class);
        mockEmployeeService = mock(EmployeeService.class);
        mockVacationCalculatorService = mock(VacationCalculatorService.class);
        mockAccrualRateCalculator = mock(AccrualRateCalculator.class);
        mockDateParserService = mock(DateParserService.class);
        mockPersonalDaysCalculator = mock(PersonalDaysCalculator.class);
    }

    @Test
    public void post_shouldReturnHomeView() throws IOException, ParseException {
        HomeController homeController = new HomeController(mockEmployeeService, mockSalesForceParserService, mockVacationCalculatorService, mockAccrualRateCalculator, mockDateParserService, mockPersonalDaysCalculator);
        when(mockHttpServletRequest.getParameter("rolloverdays")).thenReturn("1");
        when(mockHttpServletRequest.getParameter("accrualRate")).thenReturn("10");
        when(mockHttpServletRequest.getParameter("salesForceText")).thenReturn("Test");
        when(mockHttpServletRequest.getParameter("startDate")).thenReturn("10/22/2012");
        when(mockHttpServletRequest.getParameter("endDate")).thenReturn("11/22/2013");
        assertThat(homeController.postDate(mockHttpServletRequest).getViewName(), is("home"));
    }

    @Test
    public void get_shouldReturnHomeView() {
        HomeController homeController = new HomeController(mockEmployeeService, mockSalesForceParserService, mockVacationCalculatorService, mockAccrualRateCalculator, mockDateParserService, mockPersonalDaysCalculator);
        assertThat(homeController.get(), is("home"));
    }

    @Test
    public void shouldInteractWithSalesForceParserServiceAndEmployeeService() throws Exception {

        HomeController homeController = new HomeController(mockEmployeeService, mockSalesForceParserService, mockVacationCalculatorService, mockAccrualRateCalculator, mockDateParserService, mockPersonalDaysCalculator);

        when(mockHttpServletRequest.getParameter("rolloverdays")).thenReturn("1");
        when(mockHttpServletRequest.getParameter("accrualRate")).thenReturn("10");
        when(mockHttpServletRequest.getParameter("salesForceText")).thenReturn("Test");
        when(mockHttpServletRequest.getParameter("startDate")).thenReturn("10/22/2012");
        when(mockHttpServletRequest.getParameter("endDate")).thenReturn("11/22/2013");

        when(mockVacationCalculatorService.getVacationDays(any(Employee.class), any(AccrualRateCalculator.class), any(LocalDate.class))).thenReturn(20d);
        when(mockPersonalDaysCalculator.calculatePersonalDays(any(Employee.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(0d);

        homeController.postDate(mockHttpServletRequest);

        verify(mockSalesForceParserService, times(2)).extractDatesAndHoursFromSalesForceText(anyString(), anyList());
        verify(mockEmployeeService, times(1)).createEmployee(any(LocalDate.class), anyString(), anyMap(), anyMap(), anyString());
        verify(mockDateParserService, times(2)).parse(anyString());
    }

}
