package com.bankfy.bank_meet.infrastructure.adapters.output.persistence.movimiento;

import com.bankfy.bank_meet.application.ports.output.movimiento.MovimientoPersistencePort;
import com.bankfy.bank_meet.domain.models.movimiento.Movimiento;
import com.bankfy.bank_meet.infrastructure.adapters.output.repository.MovimientosRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MovimientoPersistenceAdapter implements MovimientoPersistencePort {

    private final MovimientosRepository repository;

    @Override
    public Movimiento save(Movimiento movimiento) {
        return repository.save(movimiento);
    }

    @Override
    public Optional<Movimiento> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public BigDecimal sumRetirosDelDia(Long clienteId, LocalDateTime inicio, LocalDateTime fin) {
        BigDecimal total = repository.sumRetirosDelDia(clienteId, inicio, fin);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public Page<Movimiento> findByClienteAndFechaRange(Long clienteId, LocalDateTime inicio, LocalDateTime fin,
            Pageable pageable) {
        return repository.findByClienteAndFechaRange(clienteId, inicio, fin, pageable);
    }

    @Override
    public List<Movimiento> findByClienteAndFechaRange(Long clienteId, LocalDateTime inicio, LocalDateTime fin) {
        return repository.findByClienteAndFechaRange(clienteId, inicio, fin);
    }

    @Override
    public Page<Movimiento> findAllByFechaRange(LocalDateTime inicio, LocalDateTime fin, Pageable pageable) {
        return repository.findAllByFechaBetween(inicio, fin, pageable);
    }
}