package com.bankfy.bank_meet.infrastructure.adapters.input.dtos.movimiento;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

import com.bankfy.bank_meet.domain.models.movimiento.Movimiento;

@Data
@Builder
public class ReporteEstadoCuentaDTO {
    private String cliente;
    private String rangoFechas;
    private List<Movimiento> movimientos;
    private BigDecimal totalCreditos;
    private BigDecimal totalDebitos;
    private String pdfBase64;
}