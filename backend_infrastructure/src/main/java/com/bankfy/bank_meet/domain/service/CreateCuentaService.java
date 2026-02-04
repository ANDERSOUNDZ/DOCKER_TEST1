package com.bankfy.bank_meet.domain.service;

import com.bankfy.bank_meet.domain.models.Cuenta;
import com.bankfy.bank_meet.domain.ports.in.CreateCuentaUseCase;
import com.bankfy.bank_meet.infrastructure.output.CuentaRepository;
import com.bankfy.bank_meet.infrastructure.output.ClienteRepository;
import lombok.RequiredArgsConstructor;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCuentaService implements CreateCuentaUseCase {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;
    private final IdGeneratorService idGenerator;

    @Override
    public Cuenta execute(Cuenta cuenta) {
        validarTipoDeCuenta(cuenta.getTipoCuenta());

        if (cuenta.getCliente() == null || cuenta.getCliente().getId() == null) {
            throw new IllegalArgumentException("El ID del cliente es obligatorio para crear una cuenta.");
        }

        Long clienteId = Objects.requireNonNull(cuenta.getCliente().getId());

        if (!clienteRepository.existsById(clienteId)) {
            throw new IllegalArgumentException("Error: El cliente con ID " + clienteId + " no existe.");
        }

        String numeroGenerado = generarNumeroUnico();
        cuenta.setNumeroCuenta(numeroGenerado);
        cuenta.setEstado(false);

        return cuentaRepository.save(cuenta);
    }

    private void validarTipoDeCuenta(String tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de cuenta no puede estar vacío.");
        }

        boolean esValido = tipo.equals("Ahorros") ||
                tipo.equals("Corriente") ||
                tipo.equals("Ahorro Programado") ||
                tipo.equals("Poliza");

        if (!esValido) {
            throw new IllegalArgumentException("El tipo '" + tipo
                    + "' no es válido. Solo aceptamos: Ahorros, Corriente, Ahorro Programado o Poliza.");
        }
    }

    private String generarNumeroUnico() {
        String numero;
        do {
            numero = idGenerator.generateAccountNumber();
        } while (cuentaRepository.existsByNumeroCuenta(numero));
        return numero;
    }
}