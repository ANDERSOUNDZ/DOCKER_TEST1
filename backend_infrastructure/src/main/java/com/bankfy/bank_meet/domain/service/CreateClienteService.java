package com.bankfy.bank_meet.domain.service;

import com.bankfy.bank_meet.domain.models.Cliente;
import com.bankfy.bank_meet.domain.ports.in.CreateClienteUseCase;
import com.bankfy.bank_meet.infrastructure.output.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateClienteService implements CreateClienteUseCase {

    private final ClienteRepository clienteRepository;
    private final IdGeneratorService idGenerator;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Cliente execute(Cliente cliente) {
        cliente.setClienteId(idGenerator.generateNumericId());

        String encodedPassword = passwordEncoder.encode(cliente.getContrasena());
        cliente.setContrasena(encodedPassword);

        cliente.setEstado(true);

        return clienteRepository.save(cliente);
    }
}