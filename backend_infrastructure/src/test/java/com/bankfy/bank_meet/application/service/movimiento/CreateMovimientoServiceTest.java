package com.bankfy.bank_meet.application.service.movimiento;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

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
class CreateMovimientoServiceTest {

    @Mock
    private MovimientoPersistencePort persistencePort;

    @Mock
    private CuentaPersistencePort cuentaPort;

    @InjectMocks
    private CreateMovimientoService createService;

    @Test
    void execute_ShouldThrowException_WhenDailyLimitExceeded() {
        // 1. Arrange: Configuración exhaustiva del escenario
        Long clienteId = 1L;
        String numeroCuenta = "123456";

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);

        Cuenta cuentaMock = new Cuenta();
        cuentaMock.setNumeroCuenta(numeroCuenta);
        cuentaMock.setSaldoInicial(new BigDecimal("2000.00")); // Evita "Saldo no disponible"
        cuentaMock.setCliente(cliente); // Evita NPE en validarLimiteDiario
        cuentaMock.setEstado(true); // Evita "Cuenta inactiva"
        cuentaMock.setTipoCuenta("Ahorros"); // Evita NPE en validarRestriccionesProducto

        Movimiento nuevoRetiro = new Movimiento();
        nuevoRetiro.setTipoMovimiento("Retiro");
        nuevoRetiro.setValor(new BigDecimal("-200.00"));
        nuevoRetiro.setCuenta(cuentaMock);

        // --- Mocks ---
        // Simular que la cuenta existe en la base de datos
        when(cuentaPort.findByNumeroCuenta(anyString()))
                .thenReturn(Optional.of(cuentaMock));

        // Simular que ya se retiraron 900 hoy (el límite es 1000)
        when(persistencePort.sumRetirosDelDia(any(), any(), any()))
                .thenReturn(new BigDecimal("900.00"));

        // 2. Act & 3. Assert
        // Verificamos que lance la excepción de dominio personalizada
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            createService.execute(nuevoRetiro);
        });

        // Verificamos que el mensaje sea el esperado tras corregir el Service
        assertTrue(exception.getMessage().contains("Cupo diario excedido"));
    }
}