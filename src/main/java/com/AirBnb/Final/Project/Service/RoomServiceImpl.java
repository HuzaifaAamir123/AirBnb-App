package com.AirBnb.Final.Project.Service;


import com.AirBnb.Final.Project.Dto.AdminRoomDto;
import com.AirBnb.Final.Project.Entity.Hotel;
import com.AirBnb.Final.Project.Entity.Room;
import com.AirBnb.Final.Project.Entity.User;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService{

    private final ModelMapper modelMapper;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryService inventoryService;


    private User getCurrentAdmin(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Transactional
    @Override
    public AdminRoomDto createNewRoom(Long hotelId, AdminRoomDto adminRoomDto) {

        log.info("admin is start the creation of new room");

        Hotel hotel=hotelRepository.findById(hotelId)
                .orElseThrow(()->{
                    log.error("hotel with id:{} is not found",hotelId);
                    return new ResourceNotFoundException("hotel with id: "+hotelId+" is not found");
                });

        if (!hotel.getOwner().equals(getCurrentAdmin())){
            log.error("only hotel manager can create new rooms");
            throw new AccessDeniedException("only hotel manager can create new rooms");
        }

        List<Room>rooms=hotel.getRooms();

        if (!rooms.isEmpty()){

            for (Room room:rooms){

                if (room.getName().equals(adminRoomDto.getName().toLowerCase())){
                    log.error("{} is already exists in {}",adminRoomDto.getName().toUpperCase(),hotel.getName().toUpperCase());
                    throw new AlreadyExistsException(adminRoomDto.getName().toUpperCase()+" is already exists in "+hotel.getName().toUpperCase());
                }

            }

        }


        Room newRoom=modelMapper.map(adminRoomDto,Room.class);
        newRoom.setName(adminRoomDto.getName().toLowerCase());
        newRoom.setHotel(hotel);
        newRoom= roomRepository.save(newRoom);


        if(newRoom.getHotel().isActive() && newRoom.getInventories().isEmpty()){
            inventoryService.initializeInventory(newRoom);
        }

        AdminRoomDto adminRoomDto1 =modelMapper.map(newRoom, AdminRoomDto.class);

        adminRoomDto1.setName(adminRoomDto1.getName().toUpperCase());
        adminRoomDto1.setHotel(newRoom.getHotel().getName().toUpperCase());

        log.info("{} is created ",adminRoomDto1.getName().toUpperCase());

        return adminRoomDto1;
    }

    @Override
    public AdminRoomDto getRoomById(Long roomId) {

        log.info("admin want to fetch the room with id:"+roomId);

        Room room=roomRepository.findById(roomId)
                .orElseThrow(()->{
                    log.error("room with id: "+roomId+" is not found");
                    return new ResourceNotFoundException("room with id: "+roomId+" is not found");
                });

        if (!room.getHotel().getOwner().equals(getCurrentAdmin())){
            log.info("only hotel manager can get rooms details\"");
            throw new AccessDeniedException("only hotel manager can get rooms details");
        }

        AdminRoomDto adminRoomDto =modelMapper.map(room, AdminRoomDto.class);

        adminRoomDto.setName(adminRoomDto.getName().toUpperCase());
        adminRoomDto.setHotel(room.getHotel().getName().toUpperCase());

        log.info("admin is successfully fetched the room with id:"+roomId);

        return adminRoomDto;
    }

    @Override
    public List<AdminRoomDto> getAllRoomsInHotel(Long hotelId) {

        log.info("admin want to fetch the all room in particular hotel");

        Hotel hotel=hotelRepository.findById(hotelId)
                .orElseThrow(()->{
                    log.error("hotel with id:{} is not found",hotelId);
                    return new ResourceNotFoundException("hotel with id: "+hotelId+" is not found");
                });

        if (!hotel.getOwner().equals(getCurrentAdmin())){
            log.error("only hotel manager can get rooms in hotel");
            throw new AccessDeniedException("only hotel manager can get rooms in hotel");
        }

        List<Room>rooms=hotel.getRooms();

        if (rooms.isEmpty()){
            log.error("rooms are not available in {}",hotel.getName().toUpperCase());
            throw new ResourceNotFoundException("rooms are not available in "+hotel.getName().toUpperCase());
        }

        log.info("admin fetched  all room in particular hotel successfully");

        return rooms.stream()
                .map(room -> {
                    AdminRoomDto adminRoomDto =modelMapper.map(room, AdminRoomDto.class);
                    adminRoomDto.setName(room.getName().toUpperCase());
                    adminRoomDto.setHotel(room.getHotel().getName().toUpperCase());
                    return adminRoomDto;
                }).collect(Collectors.toList());
    }

    @Override
    public AdminRoomDto updateRoomById(Long roomId, AdminRoomDto adminRoomDto) {

        log.info("admin want to update the room with id:"+roomId);

        Room room=roomRepository.findById(roomId)
                .orElseThrow(()->{
                    log.error("room with id: "+roomId+" is not found");
                    return new ResourceNotFoundException("room with id: "+roomId+" is not found");
                });

        if (!room.getHotel().getOwner().equals(getCurrentAdmin())){
            log.error("only hotel manager can update rooms in hotel");
            throw new AccessDeniedException("only hotel manager can update rooms in hotel");
        }

        modelMapper.map(adminRoomDto,room);
        room.setId(roomId);
        room.setName(adminRoomDto.getName().toLowerCase());
        room= roomRepository.save(room);

        AdminRoomDto adminRoomDto1 =modelMapper.map(room, AdminRoomDto.class);

        adminRoomDto1.setName(adminRoomDto.getName().toUpperCase());
        adminRoomDto1.setHotel(room.getHotel().getName().toUpperCase());

        log.info("admin is successfully updated the room with id:"+roomId);

        return adminRoomDto1;
    }

    @Transactional
    @Override
    public void deleteRoomById(Long roomId) {

        log.info("admin want to delete the room with id:"+roomId);

        Room room=roomRepository.findById(roomId)
                .orElseThrow(()->{
                    log.error("room with id: "+roomId+" is not found");
                    return new ResourceNotFoundException("room with id: "+roomId+" is not found");
                });

        if (!room.getHotel().getOwner().equals(getCurrentAdmin())){
            log.info("only hotel manager can delete rooms in hotel");
            throw new AccessDeniedException("only hotel manager can delete rooms in hotel");
        }

        inventoryRepository.deleteByRoom(room);

        roomRepository.delete(room);

        log.info("admin successfully delete the room with id:"+roomId);
    }
}
