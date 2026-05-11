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
import microservice.logistica.model.CentroAcopio;
import microservice.logistica.service.CentroAcopioService;

@RestController
@RequestMapping("/centros-acopio")
public class CentroAcopioController {

    @Autowired
    private CentroAcopioService service;

    @GetMapping
    public ResponseEntity<List<CentroAcopio>> listar() {
        List<CentroAcopio> lista = service.obtenerCentros();

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CentroAcopio> obtener(@PathVariable Integer id) {
        CentroAcopio centro = service.obtenerPorId(id);

        if (centro == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(centro);
    }

    @PostMapping
    public ResponseEntity<CentroAcopio> crear(@RequestBody CentroAcopio centro) {
        CentroAcopio nuevo = service.guardar(centro);
        return ResponseEntity.status(201).body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CentroAcopio> actualizar(@PathVariable Integer id, @RequestBody CentroAcopio centro) {

        CentroAcopio actualizado = service.actualizar(id, centro);

        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CentroAcopio> actualizarParcial(@PathVariable Integer id, @RequestBody CentroAcopio centro) {

        CentroAcopio actualizado = service.actualizarParcial(id, centro);

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

    @GetMapping("/region/{regionId}")
    public ResponseEntity<List<CentroAcopio>> porRegion(@PathVariable Integer regionId) {
        List<CentroAcopio> lista = service.obtenerPorRegion(regionId);
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<CentroAcopio>> activos() {
        List<CentroAcopio> lista = service.obtenerActivos();

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }
}