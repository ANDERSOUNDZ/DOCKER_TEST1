package com.bankfy.bank_meet.domain.service.cuenta;

import com.bankfy.bank_meet.domain.exceptions.ValidationException;
import com.bankfy.bank_meet.domain.models.Cuenta;
import com.bankfy.bank_meet.domain.ports.in.cuenta.CreateCuentaUseCase;
import com.bankfy.bank_meet.domain.service.IdGeneratorService;
import com.bankfy.bank_meet.infrastructure.output.CuentaRepository;
import com.bankfy.bank_meet.infrastructure.output.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

        validarTipoDeCuenta(cuenta.getTipoCuenta(), errores);

        if (cuenta.getCliente() != null && cuenta.getCliente().getId() != null) {
            Long clienteId = cuenta.getCliente().getId();

            if (!clienteRepository.existsById(clienteId)) {
                errores.put("cliente", "El cliente con ID " + clienteId + " no existe.");
            } else {
                validarLimitesDeCuenta(clienteId, cuenta.getTipoCuenta(), errores);

                boolean yaTieneEseTipo = cuentaRepository.existsByClienteIdAndTipoCuenta(clienteId,
                        cuenta.getTipoCuenta());

                cuenta.setEstado(!yaTieneEseTipo);
            }
        }

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        cuenta.setNumeroCuenta(generarNumeroUnico());
        return cuentaRepository.save(cuenta);
    }

    private void validarTipoDeCuenta(String tipo, Map<String, String> errores) {
        if (tipo == null || tipo.isBlank())
            return;

        List<String> validos = List.of("Ahorros", "Corriente", "Ahorro Programado", "Poliza");
        if (!validos.contains(tipo)) {
            errores.put("tipoCuenta", "Tipo de cuenta no permitido. Opciones: " + validos);
        }
    }

    private String generarNumeroUnico() {
        String numero;
        do {
            numero = idGenerator.generateAccountNumber();
        } while (cuentaRepository.existsByNumeroCuenta(numero));
        return numero;
    }

    private void validarLimitesDeCuenta(Long clienteId, String tipoCuenta, Map<String, String> errores) {
        if (tipoCuenta == null)
            return;

        long cantidadActual = cuentaRepository.countByClienteIdAndTipoCuenta(clienteId, tipoCuenta);

        int limite = switch (tipoCuenta) {
            case "Ahorros", "Corriente" -> 3;
            case "Ahorro Programado", "Poliza" -> 5;
            default -> -1;
        };

        if (limite == -1) {
            errores.put("tipoCuenta", "El tipo de cuenta '" + tipoCuenta + "' no tiene límites configurados.");
        } else if (cantidadActual >= limite) {
            errores.put("tipoCuenta",
                    "Límite alcanzado. Solo se permiten " + limite + " cuentas de tipo " + tipoCuenta);
        }
    }
}