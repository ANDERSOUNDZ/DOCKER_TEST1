package com.bankfy.bank_meet.infrastructure.input;

import com.bankfy.bank_meet.domain.models.BaseResponse;
import com.bankfy.bank_meet.domain.models.Movimientos;
import com.bankfy.bank_meet.domain.ports.in.CreateMovimientoUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientosController {

    private final CreateMovimientoUseCase createMovimientoUseCase;

    @PostMapping
    public ResponseEntity<BaseResponse<Movimientos>> crearMovimiento(@Valid @RequestBody Movimientos movimiento) {
        Movimientos nuevo = createMovimientoUseCase.execute(movimiento);
        BaseResponse<Movimientos> response = BaseResponse.<Movimientos>builder()
                .message("Movimiento procesado con éxito")
                .data(nuevo)
                .status(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<Movimientos>>> listarMovimientos(
            @RequestParam(required = false) Long cliente,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin) {

        List<Movimientos> resultado = createMovimientoUseCase.obtenerPorFiltros(cliente, fechaInicio, fechaFin);

        BaseResponse<List<Movimientos>> response = BaseResponse.<List<Movimientos>>builder()
                .message("Consulta de movimientos exitosa")
                .data(resultado)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}