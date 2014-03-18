Scenario: User gets number of vacation days accrued as of current date

Given I started on January 1 of this year
And I have <rolloverDays> rollover days
When I request my number of vacation days as of today
Then I should have <personalDays> personal days

Examples:
|rolloverDays|personalDays|
|0.0|7.0|