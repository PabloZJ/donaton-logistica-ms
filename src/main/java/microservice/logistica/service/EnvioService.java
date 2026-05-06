package microservice.logistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import microservice.logistica.model.Envio;
import microservice.logistica.repository.EnvioRepository;

@Service
@Transactional
public class EnvioService {

    @Autowired
    private EnvioRepository repository;

    public List<Envio> obtenerTodos() {
        return repository.findAll();
    }

    public Envio obtenerPorId(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public Envio guardar(Envio envio) {
        return repository.save(envio);
    }

    public Envio actualizar(Integer id, Envio envio) {
        Envio existente = repository.findById(id).orElse(null);

        if (existente != null) {
            existente.setCentroAcopio(envio.getCentroAcopio());
            existente.setDestino(envio.getDestino());
            existente.setComuna(envio.getComuna());
            existente.setFechaPlanificada(envio.getFechaPlanificada());
            existente.setFechaEntrega(envio.getFechaEntrega());
            existente.setEstado(envio.getEstado());
            existente.setResponsableUid(envio.getResponsableUid());

            return repository.save(existente);
        }

        return null;
    }

    public Envio actualizarParcial(Integer id, Envio envio) {
        Envio existente = repository.findById(id).orElse(null);

        if (existente != null) {

            if (envio.getCentroAcopio() != null)
                existente.setCentroAcopio(envio.getCentroAcopio());

            if (envio.getDestino() != null)
                existente.setDestino(envio.getDestino());

            if (envio.getComuna() != null)
                existente.setComuna(envio.getComuna());

            if (envio.getFechaPlanificada() != null)
                existente.setFechaPlanificada(envio.getFechaPlanificada());

            if (envio.getFechaEntrega() != null)
                existente.setFechaEntrega(envio.getFechaEntrega());

            if (envio.getEstado() != null)
                existente.setEstado(envio.getEstado());

            if (envio.getResponsableUid() != null)
                existente.setResponsableUid(envio.getResponsableUid());

            return repository.save(existente);
        }

        return null;
    }

    public void eliminar(Integer id) {
        Envio envio = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envio no encontrado"));

        repository.delete(envio);
    }

    // filtros

    public List<Envio> porEstado(Integer estadoId) {
        return repository.findByEstadoId(estadoId);
    }

    public List<Envio> porCentro(Integer centroId) {
        return repository.findByCentroAcopioId(centroId);
    }

    public List<Envio> porComuna(Integer comunaId) {
        return repository.findByComunaId(comunaId);
    }
}