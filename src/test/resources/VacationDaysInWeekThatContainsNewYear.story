Scenario: User takes vacation on January 2 and personal day on January 3, 2014 - Thursday and Friday of a week that started on Monday, December 30, 2013.

Given I took a day of vacation on January 2, 2014 and a personal day on January 3, 2014
And I started one month before the SalesForce accrual start date
And I have <rolloverDays> rollover days
When I request my number of vacation balance <days> days after the accrual start date
Then I should have <personalDays> personal days
And the number of vacation days I have is my daily accrual rate times <days>

Examples:
|rolloverDays|days|personalDays|
|1.0|7|6.0|