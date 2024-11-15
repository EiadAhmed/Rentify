package com.io.rentify.payment;

import com.io.rentify.webtoken.JwtService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment/secure")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payment-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfoRequest paymentInfoRequest)
            throws StripeException {

        PaymentIntent paymentIntent = paymentService.createPaymentIntent(paymentInfoRequest);
        String paymentStr = paymentIntent.toJson();

        return new ResponseEntity<>(paymentStr, HttpStatus.OK);
    }

    @PutMapping("/payment-complete")
    public ResponseEntity<String> stripePaymentComplete(@RequestHeader(value="Authorization") String token)
            throws Exception {

        // Remove "Bearer " prefix from token if it exists
        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;

        // Extract user email from JWT
        String userEmail = jwtService.extractUsername(jwt);  // Use this if username is actually the email

        if (userEmail == null) {
            throw new Exception("User email is missing");
        }

        return paymentService.stripePayment(userEmail);
    }
}
