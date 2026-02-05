package com.bankfy.bank_meet.application.service.movimiento;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
import com.bankfy.bank_meet.domain.models.cliente.Cliente;
import com.bankfy.bank_meet.domain.models.cuenta.Cuenta;
import com.bankfy.bank_meet.domain.models.movimiento.Movimiento;

@ExtendWith(MockitoExtension.class)
class TestFlujoCompleto {

    @Mock
    private MovimientoPersistencePort movimientoPersistencePort;

    @Mock
    private CuentaPersistencePort cuentaPersistencePort;

    @InjectMocks
    private CreateMovimientoService createMovimientoService;

    @Test
    @DisplayName("Flujo Completo: Jose Lema - Retiro y Actualización de Saldo (Imagen 4)")
    void testFlujoCompleto_JoseLema_Exito() {
        // --- ARRANGE ---
        Cliente jose = new Cliente();
        jose.setId(1L);
        jose.setNombre("Jose Lema");

        Cuenta cuentaJose = new Cuenta();
        cuentaJose.setNumeroCuenta("478758");
        cuentaJose.setSaldoInicial(new BigDecimal("2000.00"));
        cuentaJose.setTipoCuenta("Ahorro");
        cuentaJose.setEstado(true);
        cuentaJose.setCliente(jose);

        Movimiento retiro = new Movimiento();
        retiro.setTipoMovimiento("Retiro");
        retiro.setValor(new BigDecimal("575.00"));
        retiro.setCuenta(cuentaJose);

        when(cuentaPersistencePort.findByNumeroCuenta("478758")).thenReturn(Optional.of(cuentaJose));
        when(movimientoPersistencePort.sumRetirosDelDia(any(), any(), any())).thenReturn(BigDecimal.ZERO);
        when(movimientoPersistencePort.save(any(Movimiento.class))).thenAnswer(i -> i.getArgument(0));

        // --- ACT ---
        Movimiento resultado = createMovimientoService.execute(retiro);

        // --- ASSERT ---
        assertEquals(new BigDecimal("1425.00"), resultado.getSaldo(), "El saldo disponible debe ser 1425");
        assertEquals(new BigDecimal("-575.00"), resultado.getValor(), "El valor debe ser negativo (-575)");
        assertEquals(new BigDecimal("1425.00"), cuentaJose.getSaldoInicial(), "El saldo de la cuenta debe actualizarse");
    }
}