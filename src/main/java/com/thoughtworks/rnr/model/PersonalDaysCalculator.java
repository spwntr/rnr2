package com.thoughtworks.rnr.model;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PersonalDaysCalculator {
    public double calculatePersonalDays(Employee employee, LocalDate startDate, LocalDate endDate) {
        double personalDaysAllotted = calculatePersonalDaysBasedOnStartDate(startDate, endDate);
        double personalDaysTakenDuringTimePeriod = 0.0;

        Map<LocalDate, Double> personalDaysTaken = employee.getPersonalDaysTaken();

        for (LocalDate dateOfPersonalDay : personalDaysTaken.keySet()) {
            if (dateOfPersonalDay.getYear() == endDate.getYear() && dateOfPersonalDay.isBefore(endDate)) {
                personalDaysTakenDuringTimePeriod += personalDaysTaken.get(dateOfPersonalDay) / 8;
            }
        }

        return personalDaysAllotted - personalDaysTakenDuringTimePeriod;
    }

    private double calculatePersonalDaysBasedOnStartDate(LocalDate startDate, LocalDate endDate) {
        int personalDays = 7;

        if(startDate.getYear() == endDate.getYear()){
            int month = startDate.getMonthOfYear();
            if(month >= 9){
                personalDays = 3;
            } else if (month >= 5){
                personalDays = 4;
            }
        }

        return personalDays;
    }
}
