package com.bankfy.bank_meet.infrastructure.input.controllers;

import com.bankfy.bank_meet.domain.models.BaseResponse;
import com.bankfy.bank_meet.domain.models.Cliente;
import com.bankfy.bank_meet.domain.ports.in.cliente.*;
import com.bankfy.bank_meet.infrastructure.input.dtos.cliente.ClienteRequestDTO;
import com.bankfy.bank_meet.infrastructure.input.dtos.cliente.ClienteResponseDTO;
import com.bankfy.bank_meet.infrastructure.input.dtos.cliente.ClienteUpdateDTO;

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
        // 1. RequestDTO -> Entity (vía toEntity)
        Cliente entidadEntrada = dto.toEntity();

        // 2. Ejecución de lógica de negocio
        Cliente entidadProcesada = createUseCase.execute(entidadEntrada);

        // 3. Entity -> ResponseDTO (vía fromEntity)
        ClienteResponseDTO respuesta = ClienteResponseDTO.fromEntity(entidadProcesada);

        return buildResponse("Cliente creado exitosamente", respuesta, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<BaseResponse<Page<ClienteResponseDTO>>> listar(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // 1. Llamada al caso de uso (Retorna Page de Entidades)
        // 2. Transformación funcional: map(ClienteResponseDTO::fromEntity)
        Page<ClienteResponseDTO> data = getUseCase.getAll(search, PageRequest.of(page, size))
                .map(ClienteResponseDTO::fromEntity);

        return buildResponse("Consulta exitosa", data, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ClienteResponseDTO>> obtener(@PathVariable Long id) {
        // Obtenemos del servicio y mapeamos inmediatamente al DTO de respuesta
        Cliente cliente = getUseCase.getById(id);
        ClienteResponseDTO response = ClienteResponseDTO.fromEntity(cliente);

        return buildResponse("Cliente encontrado", response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<ClienteResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteUpdateDTO dto) {

        // 1. RequestDTO (Update) -> Entity
        Cliente datosEntidad = dto.toEntity();

        // 2. Ejecutar Lógica
        Cliente actualizado = updateUseCase.execute(id, datosEntidad);

        // 3. Entity -> ResponseDTO
        return buildResponse("Cliente actualizado exitosamente",
                ClienteResponseDTO.fromEntity(actualizado),
                HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<ClienteResponseDTO>> actualizarParcial(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        // 1. Proceso (El service maneja el Map directamente)
        Cliente actualizado = updateUseCase.executePartial(id, updates);

        // 2. Entity -> ResponseDTO
        return buildResponse("Cliente actualizado parcialmente",
                ClienteResponseDTO.fromEntity(actualizado),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> eliminar(@PathVariable Long id) {
        // Llamada directa al caso de uso
        deleteUseCase.execute(id);

        // Retornamos una respuesta limpia usando tu método buildResponse
        return buildResponse("Cliente eliminado exitosamente.", null, HttpStatus.OK);
    }

    // Método genérico para respuestas consistentes
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