package com.bankfy.bank_meet.domain.service.cliente;

import com.bankfy.bank_meet.domain.models.Cliente;
import com.bankfy.bank_meet.domain.ports.in.cliente.CreateClienteUseCase;
import com.bankfy.bank_meet.domain.service.IdGeneratorService;
import com.bankfy.bank_meet.infrastructure.output.ClienteRepository;
import lombok.RequiredArgsConstructor;
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
        prepareNewCliente(cliente);
        return clienteRepository.save(cliente);
    }

    private void prepareNewCliente(Cliente cliente) {
        cliente.setClienteId(idGenerator.generateNumericId());
        cliente.setContrasena(passwordEncoder.encode(cliente.getContrasena()));
        cliente.setEstado(true);
    }
}