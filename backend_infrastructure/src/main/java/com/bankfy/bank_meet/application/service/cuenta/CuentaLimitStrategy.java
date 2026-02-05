package com.bankfy.bank_meet.application.service.cuenta;

import java.util.Map;

public class CuentaLimitStrategy {
    private static final Map<String, Integer> LIMITES = Map.of(
            "Ahorros", 3,
            "Corriente", 3,
            "Ahorro Programado", 5,
            "Poliza", 5);

    public static int getLimit(String tipo) {
        return LIMITES.getOrDefault(tipo, -1);
    }
}