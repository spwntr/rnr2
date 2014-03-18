package com.thoughtworks.rnr.service;

import com.thoughtworks.rnr.model.AccrualRateCalculator;
import com.thoughtworks.rnr.model.Employee;
import com.thoughtworks.rnr.model.VacationCalculator;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VacationCalculatorService {

    @Autowired
    private VacationCalculator vacationCalculator;

    @Autowired
    public VacationCalculatorService(VacationCalculator vacationCalculator) {
        this.vacationCalculator = vacationCalculator;
    }

    public Double getVacationDays(Employee employee, AccrualRateCalculator accrualRateCalculator, LocalDate date) {
        return vacationCalculator.getVacationDays(employee, accrualRateCalculator, date);
    }

    public String getVacationCapNotice(Employee employee, AccrualRateCalculator accrualRateCalculator, LocalDate date) {
        double daysUntilCap = vacationCalculator.getDaysUntilCapIsReached(employee, accrualRateCalculator, date);
        String message = "You have reached your vacation day cap.";

        if (daysUntilCap >= 30.0) {
            message = "";
        } else if (daysUntilCap > 0.0) {
            message = "You are " + (int) Math.round(daysUntilCap) + " " + getPluralOrSingularDays(daysUntilCap) + " away from reaching your vacation day cap.";
        }

        return message;
    }

    private String getPluralOrSingularDays(double days){
        String result = "days";

        if((int) Math.round(days) == 1){
            result = "day";
        }

        return result;
    }
}
