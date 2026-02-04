package com.bankfy.bank_meet.domain.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El género es obligatorio")
    private String genero;

    @Min(value = 18, message = "Debe ser mayor de 18 años")
    @Max(value = 120, message = "Edad no válida")
    private Integer edad;

    @NotBlank(message = "La identificación es obligatoria")
    @Pattern(
        regexp = "^[a-zA-Z0-9]{10,13}$", 
        message = "La identificación debe tener entre 10 y 13 caracteres alfanuméricos (sin espacios ni caracteres especiales)"
    )
    @Column(unique = true)
    private String identificacion;
    
    private String direccion;
    private String telefono;
}