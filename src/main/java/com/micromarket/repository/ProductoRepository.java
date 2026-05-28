package com.micromarket.repository;

import com.micromarket.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsByCodigoBarras(String codigoBarras);
    Optional<Producto> findByIdAndActivoTrue(Long id);
    List<Producto> findAllByActivoTrue();
    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);
}