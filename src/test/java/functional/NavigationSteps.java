package functional;

import com.thoughtworks.rnr.model.Constants;
import org.jbehave.core.annotations.*;
import org.joda.time.LocalDate;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class NavigationSteps extends UserJourneyBase {

    public static final String DATE_PICKER_MONTH =
            "//select[@class='ui-datepicker-month']";
    public static final String DATE_PICKER_YEAR =
            "//select[@class='ui-datepicker-year']";

    public static final String START_DATE_FIELD_ID = "start-date-picker";
    public static final String END_DATE_FIELD_ID = "end-date-picker";
    public static final String ROLLOVER_DAYS_FIELD_ID = "rolloverdays-field";
    public static final String PERSONAL_DAYS_FIELD_ID = "personal-days";
    public static final String SUBMIT_BUTTON_ID = "submit-button";
    public static final String VACATION_DAYS_ID = "vacation-days";
    public static final String SALESFORCE_INPUT_FIELD_ID = "sales-force-text";

    WebDriver driver;

    @BeforeScenario
    public void openBrowser() {
        driver = super.getDriver();
    }

    @AfterScenario
    public void clearFields() {
        driver.findElement(By.id(SALESFORCE_INPUT_FIELD_ID)).clear();
        driver.findElement(By.id(ROLLOVER_DAYS_FIELD_ID)).clear();
    }

    @AfterStories
    public void closeBrowser() {
        driver.quit();
    }

    @Given("I started <days> days ago")
    public void iEnterStartDate(@Named("days") int days) {
        LocalDate startDate = new LocalDate().minusDays(days);

        pickDate(START_DATE_FIELD_ID, startDate);
    }

    @Given("I started on January 1 of this year")
    public void iStartedOnJanuary1() {
        pickDate(START_DATE_FIELD_ID, new LocalDate());
    }

    @Given("I have <rolloverDays> rollover days")
    public void iHaveNoRolloverDays(@Named("rolloverDays") double rolloverDays){
        enterRolloverDays(rolloverDays);
    }

    @Given("I took a day of vacation on January 2, 2014 and a personal day on January 3, 2014")
    public void iTookVacationInTheFirstWeekOfJanuary2014() {
        String salesforceText = "\n" +
                "Filtered By:   Edit \n" +
                "   \tResource: Full Name equals Benjamin Davis,Davis Benjamin Clear \n" +
                "   \tAND Project Name contains sick Clear \n" +
                " \tProject Name\tTimecard Id\tStart Date\tEnd Date\tTotal Hours\tMonday Hours\tTuesday Hours\tWednesday Hours\tThursday Hours\tFriday Hours\tSaturday Hours\tSunday Hours\tTotal Days\n" +
                "\tSub-Project Name: Annual lv; vacation (4 records)\n" +
                " \t \t \t \t \t \t11.50\n" +
                " \tNon-sick leave\tTCH-07-21-2013-016842\t12/30/2013\t1/5/2014\t8.00\t0.00\t0.00\t0.00\t8.00\t0.00\t0.00\t0.00\t \n" +
                "\tSub-Project Name: Personal/Sick (US/CAN only) (5 records)\n" +
                " \t \t \t \t \t \t7.00\n" +
                " \tSick leave\tTCH-02-25-2013-001699\t12/30/2013\t1/5/2014\t8.00\t0.00\t0.00\t0.00\t0.00\t8.00\t0.00\t0.00\t \n" +
                " \tGrand Totals (17 records)\n" +
                " \t \t \t \t \t \t24.50";

        setTextWithoutTyping(SALESFORCE_INPUT_FIELD_ID, salesforceText);
    }

    @Given("I started one month before the SalesForce accrual start date")
    public void iStartedOneMonthBeforeSalesForceStartDate(){
        pickDate(START_DATE_FIELD_ID, Constants.SALESFORCE_START_DATE.minusMonths(1));
    }

    @Given("I started the year with <vacationDays> vacation days")
    public void iStartedWith(@Named("vacationDays") double vacationDays) {
        sendTextToField(ROLLOVER_DAYS_FIELD_ID, Double.toString(vacationDays));
    }

    @When("I request my number of vacation days as of today")
    public void iClickSubmit() {
        driver.findElement(By.id(SUBMIT_BUTTON_ID)).click();
    }

    @When("I request my number of vacation balance <days> days after the accrual start date")
    public void requestVacationBalanceAsOf(@Named("days") int days){
        LocalDate endDate = Constants.SALESFORCE_START_DATE.plusDays(days);
        pickDate(END_DATE_FIELD_ID, endDate);
        iClickSubmit();
    }

    @Then("I should have <personalDays> personal days")
    public void shouldHavePersonalDays(@Named("personalDays") double personalDays) {
        double actualPersonalDays = Double.parseDouble(getTextFromFieldByID(PERSONAL_DAYS_FIELD_ID));

        assertThat(actualPersonalDays, is(personalDays));
    }

    @Then("the number of vacation days I have is my daily accrual rate times <days>")
    public void vacationDaysAccruedAfter(@Named("days") int days) {
        double actualVacationDays = Double.parseDouble(getTextFromFieldByID(VACATION_DAYS_ID));
        double expectedVacationDays = Math.round((Constants.DEFAULT_ACCRUAL_RATE / Constants.YEAR_IN_DAYS) * days * 100) / 100.0;

        assertThat(actualVacationDays, is(expectedVacationDays));
    }

    private void pickDate(String fieldID, LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthOfYear();
        int day = date.getDayOfMonth();

        driver.findElement(By.id(fieldID)).click();

        Select yearSelect = new Select(driver.findElement(By.xpath(DATE_PICKER_YEAR)));
        yearSelect.selectByValue(String.valueOf(year));

        Select monthSelect = new Select(driver.findElement(By.xpath(DATE_PICKER_MONTH)));
        monthSelect.selectByValue(String.valueOf(month - 1));

        WebElement dateWidget = driver.findElement(By.id("ui-datepicker-div"));
        List<WebElement> columns = dateWidget.findElements(By.tagName("td"));

        for (WebElement cell : columns) {
            if (cell.getText().equals(String.valueOf(day))) {
                cell.findElement(By.linkText(String.valueOf(day))).click();
                break;
            }
        }
    }

    private void enterRolloverDays(double rolloverDays) {
        String rolloverDaysAsString = Double.toString(rolloverDays);
        sendTextToField(ROLLOVER_DAYS_FIELD_ID, rolloverDaysAsString);
    }

    private String getTextFromFieldByID(String id) {
        return driver.findElement(By.id(id)).getText();
    }

    private void sendTextToField(String fieldID, String textToSend) {
        driver.findElement(By.id(fieldID)).sendKeys(textToSend);
    }

    private void setTextWithoutTyping(String fieldID, String value) {
        WebElement element = driver.findElement(By.id(fieldID));
        ((JavascriptExecutor)driver).executeScript("arguments[0].value = arguments[1]", element, value);
    }
}