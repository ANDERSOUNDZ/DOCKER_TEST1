package com.bankfy.bank_meet.infrastructure.adapters.input.dtos.movimiento;

import com.bankfy.bank_meet.domain.models.cuenta.Cuenta;
import com.bankfy.bank_meet.domain.models.movimiento.Movimiento;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record MovimientoRequestDTO(
        @NotBlank(message = "El número de cuenta es obligatorio") String numeroCuenta,
        @NotBlank(message = "El tipo de movimiento es obligatorio (Retiro/Deposito)") @Pattern(regexp = "^(Retiro|Deposito|Debito|Credito)$", message = "Tipo inválido. Use: Retiro, Deposito, Debito o Credito") String tipoMovimiento,
        @NotNull(message = "El valor es obligatorio") @DecimalMin(value = "0.01", message = "El valor debe ser mayor a cero") BigDecimal valor) {
    public Movimiento toEntity() {
        Movimiento m = new Movimiento();
        m.setTipoMovimiento(this.tipoMovimiento);
        m.setValor(this.valor);

        Cuenta c = new Cuenta();
        c.setNumeroCuenta(this.numeroCuenta);
        m.setCuenta(c);
        return m;
    }
}