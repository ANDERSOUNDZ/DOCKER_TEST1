package com.bankfy.bank_meet.infrastructure.input.dtos.cuenta;

import com.bankfy.bank_meet.domain.models.Cliente;
import com.bankfy.bank_meet.domain.models.Cuenta;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CuentaRequestDTO(
        @NotBlank(message = "El tipo de cuenta es obligatorio") @Pattern(regexp = "^(Ahorros|Corriente|Ahorro Programado|Poliza)$", message = "Tipo de cuenta inválido. Opciones: Ahorros, Corriente, Ahorro Programado, Poliza") String tipoCuenta,

        @NotNull(message = "El saldo inicial es obligatorio") @DecimalMin(value = "0.0", message = "El saldo inicial no puede ser negativo") BigDecimal saldoInicial,

        @NotNull(message = "El ID del cliente es obligatorio") @Positive(message = "ID de cliente inválido") Long clienteId) {
    public Cuenta toEntity() {
        Cuenta c = new Cuenta();
        c.setTipoCuenta(this.tipoCuenta);
        c.setSaldoInicial(this.saldoInicial);
        c.setEstado(true); // Por defecto activa al crear

        Cliente cl = new Cliente();
        cl.setId(this.clienteId);
        c.setCliente(cl);
        return c;
    }
}