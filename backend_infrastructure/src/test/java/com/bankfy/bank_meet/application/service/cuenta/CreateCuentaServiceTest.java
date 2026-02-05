package com.bankfy.bank_meet.application.service.cuenta;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bankfy.bank_meet.application.ports.output.cliente.ClientePersistencePort;
import com.bankfy.bank_meet.application.ports.output.cuenta.CuentaPersistencePort;
import com.bankfy.bank_meet.domain.models.cliente.Cliente;
import com.bankfy.bank_meet.domain.models.cuenta.Cuenta;
import com.bankfy.bank_meet.domain.exceptions.ValidationException;

@ExtendWith(MockitoExtension.class)
class CreateCuentaServiceTest {

    @Mock
    private CuentaPersistencePort cuentaPersistencePort;

    @Mock
    private ClientePersistencePort clientePersistencePort;

    @InjectMocks
    private CreateCuentaService createCuentaService;

    @Test
    void execute_ShouldThrowException_WhenAccountLimitReached() {
        // 1. Arrange
        Long clienteId = 1L;
        String tipoCuenta = "Ahorros";

        Cliente clienteMock = new Cliente();
        clienteMock.setId(clienteId);

        Cuenta nuevaCuenta = new Cuenta();
        nuevaCuenta.setTipoCuenta(tipoCuenta);
        nuevaCuenta.setCliente(clienteMock);

        // CORRECCIÓN 1: El service usa existsById, no findById
        when(clientePersistencePort.existsById(clienteId)).thenReturn(true);

        // CORRECCIÓN 2: Aseguramos que count devuelva el límite (3 para Ahorros)
        when(cuentaPersistencePort.countByClienteIdAndTipoCuenta(clienteId, tipoCuenta)).thenReturn((long) 3);

        // 2. Act & 3. Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            createCuentaService.execute(nuevaCuenta);
        });

        // CORRECCIÓN 3: El mensaje ahora coincide con el Service
        assertTrue(exception.getMessage().contains("Error en la creación de cuenta"));

        // Verificamos que el error específico esté en el mapa interno de la excepción
        assertTrue(exception.getErrors().get("tipoCuenta").contains("Límite de 3 alcanzado"));

        verify(cuentaPersistencePort, never()).save(any());
    }
}