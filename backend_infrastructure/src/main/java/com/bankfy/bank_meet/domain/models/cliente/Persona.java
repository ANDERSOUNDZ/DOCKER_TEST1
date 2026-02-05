package com.bankfy.bank_meet.domain.models.cliente;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "persona")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 20)
    private String genero;

    @Column(nullable = false)
    private Integer edad;

    @Column(unique = true, nullable = false, length = 13)
    private String identificacion;

    private String direccion;
    private String telefono;
}