package com.bankfy.bank_meet.application.service.cliente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bankfy.bank_meet.application.ports.output.cliente.ClientePersistencePort;
import com.bankfy.bank_meet.application.service.config.IdGeneratorService;
import com.bankfy.bank_meet.domain.models.cliente.Cliente;

@ExtendWith(MockitoExtension.class)
class CreateClienteServiceTest {

    @Mock
    private ClientePersistencePort clientePersistencePort;

    @Mock
    private IdGeneratorService idGenerator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateClienteService createClienteService;

    @Test
    void testCrearUsuarios_Imagen1_Exito() {
        // Arrange
        Cliente clienteJose = new Cliente();
        clienteJose.setNombre("Jose Lema");
        clienteJose.setContrasena("1234");

        when(idGenerator.generateNumericId()).thenReturn("CL-001");
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_1234");
        when(clientePersistencePort.save(any(Cliente.class))).thenReturn(clienteJose);

        // Act
        Cliente resultado = createClienteService.execute(clienteJose);

        // Assert
        assertNotNull(resultado);
        assertEquals("Jose Lema", resultado.getNombre());
        assertEquals(true, resultado.getEstado()); 
    }
}