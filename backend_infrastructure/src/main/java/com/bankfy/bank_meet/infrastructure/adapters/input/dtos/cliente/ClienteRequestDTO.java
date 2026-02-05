package com.bankfy.bank_meet.infrastructure.adapters.input.dtos.cliente;

import com.bankfy.bank_meet.domain.models.cliente.Cliente;

import jakarta.validation.constraints.*;

public record ClienteRequestDTO(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "La dirección es obligatoria") String direccion,
        @NotBlank(message = "El teléfono es obligatorio") String telefono,
        @NotBlank(message = "La contraseña es obligatoria") @Size(min = 4, message = "Mínimo 4 caracteres") String contrasena,
        @NotBlank(message = "El género es obligatorio") String genero,
        @NotNull(message = "La edad es obligatoria") @Min(value = 18, message = "Debe ser mayor de 18 años") Integer edad,
        @NotBlank(message = "La identificación es obligatoria") @Pattern(regexp = "^[a-zA-Z0-9]{10,13}$", message = "Identificación inválida") String identificacion) {
    public Cliente toEntity() {
        Cliente c = new Cliente();
        c.setNombre(nombre);
        c.setDireccion(direccion);
        c.setTelefono(telefono);
        c.setContrasena(contrasena);
        c.setGenero(genero);
        c.setEdad(edad);
        c.setIdentificacion(identificacion);
        return c;
    }
}