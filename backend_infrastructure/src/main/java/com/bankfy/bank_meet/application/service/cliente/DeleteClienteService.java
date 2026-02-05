package com.bankfy.bank_meet.application.service.cliente;

import org.springframework.stereotype.Service;

import com.bankfy.bank_meet.application.ports.input.cliente.DeleteClienteUseCase;
import com.bankfy.bank_meet.application.ports.output.cliente.ClientePersistencePort;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteClienteService implements DeleteClienteUseCase {

    private final ClientePersistencePort clientePersistencePort;
    private final com.bankfy.bank_meet.infrastructure.adapters.output.repository.CuentaRepository cuentaRepository;

    @Override
    @Transactional
    public void execute(Long id) {
        var cliente = clientePersistencePort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se puede eliminar: El cliente con ID " + id + " no existe."));

        if (cuentaRepository.existsByClienteId(id)) {
            throw new IllegalArgumentException(
                    "No se puede desactivar el cliente: Tiene cuentas bancarias activas o registradas.");
        }

        cliente.setEstado(false);

        clientePersistencePort.save(cliente);
    }
}