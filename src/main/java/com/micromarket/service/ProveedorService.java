package com.micromarket.service;

import com.micromarket.dto.request.EntradaAlmacenRequestDTO;
import com.micromarket.dto.request.ProveedorRequestDTO;
import com.micromarket.dto.response.ProductoResponseDTO;
import com.micromarket.dto.response.ProveedorResponseDTO;
import com.micromarket.entity.Producto;
import com.micromarket.entity.Proveedor;
import com.micromarket.exception.ResourceNotFoundException;
import com.micromarket.repository.ProductoRepository;
import com.micromarket.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;

    public ProveedorService(ProveedorRepository proveedorRepository,
                            ProductoRepository productoRepository) {
        this.proveedorRepository = proveedorRepository;
        this.productoRepository = productoRepository;
    }

    public ProveedorResponseDTO crear(ProveedorRequestDTO dto) {
        if (proveedorRepository.existsByNit(dto.getNit())) {
            throw new IllegalArgumentException(
                    "Ya existe un proveedor con el NIT: " + dto.getNit());
        }
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(dto.getNombre());
        proveedor.setNit(dto.getNit());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setEmail(dto.getEmail());
        return toDTO(proveedorRepository.save(proveedor));
    }

    public List<ProveedorResponseDTO> listarTodos() {
        return proveedorRepository.findAll().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public ProveedorResponseDTO buscarPorId(Long id) {
        return toDTO(proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Proveedor no encontrado con ID: " + id)));
    }

    public ProveedorResponseDTO actualizar(Long id, ProveedorRequestDTO dto) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Proveedor no encontrado con ID: " + id));
        proveedor.setNombre(dto.getNombre());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setEmail(dto.getEmail());
        return toDTO(proveedorRepository.save(proveedor));
    }

    public void eliminar(Long id) {
        proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Proveedor no encontrado con ID: " + id));
        proveedorRepository.deleteById(id);
    }

    @Transactional
    public ProductoResponseDTO entradaAlmacen(EntradaAlmacenRequestDTO dto) {
        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con ID: " + dto.getProductoId()));
        Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Proveedor no encontrado con ID: " + dto.getProveedorId()));
        producto.setStock(producto.getStock() + dto.getCantidad());
        producto.getProveedores().add(proveedor);
        productoRepository.save(producto);

        ProductoResponseDTO pd = new ProductoResponseDTO();
        pd.setId(producto.getId());
        pd.setNombre(producto.getNombre());
        pd.setCodigoBarras(producto.getCodigoBarras());
        pd.setPrecio(producto.getPrecio());
        pd.setStock(producto.getStock());
        pd.setActivo(producto.isActivo());
        return pd;
    }

    private ProveedorResponseDTO toDTO(Proveedor p) {
        ProveedorResponseDTO dto = new ProveedorResponseDTO();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setNit(p.getNit());
        dto.setTelefono(p.getTelefono());
        dto.setEmail(p.getEmail());
        return dto;
    }
}