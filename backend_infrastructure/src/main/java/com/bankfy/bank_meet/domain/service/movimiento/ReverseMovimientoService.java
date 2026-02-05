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
        Movimientos original = movimientosRepository.findById(movimientoId)
                .orElseThrow(() -> new IllegalArgumentException("Movimiento original no encontrado"));

        Movimientos reverso = new Movimientos();
        reverso.setTipoMovimiento("REVERSO - " + original.getTipoMovimiento());

        reverso.setValor(original.getValor().negate());

        reverso.setCuenta(original.getCuenta());

        return createMovimientoUseCase.execute(reverso);
    }
}