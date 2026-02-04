package com.bankfy.bank_meet.domain.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class IdGeneratorService {
    public String generateNumericId() {
        long timestamp = System.currentTimeMillis() % 1000000; // 6 dígitos del tiempo
        int random = ThreadLocalRandom.current().nextInt(100, 999); // 3 dígitos aleatorios
        return String.valueOf(timestamp) + random;
    }
}