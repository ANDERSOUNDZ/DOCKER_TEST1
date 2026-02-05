package com.bankfy.bank_meet.application.service.movimiento;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bankfy.bank_meet.application.ports.output.cliente.ClientePersistencePort;
import com.bankfy.bank_meet.application.ports.output.movimiento.MovimientoPersistencePort;
import com.bankfy.bank_meet.domain.models.movimiento.Movimiento;
import com.bankfy.bank_meet.domain.models.cuenta.Cuenta;
import com.bankfy.bank_meet.domain.models.cliente.Cliente;
import com.bankfy.bank_meet.infrastructure.adapters.input.dtos.movimiento.ReporteEstadoCuentaDTO;

@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock
    private MovimientoPersistencePort movimientoPersistencePort;

    @Mock
    private ClientePersistencePort clientePersistencePort;

    @InjectMocks
    private GetReporteService generarReporteService;

    @Test
    @DisplayName("Caso de Uso 5: Generar Reporte Completo (JSON/Base64) - Imagen 5 y 6")
    void testGenerarReporte_Exito() {
        // --- ARRANGE ---
        LocalDateTime inicio = LocalDateTime.now().minusDays(1);
        LocalDateTime fin = LocalDateTime.now();
        Long clienteId = 1L;

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        cliente.setNombre("Jose Lema");

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("478758");
        cuenta.setTipoCuenta("Ahorros");
        cuenta.setEstado(true);

        Movimiento mov = new Movimiento();
        mov.setFecha(LocalDateTime.now());
        mov.setValor(new BigDecimal("-575.00")); // Débito
        mov.setSaldoAnterior(new BigDecimal("2000.00"));
        mov.setSaldo(new BigDecimal("1425.00"));
        mov.setCuenta(cuenta);

        // Mocks
        when(clientePersistencePort.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(movimientoPersistencePort.findByClienteAndFechaRange(eq(clienteId), any(), any()))
                .thenReturn(List.of(mov));

        // --- ACT ---
        ReporteEstadoCuentaDTO reporte = generarReporteService.generarReporte(clienteId, inicio, fin);

        // --- ASSERT ---
        assertNotNull(reporte);
        assertEquals("Jose Lema", reporte.getCliente());

        assertEquals(new BigDecimal("-575.00"), reporte.getTotalDebitos());
        assertEquals(BigDecimal.ZERO, reporte.getTotalCreditos());

        assertNotNull(reporte.getPdfBase64());
        assertFalse(reporte.getPdfBase64().isEmpty());

        assertDoesNotThrow(() -> Base64.getDecoder().decode(reporte.getPdfBase64()));
    }
}