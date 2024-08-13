package com.example.recommendation.functionality;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/payments")
public class StreamingPaymentsProcessor {

    @PostMapping("/process")
    public ResponseEntity<String> processPayments() {
        try (ChecksumOutputStream checksumStream = new ChecksumOutputStream(new ByteArrayOutputStream())) {
            streamPaymentsToStorage(checksumStream);
            return ResponseEntity.ok("Checksum: " + checksumStream.getChecksum());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing payments");
        }
    }

    private void streamPaymentsToStorage(OutputStream storage) throws IOException {
        for (int i = 0; i < 10; i++) {
            storage.write(new byte[]{1, 2, 3, 4, 5});
        }
    }
}

