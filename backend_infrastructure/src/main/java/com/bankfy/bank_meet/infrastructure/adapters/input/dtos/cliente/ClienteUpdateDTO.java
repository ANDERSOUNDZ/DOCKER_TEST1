package com.bankfy.bank_meet.infrastructure.adapters.input.dtos.cliente;

import com.bankfy.bank_meet.domain.models.cliente.Cliente;

import jakarta.validation.constraints.*;

public record ClienteUpdateDTO(
        @NotBlank(message = "La dirección es obligatoria") String direccion,
        @NotBlank(message = "El teléfono es obligatorio") String telefono,
        @NotBlank(message = "El género es obligatorio") String genero,
        @NotNull(message = "La edad es obligatoria") @Min(value = 18, message = "Debe ser mayor de 18 años") Integer edad,
        @NotNull(message = "El estado es obligatorio") Boolean estado,
        @Size(min = 4, message = "La contraseña debe tener mínimo 4 caracteres") String contrasena) {
    public Cliente toEntity() {
        Cliente c = new Cliente();
        c.setDireccion(direccion);
        c.setTelefono(telefono);
        c.setGenero(genero);
        c.setEdad(edad);
        c.setEstado(estado);
        c.setContrasena(contrasena);
        return c;
    }
}