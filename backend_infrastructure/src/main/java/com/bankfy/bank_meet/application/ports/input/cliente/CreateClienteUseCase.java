package com.bankfy.bank_meet.application.ports.input.cliente;

import com.bankfy.bank_meet.domain.models.Cliente;

public interface CreateClienteUseCase {
    Cliente execute(Cliente cliente);
}