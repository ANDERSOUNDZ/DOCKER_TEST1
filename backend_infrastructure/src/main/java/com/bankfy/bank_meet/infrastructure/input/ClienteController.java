package com.bankfy.bank_meet.infrastructure.input;

import com.bankfy.bank_meet.domain.models.Cliente;
import com.bankfy.bank_meet.domain.ports.in.CreateClienteUseCase;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final CreateClienteUseCase createClienteUseCase;

    // Constructor para Inyección de Dependencias
    public ClienteController(CreateClienteUseCase createClienteUseCase) {
        this.createClienteUseCase = createClienteUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente crearCliente(@Valid @RequestBody Cliente cliente) {
        return createClienteUseCase.execute(cliente);
    }
}