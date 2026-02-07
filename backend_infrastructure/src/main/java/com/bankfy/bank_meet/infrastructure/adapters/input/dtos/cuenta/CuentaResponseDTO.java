package com.bankfy.bank_meet.infrastructure.adapters.input.dtos.cuenta;

import java.math.BigDecimal;

import com.bankfy.bank_meet.domain.models.cuenta.Cuenta;

public record CuentaResponseDTO(
        Long id,
        Long clienteId,
        String numeroCuenta,
        String tipoCuenta,
        BigDecimal saldoActual,
        Boolean estado,
        String nombreCliente,
        String identificacionCliente) {
    public static CuentaResponseDTO fromEntity(Cuenta c) {
        return new CuentaResponseDTO(
                c.getId(),
                c.getCliente() != null ? c.getCliente().getId() : null,
                c.getNumeroCuenta(),
                c.getTipoCuenta(),
                c.getSaldoInicial(),
                c.getEstado(),
                c.getCliente() != null ? c.getCliente().getNombre() : "N/A",
                c.getCliente() != null ? c.getCliente().getIdentificacion() : "N/A");
    }
}