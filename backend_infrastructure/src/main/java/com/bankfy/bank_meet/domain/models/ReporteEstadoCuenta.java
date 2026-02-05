package com.bankfy.bank_meet.domain.models;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ReporteEstadoCuenta {
    private String cliente;
    private String rangoFechas;
    private List<Movimientos> movimientos;
    private BigDecimal totalCreditos;
    private BigDecimal totalDebitos;
    private String pdfBase64;
}