package com.bankfy.bank_meet.application.service.movimiento;

import com.bankfy.bank_meet.application.ports.input.movimiento.GetMovimientoUseCase;
import com.bankfy.bank_meet.application.ports.output.movimiento.MovimientoPersistencePort;
import com.bankfy.bank_meet.domain.models.movimiento.Movimiento;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetMovimientoService implements GetMovimientoUseCase {

    private final MovimientoPersistencePort movimientoPersistencePort;

    @Override
    public Movimiento obtenerPorId(Long id) {
        return movimientoPersistencePort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movimiento no encontrado con ID: " + id));
    }

    @Override
    public List<Movimiento> obtenerPorFiltros(Long clienteId, String fechaInicio, String fechaFin) {
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
    public Page<Movimiento> obtenerPorFiltros(Long clienteId, String fechaInicio, String fechaFin, String search,
            Pageable pageable) {
        LocalDateTime inicio = (fechaInicio != null && !fechaInicio.isEmpty())
                ? LocalDate.parse(fechaInicio).atStartOfDay()
                : LocalDateTime.now().minusDays(30).with(LocalTime.MIN);

        LocalDateTime fin = (fechaFin != null && !fechaFin.isEmpty())
                ? LocalDate.parse(fechaFin).atTime(LocalTime.MAX)
                : LocalDateTime.now().with(LocalTime.MAX);
        if (clienteId == null) {
            return movimientoPersistencePort.findAllWithSearch(inicio, fin, search, pageable);
        }

        return movimientoPersistencePort.findByClienteAndFechaRange(clienteId, inicio, fin, pageable);
    }
}