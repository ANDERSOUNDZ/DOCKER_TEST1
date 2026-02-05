package com.bankfy.bank_meet.application.service.cliente;

import com.bankfy.bank_meet.application.ports.input.cliente.CreateClienteUseCase;
import com.bankfy.bank_meet.application.ports.output.cliente.ClientePersistencePort;
import com.bankfy.bank_meet.application.service.config.IdGeneratorService;
import com.bankfy.bank_meet.domain.models.cliente.Cliente;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateClienteService implements CreateClienteUseCase {
    private final ClientePersistencePort clientePersistencePort;
    private final IdGeneratorService idGenerator;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Cliente execute(Cliente cliente) {
        return Optional.of(cliente)
                .map(this::prepareNewCliente)
                .map(clientePersistencePort::save)
                .orElseThrow(() -> new RuntimeException("Error al procesar el cliente"));
    }

    private Cliente prepareNewCliente(Cliente c) {
        c.setClienteId(idGenerator.generateNumericId());
        c.setContrasena(passwordEncoder.encode(c.getContrasena()));
        c.setEstado(true);
        return c;
    }
}