package com.thoughtworks.rnr.service;

import com.thoughtworks.rnr.model.Constants;
import com.thoughtworks.rnr.model.Employee;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EmployeeService {
    public Employee createEmployee(LocalDate startDate, String rolloverDays, Map<LocalDate, Double> daysOff, Map<LocalDate, Double> personalDaysTaken, String initialAccrualRate) {
        double convertedRolloverDays = parseStringWithDefaultValue(rolloverDays, 0d);
        double convertedInitialAccrualRate = parseStringWithDefaultValue(initialAccrualRate, Constants.DEFAULT_ACCRUAL_RATE);

        return new Employee(startDate, convertedRolloverDays, daysOff, personalDaysTaken, convertedInitialAccrualRate);
    }

    private double parseStringWithDefaultValue(String userEntry, double defaultValue){
        double convertedValue = defaultValue;

        boolean userEnteredAValue = !userEntry.equals("");

        if(userEnteredAValue) {
            convertedValue = Double.parseDouble(userEntry);
        }

        return convertedValue;
    }
}