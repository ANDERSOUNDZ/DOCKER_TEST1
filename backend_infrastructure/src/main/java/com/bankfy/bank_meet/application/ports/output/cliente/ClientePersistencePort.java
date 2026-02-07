package com.bankfy.bank_meet.application.ports.output.cliente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bankfy.bank_meet.domain.models.cliente.Cliente;

import java.util.Optional;

public interface ClientePersistencePort {
    Cliente save(Cliente cliente);

    Optional<Cliente> findById(Long id);
    Optional<Cliente> findByClienteId(String clienteId);

    boolean existsById(Long id);

    void deleteById(Long id);

    Page<Cliente> findAll(String search, Pageable pageable);

    Optional<Cliente> findByIdentificacion(String identificacion);
}