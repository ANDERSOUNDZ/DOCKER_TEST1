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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateClienteService implements UpdateClienteUseCase {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Cliente execute(Long id, Cliente nuevosDatos) {
        return clienteRepository.findById(id)
                .map(existente -> {
                    // Solo actualizamos los campos permitidos (Inyectamos lógica funcional)
                    applyUpdates(existente, nuevosDatos);
                    return clienteRepository.save(existente);
                })
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el cliente con ID: " + id));
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

                    case "genero" -> cliente.setGenero((String) value);
                    case "direccion" -> cliente.setDireccion((String) value);
                    case "telefono" -> cliente.setTelefono((String) value);
                    case "estado" -> cliente.setEstado((Boolean) value);
                    case "edad" -> {
                        Integer edad = (Integer) value;
                        if (edad < 18)
                            errores.put("edad", "Debe ser mayor de 18 años.");
                        else
                            cliente.setEdad(edad);
                    }
                    case "contrasena" -> {
                        String pass = (String) value;
                        if (pass.length() < 4)
                            errores.put("contrasena", "Mínimo 4 caracteres.");
                        else
                            cliente.setContrasena(passwordEncoder.encode(pass));
                    }
                }
            } catch (Exception e) {
                errores.put(key, "Formato de valor inválido.");
            }
        });

        if (!errores.isEmpty())
            throw new ValidationException(errores);

        return clienteRepository.save(cliente);
    }

    private void applyUpdates(Cliente target, Cliente source) {
        target.setGenero(source.getGenero());
        target.setEdad(source.getEdad());
        target.setDireccion(source.getDireccion());
        target.setTelefono(source.getTelefono());
        target.setEstado(source.getEstado());

        Optional.ofNullable(source.getContrasena())
                .filter(pass -> !pass.isBlank())
                .ifPresent(pass -> target.setContrasena(passwordEncoder.encode(pass)));
    }
}