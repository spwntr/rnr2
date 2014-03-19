package com.thoughtworks.rnr.unit.controller;

import com.thoughtworks.rnr.controller.HomeController;
import com.thoughtworks.rnr.service.SAMLService;
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
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class HomeControllerTest {
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
    @Mock
    SAMLService mockSAMLService;
    private String startDate;
    private String rolloverDays;
    private String accrualRate;
    private String salesForceText;
    private String endDate;

    private HomeController homeController;
    private final String samlRequestURL = "redirect:http://rain.okta1.com:1802/app/template_saml_2_0/k8lqGUUCIFIJHKUOGQKG/sso/saml?";

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        homeController = new HomeController(mockEmployeeService, mockSalesForceParserService, mockVacationCalculatorService, mockAccrualRateCalculator, mockDateParserService, mockPersonalDaysCalculator, mockSAMLService);

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
        when(mockSAMLService.sessionToken()).thenReturn(true);
        assertThat(homeController.checkForSSO(), is("home"));
        verify(mockSAMLService).sessionToken();
    }

    @Test
    public void get_shouldReturnEncodedExternalRedirect() {
        when(mockSAMLService.redirectToIDPWithSAMLRequest()).thenReturn(samlRequestURL);
        when(mockSAMLService.sessionToken()).thenReturn(false);
        assertThat(homeController.checkForSSO(), is(samlRequestURL));
        verify(mockSAMLService).sessionToken();
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
/*
In SP initiated the flow is:

User goes to the target SP first. They do not have a session established with the SP
SP redirects the user to the configured Login URL (Okta’s generated app instance url) sending the SAMLRequest.
Okta is sent SAMLRequest (assumption is that the user has an existing Okta session)
Okta sends a SAMLResponse to the configured SP
SP receives the SAMLResponse and verifies that it is correct. A session is established on the SP side.
User is authenticated
In both cases, there is additional configuration required in the target app (SP) you are configuring SAML with. Okta provides this information in our SAML 2.0 app instructions (accessible from the Sign on tab in the app wizard). This is typically the following: External key, certificate, and login url (normally only needed in the SP initiated flow mentioned above.
 */