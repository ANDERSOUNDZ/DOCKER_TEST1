package com.bankfy.bank_meet.domain.service;

import com.bankfy.bank_meet.domain.models.Cliente;
import com.bankfy.bank_meet.domain.ports.in.CreateClienteUseCase;
import com.bankfy.bank_meet.infrastructure.output.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder; // Importamos el encoder
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateClienteService implements CreateClienteUseCase {

    private final ClienteRepository clienteRepository;
    private final IdGeneratorService idGenerator;
    private final PasswordEncoder passwordEncoder; // Inyectamos el encoder

    @Override
    public Cliente execute(Cliente cliente) {
        // 1. Generación de ID Automático (el que ya hicimos)
        cliente.setClienteId(idGenerator.generateNumericId());

        // 2. CIFRADO DE CONTRASEÑA (Seguridad estricta)
        // Convertimos "Secreto123" en un hash ilegible como "$2a$10$..."
        String encodedPassword = passwordEncoder.encode(cliente.getContrasena());
        cliente.setContrasena(encodedPassword);

        // 3. Persistencia
        return clienteRepository.save(cliente);
    }
}