package com.bankfy.bank_meet.domain.service.cliente;

import org.springframework.stereotype.Service;

import com.bankfy.bank_meet.domain.ports.in.cliente.DeleteClienteUseCase;
import com.bankfy.bank_meet.infrastructure.output.ClienteRepository;
import com.bankfy.bank_meet.infrastructure.output.CuentaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteClienteService implements DeleteClienteUseCase {

    private final ClienteRepository clienteRepository;
    private final CuentaRepository cuentaRepository;

    @Override
    @Transactional
    public void execute(Long id) {
        // Validación de existencia: Si falla, el GlobalExceptionHandler captura la
        // IllegalArgumentException
        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar: El cliente con ID " + id + " no existe.");
        }

        // Validación de reglas de negocio: No borrar clientes con cuentas
        if (cuentaRepository.existsByClienteId(id)) {
            throw new IllegalArgumentException(
                    "No se puede eliminar el cliente: Tiene cuentas bancarias activas o registradas.");
        }

        clienteRepository.deleteById(id);
    }
}