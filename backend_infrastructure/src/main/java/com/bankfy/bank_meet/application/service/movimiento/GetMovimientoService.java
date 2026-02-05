package com.bankfy.bank_meet.application.service.movimiento;

import com.bankfy.bank_meet.application.ports.input.movimiento.GetMovimientoUseCase;
import com.bankfy.bank_meet.application.ports.output.movimiento.MovimientoPersistencePort;
import com.bankfy.bank_meet.domain.models.Movimiento;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetMovimientoService implements GetMovimientoUseCase {

    private final MovimientoPersistencePort movimientoPersistencePort;

    @Override
    public List<Movimiento> obtenerPorFiltros(Long clienteId, String fechaInicio, String fechaFin) {
        // Manejo de fechas seguro
        LocalDateTime inicio = (fechaInicio != null)
                ? LocalDateTime.parse(fechaInicio)
                : LocalDateTime.now().minusDays(30);

        LocalDateTime fin = (fechaFin != null)
                ? LocalDateTime.parse(fechaFin)
                : LocalDateTime.now();

        if (clienteId == null) {
            return List.of();
        }

        return movimientoPersistencePort.findByClienteAndFechaRange(clienteId, inicio, fin);
    }

    @Override
    public Movimiento obtenerPorId(Long id) {
        return movimientoPersistencePort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movimiento no encontrado con ID: " + id));
    }
}