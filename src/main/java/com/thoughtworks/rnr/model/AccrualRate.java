package com.thoughtworks.rnr.model;

public enum AccrualRate {
    LESS_THAN_ONE_YEAR(Constants.YEAR_IN_DAYS, 10.0),
    LESS_THAN_THREE_YEARS(Constants.YEAR_IN_DAYS * 3, 15.0),
    LESS_THAN_SIX_YEARS(Constants.YEAR_IN_DAYS * 6, 20.0),
    MORE_THAN_SIX_YEARS(Double.MAX_VALUE, 25.0);

    private double accrualRate;
    private double tenure;

    private AccrualRate(double tenure, double accrualRate) {
        this.accrualRate = accrualRate;
        this.tenure = tenure;
    }

    public double getTenure() {
        return tenure;
    }

    public double getAccrualRate(){
        return accrualRate;
    }
}