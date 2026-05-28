package com.micromarket.dto.response;

import java.util.List;

public class CategoriaResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private List<ProductoResponseDTO> productos;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public List<ProductoResponseDTO> getProductos() { return productos; }
    public void setProductos(List<ProductoResponseDTO> productos) { this.productos = productos; }
}