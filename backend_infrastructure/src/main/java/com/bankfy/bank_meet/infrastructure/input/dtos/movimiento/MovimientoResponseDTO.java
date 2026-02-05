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
        return new MovimientoResponseDTO(
                m.getFecha().toString(),
                m.getCuenta().getCliente().getNombre(),
                m.getCuenta().getNumeroCuenta(),
                m.getCuenta().getTipoCuenta(),
                m.getSaldoAnterior(), // Lo que capturamos antes del movimiento
                m.getCuenta().getEstado(),
                m.getValor(),
                m.getSaldo() // Saldo disponible después del movimiento
        );
    }
}