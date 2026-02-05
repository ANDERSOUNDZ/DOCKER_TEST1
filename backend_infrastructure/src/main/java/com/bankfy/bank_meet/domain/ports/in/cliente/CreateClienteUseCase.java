package com.bankfy.bank_meet.domain.ports.in.cliente;

import com.bankfy.bank_meet.domain.models.Cliente;

public interface CreateClienteUseCase {
    Cliente execute(Cliente cliente);
}