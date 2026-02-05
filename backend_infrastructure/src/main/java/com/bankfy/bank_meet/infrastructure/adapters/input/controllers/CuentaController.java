package com.bankfy.bank_meet.infrastructure.adapters.input.controllers;

import com.bankfy.bank_meet.application.ports.input.cuenta.*;
import com.bankfy.bank_meet.domain.models.BaseResponse;
import com.bankfy.bank_meet.domain.models.Cuenta;
import com.bankfy.bank_meet.infrastructure.adapters.input.dtos.cuenta.CuentaRequestDTO;
import com.bankfy.bank_meet.infrastructure.adapters.input.dtos.cuenta.CuentaResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CreateCuentaUseCase createUseCase;
    private final GetCuentaUseCase getUseCase;
    private final UpdateCuentaUseCase updateUseCase;
    private final DeleteCuentaUseCase deleteUseCase;
    private final ActivateCuentaUseCase activateUseCase;

    @PostMapping
    public ResponseEntity<BaseResponse<CuentaResponseDTO>> crear(@Valid @RequestBody CuentaRequestDTO dto) {
        Cuenta nueva = createUseCase.execute(dto.toEntity());

        return buildResponse("Cuenta gestionada con éxito",
                CuentaResponseDTO.fromEntity(nueva),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<BaseResponse<Page<CuentaResponseDTO>>> listarTodas(
            @PageableDefault(size = 10) Pageable pageable) {

        Page<CuentaResponseDTO> data = getUseCase.getAll(pageable)
                .map(CuentaResponseDTO::fromEntity);

        return buildResponse("Lista de cuentas recuperada", data, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<CuentaResponseDTO>> obtenerPorId(@PathVariable Long id) {
        CuentaResponseDTO dto = CuentaResponseDTO.fromEntity(getUseCase.getById(id));
        return buildResponse("Cuenta encontrada", dto, HttpStatus.OK);
    }

    @GetMapping("/numero/{numero}")
    public ResponseEntity<BaseResponse<CuentaResponseDTO>> obtenerPorNumero(@PathVariable String numero) {
        CuentaResponseDTO dto = CuentaResponseDTO.fromEntity(getUseCase.getByNumeroCuenta(numero));
        return buildResponse("Cuenta encontrada", dto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<CuentaResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CuentaRequestDTO dto) {

        Cuenta actualizada = updateUseCase.execute(id, dto.toEntity());

        return buildResponse("Resultado de validación de actualización",
                CuentaResponseDTO.fromEntity(actualizada),
                HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<CuentaResponseDTO>> actualizarParcial(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        Cuenta actualizada = updateUseCase.executePartial(id, updates);

        return buildResponse("Resultado de validación parcial",
                CuentaResponseDTO.fromEntity(actualizada),
                HttpStatus.OK);
    }

    @PatchMapping("/activar/{numeroCuenta}")
    public ResponseEntity<BaseResponse<CuentaResponseDTO>> activarCuenta(@PathVariable String numeroCuenta) {
        Cuenta cuentaActivada = activateUseCase.execute(numeroCuenta);

        return buildResponse("Cuenta activada exitosamente",
                CuentaResponseDTO.fromEntity(cuentaActivada),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> eliminar(@PathVariable Long id) {
        deleteUseCase.execute(id);

        return buildResponse("Cuenta inactivada con éxito (Eliminación lógica)", null, HttpStatus.OK);
    }

    private <T> ResponseEntity<BaseResponse<T>> buildResponse(String message, T data, HttpStatus status) {
        BaseResponse<T> response = BaseResponse.<T>builder()
                .message(message)
                .data(data)
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, status);
    }
}