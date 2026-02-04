package com.bankfy.bank_meet.infrastructure.input;

import com.bankfy.bank_meet.domain.models.BaseResponse;
import com.bankfy.bank_meet.domain.models.Cliente;
import com.bankfy.bank_meet.domain.ports.in.CreateClienteUseCase;
import com.bankfy.bank_meet.infrastructure.output.ClienteRepository;
import jakarta.validation.Valid;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final CreateClienteUseCase createClienteUseCase;
    private final ClienteRepository clienteRepository;

    public ClienteController(CreateClienteUseCase createClienteUseCase, ClienteRepository clienteRepository) {
        this.createClienteUseCase = createClienteUseCase;
        this.clienteRepository = clienteRepository;
    }

    @PostMapping
    public ResponseEntity<BaseResponse<Cliente>> crearCliente(@Valid @RequestBody Cliente cliente) {
        Cliente nuevo = createClienteUseCase.execute(cliente);
        BaseResponse<Cliente> response = BaseResponse.<Cliente>builder()
                .message("Cliente creado con éxito")
                .data(nuevo)
                .status(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<BaseResponse<Page<Cliente>>> listarClientes(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Cliente> resultado = (search != null && !search.isEmpty())
                ? clienteRepository.findByNombreContainingIgnoreCaseOrIdentificacionContaining(search, search, pageable)
                : clienteRepository.findAll(pageable);

        BaseResponse<Page<Cliente>> response = BaseResponse.<Page<Cliente>>builder()
                .message("Consulta exitosa")
                .data(resultado)
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
}