Scenario: User gets number of vacation days accrued as of current date

Given I started <days> days ago
And I have <rolloverDays> rollover days
When I request my number of vacation days as of today
Then the number of vacation days I have is my daily accrual rate times <days>

Examples:
|days|rolloverDays|
|14|0.0|