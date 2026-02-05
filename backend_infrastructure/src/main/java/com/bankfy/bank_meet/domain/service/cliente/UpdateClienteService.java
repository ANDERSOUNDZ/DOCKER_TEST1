package com.bankfy.bank_meet.domain.service.cliente;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bankfy.bank_meet.domain.models.Cliente;
import com.bankfy.bank_meet.domain.ports.in.cliente.UpdateClienteUseCase;
import com.bankfy.bank_meet.infrastructure.output.ClienteRepository;
import com.bankfy.bank_meet.domain.exceptions.ValidationException;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UpdateClienteService implements UpdateClienteUseCase {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Cliente execute(Long id, Cliente nuevosDatos) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el cliente con ID: " + id));

        validateImmutableFields(clienteExistente, nuevosDatos);

        applyUpdates(clienteExistente, nuevosDatos);

        return clienteRepository.save(clienteExistente);
    }

    @Override
    @Transactional
    public Cliente executePartial(Long id, Map<String, Object> updates) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        Map<String, String> errores = new HashMap<>();

        updates.forEach((key, value) -> {
            try {
                switch (key) {
                    case "nombre", "identificacion", "clienteId" ->
                        errores.put(key, "Este campo es inmutable y no puede ser actualizado.");

                    case "genero", "direccion", "telefono" -> {
                        String val = (String) value;
                        if (val == null || val.isBlank()) {
                            errores.put(key, "El campo " + key + " es obligatorio.");
                        } else {
                            if (key.equals("genero"))
                                cliente.setGenero(val);
                            if (key.equals("direccion"))
                                cliente.setDireccion(val);
                            if (key.equals("telefono"))
                                cliente.setTelefono(val);
                        }
                    }

                    case "edad" -> {
                        if (value == null) {
                            errores.put("edad", "La edad es obligatoria.");
                        } else {
                            Integer val = (Integer) value;
                            if (val < 18)
                                errores.put("edad", "Debe ser mayor de 18 años.");
                            else
                                cliente.setEdad(val);
                        }
                    }

                    case "estado" -> cliente.setEstado((Boolean) value);

                    case "contrasena" -> {
                        String pass = (String) value;
                        if (pass == null || pass.length() < 4)
                            errores.put("contrasena", "La contraseña debe tener al menos 4 caracteres.");
                        else
                            cliente.setContrasena(passwordEncoder.encode(pass));
                    }
                }
            } catch (Exception e) {
                errores.put(key, "Error en el formato del valor proporcionado.");
            }
        });

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return clienteRepository.save(cliente);
    }

    private void validateImmutableFields(Cliente actual, Cliente nuevo) {
        Map<String, String> erroresInmutables = new HashMap<>();

        if (!actual.getNombre().equals(nuevo.getNombre())) {
            erroresInmutables.put("nombre", "No se permite cambiar el nombre por seguridad bancaria.");
        }
        if (!actual.getIdentificacion().equals(nuevo.getIdentificacion())) {
            erroresInmutables.put("identificacion", "No se permite cambiar la identificación.");
        }

        if (!erroresInmutables.isEmpty()) {
            throw new ValidationException(erroresInmutables);
        }
    }

    private void applyUpdates(Cliente target, Cliente source) {
        target.setGenero(source.getGenero());
        target.setEdad(source.getEdad());
        target.setDireccion(source.getDireccion());
        target.setTelefono(source.getTelefono());
        target.setEstado(source.getEstado());

        if (source.getContrasena() != null && !source.getContrasena().isBlank()) {
            target.setContrasena(passwordEncoder.encode(source.getContrasena()));
        }
    }
}