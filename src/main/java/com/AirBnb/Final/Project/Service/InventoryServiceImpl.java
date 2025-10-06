package com.AirBnb.Final.Project.Service;


import com.AirBnb.Final.Project.Entity.Inventory;
import com.AirBnb.Final.Project.Entity.Room;
import com.AirBnb.Final.Project.Entity.User;
import com.AirBnb.Final.Project.Repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService{

    private final ModelMapper modelMapper;
    private final InventoryRepository inventoryRepository;

    private User getCurrentAdmin(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    @Override
    public void initializeInventory(Room room) {

        log.info("admin want to create the inventory of {} ",room.getName().toUpperCase());

        if (!room.getHotel().getOwner().equals(getCurrentAdmin())){
            log.error("only hotel manager create the inventory of rooms");
            throw new AccessDeniedException("only hotel manager create the inventory of rooms");
        }

        LocalDate today=LocalDate.now();
        LocalDate end=today.plusMonths(3);

        for (;!today.isAfter(end);today=today.plusDays(1)){

            Inventory newInventory=Inventory.builder()
                    .room(room)
                    .hotel(room.getHotel())
                    .date(today)
                    .city(room.getHotel().getCity())
                    .totalCount(room.getTotalCount())
                    .reservedCount(0)
                    .bookedCount(0)
                    .basePrice(room.getBasePrice())
                    .price(room.getBasePrice())
                    .surgeFactor(BigDecimal.valueOf(1.2))
                    .close(false)
                    .build();

            inventoryRepository.save(newInventory);
        }

        log.info("admin successfully create the inventory of {} ",room.getName().toUpperCase());

    }

}
