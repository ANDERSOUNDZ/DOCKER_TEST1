package com.bankfy.bank_meet.domain.service.movimiento;

import com.bankfy.bank_meet.domain.models.Movimientos;
import com.bankfy.bank_meet.domain.models.ReporteEstadoCuenta;
import com.bankfy.bank_meet.domain.ports.in.movimiento.GetReporteUseCase;
import com.bankfy.bank_meet.infrastructure.output.MovimientosRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetReporteService implements GetReporteUseCase {

    private final MovimientosRepository movimientosRepository;

    @Override
    public ReporteEstadoCuenta generarReporte(Long clienteId, LocalDateTime inicio, LocalDateTime fin) {
        List<Movimientos> movs = movimientosRepository.findByClienteAndFechaRange(clienteId, inicio, fin);

        if (movs.isEmpty())
            throw new IllegalArgumentException("No hay movimientos para este cliente en el rango de fechas.");

        // Calcular Totales
        BigDecimal totalCreditos = movs.stream()
                .map(Movimientos::getValor)
                .filter(v -> v.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDebitos = movs.stream()
                .map(Movimientos::getValor)
                .filter(v -> v.compareTo(BigDecimal.ZERO) < 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String clienteNombre = movs.get(0).getCuenta().getCliente().getNombre();

        // Generar PDF y convertir a Base64
        String pdfBase64 = generarPdfBase64(movs, clienteNombre, inicio, fin);

        return ReporteEstadoCuenta.builder()
                .cliente(clienteNombre)
                .rangoFechas(inicio + " a " + fin)
                .movimientos(movs)
                .totalCreditos(totalCreditos)
                .totalDebitos(totalDebitos)
                .pdfBase64(pdfBase64)
                .build();
    }

    private String generarPdfBase64(List<Movimientos> movs, String cliente, LocalDateTime inicio, LocalDateTime fin) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("ESTADO DE CUENTA - BANKFY"));
            document.add(new Paragraph("Cliente: " + cliente));
            document.add(new Paragraph("Rango: " + inicio + " a " + fin));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.addCell("Fecha");
            table.addCell("Tipo");
            table.addCell("Valor");
            table.addCell("Saldo Disponible");

            for (Movimientos m : movs) {
                table.addCell(m.getFecha().toString());
                table.addCell(m.getTipoMovimiento());
                table.addCell(m.getValor().toString());
                table.addCell(m.getSaldo().toString());
            }

            document.add(table);
            document.close();
            return Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF", e);
        }
    }
}