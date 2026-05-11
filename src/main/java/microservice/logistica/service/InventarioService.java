package microservice.logistica.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import microservice.logistica.model.Inventario;
import microservice.logistica.repository.InventarioRepository;

@Service
@Transactional
public class InventarioService {

    @Autowired
    private InventarioRepository repository;

    public List<Inventario> obtenerTodos() {
        return repository.findAll();
    }

    public Inventario obtenerPorId(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public Inventario guardar(Inventario inventario) {
        return repository.save(inventario);
    }

    public Inventario actualizar(Integer id, Inventario inventario) {

        Inventario existente = repository.findById(id).orElse(null);

        if (existente != null) {

            existente.setCentroAcopioId(inventario.getCentroAcopioId());
            existente.setTipoRecursoId(inventario.getTipoRecursoId());
            existente.setCantidadDisponible(inventario.getCantidadDisponible());
            existente.setActualizadoEn(inventario.getActualizadoEn());

            return repository.save(existente);
        }

        return null;
    }

    public Inventario actualizarParcial(Integer id, Inventario inventario) {

        Inventario existente = repository.findById(id).orElse(null);

        if (existente != null) {

            if (inventario.getCentroAcopioId() != null)
                existente.setCentroAcopioId(inventario.getCentroAcopioId());

            if (inventario.getTipoRecursoId() != null)
                existente.setTipoRecursoId(inventario.getTipoRecursoId());

            if (inventario.getCantidadDisponible() != null)
                existente.setCantidadDisponible(inventario.getCantidadDisponible());

            if (inventario.getActualizadoEn() != null)
                existente.setActualizadoEn(inventario.getActualizadoEn());

            return repository.save(existente);
        }

        return null;
    }

    public void eliminar(Integer id) {

        Inventario inventario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));

        repository.delete(inventario);
    }

    // filtros

    public List<Inventario> porCentro(Integer centroAcopioId) {
        return repository.findByCentroAcopioId(centroAcopioId);
    }

    public List<Inventario> porTipoRecurso(Integer tipoRecursoId) {
        return repository.findByTipoRecursoId(tipoRecursoId);
    }
    
    public Inventario agregarStock(Integer centroAcopioId, Integer tipoRecursoId, BigDecimal cantidad) {
    Inventario inv = repository.findByCentroAcopioIdAndTipoRecursoId(centroAcopioId, tipoRecursoId)
        .orElse(new Inventario(null, centroAcopioId, tipoRecursoId, BigDecimal.ZERO, LocalDateTime.now()));
    inv.setCantidadDisponible(inv.getCantidadDisponible().add(cantidad));
    inv.setActualizadoEn(LocalDateTime.now());
    return repository.save(inv);
    }

    public Inventario restarStock(Integer centroAcopioId, Integer tipoRecursoId, BigDecimal cantidad) {
        Inventario inv = repository.findByCentroAcopioIdAndTipoRecursoId(centroAcopioId, tipoRecursoId)
            .orElseThrow(() -> new RuntimeException("No hay inventario para ese recurso"));
        if (inv.getCantidadDisponible().compareTo(cantidad) < 0)
            throw new RuntimeException("Stock insuficiente");
        inv.setCantidadDisponible(inv.getCantidadDisponible().subtract(cantidad));
        inv.setActualizadoEn(LocalDateTime.now());
        return repository.save(inv);
    }
}