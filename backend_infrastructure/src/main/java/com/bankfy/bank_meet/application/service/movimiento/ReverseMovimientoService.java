package com.bankfy.bank_meet.application.service.movimiento;

import com.bankfy.bank_meet.application.ports.input.movimiento.CreateMovimientoUseCase;
import com.bankfy.bank_meet.application.ports.input.movimiento.ReverseMovimientoUseCase;
import com.bankfy.bank_meet.application.ports.output.movimiento.MovimientoPersistencePort;
import com.bankfy.bank_meet.domain.models.Movimiento;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReverseMovimientoService implements ReverseMovimientoUseCase {

    private final MovimientoPersistencePort movimientoPersistencePort;
    private final CreateMovimientoUseCase createMovimientoUseCase;

    @Override
    @Transactional
    public Movimiento execute(Long movimientoId) {
        Movimiento original = movimientoPersistencePort.findById(movimientoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Movimiento original no encontrado con ID: " + movimientoId));

        if (original.getTipoMovimiento().startsWith("REVERSO")) {
            throw new IllegalArgumentException("No se puede reversar un movimiento que ya es un reverso.");
        }

        Movimiento reverso = new Movimiento();
        reverso.setTipoMovimiento("REVERSO - " + original.getTipoMovimiento());

        reverso.setValor(original.getValor().negate());

        reverso.setCuenta(original.getCuenta());

        return createMovimientoUseCase.execute(reverso);
    }
}