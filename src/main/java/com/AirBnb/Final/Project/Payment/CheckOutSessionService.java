package com.AirBnb.Final.Project.Payment;

import com.AirBnb.Final.Project.Entity.Booking;

public interface CheckOutSessionService {

    String  getCheckOutSessionUrl(Booking booking,String successfulUrl,String failedUrl);

}
