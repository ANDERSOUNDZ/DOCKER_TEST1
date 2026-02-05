package com.bankfy.bank_meet.application.service.config;

import org.springframework.stereotype.Service;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class IdGeneratorService {
    public String generateNumericId() {
        long timestamp = System.currentTimeMillis() % 1000000;
        int random = ThreadLocalRandom.current().nextInt(100, 999);
        return String.valueOf(timestamp) + random;
    }

    public String generateAccountNumber() {
        long number = (long) (Math.random() * 9_000_000_000L) + 1_000_000_000L;
        return String.valueOf(number);
    }
}