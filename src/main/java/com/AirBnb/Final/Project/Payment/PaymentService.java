package com.AirBnb.Final.Project.Payment;

import com.stripe.model.Event;

public interface PaymentService {

    String initiatePayment(Long bookingId);

    void getEventForBookingConfirmation(Event event);


}
