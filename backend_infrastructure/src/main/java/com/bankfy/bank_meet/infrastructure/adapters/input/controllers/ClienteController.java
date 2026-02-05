package com.bankfy.bank_meet.infrastructure.adapters.input.controllers;

import com.bankfy.bank_meet.application.ports.input.cliente.*;
import com.bankfy.bank_meet.domain.models.cliente.Cliente;
import com.bankfy.bank_meet.infrastructure.adapters.input.dtos.BaseResponse;
import com.bankfy.bank_meet.infrastructure.adapters.input.dtos.cliente.ClienteRequestDTO;
import com.bankfy.bank_meet.infrastructure.adapters.input.dtos.cliente.ClienteResponseDTO;
import com.bankfy.bank_meet.infrastructure.adapters.input.dtos.cliente.ClienteUpdateDTO;

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
    public ResponseEntity<BaseResponse<ClienteResponseDTO>> crear(@Valid @RequestBody ClienteRequestDTO dto) {
        Cliente entidadEntrada = dto.toEntity();

        Cliente entidadProcesada = createUseCase.execute(entidadEntrada);

        ClienteResponseDTO respuesta = ClienteResponseDTO.fromEntity(entidadProcesada);

        return buildResponse("Cliente creado exitosamente", respuesta, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<BaseResponse<Page<ClienteResponseDTO>>> listar(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ClienteResponseDTO> data = getUseCase.getAll(search, PageRequest.of(page, size))
                .map(ClienteResponseDTO::fromEntity);

        return buildResponse("Consulta exitosa", data, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ClienteResponseDTO>> obtener(@PathVariable Long id) {
        Cliente cliente = getUseCase.getById(id);
        ClienteResponseDTO response = ClienteResponseDTO.fromEntity(cliente);

        return buildResponse("Cliente encontrado", response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<ClienteResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteUpdateDTO dto) {

        Cliente datosEntidad = dto.toEntity();

        Cliente actualizado = updateUseCase.execute(id, datosEntidad);

        return buildResponse("Cliente actualizado exitosamente",
                ClienteResponseDTO.fromEntity(actualizado),
                HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<ClienteResponseDTO>> actualizarParcial(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        Cliente actualizado = updateUseCase.executePartial(id, updates);

        return buildResponse("Cliente actualizado parcialmente",
                ClienteResponseDTO.fromEntity(actualizado),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> eliminar(@PathVariable Long id) {
        deleteUseCase.execute(id);

        return buildResponse("Cliente desactivado exitosamente.", null, HttpStatus.OK);
    }

    private <T> ResponseEntity<BaseResponse<T>> buildResponse(String msg, T data, HttpStatus status) {
        BaseResponse<T> response = BaseResponse.<T>builder()
                .message(msg)
                .data(data)
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, status);
    }
}