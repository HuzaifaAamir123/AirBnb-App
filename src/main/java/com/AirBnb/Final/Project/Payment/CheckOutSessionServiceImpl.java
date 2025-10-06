package com.AirBnb.Final.Project.Payment;

import com.AirBnb.Final.Project.Entity.Booking;
import com.AirBnb.Final.Project.Entity.User;
import com.AirBnb.Final.Project.Enum.BookingStatus;
import com.AirBnb.Final.Project.Repository.BookingRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckOutSessionServiceImpl implements CheckOutSessionService{

    private final BookingRepository bookingRepository;

    @Override
    public String getCheckOutSessionUrl(Booking booking, String successfulUrl, String failedUrl) {

        log.info("for payment checkout session url section start here");

        User loginUSer= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {

            CustomerCreateParams customerParam=CustomerCreateParams.builder()
                    .setName(loginUSer.getName())
                    .setEmail(loginUSer.getEmail())
                    .build();

            Customer customer=Customer.create(customerParam);

            SessionCreateParams sessionParam= SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                    .setCustomer(customer.getId())
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("inr")
                                    .setUnitAmount(booking.getPrice().multiply(BigDecimal.valueOf(100)).longValue())
                                    .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName(booking.getHotel().getName().toUpperCase()+" + "+booking.getRoom().getName().toUpperCase())
                                            .setDescription("booking ID:"+booking.getId())
                                            .build())
                                    .build()
                            )
                            .build()
                    )
                    .setSuccessUrl(successfulUrl)
                    .setCancelUrl(failedUrl)
                    .build();

            Session session=Session.create(sessionParam);
            booking.setCheckOutSessionId(session.getId());
            booking.setBookingStatus(BookingStatus.PAYMENT_PENDING);
            bookingRepository.save(booking);

            log.info("for payment checkout session url section successfully done");

            return session.getUrl();

        }catch (StripeException e){

            log.error("stripe exception error while getiing checkout sesion url {}",e.getMessage());

            throw new RuntimeException(e);

        }

    }

}
