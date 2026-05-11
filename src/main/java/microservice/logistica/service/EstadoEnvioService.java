package microservice.logistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import microservice.logistica.model.EstadoEnvio;
import microservice.logistica.repository.EstadoEnvioRepository;

@Service
@Transactional
public class EstadoEnvioService {

    @Autowired
    private EstadoEnvioRepository repository;

    public List<EstadoEnvio> obtenerTodos() {
        return repository.findAll();
    }

    public EstadoEnvio obtenerPorId(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public EstadoEnvio guardar(EstadoEnvio estado) {
        return repository.save(estado);
    }

    public EstadoEnvio actualizar(Integer id, EstadoEnvio estado) {
        EstadoEnvio existente = repository.findById(id).orElse(null);

        if (existente != null) {
            existente.setNombre(estado.getNombre());
            return repository.save(existente);
        }
        return null;
    }

    public EstadoEnvio actualizarParcial(Integer id, EstadoEnvio estado) {
        EstadoEnvio existente = repository.findById(id).orElse(null);

        if (existente != null) {
            if (estado.getNombre() != null)
                existente.setNombre(estado.getNombre());

            return repository.save(existente);
        }
        return null;
    }

    public void eliminar(Integer id) {
        EstadoEnvio estado = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        repository.delete(estado);
    }
}