package com.AirBnb.Final.Project.Payment;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "/booking")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping(path = "/{bookingId}/payment")
    ResponseEntity<Map<String,String>> initiatePayment(@PathVariable Long bookingId){

        String checkOutSessionUrl= paymentService.initiatePayment(bookingId);

        Map<String,String>map=Map.of("checkOutSessionUrl",checkOutSessionUrl);

        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }


}
