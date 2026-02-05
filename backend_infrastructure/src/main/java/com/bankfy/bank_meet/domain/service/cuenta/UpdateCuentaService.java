package com.bankfy.bank_meet.domain.service.cuenta;

import com.bankfy.bank_meet.domain.exceptions.ValidationException;
import com.bankfy.bank_meet.domain.models.Cuenta;
import com.bankfy.bank_meet.domain.ports.in.cuenta.UpdateCuentaUseCase;
import com.bankfy.bank_meet.infrastructure.output.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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

        if (!cuentaExistente.getSaldoInicial().equals(nuevosDatos.getSaldoInicial())) {
            errores.put("saldoInicial", "El saldo solo puede ser modificado mediante transacciones/movimientos.");
        }
        if (!cuentaExistente.getTipoCuenta().equals(nuevosDatos.getTipoCuenta())) {
            errores.put("tipoCuenta", "No se permite el cambio de tipo de cuenta. Debe abrir una nueva.");
        }
        
        if (!errores.isEmpty()) throw new ValidationException(errores);

        return cuentaExistente; 
    }

    @Override
    @Transactional
    public Cuenta executePartial(Long id, Map<String, Object> updates) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));

        Map<String, String> errores = new HashMap<>();

        updates.forEach((key, value) -> {
            switch (key) {
                case "id", "numeroCuenta", "cliente", "saldoInicial", "tipoCuenta" -> 
                    errores.put(key, "Este campo es inmutable por razones de seguridad financiera.");
                
                case "estado" -> {
                    errores.put(key, "Use los endpoints específicos de /activar o /desactivar.");
                }
                
                default -> errores.put(key, "El campo no existe o no se permite su edición manual.");
            }
        });

        if (!errores.isEmpty()) throw new ValidationException(errores);
        
        return cuentaRepository.save(cuenta);
    }
}