package com.thoughtworks.rnr.model;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.joda.time.Days.daysBetween;

@Component
public class AccrualRateCalculator {
    final double MAX_VACATION_DAYS = 30d;

    public double calculateDailyAccrualRate(Employee employee, LocalDate endDate) {
        double rate = 0;

        double tenure = daysBetween(employee.getStartDate(), endDate).getDays();

        double initialAccrualRate = employee.getInitialAccrualRate();

        for(AccrualRate accrualRate : AccrualRate.values()){
            if(tenure < accrualRate.getTenure()){
                rate = max(accrualRate.getAccrualRate(), initialAccrualRate) / Constants.YEAR_IN_DAYS;
                break;
            }
        }

        return rate;
    }

    public double calculateVacationDayCap(Employee employee, LocalDate endDate){
        double currentAccrualRate = calculateDailyAccrualRate(employee, endDate);
        return min(currentAccrualRate * 1.5 * Constants.YEAR_IN_DAYS, MAX_VACATION_DAYS);
    }
}
