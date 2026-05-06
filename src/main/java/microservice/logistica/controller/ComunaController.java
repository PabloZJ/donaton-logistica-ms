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
import microservice.logistica.model.Comuna;
import microservice.logistica.service.ComunaService;

@RestController
@RequestMapping("/comunas")
public class ComunaController {

    @Autowired
    private ComunaService service;

    @GetMapping
    public ResponseEntity<List<Comuna>> listar() {
        List<Comuna> lista = service.obtenerComunas();

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comuna> obtener(@PathVariable Integer id) {
        Comuna comuna = service.obtenerPorId(id);

        if (comuna == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(comuna);
    }

    @PostMapping
    public ResponseEntity<Comuna> crear(@RequestBody Comuna comuna) {
        Comuna nueva = service.guardar(comuna);
        return ResponseEntity.status(201).body(nueva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comuna> actualizar(@PathVariable Integer id, @RequestBody Comuna comuna) {

        Comuna actualizada = service.actualizar(id, comuna);

        if (actualizada == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(actualizada);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Comuna> actualizarParcial(@PathVariable Integer id, @RequestBody Comuna comuna) {

        Comuna actualizada = service.actualizarParcial(id, comuna);

        if (actualizada == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/region/{regionId}")
    public ResponseEntity<List<Comuna>> porRegion(@PathVariable Integer regionId) {
        List<Comuna> lista = service.obtenerPorRegion(regionId);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }
}