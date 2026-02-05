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
import com.bankfy.bank_meet.domain.models.cliente.Cliente;
import com.bankfy.bank_meet.domain.models.cuenta.Cuenta;
import com.bankfy.bank_meet.domain.models.movimiento.Movimiento;

@ExtendWith(MockitoExtension.class)
class ProcessMovimientoTest {

    @Mock
    private MovimientoPersistencePort movimientoPersistencePort;

    @Mock
    private CuentaPersistencePort cuentaPersistencePort;

    @InjectMocks
    private CreateMovimientoService createMovimientoService;

    @Test
    @DisplayName("Caso de Uso 4: Retiro Jose Lema $575.00 - Imagen 4")
    void testEjecutarMovimientos_Imagen4_Exito() {
        // --- ARRANGE ---
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Cuenta cuentaJose = new Cuenta();
        cuentaJose.setNumeroCuenta("478758");
        cuentaJose.setSaldoInicial(new BigDecimal("2000.00"));
        cuentaJose.setTipoCuenta("Ahorros");
        cuentaJose.setEstado(true);
        cuentaJose.setCliente(cliente);

        Movimiento retiroJose = new Movimiento();
        retiroJose.setTipoMovimiento("Retiro");
        retiroJose.setValor(new BigDecimal("575.00"));
        retiroJose.setCuenta(cuentaJose);

        when(cuentaPersistencePort.findByNumeroCuenta("478758")).thenReturn(Optional.of(cuentaJose));
        when(movimientoPersistencePort.sumRetirosDelDia(any(), any(), any())).thenReturn(BigDecimal.ZERO);
        when(movimientoPersistencePort.save(any(Movimiento.class))).thenAnswer(i -> i.getArgument(0));

        // --- ACT ---
        Movimiento resultado = createMovimientoService.execute(retiroJose);

        // --- ASSERT ---
        assertEquals(new BigDecimal("1425.00"), resultado.getSaldo());
        assertEquals(new BigDecimal("-575.00"), resultado.getValor());
    }

    @Test
    @DisplayName("Caso de Uso 4: Depósito Marianela Montalvo $600.00 - Imagen 4")
    void testDepositoMarianela_Imagen4_Exito() {
        // --- ARRANGE ---
        Cuenta cuentaMarianela = new Cuenta();
        cuentaMarianela.setNumeroCuenta("225487");
        cuentaMarianela.setSaldoInicial(new BigDecimal("100.00"));
        cuentaMarianela.setTipoCuenta("Ahorros");
        cuentaMarianela.setEstado(true);

        Movimiento deposito = new Movimiento();
        deposito.setTipoMovimiento("Deposito");
        deposito.setValor(new BigDecimal("600.00"));
        deposito.setCuenta(cuentaMarianela);

        when(cuentaPersistencePort.findByNumeroCuenta("225487")).thenReturn(Optional.of(cuentaMarianela));
        when(movimientoPersistencePort.save(any(Movimiento.class))).thenAnswer(i -> i.getArgument(0));

        // --- ACT ---
        Movimiento resultado = createMovimientoService.execute(deposito);

        // --- ASSERT ---
        assertEquals(new BigDecimal("700.00"), resultado.getSaldo());
        assertEquals(new BigDecimal("600.00"), resultado.getValor());
    }
}