package com.bankfy.bank_meet.infrastructure.input.dtos.cliente;

import com.bankfy.bank_meet.domain.models.Cliente;

public record ClienteResponseDTO(
        Long id,
        String clienteId,
        String nombre,
        String identificacion,
        String direccion,
        String telefono,
        String genero,
        Integer edad,
        Boolean estado) {
    // Método estático (Factory) para convertir Entidad a DTO de respuesta
    public static ClienteResponseDTO fromEntity(Cliente c) {
        return new ClienteResponseDTO(
                c.getId(), c.getClienteId(), c.getNombre(), c.getIdentificacion(),
                c.getDireccion(), c.getTelefono(), c.getGenero(), c.getEdad(), c.getEstado());
    }
}