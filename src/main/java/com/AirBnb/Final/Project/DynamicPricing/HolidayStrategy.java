package com.AirBnb.Final.Project.DynamicPricing;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayStrategy {



    List<String> allHolidayDates(){

        return List.of(
                "2025-01-14",
                "2025-01-26",
                "2025-02-26",
                "2025-03-14",
                "2025-03-31",
                "2025-04-06",
                "2025-04-10",
                "2025-04-18",
                "2025-05-12",
                "2025-06-07",
                "2025-07-06",
                "2025-08-09",
                "2025-08-15",
                "2025-08-18",
                "2025-08-27",
                "2025-10-02",
                "2025-10-20",
                "2025-11-05",
                "2025-12-25"
        );
    }



}


