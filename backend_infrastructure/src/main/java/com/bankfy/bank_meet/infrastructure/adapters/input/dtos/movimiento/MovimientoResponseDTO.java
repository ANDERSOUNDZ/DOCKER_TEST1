package com.bankfy.bank_meet.infrastructure.adapters.input.dtos.movimiento;

import com.bankfy.bank_meet.domain.models.Movimiento;
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
    public static MovimientoResponseDTO fromEntity(Movimiento m) {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return new MovimientoResponseDTO(
                m.getFecha().format(formatter),
                m.getCuenta().getCliente().getNombre(),
                m.getCuenta().getNumeroCuenta(),
                m.getCuenta().getTipoCuenta(),
                m.getSaldoAnterior(),
                m.getCuenta().getEstado(),
                m.getValor(),
                m.getSaldo());
    }
}