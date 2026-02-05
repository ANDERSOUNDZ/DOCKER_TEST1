package com.bankfy.bank_meet.infrastructure.input.controllers;

import com.bankfy.bank_meet.domain.models.BaseResponse;
import com.bankfy.bank_meet.domain.models.Movimientos;
import com.bankfy.bank_meet.domain.models.ReporteEstadoCuenta;
import com.bankfy.bank_meet.domain.ports.in.movimiento.*;
import com.bankfy.bank_meet.infrastructure.input.dtos.movimiento.MovimientoRequestDTO;
import com.bankfy.bank_meet.infrastructure.input.dtos.movimiento.MovimientoResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
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
    private final GetReporteUseCase getReporteUseCase;

    @Operation(summary = "Crear un nuevo movimiento", description = "Registra un depósito o retiro y actualiza el saldo de la cuenta automáticamente.")
    @PostMapping
    public ResponseEntity<BaseResponse<MovimientoResponseDTO>> crear(
            @Valid @RequestBody MovimientoRequestDTO dto) {

        // DTO -> Entidad
        Movimientos movimientoRequest = dto.toEntity();

        // Ejecución
        Movimientos resultado = createUseCase.execute(movimientoRequest);

        // Entidad -> ResponseDTO
        return buildResponse("Movimiento procesado exitosamente",
                MovimientoResponseDTO.fromEntity(resultado),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<MovimientoResponseDTO>>> listar(
            @RequestParam(required = false) Long cliente,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin) {

        // 1. Obtenemos las entidades
        List<Movimientos> entidades = getUseCase.obtenerPorFiltros(cliente, fechaInicio, fechaFin);

        // 2. Transformación funcional: List<Entidad> -> List<DTO>
        List<MovimientoResponseDTO> dtos = entidades.stream()
                .map(MovimientoResponseDTO::fromEntity)
                .toList();

        return buildResponse("Consulta de movimientos exitosa", dtos, HttpStatus.OK);
    }

    @GetMapping("/reporte")
    public ResponseEntity<BaseResponse<ReporteEstadoCuenta>> generarReporte(
            @RequestParam Long clienteId,
            @RequestParam String inicio,
            @RequestParam String fin) {

        LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
        LocalDateTime fechaFin = LocalDateTime.parse(fin);

        ReporteEstadoCuenta reporte = getReporteUseCase.generarReporte(clienteId, fechaInicio, fechaFin);

        return buildResponse("Reporte generado con éxito", reporte, HttpStatus.OK);
    }

    @Operation(summary = "Reversar una transacción", description = "Crea una contrapartida para anular un movimiento previo por su ID.")
    @PostMapping("/reversar/{id}")
    public ResponseEntity<BaseResponse<MovimientoResponseDTO>> reversar(@PathVariable Long id) {
        Movimientos resultado = reverseUseCase.execute(id);

        MovimientoResponseDTO dto = MovimientoResponseDTO.fromEntity(resultado);

        return buildResponse("Reverso procesado y saldo ajustado exitosamente",
                dto,
                HttpStatus.CREATED);
    }

    private <T> ResponseEntity<BaseResponse<T>> buildResponse(String msg, T data, HttpStatus status) {
        return ResponseEntity.status(status).body(BaseResponse.<T>builder()
                .message(msg).data(data).status(status.value()).timestamp(LocalDateTime.now()).build());
    }
}