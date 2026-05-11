package microservice.logistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import microservice.logistica.model.DetalleEnvio;
import microservice.logistica.repository.DetalleEnvioRepository;

@Service
@Transactional
public class DetalleEnvioService {

    @Autowired
    private DetalleEnvioRepository repository;

    public List<DetalleEnvio> obtenerTodos() {
        return repository.findAll();
    }

    public DetalleEnvio obtenerPorId(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public DetalleEnvio guardar(DetalleEnvio detalle) {
        return repository.save(detalle);
    }

    public DetalleEnvio actualizar(Integer id, DetalleEnvio detalle) {
        DetalleEnvio existente = repository.findById(id).orElse(null);

        if (existente != null) {
            existente.setEnvio(detalle.getEnvio());
            existente.setTipoRecursoId(detalle.getTipoRecursoId());
            existente.setCantidad(detalle.getCantidad());

            return repository.save(existente);
        }

        return null;
    }

    public DetalleEnvio actualizarParcial(Integer id, DetalleEnvio detalle) {
        DetalleEnvio existente = repository.findById(id).orElse(null);

        if (existente != null) {

            if (detalle.getEnvio() != null)
                existente.setEnvio(detalle.getEnvio());

            if (detalle.getTipoRecursoId() != null)
                existente.setTipoRecursoId(detalle.getTipoRecursoId());

            if (detalle.getCantidad() != null)
                existente.setCantidad(detalle.getCantidad());

            return repository.save(existente);
        }

        return null;
    }

    public void eliminar(Integer id) {
        DetalleEnvio detalle = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));

        repository.delete(detalle);
    }

    // filtros

    public List<DetalleEnvio> porEnvio(Integer envioId) {
        return repository.findByEnvioId(envioId);
    }

    public List<DetalleEnvio> porTipoRecurso(Integer tipoId) {
        return repository.findByTipoRecursoId(tipoId);
    }
}