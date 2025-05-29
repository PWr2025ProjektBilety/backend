package com.example.backend.purchasedticket.service;

import org.springframework.stereotype.Component;
import java.util.Random;
import java.time.Instant;

@Component
public class PurchasedTicketCodeGenerator {


        private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        private static final int RANDOM_PART_LENGTH = 4;
        private final Random random = new Random();

        public String generateCode() {
            long timestamp = Instant.now().toEpochMilli();
            String randomPart = generateRandomPart();
            return timestamp + "-" + randomPart;
        }

        private String generateRandomPart() {
            StringBuilder sb = new StringBuilder(RANDOM_PART_LENGTH);
            for (int i = 0; i < RANDOM_PART_LENGTH; i++) {
                sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
            }
            return sb.toString();
        }
}

