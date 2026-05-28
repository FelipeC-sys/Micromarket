package com.micromarket.service;

import com.micromarket.dto.request.CategoriaRequestDTO;
import com.micromarket.dto.response.CategoriaResponseDTO;
import com.micromarket.dto.response.ProductoResponseDTO;
import com.micromarket.entity.Categoria;
import com.micromarket.exception.ResourceNotFoundException;
import com.micromarket.repository.CategoriaRepository;
import com.micromarket.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public CategoriaService(CategoriaRepository categoriaRepository,
                            ProductoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    public CategoriaResponseDTO crear(CategoriaRequestDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        return toDTO(categoriaRepository.save(categoria));
    }

    public List<CategoriaResponseDTO> listarTodas() {
        return categoriaRepository.findAll().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public CategoriaResponseDTO buscarPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoría no encontrada con ID: " + id));
        return toDTO(categoria);
    }

    public CategoriaResponseDTO actualizar(Long id, CategoriaRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoría no encontrada con ID: " + id));
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        return toDTO(categoriaRepository.save(categoria));
    }

    public void eliminar(Long id) {
        categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoría no encontrada con ID: " + id));
        categoriaRepository.deleteById(id);
    }

    private CategoriaResponseDTO toDTO(Categoria c) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(c.getId());
        dto.setNombre(c.getNombre());
        dto.setDescripcion(c.getDescripcion());
        List<ProductoResponseDTO> productosActivos = productoRepository
                .findByCategoriaIdAndActivoTrue(c.getId())
                .stream().map(p -> {
                    ProductoResponseDTO pd = new ProductoResponseDTO();
                    pd.setId(p.getId());
                    pd.setNombre(p.getNombre());
                    pd.setCodigoBarras(p.getCodigoBarras());
                    pd.setDescripcion(p.getDescripcion());
                    pd.setPrecio(p.getPrecio());
                    pd.setStock(p.getStock());
                    pd.setActivo(p.isActivo());
                    pd.setCategoriaId(c.getId());
                    return pd;
                }).collect(Collectors.toList());
        dto.setProductos(productosActivos);
        return dto;
    }
}