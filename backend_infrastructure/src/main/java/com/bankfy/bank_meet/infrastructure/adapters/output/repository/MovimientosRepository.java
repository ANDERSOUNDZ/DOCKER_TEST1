package com.bankfy.bank_meet.infrastructure.adapters.output.repository;

import com.bankfy.bank_meet.domain.models.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientosRepository extends JpaRepository<Movimiento, Long> {

    @Query("SELECT m FROM Movimientos m WHERE m.cuenta.cliente.id = :clienteId " +
            "AND m.fecha BETWEEN :inicio AND :fin")
    List<Movimiento> findByClienteAndFechaRange(Long clienteId, LocalDateTime inicio, LocalDateTime fin);

    // NUEVA CONSULTA PARA EL PASO 3 Y 4:
    // Suma el valor absoluto de los movimientos tipo 'Retiro' o 'Debito' de un
    // cliente en un rango de fechas
    @Query("SELECT SUM(ABS(m.valor)) FROM Movimientos m " +
            "WHERE m.cuenta.cliente.id = :clienteId " +
            "AND (m.tipoMovimiento = 'Retiro' OR m.tipoMovimiento = 'Debito') " +
            "AND m.fecha >= :inicioDia AND m.fecha <= :finDia")
    BigDecimal sumRetirosDelDia(@Param("clienteId") Long clienteId,
            @Param("inicioDia") LocalDateTime inicioDia,
            @Param("finDia") LocalDateTime finDia);
}