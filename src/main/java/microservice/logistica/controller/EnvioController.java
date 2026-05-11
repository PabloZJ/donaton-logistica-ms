package microservice.logistica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import microservice.logistica.model.Envio;
import microservice.logistica.service.EnvioService;

@RestController
@RequestMapping("/envios")
public class EnvioController {

    @Autowired
    private EnvioService service;

    @GetMapping
    public ResponseEntity<List<Envio>> listar() {
        List<Envio> lista = service.obtenerTodos();

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Envio> obtener(@PathVariable Integer id) {
        Envio envio = service.obtenerPorId(id);

        if (envio == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(envio);
    }

    @PostMapping
    public ResponseEntity<Envio> crear(@RequestBody Envio envio) {
        Envio nuevo = service.guardar(envio);
        return ResponseEntity.status(201).body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Envio> actualizar(@PathVariable Integer id, @RequestBody Envio envio) {

        Envio actualizado = service.actualizar(id, envio);

        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Envio> actualizarParcial(@PathVariable Integer id, @RequestBody Envio envio) {

        Envio actualizado = service.actualizarParcial(id, envio);

        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // filtros

    @GetMapping("/estado/{estadoId}")
    public ResponseEntity<List<Envio>> porEstado(@PathVariable Integer estadoId) {
        List<Envio> lista = service.porEstado(estadoId);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/centro/{centroId}")
    public ResponseEntity<List<Envio>> porCentro(@PathVariable Integer centroId) {
        List<Envio> lista = service.porCentro(centroId);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/comuna/{comunaId}")
    public ResponseEntity<List<Envio>> porComuna(@PathVariable Integer comunaId) {
        List<Envio> lista = service.porComuna(comunaId);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }
}