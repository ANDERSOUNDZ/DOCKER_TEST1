package com.bankfy.bank_meet.application.ports.output.cliente;

import com.bankfy.bank_meet.domain.models.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface ClientePersistencePort {
    Cliente save(Cliente cliente);

    Optional<Cliente> findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    Page<Cliente> findAll(String search, Pageable pageable);

    Optional<Cliente> findByIdentificacion(String identificacion);
}