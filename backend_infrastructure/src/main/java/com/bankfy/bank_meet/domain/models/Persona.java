package com.bankfy.bank_meet.domain.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "persona")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, updatable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El género es obligatorio")
    @Column(nullable = false, length = 20)
    private String genero;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 18, message = "Debe ser mayor de 18 años")
    @Max(value = 120, message = "Edad no válida")
    private Integer edad;

    @NotBlank(message = "La identificación es obligatoria")
    @Pattern(regexp = "^[a-zA-Z0-9]{10,13}$", message = "Identificación inválida")
    @Column(unique = true, nullable = false, updatable = false, length = 13)
    private String identificacion;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;
}