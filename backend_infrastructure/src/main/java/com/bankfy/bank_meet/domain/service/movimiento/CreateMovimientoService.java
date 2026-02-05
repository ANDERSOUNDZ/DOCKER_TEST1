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
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class CreateMovimientoService implements CreateMovimientoUseCase {

    private final MovimientosRepository movimientosRepository;
    private final CuentaRepository cuentaRepository;
    private static final BigDecimal LIMITE_DIARIO = new BigDecimal("1000");

    @Override
    @Transactional
    public Movimientos execute(Movimientos movimiento) {
        // 1. Buscar y validar cuenta (Programación funcional con orElseThrow)
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(movimiento.getCuenta().getNumeroCuenta())
                .orElseThrow(() -> new IllegalArgumentException("La cuenta no existe o es incorrecta."));

        if (!Boolean.TRUE.equals(cuenta.getEstado())) {
            throw new IllegalArgumentException("No se pueden realizar movimientos en una cuenta inactiva.");
        }

        // 2. Capturar Snapshot del saldo ANTES del movimiento
        BigDecimal saldoAnterior = cuenta.getSaldoInicial();
        movimiento.setSaldoAnterior(saldoAnterior);

        // 3. Normalizar valor (Retiro = negativo)
        prepararValorSegunTipo(movimiento);

        // 4. Validaciones de Negocio
        validarRestriccionesProducto(movimiento, cuenta);

        if (movimiento.getValor().signum() == -1) { // Si es negativo (Retiro/Debito)
            validarLimiteDiario(cuenta, movimiento.getValor().abs());
        }

        // 5. Cálculo de nuevo saldo
        BigDecimal nuevoSaldo = saldoAnterior.add(movimiento.getValor());

        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Saldo no disponible");
        }

        // 6. Persistencia Atómica
        cuenta.setSaldoInicial(nuevoSaldo);
        cuentaRepository.save(cuenta); // Actualizamos la cuenta

        movimiento.setFecha(LocalDateTime.now());
        movimiento.setSaldo(nuevoSaldo); // Saldo resultante
        movimiento.setCuenta(cuenta);

        return movimientosRepository.save(movimiento);
    }

    private void validarLimiteDiario(Cuenta cuenta, BigDecimal montoARetirar) {
        // Definimos el rango del día de hoy: desde 00:00:00 hasta 23:59:59
        LocalDateTime inicioDia = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime finDia = LocalDateTime.now().with(LocalTime.MAX);

        BigDecimal acumuladoHoy = movimientosRepository.sumRetirosDelDia(
                cuenta.getCliente().getId(), inicioDia, finDia);

        if (acumuladoHoy == null)
            acumuladoHoy = BigDecimal.ZERO;

        if (acumuladoHoy.add(montoARetirar).compareTo(LIMITE_DIARIO) > 1) {
            throw new IllegalArgumentException("Cupo diario Excedido");
        }
    }

    private void prepararValorSegunTipo(Movimientos mov) {
        BigDecimal valor = mov.getValor().abs();
        if (mov.getTipoMovimiento().equalsIgnoreCase("Retiro") ||
                mov.getTipoMovimiento().equalsIgnoreCase("Debito")) {
            mov.setValor(valor.negate());
        } else {
            mov.setValor(valor);
        }
    }

    private void validarRestriccionesProducto(Movimientos mov, Cuenta cuenta) {
        if (mov.getValor().compareTo(BigDecimal.ZERO) < 0) {
            if (cuenta.getTipoCuenta().equalsIgnoreCase("Poliza") ||
                    cuenta.getTipoCuenta().equalsIgnoreCase("Ahorro Programado")) {
                throw new IllegalArgumentException(
                        "No se permiten retiros en cuentas de tipo " + cuenta.getTipoCuenta());
            }
        }
    }
}