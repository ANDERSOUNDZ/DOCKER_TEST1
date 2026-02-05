package com.bankfy.bank_meet.domain.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movimiento")
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime fecha;

    @NotBlank(message = "El tipo de movimiento es obligatorio (Retiro/Deposito)")
    private String tipoMovimiento;

    @JsonProperty("movimiento")
    @NotNull(message = "El valor es obligatorio")
    private BigDecimal valor;

    private BigDecimal saldoAnterior;

    private BigDecimal saldo; 

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cuenta_id", nullable = false)
    @NotNull(message = "El movimiento debe estar asociado a una cuenta")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Cuenta cuenta;
}