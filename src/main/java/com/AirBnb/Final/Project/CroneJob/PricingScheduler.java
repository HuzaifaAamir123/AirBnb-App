package com.AirBnb.Final.Project.CroneJob;


import com.AirBnb.Final.Project.DynamicPricing.TotalStrategy;
import com.AirBnb.Final.Project.Entity.Hotel;
import com.AirBnb.Final.Project.Entity.HotelMinPrice;
import com.AirBnb.Final.Project.Entity.Inventory;
import com.AirBnb.Final.Project.Repository.HotelMinPriceRepository;
import com.AirBnb.Final.Project.Repository.HotelRepository;
import com.AirBnb.Final.Project.Repository.InventoryRepository;
import com.AirBnb.Final.Project.Repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PricingScheduler {

    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final TotalStrategy totalStrategy;

    @Scheduled(fixedRate = 5000)
    public void hotelPriceUpdations(){

         int pageNumber=0;
         int pageSize=100;

        while (true){

            Page<Hotel>hotelPage=hotelRepository.findAll(PageRequest.of(pageNumber,pageSize));

            List<Hotel>hotels=hotelPage.getContent();

            hotelMinPriceRepository.truncateHotelMinPrice();

            hotels.forEach(hotel -> {
                updateHotels(hotel);
            });

            if (!hotelPage.hasNext()){
                break;
            }

            pageNumber++;

        }


    }

    private void updateHotels(Hotel hotel) {

        List<Inventory>inventories=hotel.getInventories();

        updateInventoryPricing(inventories);

        updateHotelMinPrice(inventories);
    }


    private void updateInventoryPricing(List<Inventory> inventories) {

        for (Inventory inventory:inventories){
            inventory.setPrice(totalStrategy.updatePrice(inventory));
            inventoryRepository.save(inventory);
            log.info("inventory price {}",inventory.getPrice());
        }

    }


    private void updateHotelMinPrice(List<Inventory> inventories) {

        LocalDate today=LocalDate.now();
        LocalDate end=today.plusDays(30);

       for (Inventory inventory:inventories){

           if (!inventory.getDate().isBefore(today)  && !inventory.getDate().isAfter(end)){

               HotelMinPrice hotelMinPrice=HotelMinPrice.builder()
                       .hotel(inventory.getHotel())
                       .date(inventory.getDate())
                       .price(inventory.getPrice())
                       .build();

               hotelMinPrice= hotelMinPriceRepository.save(hotelMinPrice);
           }

       }

    }


}
