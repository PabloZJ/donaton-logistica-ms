package microservice.logistica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

import microservice.logistica.model.EstadoEnvio;
import microservice.logistica.service.EstadoEnvioService;

@RestController
@RequestMapping("/estados-envio")
public class EstadoEnvioController {

    @Autowired
    private EstadoEnvioService service;

    @GetMapping
    public ResponseEntity<List<EstadoEnvio>> listar() {
        List<EstadoEnvio> lista = service.obtenerTodos();

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoEnvio> obtener(@PathVariable Integer id) {
        EstadoEnvio estado = service.obtenerPorId(id);

        if (estado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(estado);
    }

    @PostMapping
    public ResponseEntity<EstadoEnvio> crear(@RequestBody EstadoEnvio estado) {
        EstadoEnvio nuevo = service.guardar(estado);
        return ResponseEntity.status(201).body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoEnvio> actualizar(@PathVariable Integer id, @RequestBody EstadoEnvio estado) {

        EstadoEnvio actualizado = service.actualizar(id, estado);

        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EstadoEnvio> actualizarParcial(@PathVariable Integer id, @RequestBody EstadoEnvio estado) {

        EstadoEnvio actualizado = service.actualizarParcial(id, estado);

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
}