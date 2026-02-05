package com.bankfy.bank_meet.application.service.cuenta;

import com.bankfy.bank_meet.application.ports.input.cuenta.CreateCuentaUseCase;
import com.bankfy.bank_meet.application.ports.output.cuenta.CuentaPersistencePort;
import com.bankfy.bank_meet.application.ports.output.cliente.ClientePersistencePort;
import com.bankfy.bank_meet.application.service.config.IdGeneratorService;
import com.bankfy.bank_meet.domain.exceptions.ValidationException;
import com.bankfy.bank_meet.domain.models.cliente.Cliente;
import com.bankfy.bank_meet.domain.models.cuenta.Cuenta;
import com.bankfy.bank_meet.domain.models.cuenta.CuentaLimitStrategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CreateCuentaService implements CreateCuentaUseCase {

    private final CuentaPersistencePort cuentaPersistencePort;
    private final ClientePersistencePort clientePersistencePort;
    private final IdGeneratorService idGenerator;

    @Override
    @Transactional
    public Cuenta execute(Cuenta cuenta) {
        Map<String, String> errores = new HashMap<>();

        Optional.ofNullable(cuenta.getCliente())
                .map(Cliente::getId)
                .ifPresentOrElse(
                        id -> {
                            if (!clientePersistencePort.existsById(id)) {
                                errores.put("cliente", "ID " + id + " no existe.");
                            } else {
                                validarNegocio(cuenta, errores, id);
                            }
                        },
                        () -> errores.put("cliente", "Debe especificar un cliente."));

        if (!errores.isEmpty())
            throw new ValidationException(errores);

        cuenta.setNumeroCuenta(generarNumeroUnico());
        return cuentaPersistencePort.save(cuenta);
    }

    private void validarNegocio(Cuenta cuenta, Map<String, String> errores, Long clienteId) {
        int limite = CuentaLimitStrategy.getLimit(cuenta.getTipoCuenta());

        if (limite == -1) {
            errores.put("tipoCuenta", "Tipo no soportado.");
            return;
        }

        long actual = cuentaPersistencePort.countByClienteIdAndTipoCuenta(clienteId, cuenta.getTipoCuenta());
        if (actual >= limite) {
            errores.put("tipoCuenta", "Límite de " + limite + " alcanzado.");
        }

        boolean yaTiene = actual > 0;
        cuenta.setEstado(!yaTiene);
    }

    private String generarNumeroUnico() {
        return Stream.generate(idGenerator::generateAccountNumber)
                .filter(num -> !cuentaPersistencePort.existsByNumeroCuenta(num))
                .findFirst()
                .orElseThrow();
    }
}