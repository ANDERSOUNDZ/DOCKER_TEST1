package com.bankfy.bank_meet.infrastructure.adapters.input.dtos.cuenta;

import com.bankfy.bank_meet.domain.models.Cuenta;
import java.math.BigDecimal;

public record CuentaResponseDTO(
        Long id,
        String numeroCuenta,
        String tipoCuenta,
        BigDecimal saldoActual,
        Boolean estado,
        String nombreCliente,
        String identificacionCliente) {
    public static CuentaResponseDTO fromEntity(Cuenta c) {
        return new CuentaResponseDTO(
                c.getId(),
                c.getNumeroCuenta(),
                c.getTipoCuenta(),
                c.getSaldoInicial(),
                c.getEstado(),
                c.getCliente() != null ? c.getCliente().getNombre() : "N/A",
                c.getCliente() != null ? c.getCliente().getIdentificacion() : "N/A");
    }
}