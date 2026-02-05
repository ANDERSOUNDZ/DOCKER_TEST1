package com.bankfy.bank_meet.application.service.movimiento;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bankfy.bank_meet.application.ports.input.movimiento.CreateMovimientoUseCase;
import com.bankfy.bank_meet.application.ports.output.movimiento.MovimientoPersistencePort;
import com.bankfy.bank_meet.domain.models.movimiento.Movimiento;

@ExtendWith(MockitoExtension.class)
class ReverseMovimientoServiceTest {

    @Mock
    private MovimientoPersistencePort persistencePort;
    @Mock
    private CreateMovimientoUseCase createUseCase;

    @InjectMocks
    private ReverseMovimientoService reverseService;

    @Test
    void execute_ShouldCreateReverseMovement_WhenOriginalIsValid() {
        // Arrange (Preparar datos)
        Long id = 1L;
        Movimiento original = new Movimiento();
        original.setTipoMovimiento("Retiro");
        original.setValor(new BigDecimal("-50.00"));

        when(persistencePort.findById(id)).thenReturn(Optional.of(original));
        when(createUseCase.execute(any(Movimiento.class))).thenAnswer(i -> i.getArgument(0));

        // Act (Ejecutar)
        Movimiento resultado = reverseService.execute(id);

        // Assert (Verificar)
        assertEquals(new BigDecimal("50.00"), resultado.getValor()); // El negativo de -50 es 50
        assertTrue(resultado.getTipoMovimiento().contains("REVERSO"));
        verify(createUseCase, times(1)).execute(any());
    }

    @Test
    void execute_ShouldThrowException_WhenAlreadyAReverse() {
        // Arrange
        Long id = 2L;
        Movimiento reversoPrevio = new Movimiento();
        reversoPrevio.setTipoMovimiento("REVERSO - Retiro");

        when(persistencePort.findById(id)).thenReturn(Optional.of(reversoPrevio));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> reverseService.execute(id));
    }
}