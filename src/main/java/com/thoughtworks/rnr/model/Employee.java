package com.thoughtworks.rnr.model;

import org.joda.time.LocalDate;

import java.util.Map;

public class Employee {
    private final LocalDate startDate;
    private final double rolloverDays;
    private final Map<LocalDate, Double> daysOff;
    private final Map<LocalDate, Double> personalDaysTaken;
    private double initialAccrualRate;

    public Employee(LocalDate startDate, double rolloverDays, Map<LocalDate, Double> daysOff, Map<LocalDate, Double> personalDaysTaken, double initialAccrualRate) {
        this.startDate = startDate;
        this.rolloverDays = rolloverDays;
        this.daysOff = daysOff;
        this.personalDaysTaken = personalDaysTaken;
        this.initialAccrualRate = initialAccrualRate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public double getRolloverDays() {
        return rolloverDays;
    }

    public Map<LocalDate, Double> getDaysOff() {
        return daysOff;
    }

    public Map<LocalDate, Double> getPersonalDaysTaken() {
        return personalDaysTaken;
    }

    public double getInitialAccrualRate() {
        return initialAccrualRate;
    }
}
