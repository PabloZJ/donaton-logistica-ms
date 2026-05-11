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

import microservice.logistica.model.Asignacion;
import microservice.logistica.service.AsignacionService;

@RestController
@RequestMapping("/asignaciones")
public class AsignacionController {

    @Autowired
    private AsignacionService service;

    @GetMapping
    public ResponseEntity<List<Asignacion>> obtenerTodas() {
        List<Asignacion> lista = service.obtenerTodas();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Asignacion> obtenerPorId(@PathVariable Integer id) {
        Asignacion asignacion = service.obtenerPorId(id);
        if (asignacion == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(asignacion);
    }

    @PostMapping
    public ResponseEntity<Asignacion> crear(@RequestBody Asignacion asignacion) {
        return ResponseEntity.status(201).body(service.guardar(asignacion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Asignacion> actualizar(@PathVariable Integer id, @RequestBody Asignacion asignacion) {
        Asignacion actualizada = service.actualizar(id, asignacion);
        if (actualizada == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizada);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Asignacion> actualizarParcial(@PathVariable Integer id, @RequestBody Asignacion asignacion) {
        Asignacion actualizada = service.actualizarParcial(id, asignacion);
        if (actualizada == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/necesidad/{necesidadId}")
    public ResponseEntity<List<Asignacion>> obtenerPorNecesidad(@PathVariable Integer necesidadId) {
        List<Asignacion> lista = service.obtenerPorNecesidad(necesidadId);
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }
}