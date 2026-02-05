package com.bankfy.bank_meet.application.service.cliente;

import com.bankfy.bank_meet.application.ports.input.cliente.GetClienteUseCase;
import com.bankfy.bank_meet.application.ports.output.cliente.ClientePersistencePort;
import com.bankfy.bank_meet.domain.models.Cliente;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetClienteService implements GetClienteUseCase {

    private final ClientePersistencePort clientePersistencePort;

    @Override
    @Transactional(readOnly = true)
    public Cliente getById(Long id) {
        return clientePersistencePort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> getAll(String search, Pageable pageable) {
        return clientePersistencePort.findAll(search, pageable);
    }
}