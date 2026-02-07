package com.bankfy.bank_meet.infrastructure.adapters.output.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bankfy.bank_meet.domain.models.movimiento.Movimiento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientosRepository extends JpaRepository<Movimiento, Long> {

        @Query("SELECT m FROM Movimiento m WHERE m.cuenta.cliente.id = :clienteId " +
                        "AND m.fecha BETWEEN :inicio AND :fin")
        Page<Movimiento> findByClienteAndFechaRange(
                        @Param("clienteId") Long clienteId,
                        @Param("inicio") LocalDateTime inicio,
                        @Param("fin") LocalDateTime fin,
                        Pageable pageable);

        @Query("SELECT m FROM Movimiento m WHERE m.cuenta.cliente.id = :clienteId " +
                        "AND m.fecha BETWEEN :inicio AND :fin")
        List<Movimiento> findByClienteAndFechaRange(Long clienteId, LocalDateTime inicio, LocalDateTime fin);

        @Query("SELECT m FROM Movimiento m WHERE m.fecha BETWEEN :inicio AND :fin")
        Page<Movimiento> findAllByFechaBetween(
                        @Param("inicio") LocalDateTime inicio,
                        @Param("fin") LocalDateTime fin,
                        Pageable pageable);

        @Query("SELECT SUM(ABS(m.valor)) FROM Movimiento m " +
                        "WHERE m.cuenta.cliente.id = :clienteId " +
                        "AND (m.tipoMovimiento LIKE 'Retiro%' OR m.tipoMovimiento LIKE 'Debito%') " +
                        "AND m.fecha >= :inicioDia AND m.fecha <= :finDia")
        BigDecimal sumRetirosDelDia(@Param("clienteId") Long clienteId,
                        @Param("inicioDia") LocalDateTime inicioDia,
                        @Param("finDia") LocalDateTime finDia);

        @Query("SELECT m FROM Movimiento m WHERE " +
                        "(m.fecha BETWEEN :inicio AND :fin) AND " +
                        "(:search IS NULL OR :search = '' OR " +
                        "LOWER(m.cuenta.numeroCuenta) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                        "LOWER(m.cuenta.cliente.nombre) LIKE LOWER(CONCAT('%', :search, '%')))")
        Page<Movimiento> findAllWithSearch(
                        @Param("inicio") LocalDateTime inicio,
                        @Param("fin") LocalDateTime fin,
                        @Param("search") String search,
                        Pageable pageable);
}