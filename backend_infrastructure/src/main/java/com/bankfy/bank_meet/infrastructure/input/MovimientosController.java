package com.bankfy.bank_meet.infrastructure.input;

import com.bankfy.bank_meet.domain.models.BaseResponse;
import com.bankfy.bank_meet.domain.models.Movimientos;
import com.bankfy.bank_meet.domain.ports.in.movimiento.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientosController {

    private final CreateMovimientoUseCase createUseCase;
    private final GetMovimientoUseCase getUseCase;
    private final ReverseMovimientoUseCase reverseUseCase;

    @PostMapping
    public ResponseEntity<BaseResponse<Movimientos>> crear(@Valid @RequestBody Movimientos mov) {
        return buildResponse("Procesado", createUseCase.execute(mov), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<Movimientos>>> listar(
            @RequestParam(required = false) Long cliente,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin) {
        return buildResponse("Consulta exitosa", getUseCase.obtenerPorFiltros(cliente, fechaInicio, fechaFin),
                HttpStatus.OK);
    }

    @PostMapping("/reversar/{id}")
    public ResponseEntity<BaseResponse<Movimientos>> reversar(@PathVariable Long id) {
        Movimientos resultado = reverseUseCase.execute(id);
        return buildResponse("Reverso procesado y saldo ajustado", resultado, HttpStatus.CREATED);
    }

    private <T> ResponseEntity<BaseResponse<T>> buildResponse(String msg, T data, HttpStatus status) {
        return ResponseEntity.status(status).body(BaseResponse.<T>builder()
                .message(msg).data(data).status(status.value()).timestamp(LocalDateTime.now()).build());
    }
}