package com.AirBnb.Final.Project.DynamicPricing;


import com.AirBnb.Final.Project.Entity.Inventory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TotalStrategy {

    private final SurgeStrategy surgeStrategy;
    private final HolidayStrategy holidayStrategy;

    public BigDecimal updatePrice(Inventory inventory){

        LocalDate today=LocalDate.now();

        double occupancyRate= (double) (inventory.getBookedCount()/ inventory.getTotalCount());

        List<String>holidayApiDtoList=holidayStrategy.allHolidayDates();


       for (String holidayApiDto:holidayApiDtoList){

           LocalDate holidayDate=LocalDate.parse(holidayApiDto);

           if (!inventory.getDate().isBefore(today) && inventory.getDate().isBefore(today.plusDays(7)) && occupancyRate >=0.75 && holidayDate.equals(inventory.getDate())){
               return surgeStrategy.SurgePrice(inventory).multiply(BigDecimal.valueOf(1.3)).multiply(BigDecimal.valueOf(1.4)).multiply(BigDecimal.valueOf(1.5));
           }

            if (!inventory.getDate().isBefore(today) && inventory.getDate().isBefore(today.plusDays(7)) && holidayDate.equals(inventory.getDate())){
               return surgeStrategy.SurgePrice(inventory).multiply(BigDecimal.valueOf(1.3)).multiply(BigDecimal.valueOf(1.5));
           }

            if (occupancyRate >=0.75 && holidayDate.equals(inventory.getDate())){
               return surgeStrategy.SurgePrice(inventory).multiply(BigDecimal.valueOf(1.4)).multiply(BigDecimal.valueOf(1.5));
           }


       }


       if (!inventory.getDate().isBefore(today) && inventory.getDate().isBefore(today.plusDays(7)) && occupancyRate >=0.75){
           return surgeStrategy.SurgePrice(inventory).multiply(BigDecimal.valueOf(1.3)).multiply(BigDecimal.valueOf(1.4));
       }

       if (!inventory.getDate().isBefore(today) && inventory.getDate().isBefore(today.plusDays(7))){
           return surgeStrategy.SurgePrice(inventory).multiply(BigDecimal.valueOf(1.3));
       }

        if (!inventory.getDate().isBefore(today) && occupancyRate >=0.75){
            return surgeStrategy.SurgePrice(inventory).multiply(BigDecimal.valueOf(1.4));
        }

        for (String holidayApiDto:holidayApiDtoList){

            LocalDate holidayDate = LocalDate.parse(holidayApiDto);

            if (!inventory.getDate().isBefore(today) && holidayDate.equals(inventory.getDate())){
                return surgeStrategy.SurgePrice(inventory).multiply(BigDecimal.valueOf(1.5));
            }
        }

        return surgeStrategy.SurgePrice(inventory);
    }



}
