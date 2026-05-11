package microservice.logistica.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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

import microservice.logistica.model.Inventario;
import microservice.logistica.service.InventarioService;

@RestController
@RequestMapping("/inventario")
public class InventarioController {

    @Autowired
    private InventarioService service;

    @GetMapping
    public ResponseEntity<List<Inventario>> listar() {

        List<Inventario> lista = service.obtenerTodos();

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> obtener(@PathVariable Integer id) {

        Inventario inventario = service.obtenerPorId(id);

        if (inventario == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(inventario);
    }

    @PostMapping
    public ResponseEntity<Inventario> crear(@RequestBody Inventario inventario) {

        Inventario nuevo = service.guardar(inventario);

        return ResponseEntity.status(201).body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizar(
            @PathVariable Integer id,
            @RequestBody Inventario inventario
    ) {

        Inventario actualizado = service.actualizar(id, inventario);

        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Inventario> actualizarParcial(
            @PathVariable Integer id,
            @RequestBody Inventario inventario
    ) {

        Inventario actualizado = service.actualizarParcial(id, inventario);

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

    @GetMapping("/centro/{centroAcopioId}")
    public ResponseEntity<List<Inventario>> porCentro(
            @PathVariable Integer centroAcopioId
    ) {

        List<Inventario> lista = service.porCentro(centroAcopioId);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/tipo-recurso/{tipoRecursoId}")
    public ResponseEntity<List<Inventario>> porTipoRecurso(
            @PathVariable Integer tipoRecursoId
    ) {

        List<Inventario> lista = service.porTipoRecurso(tipoRecursoId);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lista);
    }

        @PatchMapping("/entrada")
    public ResponseEntity<Inventario> entrada(@RequestBody Map<String, Object> body) {
        Integer centroId = (Integer) body.get("centroAcopioId");
        Integer tipoId = (Integer) body.get("tipoRecursoId");
        BigDecimal cantidad = new BigDecimal(body.get("cantidad").toString());
        return ResponseEntity.ok(service.agregarStock(centroId, tipoId, cantidad));
    }

    @PatchMapping("/salida")
    public ResponseEntity<Inventario> salida(@RequestBody Map<String, Object> body) {
        Integer centroId = (Integer) body.get("centroAcopioId");
        Integer tipoId = (Integer) body.get("tipoRecursoId");
        BigDecimal cantidad = new BigDecimal(body.get("cantidad").toString());
        return ResponseEntity.ok(service.restarStock(centroId, tipoId, cantidad));
    }
}