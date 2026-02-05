package com.bankfy.bank_meet.domain.ports.in.cliente;

import java.util.Map;

import com.bankfy.bank_meet.domain.models.Cliente;

public interface UpdateClienteUseCase {
    Cliente execute(Long id, Cliente cliente);
    Cliente executePartial(Long id, Map<String, Object> updates);
}