package com.bankfy.bank_meet.application.service.cuenta;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bankfy.bank_meet.application.ports.output.cliente.ClientePersistencePort;
import com.bankfy.bank_meet.application.ports.output.cuenta.CuentaPersistencePort;
import com.bankfy.bank_meet.application.service.config.IdGeneratorService;
import com.bankfy.bank_meet.domain.models.cliente.Cliente;
import com.bankfy.bank_meet.domain.models.cuenta.Cuenta;

@ExtendWith(MockitoExtension.class)
class CreateCuentaCorrienteTest {

    @Mock
    private CuentaPersistencePort cuentaPersistencePort;

    @Mock
    private ClientePersistencePort clientePersistencePort;

    @Mock
    private IdGeneratorService idGenerator;

    @InjectMocks
    private CreateCuentaService createCuentaService;

    @Test
    @DisplayName("Caso de Uso 3: Crear Cuenta Corriente para Jose Lema")
    void testCrearCuentaCorriente_JoseLema_Exito() {
        // --- ARRANGE ---
        Long clienteId = 1L;
        String numeroCuentaEsperado = "585545"; 
        BigDecimal saldoInicial = new BigDecimal("1000.00");

        Cliente joseLema = new Cliente();
        joseLema.setId(clienteId);
        joseLema.setNombre("Jose Lema");

        Cuenta nuevaCuentaCorriente = new Cuenta();
        nuevaCuentaCorriente.setTipoCuenta("Corriente");
        nuevaCuentaCorriente.setSaldoInicial(saldoInicial);
        nuevaCuentaCorriente.setCliente(joseLema);

        // Mocks
        when(clientePersistencePort.existsById(clienteId)).thenReturn(true);
        when(idGenerator.generateAccountNumber()).thenReturn(numeroCuentaEsperado);
        when(cuentaPersistencePort.existsByNumeroCuenta(numeroCuentaEsperado)).thenReturn(false);
        when(cuentaPersistencePort.countByClienteIdAndTipoCuenta(clienteId, "Corriente")).thenReturn(0L);
        
        when(cuentaPersistencePort.save(any(Cuenta.class))).thenAnswer(i -> i.getArgument(0));

        // --- ACT ---
        Cuenta resultado = createCuentaService.execute(nuevaCuentaCorriente);

        // --- ASSERT ---
        assertNotNull(resultado);
        assertEquals(numeroCuentaEsperado, resultado.getNumeroCuenta());
        assertEquals(saldoInicial, resultado.getSaldoInicial());
        assertEquals("Corriente", resultado.getTipoCuenta());
        assertTrue(resultado.getEstado());
    }
}