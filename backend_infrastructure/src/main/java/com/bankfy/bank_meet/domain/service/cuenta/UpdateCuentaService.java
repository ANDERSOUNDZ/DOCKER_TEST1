package com.bankfy.bank_meet.domain.service.cuenta;

import com.bankfy.bank_meet.domain.exceptions.ValidationException;
import com.bankfy.bank_meet.domain.models.Cuenta;
import com.bankfy.bank_meet.domain.ports.in.cuenta.UpdateCuentaUseCase;
import com.bankfy.bank_meet.infrastructure.output.CuentaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UpdateCuentaService implements UpdateCuentaUseCase {

    private final CuentaRepository cuentaRepository;

    @Override
    @Transactional
    public Cuenta execute(Long id, Cuenta nuevosDatos) {
        Cuenta cuentaExistente = cuentaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada con ID: " + id));

        Map<String, String> errores = new HashMap<>();

        // Validación de inmutabilidad
        if (nuevosDatos.getSaldoInicial() != null
                && !cuentaExistente.getSaldoInicial().equals(nuevosDatos.getSaldoInicial())) {
            errores.put("saldoInicial", "El saldo solo se modifica mediante transacciones.");
        }
        if (nuevosDatos.getTipoCuenta() != null
                && !cuentaExistente.getTipoCuenta().equals(nuevosDatos.getTipoCuenta())) {
            errores.put("tipoCuenta", "No se permite cambiar el tipo de cuenta.");
        }

        if (!errores.isEmpty())
            throw new ValidationException(errores);

        // En una cuenta bancaria, usualmente el PUT no cambia nada más que quizás
        // metadatos
        // (si existieran), por ahora devolvemos la existente ya que el resto es
        // inmutable.
        return cuentaExistente;
    }

    @Override
    @Transactional
    public Cuenta executePartial(Long id, Map<String, Object> updates) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));

        // Definimos campos prohibidos
        List<String> inmutables = List.of("id", "numeroCuenta", "cliente", "saldoInicial", "tipoCuenta", "estado");
        Map<String, String> errores = new HashMap<>();

        // Uso de lambdas y streams para validar las llaves del Map
        updates.keySet().stream()
                .filter(inmutables::contains)
                .forEach(key -> {
                    if (key.equals("estado")) {
                        errores.put(key, "Use los endpoints específicos de /activar o /desactivar.");
                    } else {
                        errores.put(key, "Este campo es inmutable por seguridad financiera.");
                    }
                });

        if (!errores.isEmpty())
            throw new ValidationException(errores);

        // Si pasara las validaciones, aquí se aplicarían los cambios con Reflection o
        // setters
        // Pero según tu lógica actual, nada es editable manualmente.
        return cuentaRepository.save(cuenta);
    }
}