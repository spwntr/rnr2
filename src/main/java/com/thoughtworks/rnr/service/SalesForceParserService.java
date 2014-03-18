package com.thoughtworks.rnr.service;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SalesForceParserService {

    private final DateParserService dateParser;

    @Autowired
    public SalesForceParserService(DateParserService dateParser) {
        this.dateParser = dateParser;
    }

    public Map<LocalDate, Double> extractDatesAndHoursFromSalesForceText(String salesForceText, List<String> listOfTimeOffCodes) {
        Map<LocalDate, Double> daysAndHours = new HashMap<LocalDate, Double>();
        List<String> subProjects = Arrays.asList(salesForceText.split("Sub-Project Name"));

        for (String subProject : subProjects) {
            List<String> subProjectLines = Arrays.asList(subProject.split("\n"));

            if (isATimeOffCode(subProjectLines.get(0), listOfTimeOffCodes) && subProjectLines.size() > 2) {
                for (int line = 2; line < subProjectLines.size() - 1; line++) {

                    if(subProjectLines.get(line).contains("Grand Totals")){
                        break;
                    }

                    List<String> parsedInformation = Arrays.asList(subProjectLines.get(line).split("\t"));

                    LocalDate startOfWeek = dateParser.parse(parsedInformation.get(3));

                    for(int dayOfWeek = 0; dayOfWeek <= 6; dayOfWeek++){
                        double hoursForDayOfWeek = Double.parseDouble(parsedInformation.get(dayOfWeek + 6));

                        LocalDate day = startOfWeek.plusDays(dayOfWeek);

                        if(hoursForDayOfWeek > 0.0){
                            if (daysAndHours.get(day) == null) {
                                daysAndHours.put(day, hoursForDayOfWeek);
                            } else {
                                double oldNumberOfHours = daysAndHours.get(day);
                                daysAndHours.put(day, hoursForDayOfWeek + oldNumberOfHours);
                            }
                        }
                    }
                }
            }
        }

        return daysAndHours;
    }

    private boolean isATimeOffCode(String stringToCheck, List<String> listOfTimeOffCodes) {
        boolean isACode = false;

        for (String code : listOfTimeOffCodes) {
            if (stringToCheck.contains(code)) {
                isACode = true;
                break;
            }
        }

        return isACode;
    }
}
