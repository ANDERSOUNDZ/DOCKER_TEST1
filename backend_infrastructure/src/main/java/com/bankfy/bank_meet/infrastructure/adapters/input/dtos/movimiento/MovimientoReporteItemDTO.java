package com.bankfy.bank_meet.infrastructure.adapters.input.dtos.movimiento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public class MovimientoReporteItemDTO {
    @JsonProperty("Fecha")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime fecha;

    @JsonProperty("Cliente")
    private String cliente;

    @JsonProperty("Numero Cuenta")
    private String numeroCuenta;

    @JsonProperty("Tipo")
    private String tipo;

    @JsonProperty("Saldo Inicial")
    private BigDecimal saldoInicial;

    @JsonProperty("Estado")
    private Boolean estado;

    @JsonProperty("Movimiento")
    private BigDecimal valor;

    @JsonProperty("Saldo Disponible")
    private BigDecimal saldoDisponible;
}