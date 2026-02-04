package com.bankfy.bank_meet.domain.service;

import com.bankfy.bank_meet.domain.models.Cuenta;
import com.bankfy.bank_meet.domain.models.Movimientos;
import com.bankfy.bank_meet.domain.ports.in.CreateMovimientoUseCase;
import com.bankfy.bank_meet.infrastructure.output.CuentaRepository;
import com.bankfy.bank_meet.infrastructure.output.MovimientosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateMovimientoService implements CreateMovimientoUseCase {

    private final MovimientosRepository movimientosRepository;
    private final CuentaRepository cuentaRepository;

    @Override
    @Transactional
    public Movimientos execute(Movimientos movimiento) {
        if (movimiento.getCuenta() == null || movimiento.getCuenta().getNumeroCuenta() == null) {
            throw new IllegalArgumentException("Debe proporcionar el número de cuenta para realizar el movimiento.");
        }
        String numeroBusqueda = movimiento.getCuenta().getNumeroCuenta();

        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroBusqueda)
                .orElseThrow(() -> new IllegalArgumentException("La cuenta número " + numeroBusqueda + " no existe."));

        if (!Boolean.TRUE.equals(cuenta.getEstado())) {
            throw new IllegalArgumentException("Cuenta inactiva. No se permiten movimientos.");
        }

        validarYNormalizarMovimiento(movimiento);

        BigDecimal saldoInicial = cuenta.getSaldoInicial();
        BigDecimal valorMovimiento = movimiento.getValor();
        BigDecimal nuevoSaldo = saldoInicial.add(valorMovimiento);

        if (valorMovimiento.compareTo(BigDecimal.ZERO) < 0 && nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Saldo no disponible");
        }

        cuenta.setSaldoInicial(nuevoSaldo);
        cuentaRepository.save(cuenta);

        movimiento.setFecha(LocalDateTime.now());
        movimiento.setSaldo(nuevoSaldo);
        movimiento.setCuenta(cuenta);

        return movimientosRepository.save(movimiento);
    }

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

    private void validarYNormalizarMovimiento(Movimientos mov) {
        if (mov.getTipoMovimiento() == null || mov.getTipoMovimiento().isEmpty()) {
            throw new IllegalArgumentException("El tipo de movimiento es obligatorio.");
        }

        if (mov.getTipoMovimiento().equalsIgnoreCase("Retiro") && mov.getValor().compareTo(BigDecimal.ZERO) > 0) {
            mov.setValor(mov.getValor().negate());
        }
    }
}