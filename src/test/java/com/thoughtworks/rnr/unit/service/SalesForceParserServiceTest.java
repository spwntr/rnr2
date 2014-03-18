package com.thoughtworks.rnr.unit.service;

import com.thoughtworks.rnr.model.Constants;
import com.thoughtworks.rnr.service.DateParserService;
import com.thoughtworks.rnr.service.SalesForceParserService;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SalesForceParserServiceTest {

    Map<LocalDate, Double> emptyMap;
    private DateParserService dateParserService;
    private SalesForceParserService salesForceParserService;

    public static final String SAMPLE_SALES_FORCE_TEXT = "\n" +
            "Filtered By:   Edit \n" +
            "   \tResource: Full Name equals Benjamin Davis,Davis Benjamin Clear \n" +
            "   \tAND Project Name contains sick Clear \n" +
            " \tProject Name\tTimecard Id\tStart Date\tEnd Date\tTotal Hours\tMonday Hours\tTuesday Hours\tWednesday Hours\tThursday Hours\tFriday Hours\tSaturday Hours\tSunday Hours\tTotal Days\n" +
            "\tSub-Project Name: Annual lv; vacation (4 records)\n" +
            " \t \t \t \t \t \t11.50\n" +
            " \tNon-sick leave\tTCH-07-21-2013-016842\t7/22/2013\t7/28/2013\t35.00\t2.00\t3.00\t4.00\t5.00\t6.00\t7.00\t8.00\t \n" +
            " \tNon-sick leave\tTCH-07-21-2013-016842\t7/29/2013\t8/4/2013\t28.00\t0.00\t4.00\t8.00\t8.00\t8.00\t0.00\t0.00\t \n" +
            " \tNon-sick leave\tTCH-07-21-2013-016843\t8/5/2013\t8/11/2013\t40.00\t8.00\t8.00\t8.00\t8.00\t8.00\t0.00\t0.00\t \n" +
            " \tNon-sick leave\tTCH-07-21-2013-016844\t8/12/2013\t8/18/2013\t8.00\t0.00\t8.00\t0.00\t0.00\t0.00\t0.00\t0.00\t \n" +
            " \tNon-sick leave\tTCH-09-04-2013-044182\t9/2/2013\t9/8/2013\t16.00\t0.00\t0.00\t8.00\t0.00\t8.00\t0.00\t0.00\t \n" +
            "\tSub-Project Name: Personal/Sick (US/CAN only) (5 records)\n" +
            " \t \t \t \t \t \t7.00\n" +
            " \tSick leave\tTCH-02-25-2013-001699\t2/18/2013\t2/24/2013\t4.00\t0.00\t0.00\t0.00\t0.00\t0.00\t0.00\t4.00\t \n" +
            " \tSick leave\tTCH-03-28-2013-002225\t3/25/2013\t3/31/2013\t20.00\t0.00\t0.00\t8.00\t8.00\t4.00\t0.00\t0.00\t \n" +
            " \tSick leave\tTCH-04-22-2013-003111\t4/15/2013\t4/21/2013\t8.00\t8.00\t0.00\t0.00\t0.00\t0.00\t0.00\t0.00\t \n" +
            " \tSick leave\tTCH-07-12-2013-011704\t7/29/2013\t8/4/2013\t12.00\t4.00\t8.00\t0.00\t0.00\t0.00\t0.00\t0.00\t \n" +
            " \tSick leave\tTCH-07-21-2013-016841\t7/22/2013\t7/28/2013\t12.00\t0.00\t0.00\t8.00\t0.00\t4.00\t0.00\t0.00\t \n" +
            "\tSub-Project Name: Public holiday (7 records)\n" +
            " \t \t \t \t \t \t5.00\n" +
            " \tNon-sick leave\tTCH-12-21-2012-000612\t12/31/2012\t1/6/2013\t8.00\t0.00\t0.00\t8.00\t0.00\t0.00\t0.00\t0.00\t \n" +
            " \tNon-sick leave\tTCH-01-25-2013-001132\t1/21/2013\t1/27/2013\t8.00\t0.00\t0.00\t8.00\t0.00\t0.00\t0.00\t0.00\t \n" +
            " \tNon-sick leave\tTCH-06-02-2013-004688\t5/27/2013\t6/2/2013\t4.00\t0.00\t4.00\t0.00\t0.00\t0.00\t0.00\t0.00\t \n" +
            " \tNon-sick leave\tTCH-06-07-2013-004853\t6/3/2013\t6/9/2013\t4.00\t0.00\t0.00\t4.00\t0.00\t0.00\t0.00\t0.00\t \n" +
            " \tNon-sick leave\tTCH-07-06-2013-009895\t7/1/2013\t7/7/2013\t4.00\t0.00\t0.00\t4.00\t0.00\t0.00\t0.00\t0.00\t \n" +
            " \tNon-sick leave\tTCH-07-21-2013-016840\t7/22/2013\t7/28/2013\t4.00\t0.00\t0.00\t4.00\t0.00\t0.00\t0.00\t0.00\t \n" +
            " \tNon-sick leave\tTCH-09-04-2013-044180\t9/2/2013\t9/8/2013\t8.00\t0.00\t0.00\t0.00\t0.00\t0.00\t8.00\t0.00\t \n" +
            " \tGrand Totals (17 records)\n" +
            " \t \t \t \t \t \t24.50";

    public static final String SAMPLE_SALES_FORCE_TEXT_WITH_ALTERNATE_VACATION_CODES = "\n" +
            "Filtered By:   Edit \n" +
            "   \tResource: Full Name equals Benjamin Davis,Davis Benjamin Clear \n" +
            "   \tAND Project Name contains sick Clear \n" +
            " \tProject Name\tTimecard Id\tStart Date\tEnd Date\tTotal Hours\tMonday Hours\tTuesday Hours\tWednesday Hours\tThursday Hours\tFriday Hours\tSaturday Hours\tSunday Hours\tTotal Days\n" +
            "\tSub-Project Name: Annual lv; vacation (4 records)\n" +
            " \t \t \t \t \t \t11.50\n" +
            " \tNon-sick leave\tTCH-07-21-2013-016842\t7/29/2013\t8/4/2013\t28.00\t0.00\t4.00\t8.00\t8.00\t8.00\t0.00\t0.00\t \n" +
            "\tSub-Project Name: FMLA Personal time (US only) (1 record)\n" +
            " \t \t \t \t \t \t1.00\n" +
            " \tNon-sick leave\tTCH-12-21-2012-000613\t12/31/2012\t1/6/2013\t8.00\t0.00\t0.00\t0.00\t0.00\t8.00\t0.00\t0.00\t \n" +
            "\tSub-Project Name: Dr, dentist ,antenatel (UK) (5 records)\n" +
            " \t \t \t \t \t \t7.00\n" +
            " \tSick leave\tTCH-02-25-2013-001699\t2/18/2013\t2/24/2013\t4.00\t0.00\t0.00\t0.00\t0.00\t0.00\t0.00\t4.00\t \n" +
            " \tSick leave\tTCH-03-28-2013-002225\t3/25/2013\t3/31/2013\t20.00\t0.00\t0.00\t8.00\t8.00\t4.00\t0.00\t0.00\t \n" +
            " \tSick leave\tTCH-04-22-2013-003111\t4/15/2013\t4/21/2013\t8.00\t8.00\t0.00\t0.00\t0.00\t0.00\t0.00\t0.00\t \n" +
            " \tSick leave\tTCH-07-12-2013-011704\t7/29/2013\t8/4/2013\t12.00\t4.00\t8.00\t0.00\t0.00\t0.00\t0.00\t0.00\t \n" +
            " \tSick leave\tTCH-07-21-2013-016841\t7/22/2013\t7/28/2013\t12.00\t0.00\t0.00\t8.00\t0.00\t4.00\t0.00\t0.00\t \n" +
            "\tSub-Project Name: FMLA Vacation time (US only) (7 records)\n" +
            " \t \t \t \t \t \t5.00\n" +
            " \tNon-sick leave\tTCH-12-21-2012-000612\t7/29/2013\t8/4/2013\t10.00\t8.00\t2.00\t0.00\t0.00\t0.00\t0.00\t0.00\t \n" +
            " \tGrand Totals (17 records)\n" +
            " \t \t \t \t \t \t24.50";

    @Before
    public void setUp() throws Exception {
        emptyMap = new HashMap<LocalDate, Double>();
        dateParserService = new DateParserService();
        salesForceParserService = new SalesForceParserService(dateParserService);
    }

    @Test
    public void shouldParseSalesForceExampleTextAndReturnTheNumberOfVacationDays() throws Exception {

        Map<LocalDate, Double> expectedVacationDaysUsed = new HashMap<LocalDate, Double>();

        expectedVacationDaysUsed.put(new LocalDate(2013, 7, 22), 2.0);
        expectedVacationDaysUsed.put(new LocalDate(2013, 7, 23), 3.0);
        expectedVacationDaysUsed.put(new LocalDate(2013, 7, 24), 4.0);
        expectedVacationDaysUsed.put(new LocalDate(2013, 7, 25), 5.0);
        expectedVacationDaysUsed.put(new LocalDate(2013, 7, 26), 6.0);
        expectedVacationDaysUsed.put(new LocalDate(2013, 7, 27), 7.0);
        expectedVacationDaysUsed.put(new LocalDate(2013, 7, 28), 8.0);

        Map<LocalDate, Double> actualVacationDaysUsed = salesForceParserService.extractDatesAndHoursFromSalesForceText(SAMPLE_SALES_FORCE_TEXT, Constants.VACATION_DAY_CODES);

        for(LocalDate vacationDate : expectedVacationDaysUsed.keySet()){
            assertThat(actualVacationDaysUsed.get(vacationDate), is(expectedVacationDaysUsed.get(vacationDate)));
        }
    }

    @Test
    public void shouldTakeTimeFromMultipleSubProjectNames() {

        Map<LocalDate, Double> expectedVacationDaysUsed = new HashMap<LocalDate, Double>();

        expectedVacationDaysUsed.put(new LocalDate(2013, 7, 29), 8.0);
        expectedVacationDaysUsed.put(new LocalDate(2013, 7, 30), 6.0);
        expectedVacationDaysUsed.put(new LocalDate(2013, 7, 31), 8.0);
        expectedVacationDaysUsed.put(new LocalDate(2013, 8, 1), 8.0);
        expectedVacationDaysUsed.put(new LocalDate(2013, 8, 2), 8.0);

        Map<LocalDate, Double> actualVacationDaysUsed = salesForceParserService.extractDatesAndHoursFromSalesForceText(SAMPLE_SALES_FORCE_TEXT_WITH_ALTERNATE_VACATION_CODES, Constants.VACATION_DAY_CODES);

        for(LocalDate vacationDate : expectedVacationDaysUsed.keySet()){
            assertThat(actualVacationDaysUsed.get(vacationDate), is(expectedVacationDaysUsed.get(vacationDate)));
        }
    }

    @Test
    public void shouldReturnEmptyMapIfNoLineContainsVacation() throws Exception {
        String salesForceText = "hi";

        Map<LocalDate, Double> actualMap = salesForceParserService.extractDatesAndHoursFromSalesForceText(salesForceText, Constants.VACATION_DAY_CODES);

        assertThat(actualMap, is(emptyMap));
    }

    @Test
    public void shouldReturnEmptyMapIfLastLineContainsFirstInstanceOfWordVacation() throws Exception {
        for (String incompleteInformation : Constants.PERSONAL_DAY_CODES){
            Map<LocalDate, Double> vacationDayMap = salesForceParserService.extractDatesAndHoursFromSalesForceText(incompleteInformation, Constants.VACATION_DAY_CODES);
            assertThat(vacationDayMap, is(emptyMap));
        }
    }

    @Test
    public void shouldReturnEmptyMapIfRandomStringDoesNotContainNonSickLeave() throws Exception {
        String salesForceText = "laksjdflkasdjf" + "\nlaksjdflkasdjf" + "\nlaksjdflkasdjf" + "\nlaksjdflkasdjf" +
                "vacation" + "\nlaksjdflkasdjf" + "\nlaksjdflkasdjf" + "\nlaksjdflkasdjf";

        Map<LocalDate, Double> vacationDayMap = salesForceParserService.extractDatesAndHoursFromSalesForceText(salesForceText, Constants.VACATION_DAY_CODES);

        assertThat(vacationDayMap, is(emptyMap));
    }

    @Test
    public void shouldReturnEmptyMapIfEmptyString() throws Exception {
        String salesForceText = "";

        Map<LocalDate, Double> vacationDayMap = salesForceParserService.extractDatesAndHoursFromSalesForceText(salesForceText, Constants.VACATION_DAY_CODES);
        assertThat(vacationDayMap, is(emptyMap));
    }
}
