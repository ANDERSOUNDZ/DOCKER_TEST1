package com.bankfy.bank_meet.infrastructure.input;

import com.bankfy.bank_meet.domain.models.BaseResponse;
import com.bankfy.bank_meet.domain.models.Cuenta;
import com.bankfy.bank_meet.domain.ports.in.CreateCuentaUseCase;
import com.bankfy.bank_meet.infrastructure.output.CuentaRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CreateCuentaUseCase createCuentaUseCase;
    private final CuentaRepository cuentaRepository;

    @PostMapping
    public ResponseEntity<BaseResponse<Cuenta>> crearCuenta(@Valid @RequestBody Cuenta cuenta) {
        Cuenta nueva = createCuentaUseCase.execute(cuenta);
        BaseResponse<Cuenta> response = BaseResponse.<Cuenta>builder()
                .message("Cuenta solicitada con éxito (Pendiente de activación)")
                .data(nueva)
                .status(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<BaseResponse<Cuenta>> activarCuenta(@PathVariable Long id) {

        Long idSeguro = Objects.requireNonNull(id, "El ID de la cuenta no puede ser nulo");

        Cuenta cuenta = cuentaRepository.findById(idSeguro)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada con ID: " + idSeguro));

        cuenta.setEstado(true);
        cuentaRepository.save(cuenta);

        BaseResponse<Cuenta> response = BaseResponse.<Cuenta>builder()
                .message("Cuenta autorizada y activada con éxito")
                .data(cuenta)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}