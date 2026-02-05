package com.bankfy.bank_meet.application.service.movimiento;

import com.bankfy.bank_meet.application.ports.input.movimiento.GetReporteUseCase;
import com.bankfy.bank_meet.application.ports.output.cliente.ClientePersistencePort;
import com.bankfy.bank_meet.application.ports.output.movimiento.MovimientoPersistencePort;
import com.bankfy.bank_meet.domain.models.Cliente;
import com.bankfy.bank_meet.domain.models.Movimiento;
import com.bankfy.bank_meet.domain.models.ReporteEstadoCuenta;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetReporteService implements GetReporteUseCase {

    private final MovimientoPersistencePort movimientoPersistencePort;
    private final ClientePersistencePort clientePersistencePort;

    @Override
    public ReporteEstadoCuenta generarReporte(Long idPersona, LocalDateTime inicio, LocalDateTime fin) {
        Cliente cliente = clientePersistencePort.findById(idPersona)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no existe."));

        List<Movimiento> movs = movimientoPersistencePort.findByClienteAndFechaRange(idPersona, inicio, fin);

        BigDecimal totalCreditos = movs.stream()
                .map(Movimiento::getValor)
                .filter(v -> v.signum() == 1)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDebitos = movs.stream()
                .map(Movimiento::getValor)
                .filter(v -> v.signum() == -1)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String pdfBase64 = generarPdfBase64(movs, cliente.getNombre(), inicio, fin);

        return ReporteEstadoCuenta.builder()
                .cliente(cliente.getNombre())
                .rangoFechas(inicio.toLocalDate() + " a " + fin.toLocalDate())
                .movimientos(movs)
                .totalCreditos(totalCreditos)
                .totalDebitos(totalDebitos)
                .pdfBase64(pdfBase64)
                .build();
    }

    private String generarPdfBase64(List<Movimiento> movs, String clienteNombre, LocalDateTime inicio,
            LocalDateTime fin) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4.rotate()); // Horizontal para que quepan tantas columnas
            PdfWriter.getInstance(document, out);
            document.open();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            document.add(
                    new Paragraph("ESTADO DE CUENTA - BANKFY", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
            document.add(new Paragraph("Cliente: " + clienteNombre));
            document.add(new Paragraph("Rango: " + inicio.format(formatter) + " a " + fin.format(formatter)));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            String[] headers = { "Fecha", "Cliente", "N° Cuenta", "Tipo", "Saldo Inicial", "Estado", "Movimiento",
                    "Saldo Disponible" };

            for (String header : headers) {
                table.addCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
            }

            for (Movimiento m : movs) {
                table.addCell(m.getFecha().format(formatter));
                table.addCell(clienteNombre);
                table.addCell(m.getCuenta().getNumeroCuenta());
                table.addCell(m.getCuenta().getTipoCuenta());
                table.addCell(m.getSaldoAnterior().toString());
                table.addCell(m.getCuenta().getEstado() ? "True" : "False");
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