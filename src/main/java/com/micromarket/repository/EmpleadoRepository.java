package com.micromarket.repository;

import com.micromarket.entity.Empleado;
import com.micromarket.enums.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    List<Empleado> findByCargo(Cargo cargo);

    List<Empleado> findByFechaIngresoBetween(LocalDate desde, LocalDate hasta);

    boolean existsByCedula(String cedula);
}