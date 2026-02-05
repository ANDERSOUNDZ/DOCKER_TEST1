package com.bankfy.bank_meet.infrastructure.adapters.output.repository;

import com.bankfy.bank_meet.domain.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Page<Cliente> findByNombreContainingIgnoreCaseOrIdentificacionContaining(
            String nombre, String identificacion, Pageable pageable);
}