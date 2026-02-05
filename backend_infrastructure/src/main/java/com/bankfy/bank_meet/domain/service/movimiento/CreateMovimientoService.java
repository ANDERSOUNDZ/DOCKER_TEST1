package com.bankfy.bank_meet.domain.service.movimiento;

import com.bankfy.bank_meet.domain.models.Cuenta;
import com.bankfy.bank_meet.domain.models.Movimientos;
import com.bankfy.bank_meet.domain.ports.in.movimiento.CreateMovimientoUseCase;
import com.bankfy.bank_meet.infrastructure.output.CuentaRepository;
import com.bankfy.bank_meet.infrastructure.output.MovimientosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateMovimientoService implements CreateMovimientoUseCase {

    private final MovimientosRepository movimientosRepository;
    private final CuentaRepository cuentaRepository;

    @Override
    @Transactional
    public Movimientos execute(Movimientos movimiento) {
        if (movimiento.getCuenta() == null || movimiento.getCuenta().getNumeroCuenta() == null) {
            throw new IllegalArgumentException("Debe proporcionar el número de cuenta.");
        }

        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(movimiento.getCuenta().getNumeroCuenta())
                .orElseThrow(() -> new IllegalArgumentException("La cuenta no existe."));

        if (!Boolean.TRUE.equals(cuenta.getEstado())) {
            throw new IllegalArgumentException("Cuenta inactiva (bloqueada). No se permiten movimientos.");
        }

        validarReglasDeNegocio(movimiento, cuenta);

        BigDecimal saldoActual = cuenta.getSaldoInicial();
        BigDecimal valorMov = movimiento.getValor();
        BigDecimal nuevoSaldo = saldoActual.add(valorMov);

        if (valorMov.compareTo(BigDecimal.ZERO) < 0 && nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Saldo no disponible.");
        }

        cuenta.setSaldoInicial(nuevoSaldo);
        cuentaRepository.save(cuenta);

        movimiento.setFecha(LocalDateTime.now());
        movimiento.setSaldo(nuevoSaldo);
        movimiento.setCuenta(cuenta);

        return movimientosRepository.save(movimiento);
    }

    private void validarReglasDeNegocio(Movimientos mov, Cuenta cuenta) {
        String tipoMov = mov.getTipoMovimiento();
        String tipoCta = cuenta.getTipoCuenta();

        if (tipoMov.equalsIgnoreCase("Retiro") && mov.getValor().compareTo(BigDecimal.ZERO) > 0) {
            mov.setValor(mov.getValor().negate());
        }

        if (tipoMov.equalsIgnoreCase("Retiro")) {
            if (tipoCta.equalsIgnoreCase("Poliza") || tipoCta.equalsIgnoreCase("Ahorro Programado")) {
                throw new IllegalArgumentException("No se permiten retiros en cuentas de tipo " + tipoCta + ".");
            }
        }
    }
}