package com.micromarket.service;

import com.micromarket.dto.request.EmpleadoRequestDTO;
import com.micromarket.dto.response.EmpleadoResponseDTO;
import com.micromarket.entity.Empleado;
import com.micromarket.enums.Cargo;
import com.micromarket.exception.ResourceNotFoundException;
import com.micromarket.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public EmpleadoResponseDTO crear(EmpleadoRequestDTO dto) {
        Empleado empleado = new Empleado();
        empleado.setCedula(dto.getCedula());
        empleado.setNombre(dto.getNombre());
        empleado.setCargo(dto.getCargo());
        empleado.setFechaIngreso(dto.getFechaIngreso());
        empleado.setSalario(dto.getSalario());
        return toDTO(empleadoRepository.save(empleado));
    }

    public List<EmpleadoResponseDTO> listarTodos() {
        return empleadoRepository.findAll().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public EmpleadoResponseDTO buscarPorId(Long id) {
        return toDTO(findOrThrow(id));
    }

    public EmpleadoResponseDTO actualizar(Long id, EmpleadoRequestDTO dto) {
        Empleado empleado = findOrThrow(id);
        empleado.setNombre(dto.getNombre());
        empleado.setCargo(dto.getCargo());
        empleado.setFechaIngreso(dto.getFechaIngreso());
        empleado.setSalario(dto.getSalario());
        return toDTO(empleadoRepository.save(empleado));
    }

    public void eliminar(Long id) {
        findOrThrow(id);
        empleadoRepository.deleteById(id);
    }

    public List<EmpleadoResponseDTO> filtrarPorCargo(Cargo cargo) {
        return empleadoRepository.findByCargo(cargo).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public List<EmpleadoResponseDTO> filtrarPorFecha(LocalDate desde, LocalDate hasta) {
        return empleadoRepository.findByFechaIngresoBetween(desde, hasta).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public Empleado findOrThrow(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Empleado no encontrado con ID: " + id));
    }

    private EmpleadoResponseDTO toDTO(Empleado e) {
        EmpleadoResponseDTO dto = new EmpleadoResponseDTO();
        dto.setId(e.getId());
        dto.setCedula(e.getCedula());
        dto.setNombre(e.getNombre());
        dto.setCargo(e.getCargo());
        dto.setFechaIngreso(e.getFechaIngreso());
        dto.setSalario(e.getSalario());
        return dto;
    }
}