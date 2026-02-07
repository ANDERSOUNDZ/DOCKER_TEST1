package com.bankfy.bank_meet.application.service.movimiento;

import com.bankfy.bank_meet.application.ports.input.movimiento.CreateMovimientoUseCase;
import com.bankfy.bank_meet.application.ports.output.cuenta.CuentaPersistencePort;
import com.bankfy.bank_meet.application.ports.output.movimiento.MovimientoPersistencePort;
import com.bankfy.bank_meet.domain.models.cuenta.Cuenta;
import com.bankfy.bank_meet.domain.models.movimiento.Movimiento;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class CreateMovimientoService implements CreateMovimientoUseCase {

    private final MovimientoPersistencePort movimientoPersistencePort;
    private final CuentaPersistencePort cuentaPersistencePort;
    private static final BigDecimal LIMITE_DIARIO = new BigDecimal("1000");

    @Override
    @Transactional
    public Movimiento execute(Movimiento movimiento) {

        Cuenta cuenta = cuentaPersistencePort.findByNumeroCuenta(movimiento.getCuenta().getNumeroCuenta())
                .orElseThrow(() -> new IllegalArgumentException("La cuenta no existe."));

        validarEstadoYValor(movimiento, cuenta);

        BigDecimal saldoAnterior = cuenta.getSaldoInicial();
        movimiento.setSaldoAnterior(saldoAnterior);

        prepararValorSegunTipo(movimiento);

        validarRestriccionesProducto(movimiento, cuenta);

        if (movimiento.getValor().signum() == -1) {
            validarLimiteDiario(cuenta, movimiento.getValor().abs());
        }

        BigDecimal nuevoSaldo = saldoAnterior.add(movimiento.getValor());
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Saldo no disponible");
        }

        cuenta.setSaldoInicial(nuevoSaldo);
        cuentaPersistencePort.save(cuenta);

        ZoneId zonaEcuador = ZoneId.of("America/Guayaquil");
        LocalDateTime horaEcuador = ZonedDateTime.now(zonaEcuador).toLocalDateTime();
        movimiento.setFecha(horaEcuador.truncatedTo(ChronoUnit.SECONDS));

        movimiento.setSaldo(nuevoSaldo);
        movimiento.setCuenta(cuenta);

        return movimientoPersistencePort.save(movimiento);
    }

    private void validarEstadoYValor(Movimiento mov, Cuenta cuenta) {
        if (mov.getValor().compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("El valor no puede ser cero.");
        }
        if (!Boolean.TRUE.equals(cuenta.getEstado())) {
            throw new IllegalArgumentException("Cuenta inactiva.");
        }
    }

    private void validarLimiteDiario(Cuenta cuenta, BigDecimal monto) {
        ZoneId zonaEcuador = ZoneId.of("America/Guayaquil");
        LocalDateTime inicio = ZonedDateTime.now(zonaEcuador).toLocalDateTime().with(LocalTime.MIN);
        LocalDateTime fin = ZonedDateTime.now(zonaEcuador).toLocalDateTime().with(LocalTime.MAX);

        BigDecimal acumuladoHoy = movimientoPersistencePort.sumRetirosDelDia(
                cuenta.getCliente().getId(), inicio, fin);

        if (acumuladoHoy.add(monto).compareTo(LIMITE_DIARIO) > 0) {
            throw new com.bankfy.bank_meet.domain.exceptions.ValidationException(
                    "Cupo diario excedido",
                    java.util.Map.of("movimiento", "Cupo Diario Excedido"));
        }
    }

    private void prepararValorSegunTipo(Movimiento mov) {
        BigDecimal valorAbsoluto = mov.getValor().abs();
        boolean esDebito = mov.getTipoMovimiento().equalsIgnoreCase("Retiro") ||
                mov.getTipoMovimiento().equalsIgnoreCase("Debito");

        mov.setValor(esDebito ? valorAbsoluto.negate() : valorAbsoluto);

        String prefijo = esDebito ? "Retiro de " : "Deposito de ";
        mov.setTipoMovimiento(prefijo + valorAbsoluto);
    }

    private void validarRestriccionesProducto(Movimiento mov, Cuenta cuenta) {
        if (mov.getValor().signum() == -1) {
            String tipo = cuenta.getTipoCuenta().toLowerCase();
            if (tipo.contains("poliza") || tipo.contains("programado")) {
                throw new IllegalArgumentException("Retiros no permitidos para este tipo de cuenta.");
            }
        }
    }
}