package com.bankfy.bank_meet.infrastructure.input;

import com.bankfy.bank_meet.domain.models.BaseResponse;
import com.bankfy.bank_meet.domain.models.Cuenta;
import com.bankfy.bank_meet.domain.ports.in.cuenta.*;
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
    public ResponseEntity<BaseResponse<Cuenta>> crear(@Valid @RequestBody Cuenta cuenta) {
        Cuenta nueva = createUseCase.execute(cuenta);
        return buildResponse("Cuenta gestionada con éxito", nueva, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<BaseResponse<Page<Cuenta>>> listarTodas(
            @PageableDefault(size = 10) Pageable pageable) {
        return buildResponse("Lista de cuentas recuperada", getUseCase.getAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<Cuenta>> obtenerPorId(@PathVariable Long id) {
        return buildResponse("Cuenta encontrada", getUseCase.getById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Cuenta>> actualizar(@PathVariable Long id, @Valid @RequestBody Cuenta cuenta) {
        return buildResponse("Cuenta actualizada con éxito", updateUseCase.execute(id, cuenta), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<Cuenta>> actualizarParcial(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        return buildResponse("Resultado de actualización", updateUseCase.executePartial(id, updates), HttpStatus.OK);
    }

    @PatchMapping("/activar/{numeroCuenta}")
    public ResponseEntity<BaseResponse<Cuenta>> activarCuenta(@PathVariable String numeroCuenta) {
        Cuenta cuentaActivada = activateUseCase.execute(numeroCuenta);
        return buildResponse("Cuenta " + numeroCuenta + " activada exitosamente", cuentaActivada, HttpStatus.OK);
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