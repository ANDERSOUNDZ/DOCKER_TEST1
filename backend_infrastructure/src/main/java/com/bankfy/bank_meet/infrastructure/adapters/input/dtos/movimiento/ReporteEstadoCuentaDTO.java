package com.bankfy.bank_meet.infrastructure.adapters.input.dtos.movimiento;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ReporteEstadoCuentaDTO {
    private String cliente;
    private String rangoFechas;
    private List<MovimientoReporteItemDTO> movimientos; 
    private BigDecimal totalCreditos;
    private BigDecimal totalDebitos;
    private String pdfBase64;
}