package com.bankfy.bank_meet.application.service.cuenta;

import com.bankfy.bank_meet.application.ports.input.cuenta.UpdateCuentaUseCase;
import com.bankfy.bank_meet.application.ports.output.cuenta.CuentaPersistencePort;
import com.bankfy.bank_meet.domain.exceptions.ValidationException;
import com.bankfy.bank_meet.domain.models.Cuenta;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UpdateCuentaService implements UpdateCuentaUseCase {

    private final CuentaPersistencePort cuentaPersistencePort;

    @Override
    @Transactional
    public Cuenta execute(Long id, Cuenta nuevosDatos) {
        Cuenta cuentaExistente = cuentaPersistencePort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada con ID: " + id));

        validarInmutabilidad(cuentaExistente, nuevosDatos);

        return cuentaExistente;
    }

    @Override
    @Transactional
    public Cuenta executePartial(Long id, Map<String, Object> updates) {
        Cuenta cuenta = cuentaPersistencePort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));

        List<String> inmutables = List.of("id", "numeroCuenta", "cliente", "saldoInicial", "tipoCuenta", "estado");
        Map<String, String> errores = new HashMap<>();

        updates.keySet().stream()
                .filter(inmutables::contains)
                .forEach(key -> {
                    String msg = key.equals("estado") ? "Use /activar o /desactivar." : "Campo inmutable.";
                    errores.put(key, msg);
                });

        if (!errores.isEmpty())
            throw new ValidationException(errores);

        return cuentaPersistencePort.save(cuenta);
    }

    private void validarInmutabilidad(Cuenta existente, Cuenta nuevos) {
        Map<String, String> errores = new HashMap<>();
        if (nuevos.getSaldoInicial() != null && !existente.getSaldoInicial().equals(nuevos.getSaldoInicial()))
            errores.put("saldoInicial", "El saldo solo se modifica mediante transacciones.");

        if (nuevos.getTipoCuenta() != null && !existente.getTipoCuenta().equals(nuevos.getTipoCuenta()))
            errores.put("tipoCuenta", "No se permite cambiar el tipo de cuenta.");

        if (!errores.isEmpty())
            throw new ValidationException(errores);
    }
}