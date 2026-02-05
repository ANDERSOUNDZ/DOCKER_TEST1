package com.bankfy.bank_meet.domain.service.movimiento;

import com.bankfy.bank_meet.domain.models.Movimientos;
import com.bankfy.bank_meet.domain.ports.in.movimiento.GetMovimientoUseCase;
import com.bankfy.bank_meet.infrastructure.output.MovimientosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetMovimientoService implements GetMovimientoUseCase {

    private final MovimientosRepository movimientosRepository;

    @Override
    public List<Movimientos> obtenerPorFiltros(Long clienteId, String fechaInicio, String fechaFin) {
        if (clienteId == null && fechaInicio == null && fechaFin == null) {
            return movimientosRepository.findAll();
        }
        LocalDateTime inicio = (fechaInicio != null) ? LocalDateTime.parse(fechaInicio)
                : LocalDateTime.now().minusDays(30);
        LocalDateTime fin = (fechaFin != null) ? LocalDateTime.parse(fechaFin) : LocalDateTime.now();
        return movimientosRepository.findByClienteAndFechaRange(clienteId, inicio, fin);
    }

    @Override
    public Movimientos obtenerPorId(Long id) {
        return movimientosRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el movimiento"));
    }
}