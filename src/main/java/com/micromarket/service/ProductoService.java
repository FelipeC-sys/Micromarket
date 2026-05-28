package com.micromarket.service;

import com.micromarket.dto.request.ProductoRequestDTO;
import com.micromarket.dto.response.ProductoResponseDTO;
import com.micromarket.entity.Categoria;
import com.micromarket.entity.Producto;
import com.micromarket.exception.ResourceNotFoundException;
import com.micromarket.repository.CategoriaRepository;
import com.micromarket.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository,
                           CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public ProductoResponseDTO crear(ProductoRequestDTO dto) {
        if (productoRepository.existsByCodigoBarras(dto.getCodigoBarras())) {
            throw new IllegalArgumentException(
                    "Ya existe un producto con el código de barras: " + dto.getCodigoBarras());
        }
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setCodigoBarras(dto.getCodigoBarras());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setActivo(true);
        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Categoría no encontrada con ID: " + dto.getCategoriaId()));
            producto.setCategoria(categoria);
        }
        return toDTO(productoRepository.save(producto));
    }

    public List<ProductoResponseDTO> listarTodos() {
        return productoRepository.findAllByActivoTrue().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public ProductoResponseDTO buscarPorId(Long id) {
        Producto producto = productoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con ID: " + id));
        return toDTO(producto);
    }

    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto) {
        Producto producto = productoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con ID: " + id));
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Categoría no encontrada con ID: " + dto.getCategoriaId()));
            producto.setCategoria(categoria);
        }
        return toDTO(productoRepository.save(producto));
    }

    public void eliminar(Long id) {
        Producto producto = productoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con ID: " + id));
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    public Producto findOrThrow(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con ID: " + id));
    }

    private ProductoResponseDTO toDTO(Producto p) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setCodigoBarras(p.getCodigoBarras());
        dto.setDescripcion(p.getDescripcion());
        dto.setPrecio(p.getPrecio());
        dto.setStock(p.getStock());
        dto.setActivo(p.isActivo());
        if (p.getCategoria() != null) {
            dto.setCategoriaId(p.getCategoria().getId());
        }
        return dto;
    }
}