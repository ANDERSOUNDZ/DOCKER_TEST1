package com.bankfy.bank_meet.infrastructure.adapters.output.persistence.cliente;

import com.bankfy.bank_meet.application.ports.output.cliente.ClientePersistencePort;
import com.bankfy.bank_meet.domain.models.cliente.Cliente;
import com.bankfy.bank_meet.infrastructure.adapters.output.repository.ClienteRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClientePersistenceAdapter implements ClientePersistencePort {

    private final ClienteRepository repository;

    @Override
    public Cliente save(Cliente cliente) {
        return repository.save(cliente);
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<Cliente> findAll(String search, Pageable pageable) {
        if (search == null || search.isEmpty()) {
            return repository.findAll(pageable);
        }
        return repository.findByNombreContainingIgnoreCaseOrIdentificacionContaining(search, search, pageable);
    }

    @Override
    public Optional<Cliente> findByIdentificacion(String identificacion) {
        return Optional.empty();
    }

    @Override
    public Optional<Cliente> findByClienteId(String clienteId) {
        return repository.findByClienteId(clienteId);
    }
}