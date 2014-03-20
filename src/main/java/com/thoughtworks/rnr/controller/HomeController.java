package com.thoughtworks.rnr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.thoughtworks.rnr.model.*;
import com.thoughtworks.rnr.service.*;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@Controller
@RequestMapping("/")
public class HomeController {

    private final EmployeeService employeeService;
    private final SalesForceParserService salesForceParserService;
    private final VacationCalculatorService vacationCalculatorService;
    private final AccrualRateCalculator accrualRateCalculator;
    private final DateParserService dateParserService;
    private final PersonalDaysCalculator personalDaysCalculator;
    private SAMLService SAMLService;

    @Autowired
    public HomeController(EmployeeService employeeService, SalesForceParserService salesForceParserService,
                          VacationCalculatorService vacationCalculatorService, AccrualRateCalculator accrualRateCalculator,
                          DateParserService dateParserService, PersonalDaysCalculator personalDaysCalculator, SAMLService SAMLService) {
        this.employeeService = employeeService;
        this.salesForceParserService = salesForceParserService;
        this.vacationCalculatorService = vacationCalculatorService;
        this.accrualRateCalculator = accrualRateCalculator;
        this.dateParserService = dateParserService;
        this.personalDaysCalculator = personalDaysCalculator;
        this.SAMLService = SAMLService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String checkForSSO() {
        if (SAMLService.sessionToken()) {
            return "home";
        }
        return SAMLService.redirectToIDPWithSAMLRequest();
    }

//    @RequestMapping(value="redirect",method = RequestMethod.GET)
//    public String redirectCheck() {
//        return "redirect";
//    }

    @RequestMapping(value = "/", params={"startDate", "rolloverdays", "accrualRate", "salesForceText", "endDate"}, method = RequestMethod.POST)
    public ModelAndView postDate(@RequestParam("startDate")String startDate,
                             @RequestParam("rolloverdays")String rollover,
                             @RequestParam("accrualRate")String accrualRate,
                             @RequestParam("salesForceText")String salesForceText,
                             @RequestParam("endDate")String endDate) throws IOException, ParseException {

        LocalDate convertedStartDate = dateParserService.parse(startDate);
        LocalDate convertedEndDate = dateParserService.parse(endDate);

        Map<LocalDate, Double> parsedVacationDays = salesForceParserService.extractDatesAndHoursFromSalesForceText(salesForceText, Constants.VACATION_DAY_CODES);
        Map<LocalDate, Double> parsedPersonalDays = salesForceParserService.extractDatesAndHoursFromSalesForceText(salesForceText, Constants.PERSONAL_DAY_CODES);

        Employee employee = employeeService.createEmployee(convertedStartDate, rollover, parsedVacationDays, parsedPersonalDays, accrualRate);

        double vacationDays = vacationCalculatorService.getVacationDays(employee, accrualRateCalculator, convertedEndDate);
        double personalDays = personalDaysCalculator.calculatePersonalDays(employee, convertedStartDate, convertedEndDate);
        String capReachedMessage = vacationCalculatorService.getVacationCapNotice(employee, accrualRateCalculator, convertedEndDate);

        return showVacationDays(vacationDays, personalDays, rollover, accrualRate, salesForceText, startDate, endDate, capReachedMessage);
    }

    private ModelAndView showVacationDays(Double vacationDays, Double personalDays, String rollover,
                                          String accrualRate, String salesForceText, String startDate,
                                          String endDate, String capReachedMessage) {
        ModelMap model = new ModelMap();
        model.put("vacationDays", roundToNearestHundredth(vacationDays));
        model.put("personalDays", roundToNearestHundredth(personalDays));
        model.put("startDate", startDate);
        model.put("endDate", endDate);
        model.put("rollover", rollover);
        model.put("accrualRate", accrualRate);
        model.put("salesForceText", salesForceText);
        model.put("capReachedMessage", capReachedMessage);

        return new ModelAndView("home", "postedValues", model);
    }

    private Double roundToNearestHundredth(Double number){
        return (double) Math.round(number*100)/100;
    }
}