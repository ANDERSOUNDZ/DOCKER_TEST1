package com.bankfy.bank_meet.domain.service.movimiento;

import com.bankfy.bank_meet.domain.models.Movimientos;
import com.bankfy.bank_meet.domain.ports.in.movimiento.CreateMovimientoUseCase;
import com.bankfy.bank_meet.domain.ports.in.movimiento.ReverseMovimientoUseCase;
import com.bankfy.bank_meet.infrastructure.output.MovimientosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReverseMovimientoService implements ReverseMovimientoUseCase {

    private final MovimientosRepository movimientosRepository;
    private final CreateMovimientoUseCase createMovimientoUseCase;

    @Override
    @Transactional
    public Movimientos execute(Long movimientoId) {
        // 1. Obtener el movimiento original
        Movimientos original = movimientosRepository.findById(movimientoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Movimiento original no encontrado con ID: " + movimientoId));

        // 2. Validación de seguridad: No reversar algo ya reversado (Opcional pero
        // recomendado)
        if (original.getTipoMovimiento().startsWith("REVERSO")) {
            throw new IllegalArgumentException("No se puede reversar un movimiento que ya es un reverso.");
        }

        // 3. Crear la contrapartida
        Movimientos reverso = new Movimientos();
        reverso.setTipoMovimiento("REVERSO - " + original.getTipoMovimiento());

        // Invertimos el valor: Si era -50 (Retiro), ahora es +50. Si era +100
        // (Depósito), ahora es -100.
        reverso.setValor(original.getValor().negate());

        // Asociamos la misma cuenta
        reverso.setCuenta(original.getCuenta());

        // 4. Delegamos al Create: Esto actualiza el saldo de la cuenta y guarda el
        // nuevo movimiento
        return createMovimientoUseCase.execute(reverso);
    }
}