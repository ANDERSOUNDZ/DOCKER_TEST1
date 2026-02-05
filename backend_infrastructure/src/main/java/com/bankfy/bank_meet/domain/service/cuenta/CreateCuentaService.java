package com.bankfy.bank_meet.domain.service.cuenta;

import com.bankfy.bank_meet.domain.exceptions.ValidationException;
import com.bankfy.bank_meet.domain.models.Cliente;
import com.bankfy.bank_meet.domain.models.Cuenta;
import com.bankfy.bank_meet.domain.ports.in.cuenta.CreateCuentaUseCase;
import com.bankfy.bank_meet.domain.service.IdGeneratorService;
import com.bankfy.bank_meet.infrastructure.output.CuentaRepository;
import com.bankfy.bank_meet.infrastructure.output.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CreateCuentaService implements CreateCuentaUseCase {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;
    private final IdGeneratorService idGenerator;

    @Override
    @Transactional
    public Cuenta execute(Cuenta cuenta) {
        Map<String, String> errores = new HashMap<>();

        // Uso de Optional y lambdas para validar cliente
        Optional.ofNullable(cuenta.getCliente())
                .map(Cliente::getId)
                .ifPresentOrElse(
                        id -> {
                            if (!clienteRepository.existsById(id)) {
                                errores.put("cliente", "ID " + id + " no existe.");
                            } else {
                                validarNegocio(cuenta, errores, id);
                            }
                        },
                        () -> errores.put("cliente", "Debe especificar un cliente."));

        if (!errores.isEmpty())
            throw new ValidationException(errores);

        cuenta.setNumeroCuenta(generarNumeroUnico());
        return cuentaRepository.save(cuenta);
    }

    private void validarNegocio(Cuenta cuenta, Map<String, String> errores, Long clienteId) {
        int limite = CuentaLimitStrategy.getLimit(cuenta.getTipoCuenta()); // Patrón Strategy

        if (limite == -1) {
            errores.put("tipoCuenta", "Tipo no soportado.");
            return;
        }

        long actual = cuentaRepository.countByClienteIdAndTipoCuenta(clienteId, cuenta.getTipoCuenta());
        if (actual >= limite) {
            errores.put("tipoCuenta", "Límite de " + limite + " alcanzado.");
        }

        // Lógica de estado inicial
        boolean yaTiene = cuentaRepository.existsByClienteIdAndTipoCuenta(clienteId, cuenta.getTipoCuenta());
        cuenta.setEstado(!yaTiene);
    }

    private String generarNumeroUnico() {
        return Stream.generate(idGenerator::generateAccountNumber)
                .filter(num -> !cuentaRepository.existsByNumeroCuenta(num))
                .findFirst()
                .orElseThrow(); // Programación funcional con Stream
    }
}