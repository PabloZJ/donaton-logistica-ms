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

import microservice.logistica.model.DetalleEnvio;
import microservice.logistica.service.DetalleEnvioService;

@RestController
@RequestMapping("/detalle-envios")
public class DetalleEnvioController {

    @Autowired
    private DetalleEnvioService service;

    @GetMapping
    public ResponseEntity<List<DetalleEnvio>> listar() {
        List<DetalleEnvio> lista = service.obtenerTodos();

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleEnvio> obtener(@PathVariable Integer id) {
        DetalleEnvio detalle = service.obtenerPorId(id);

        if (detalle == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(detalle);
    }

    @PostMapping
    public ResponseEntity<DetalleEnvio> crear(@RequestBody DetalleEnvio detalle) {
        DetalleEnvio nuevo = service.guardar(detalle);
        return ResponseEntity.status(201).body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleEnvio> actualizar(@PathVariable Integer id, @RequestBody DetalleEnvio detalle) {

        DetalleEnvio actualizado = service.actualizar(id, detalle);

        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DetalleEnvio> actualizarParcial(@PathVariable Integer id, @RequestBody DetalleEnvio detalle) {

        DetalleEnvio actualizado = service.actualizarParcial(id, detalle);

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

    @GetMapping("/envio/{envioId}")
    public ResponseEntity<List<DetalleEnvio>> porEnvio(@PathVariable Integer envioId) {
        List<DetalleEnvio> lista = service.porEnvio(envioId);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/tipo-recurso/{tipoId}")
    public ResponseEntity<List<DetalleEnvio>> porTipo(@PathVariable Integer tipoId) {
        List<DetalleEnvio> lista = service.porTipoRecurso(tipoId);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }
}