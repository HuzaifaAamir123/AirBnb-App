package com.AirBnb.Final.Project.Payment;


import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/webhook")
@RequiredArgsConstructor
public class WebHookController {

    private final PaymentService paymentService;

    @Value("${stripe.webhook.secret.key}")
    private String webhookSecretKey;

    @PostMapping(path = "/payments")
    public void getEventForBookingConfirmation(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader){

        try {

            Event event= Webhook.constructEvent(payload,sigHeader,webhookSecretKey);

            paymentService.getEventForBookingConfirmation(event);

        }catch (SignatureVerificationException e){
            throw new RuntimeException(e.getMessage());
        }

    }

}
