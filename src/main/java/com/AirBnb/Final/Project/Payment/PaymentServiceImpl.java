package com.AirBnb.Final.Project.Payment;


import com.AirBnb.Final.Project.Entity.Booking;
import com.AirBnb.Final.Project.Entity.Inventory;
import com.AirBnb.Final.Project.Entity.User;
import com.AirBnb.Final.Project.Enum.BookingStatus;
import com.AirBnb.Final.Project.Exception.BookingExpiredException;
import com.AirBnb.Final.Project.Exception.ResourceNotFoundException;
import com.AirBnb.Final.Project.Repository.BookingRepository;
import com.AirBnb.Final.Project.Repository.InventoryRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService{

    private final InventoryRepository inventoryRepository;
    private final BookingRepository bookingRepository;
    private final CheckOutSessionService checkOutSessionService;


    @Value("${stripe.successful.url}")
    private String successfulUrl;

    @Value("${stripe.failed.url}")
    private String failedUrl;

    @Transactional
    @Override
    public String initiatePayment(Long bookingId) {

        log.info("initiate Payment start here");

        Booking booking=bookingRepository.findById(bookingId)
                .orElseThrow(()->{
                    log.error("booking with id:{} is not found",bookingId);
                    return new ResourceNotFoundException("booking with id:"+bookingId+" is not found");
                });

        User loginUser= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!loginUser.equals(booking.getUser())){
            log.error("booking is not link with user {}",loginUser.getName());
            throw new AccessDeniedException("booking is not link with user "+loginUser.getName());
        }

        if(hasBookingExpired(booking)){
            log.error("booking with id:{} is expired",booking.getId());
            throw new BookingExpiredException("booking with id:"+booking.getId()+" is expired");
        }

        String checkOutSessionUrl= checkOutSessionService.getCheckOutSessionUrl(booking,successfulUrl,failedUrl);

        log.info("initiate Payment successfully done here");

        return checkOutSessionUrl;
    }

    @Transactional
    @Override
    public void getEventForBookingConfirmation(Event event) {

        log.info("getEventForBookingConfirmation method start here");

        if ("checkout.session.completed".equals(event.getType())){

            log.info("before session");

            Session session= (Session) event.getDataObjectDeserializer().getObject().orElse(null);

            log.info("after session");

            if (session==null){
                return;
            }

            log.info("before booking");

            Booking booking=bookingRepository.findByCheckOutSessionId(session.getId())
                    .orElseThrow(()->{
                        log.error("booking with session id;{} is not found",session.getId());
                        return new ResourceNotFoundException("booking with session id "+session.getId()+" is not found");
                    });

            booking.setBookingStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);

            log.info("after booking");

            List<Inventory>inventories=inventoryRepository.findAndReserveInventory(
                    booking.getHotel().getId(),
                    booking.getRoom().getId(),
                    booking.getCheckInDate(),
                    booking.getCheckOutDate(),
                    booking.getTotalRooms()
            );

            inventoryRepository.confirmBooking(
                    booking.getHotel().getId(),
                    booking.getRoom().getId(),
                    booking.getCheckInDate(),
                    booking.getCheckOutDate(),
                    booking.getTotalRooms()
            );


            log.info("confirmed booking successfully");

        }else {
            log.error("event type mismatch {}",event.getType());
        }

    }



    private boolean hasBookingExpired(Booking booking){
        return booking.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(30));
    }

}
