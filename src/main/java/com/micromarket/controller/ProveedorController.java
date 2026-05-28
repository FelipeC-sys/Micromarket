package com.micromarket.controller;

import com.micromarket.dto.request.EntradaAlmacenRequestDTO;
import com.micromarket.dto.request.ProveedorRequestDTO;
import com.micromarket.dto.response.ProductoResponseDTO;
import com.micromarket.dto.response.ProveedorResponseDTO;
import com.micromarket.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @PostMapping
    public ResponseEntity<ProveedorResponseDTO> crear(@Valid @RequestBody ProveedorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(proveedorService.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProveedorResponseDTO>> listar() {
        return ResponseEntity.ok(proveedorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(proveedorService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorResponseDTO> actualizar(@PathVariable Long id,
                                                            @Valid @RequestBody ProveedorRequestDTO dto) {
        return ResponseEntity.ok(proveedorService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        proveedorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/entrada-almacen")
    public ResponseEntity<ProductoResponseDTO> entradaAlmacen(
            @Valid @RequestBody EntradaAlmacenRequestDTO dto) {
        return ResponseEntity.ok(proveedorService.entradaAlmacen(dto));
    }
}