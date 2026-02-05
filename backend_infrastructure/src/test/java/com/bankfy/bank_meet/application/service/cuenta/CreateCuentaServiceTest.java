package com.bankfy.bank_meet.application.service.cuenta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bankfy.bank_meet.application.ports.output.cliente.ClientePersistencePort;
import com.bankfy.bank_meet.application.ports.output.cuenta.CuentaPersistencePort;
import com.bankfy.bank_meet.application.service.config.IdGeneratorService;
import com.bankfy.bank_meet.application.service.cuenta.CreateCuentaService;
import com.bankfy.bank_meet.domain.models.cliente.Cliente;
import com.bankfy.bank_meet.domain.models.cuenta.Cuenta;

@ExtendWith(MockitoExtension.class)
class CreateCuentaServiceTest {

    @Mock
    private CuentaPersistencePort cuentaPersistencePort;

    @Mock
    private ClientePersistencePort clientePersistencePort;

    @Mock
    private IdGeneratorService idGenerator;

    @InjectMocks
    private CreateCuentaService createCuentaService;

    @Test
    void testCrearCuentas_Imagen2_Exito() {
        // Arrange:
        Long clienteId = 1L;
        Cliente joseLema = new Cliente();
        joseLema.setId(clienteId);

        Cuenta cuentaJose = new Cuenta();
        cuentaJose.setTipoCuenta("Ahorros");
        cuentaJose.setSaldoInicial(new BigDecimal("2000.00"));
        cuentaJose.setCliente(joseLema);

        when(clientePersistencePort.existsById(clienteId)).thenReturn(true);
        when(idGenerator.generateAccountNumber()).thenReturn("478758");
        when(cuentaPersistencePort.existsByNumeroCuenta("478758")).thenReturn(false);
        when(cuentaPersistencePort.countByClienteIdAndTipoCuenta(anyLong(), anyString())).thenReturn(0L);

        when(cuentaPersistencePort.save(any(Cuenta.class))).thenAnswer(invocation -> {
            Cuenta c = invocation.getArgument(0);
            return c;
        });

        // Act
        Cuenta resultado = createCuentaService.execute(cuentaJose);

        // Assert
        assertNotNull(resultado);
        assertEquals("478758", resultado.getNumeroCuenta());
        assertEquals(new BigDecimal("2000.00"), resultado.getSaldoInicial());
        assertEquals(true, resultado.getEstado());
    }
}