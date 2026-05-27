package com.micromarket.service;

import com.micromarket.dto.request.DetalleVentaRequestDTO;
import com.micromarket.dto.request.VentaRequestDTO;
import com.micromarket.dto.response.DetalleVentaResponseDTO;
import com.micromarket.dto.response.VentaResponseDTO;
import com.micromarket.entity.*;
import com.micromarket.exception.*;
import com.micromarket.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VentaService {

    private static final BigDecimal IVA_PORCENTAJE = new BigDecimal("0.19");

    private final VentaRepository ventaRepository;
    private final EmpleadoService empleadoService;

    public VentaService(VentaRepository ventaRepository,
                        EmpleadoService empleadoService) {
        this.ventaRepository = ventaRepository;
        this.empleadoService = empleadoService;
    }

    @Transactional
    public VentaResponseDTO procesarVenta(VentaRequestDTO dto) {
        Empleado empleado = empleadoService.findOrThrow(dto.getEmpleadoId());

        Venta venta = new Venta();
        venta.setEmpleado(empleado);

        List<DetalleVenta> detalles = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (DetalleVentaRequestDTO detalleDTO : dto.getDetalles()) {
            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProductoId(detalleDTO.getProductoId());
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(BigDecimal.ZERO);
            detalle.setSubtotal(BigDecimal.ZERO);
            detalles.add(detalle);
        }

        venta.setDetalles(detalles);
        venta.setSubtotal(subtotal.setScale(2, RoundingMode.HALF_UP));
        venta.setIva(BigDecimal.ZERO);
        venta.setTotal(BigDecimal.ZERO);

        Venta guardada = ventaRepository.save(venta);
        return toDTO(guardada);
    }

    public List<VentaResponseDTO> listarTodas() {
        return ventaRepository.findAll().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public VentaResponseDTO buscarPorId(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Venta no encontrada con ID: " + id));
        return toDTO(venta);
    }

    private VentaResponseDTO toDTO(Venta v) {
        VentaResponseDTO dto = new VentaResponseDTO();
        dto.setId(v.getId());
        dto.setEmpleadoId(v.getEmpleado().getId());
        dto.setNombreEmpleado(v.getEmpleado().getNombre());
        dto.setFecha(v.getFecha());
        dto.setSubtotal(v.getSubtotal());
        dto.setIva(v.getIva());
        dto.setTotal(v.getTotal());
        dto.setDetalles(v.getDetalles().stream().map(d -> {
            DetalleVentaResponseDTO dd = new DetalleVentaResponseDTO();
            dd.setProductoId(d.getProductoId());
            dd.setCantidad(d.getCantidad());
            dd.setPrecioUnitario(d.getPrecioUnitario());
            dd.setSubtotal(d.getSubtotal());
            return dd;
        }).collect(Collectors.toList()));
        return dto;
    }
}