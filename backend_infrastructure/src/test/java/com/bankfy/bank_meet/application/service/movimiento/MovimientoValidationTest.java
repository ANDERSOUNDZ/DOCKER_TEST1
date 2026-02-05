package com.bankfy.bank_meet.application.service.movimiento;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bankfy.bank_meet.application.ports.output.cuenta.CuentaPersistencePort;
import com.bankfy.bank_meet.application.ports.output.movimiento.MovimientoPersistencePort;
import com.bankfy.bank_meet.domain.exceptions.ValidationException;
import com.bankfy.bank_meet.domain.models.cliente.Cliente;
import com.bankfy.bank_meet.domain.models.cuenta.Cuenta;
import com.bankfy.bank_meet.domain.models.movimiento.Movimiento;

@ExtendWith(MockitoExtension.class)
class MovimientoValidationTest {

    @Mock
    private MovimientoPersistencePort movimientoPersistencePort;

    @Mock
    private CuentaPersistencePort cuentaPersistencePort;

    @InjectMocks
    private CreateMovimientoService createMovimientoService;

    @Test
    @DisplayName("Requisito 7: Error cuando el saldo es insuficiente")
    void testRetiro_SaldoInsuficiente_LanzaExcepcion() {
        // --- ARRANGE ---
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("123");
        cuenta.setSaldoInicial(new BigDecimal("10.00"));
        cuenta.setEstado(true);
        cuenta.setTipoCuenta("Ahorros");
        cuenta.setCliente(cliente);

        Movimiento retiro = new Movimiento();
        retiro.setTipoMovimiento("Retiro");
        retiro.setValor(new BigDecimal("50.00"));
        retiro.setCuenta(cuenta);

        when(cuentaPersistencePort.findByNumeroCuenta("123")).thenReturn(Optional.of(cuenta));
        when(movimientoPersistencePort.sumRetirosDelDia(any(), any(), any())).thenReturn(BigDecimal.ZERO);

        // --- ACT & ASSERT ---
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createMovimientoService.execute(retiro);
        });

        assertEquals("Saldo no disponible", exception.getMessage());
    }

    @Test
    @DisplayName("Requisito 8 y 9: Error cuando se excede el cupo diario de $1000")
    void testRetiro_CupoDiarioExcedido_LanzaExcepcion() {
        // --- ARRANGE ---
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("478758");
        cuenta.setSaldoInicial(new BigDecimal("2000.00"));
        cuenta.setEstado(true);
        cuenta.setTipoCuenta("Ahorros");
        cuenta.setCliente(cliente);

        Movimiento retiroGrande = new Movimiento();
        retiroGrande.setTipoMovimiento("Retiro");
        retiroGrande.setValor(new BigDecimal("1100.00"));
        retiroGrande.setCuenta(cuenta);

        when(cuentaPersistencePort.findByNumeroCuenta("478758")).thenReturn(Optional.of(cuenta));
        when(movimientoPersistencePort.sumRetirosDelDia(any(), any(), any())).thenReturn(BigDecimal.ZERO);

        // --- ACT & ASSERT ---
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            createMovimientoService.execute(retiroGrande);
        });

        assertTrue(exception.getMessage().contains("Cupo diario excedido"));
    }
}