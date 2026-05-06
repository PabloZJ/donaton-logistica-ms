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

import microservice.logistica.model.Region;
import microservice.logistica.service.RegionService;

@RestController
@RequestMapping("/regiones")
public class RegionController {

    @Autowired
    private RegionService service;

    @GetMapping
    public ResponseEntity<List<Region>> listar() {
        List<Region> lista = service.obtenerRegiones();

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Region> obtener(@PathVariable Integer id) {
        Region region = service.obtenerPorId(id);

        if (region == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(region);
    }

    @PostMapping
    public ResponseEntity<Region> crear(@RequestBody Region region) {
        Region nueva = service.guardar(region);
        return ResponseEntity.status(201).body(nueva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Region> actualizar(@PathVariable Integer id, @RequestBody Region region) {

        Region actualizada = service.actualizar(id, region);

        if (actualizada == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(actualizada);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Region> actualizarParcial(@PathVariable Integer id, @RequestBody Region region) {

        Region actualizada = service.actualizarParcial(id, region);

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
}