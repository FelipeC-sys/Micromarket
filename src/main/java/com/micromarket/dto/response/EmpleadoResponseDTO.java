package com.micromarket.dto.response;

import com.micromarket.enums.Cargo;
import java.math.BigDecimal;
import java.time.LocalDate;

public class EmpleadoResponseDTO {

    private Long id;
    private String cedula;
    private String nombre;
    private Cargo cargo;
    private LocalDate fechaIngreso;
    private BigDecimal salario;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Cargo getCargo() { return cargo; }
    public void setCargo(Cargo cargo) { this.cargo = cargo; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }
}