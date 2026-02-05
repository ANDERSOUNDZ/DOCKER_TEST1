package com.bankfy.bank_meet.domain.service.cliente;

import com.bankfy.bank_meet.domain.models.Cliente;
import com.bankfy.bank_meet.domain.ports.in.cliente.GetClienteUseCase;
import com.bankfy.bank_meet.infrastructure.output.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetClienteService implements GetClienteUseCase {

    private final ClienteRepository clienteRepository;

    @Override
    @Transactional(readOnly = true)
    public Cliente getById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> getAll(String search, Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            return clienteRepository.findByNombreContainingIgnoreCaseOrIdentificacionContaining(
                    search, search, pageable);
        }
        return clienteRepository.findAll(pageable);
    }
}