package com.example.recommendation.functionality;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

@RestController
@RequestMapping("/payments")
public class StreamingPaymentsProcessor2 {

    @PostMapping("/process2")
    public ResponseEntity<String> processPayments2() {
        Queue<Integer> paymentQueue = new LinkedList<>();

        streamPayments(paymentQueue::add);
        storePayments(paymentQueue.iterator());

        return ResponseEntity.ok("Payments processed successfully");
    }

    private void streamPayments(Consumer<Integer> callbackFn) {
        for (int i = 0; i < 10; i++) {
            callbackFn.accept(i);
        }
    }

    private void storePayments(Iterator<Integer> amountIterator) {
        while (amountIterator.hasNext()) {
            System.out.println("Stored payment: " + amountIterator.next());
        }
    }
}

