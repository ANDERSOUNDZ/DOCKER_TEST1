package com.bankfy.bank_meet.infrastructure.input.dtos.movimiento;

import com.bankfy.bank_meet.domain.models.Movimientos;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record MovimientoResponseDTO(
        @JsonProperty("Fecha") String fecha,
        @JsonProperty("Cliente") String cliente,
        @JsonProperty("Numero Cuenta") String numeroCuenta,
        @JsonProperty("Tipo") String tipo,
        @JsonProperty("Saldo Inicial") BigDecimal saldoInicial,
        @JsonProperty("Estado") boolean estado,
        @JsonProperty("Movimiento") BigDecimal movimiento,
        @JsonProperty("Saldo Disponible") BigDecimal saldoDisponible) {
    // EL "MAPPER" ESTÁ AQUÍ ADENTRO: Un constructor estático simple
    public static MovimientoResponseDTO fromEntity(Movimientos m) {
        // Definimos el formato deseado
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return new MovimientoResponseDTO(
                m.getFecha().format(formatter), // <-- Cambio aquí
                m.getCuenta().getCliente().getNombre(),
                m.getCuenta().getNumeroCuenta(),
                m.getCuenta().getTipoCuenta(),
                m.getSaldoAnterior(),
                m.getCuenta().getEstado(),
                m.getValor(),
                m.getSaldo());
    }
}