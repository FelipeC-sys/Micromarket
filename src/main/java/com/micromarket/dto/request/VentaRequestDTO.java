package com.micromarket.dto.request;

import jakarta.validation.constraints.*;
import java.util.List;

public class VentaRequestDTO {

    @NotNull(message = "El ID del empleado es obligatorio")
    private Long empleadoId;

    @NotEmpty(message = "Debe incluir al menos un producto")
    private List<DetalleVentaRequestDTO> detalles;

    public Long getEmpleadoId() { return empleadoId; }
    public void setEmpleadoId(Long empleadoId) { this.empleadoId = empleadoId; }
    public List<DetalleVentaRequestDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVentaRequestDTO> detalles) { this.detalles = detalles; }
}