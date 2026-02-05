package com.bankfy.bank_meet.domain.service.cliente;

import com.bankfy.bank_meet.domain.models.Cliente;
import com.bankfy.bank_meet.domain.ports.in.cliente.CreateClienteUseCase;
import com.bankfy.bank_meet.domain.service.IdGeneratorService;
import com.bankfy.bank_meet.infrastructure.output.ClienteRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateClienteService implements CreateClienteUseCase {
    private final ClienteRepository clienteRepository;
    private final IdGeneratorService idGenerator;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Cliente execute(Cliente cliente) {
        // Programación funcional para transformar y guardar
        return Optional.of(cliente)
                .map(this::prepareNewCliente) // Preparamos datos técnicos
                .map(clienteRepository::save) // Persistimos
                .orElseThrow(() -> new RuntimeException("Error al procesar el cliente"));
    }

    private Cliente prepareNewCliente(Cliente c) {
        c.setClienteId(idGenerator.generateNumericId()); // Patrón Singleton/Service para IDs
        c.setContrasena(passwordEncoder.encode(c.getContrasena()));
        c.setEstado(true);
        return c;
    }
}