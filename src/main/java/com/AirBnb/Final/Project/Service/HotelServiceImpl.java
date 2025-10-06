package com.AirBnb.Final.Project.Service;


import com.AirBnb.Final.Project.Dto.AdminHotelDto;
import com.AirBnb.Final.Project.Entity.Hotel;
import com.AirBnb.Final.Project.Entity.Room;
import com.AirBnb.Final.Project.Entity.User;
import com.AirBnb.Final.Project.Enum.Role;
import com.AirBnb.Final.Project.Exception.AlreadyExistsException;
import com.AirBnb.Final.Project.Exception.ResourceNotFoundException;
import com.AirBnb.Final.Project.Repository.HotelRepository;
import com.AirBnb.Final.Project.Repository.InventoryRepository;
import com.AirBnb.Final.Project.Repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService{

    private final ModelMapper modelMapper;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryService inventoryService;

    private User getCurrentAdmin(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public AdminHotelDto createNewHotel(AdminHotelDto adminHotelDto) throws RuntimeException{

        log.info("admin is start the creation of new Hotel");

        if (!getCurrentAdmin().getRole().contains(Role.HOTEL_MANAGER)){
            log.error("only hotel manager can access hotel details");
            throw new AccessDeniedException("only hotel manager can create new hotel");
        }

        Optional<Hotel>optionalHotel=hotelRepository
                .findByContactInfo_Email(adminHotelDto.getContactInfo().getEmail().toLowerCase());

        if (optionalHotel.isPresent()){
            log.error("hotel with email: {} is already exists ",adminHotelDto.getContactInfo().getEmail().toLowerCase());
            throw new AlreadyExistsException("hotel with email: "
                    + adminHotelDto.getContactInfo().getEmail().toLowerCase()+" is already exist");
        }

        Hotel newHotel=modelMapper.map(adminHotelDto,Hotel.class);
        newHotel.setActive(false);
        newHotel.setName(newHotel.getName().toLowerCase());
        newHotel.getContactInfo().setEmail(newHotel.getContactInfo().getEmail().toLowerCase());
        newHotel.setCity(newHotel.getCity().toLowerCase());
        newHotel.setOwner(getCurrentAdmin());
        newHotel= hotelRepository.save(newHotel);

        AdminHotelDto adminHotelDto1 =modelMapper.map(newHotel, AdminHotelDto.class);
        adminHotelDto1.setName(newHotel.getName().toUpperCase());
        adminHotelDto1.setCity(newHotel.getCity().toUpperCase());

        log.info("{} is created ",adminHotelDto1.getName().toUpperCase());

        return adminHotelDto1;
    }

    @Override
    public AdminHotelDto getHotelById(Long hotelId) {

        log.info("admin want to fetch the hotel with id:"+hotelId);

        Hotel hotel=hotelRepository.findById(hotelId)
                .orElseThrow(()->{
                    log.error("hotel with id:{} is not found",hotelId);
                    return new ResourceNotFoundException("hotel with id: "+hotelId+" is not found");
                });

        if (!hotel.getOwner().equals(getCurrentAdmin())){
            log.error("only hotel manager can access hotel details");
            throw new AccessDeniedException("only hotel manager can access hotel details");
        }

        AdminHotelDto adminHotelDto =modelMapper.map(hotel, AdminHotelDto.class);
        adminHotelDto.setName(hotel.getName().toUpperCase());
        adminHotelDto.setCity(hotel.getCity().toUpperCase());

        log.info("admin is successfully fetched the hotel with id:"+hotelId);

        return adminHotelDto;
    }

    @Override
    public AdminHotelDto updateHotel(Long hotelId, AdminHotelDto adminHotelDto) {

        log.info("admin want to update the hotel with id:"+hotelId);

        Hotel hotel=hotelRepository.findById(hotelId)
                .orElseThrow(()->{
                    log.error("hotel with id:{} is not found",hotelId);
                    return new ResourceNotFoundException("hotel with id: "+hotelId+" is not found");
                });

        if (!hotel.getOwner().equals(getCurrentAdmin())){
            log.error("only hotel manager can update hotel details");
            throw new AccessDeniedException("only hotel manager can update hotel details");
        }

        modelMapper.map(adminHotelDto,hotel);
        hotel.setId(hotelId);

        hotel= hotelRepository.save(hotel);

        AdminHotelDto adminHotelDto1 =modelMapper.map(hotel, AdminHotelDto.class);
        adminHotelDto1.setName(hotel.getName().toUpperCase());
        adminHotelDto1.setCity(hotel.getCity().toUpperCase());

        log.info("admin is successfully updated the hotel with id:"+hotelId);

        return adminHotelDto1;
    }

    @Transactional
    @Override
    public AdminHotelDto partialUpdateHotel(Long hotelId) {

        log.info("admin want to partially update the hotel with id:"+hotelId);

        Hotel hotel=hotelRepository.findById(hotelId)
                .orElseThrow(()->{
                    log.error("hotel with id:{} is not found",hotelId);
                    return new ResourceNotFoundException("hotel with id: "+hotelId+" is not found");
                });

        if (!hotel.getOwner().equals(getCurrentAdmin())){
            log.error("only hotel manager can create hotel inventories");
            throw new AccessDeniedException("only hotel manager can create hotel inventories");
        }

        hotel.setActive(true);

        //TODO: if hotel is active then initialize hotel's inventories

        if (hotel.isActive()){

            List<Room>rooms=hotel.getRooms();

            for (Room room:rooms){

                if (room.getInventories().isEmpty()){
                    inventoryService.initializeInventory(room);
                }

            }

            log.trace("hotel is active so we try update the hotel's inventory");
        }


        AdminHotelDto adminHotelDto =modelMapper.map(hotel, AdminHotelDto.class);
        adminHotelDto.setName(hotel.getName().toUpperCase());
        adminHotelDto.setCity(hotel.getCity().toUpperCase());

        log.info("admin is successfully partially update the hotel with id:"+hotelId);

        return adminHotelDto;
    }

    @Transactional
    @Override
    public void deleteHotelById(Long hotelId) {

        log.info("admin want to delete the hotel with id:"+hotelId);

        Hotel hotel=hotelRepository.findById(hotelId)
                .orElseThrow(()->{
                    log.error("hotel with id:{} is not found",hotelId);
                    return new ResourceNotFoundException("hotel with id: "+hotelId+" is not found");
                });

        if (!hotel.getOwner().equals(getCurrentAdmin())){
            log.error("only hotel manager can delete hotels");
            throw new AccessDeniedException("only hotel manager can delete hotels");
        }

        inventoryRepository.deleteByHotel(hotel);

        roomRepository.deleteByHotel(hotel);

        hotelRepository.delete(hotel);

        log.info("admin is successfully delete the hotel with id:"+hotelId);
    }

    @Override
    public List<AdminHotelDto> getAllHotelsForAdmin() {

        log.info("admin want to fetch all hotels");

        if (!getCurrentAdmin().getRole().equals(Role.HOTEL_MANAGER)){
            log.error("only hotel manager can delete hotels");
            throw new AccessDeniedException("only hotel manager can create new hotel");
        }

        List<Hotel>hotels=hotelRepository.findByOwner(getCurrentAdmin());

        if (hotels.isEmpty()){
            log.error("no hotels belong to this hotel manager");
            throw new ResourceNotFoundException("no hotels belong to this hotel manager");
        }

        List<AdminHotelDto>adminHotelDtoList=hotels.stream()
                .map(hotel -> {
                    AdminHotelDto adminHotelDto=modelMapper.map(hotel, AdminHotelDto.class);
                    adminHotelDto.setName(hotel.getName().toUpperCase());
                    adminHotelDto.setCity(hotel.getCity().toUpperCase());
                    return adminHotelDto;
                }).toList();

        log.info("admin successfully fetched all hotels");

        return adminHotelDtoList;
    }

}
