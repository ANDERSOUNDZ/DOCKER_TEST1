package com.bankfy.bank_meet.infrastructure.input;

import com.bankfy.bank_meet.domain.models.BaseResponse;
import com.bankfy.bank_meet.domain.models.Cliente;
import com.bankfy.bank_meet.domain.ports.in.cliente.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final CreateClienteUseCase createUseCase;
    private final UpdateClienteUseCase updateUseCase;
    private final DeleteClienteUseCase deleteUseCase;
    private final GetClienteUseCase getUseCase;

    @PostMapping
    public ResponseEntity<BaseResponse<Cliente>> crear(@Valid @RequestBody Cliente cliente) {
        return buildResponse("Cliente creado", createUseCase.execute(cliente), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<BaseResponse<Page<Cliente>>> listar(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Cliente> data = getUseCase.getAll(search, PageRequest.of(page, size));
        return buildResponse("Consulta exitosa", data, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<Cliente>> obtener(@PathVariable Long id) {
        return buildResponse("Cliente encontrado", getUseCase.getById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Cliente>> actualizar(
            @PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        return buildResponse("Cliente actualizado", updateUseCase.execute(id, cliente), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<Cliente>> actualizarParcial(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        Cliente actualizado = updateUseCase.executePartial(id, updates);
        return buildResponse("Cliente actualizado parcialmente", actualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> eliminar(@PathVariable Long id) {
        deleteUseCase.execute(id);
        return buildResponse("Cliente eliminado exitosamente.", null, HttpStatus.OK);
    }

    private <T> ResponseEntity<BaseResponse<T>> buildResponse(String msg, T data, HttpStatus status) {
        BaseResponse<T> response = BaseResponse.<T>builder()
                .message(msg).data(data).status(status.value())
                .timestamp(LocalDateTime.now()).build();
        return new ResponseEntity<>(response, status);
    }
}