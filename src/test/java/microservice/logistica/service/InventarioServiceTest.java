package microservice.logistica.service;

import microservice.logistica.model.Inventario;
import microservice.logistica.repository.InventarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("InventarioService - Tests unitarios")
class InventarioServiceTest {

    @Mock
    private InventarioRepository repository;

    @InjectMocks
    private InventarioService service;

    // ─── Datos de prueba ─────────────────────────────────────────────────────

    private Inventario inventarioEjemplo() {
        return new Inventario(1, 10, 5, new BigDecimal("100"), LocalDateTime.of(2026, 1, 1, 10, 0));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerTodos()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerTodos()")
    class ObtenerTodos {

        @Test
        @DisplayName("retorna lista con todos los inventarios")
        void deberiaRetornarTodosLosInventarios() {
            List<Inventario> lista = List.of(inventarioEjemplo(), inventarioEjemplo());
            when(repository.findAll()).thenReturn(lista);

            List<Inventario> resultado = service.obtenerTodos();

            assertThat(resultado).hasSize(2);
            verify(repository, times(1)).findAll();
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay inventarios")
        void deberiaRetornarListaVaciaCuandoNoHayInventarios() {
            when(repository.findAll()).thenReturn(Collections.emptyList());

            List<Inventario> resultado = service.obtenerTodos();

            assertThat(resultado).isEmpty();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerPorId()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerPorId()")
    class ObtenerPorId {

        @Test
        @DisplayName("retorna el inventario cuando existe")
        void deberiaRetornarInventarioCuandoExiste() {
            when(repository.findById(1)).thenReturn(Optional.of(inventarioEjemplo()));

            Inventario resultado = service.obtenerPorId(1);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1);
            assertThat(resultado.getCentroAcopioId()).isEqualTo(10);
            assertThat(resultado.getCantidadDisponible()).isEqualByComparingTo(new BigDecimal("100"));
        }

        @Test
        @DisplayName("retorna null cuando no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            Inventario resultado = service.obtenerPorId(99);

            assertThat(resultado).isNull();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // guardar()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("guardar()")
    class Guardar {

        @Test
        @DisplayName("guarda y retorna el inventario con id asignado")
        void deberiaGuardarYRetornarInventarioConId() {
            Inventario nuevo = new Inventario(null, 10, 5, new BigDecimal("50"), LocalDateTime.now());
            Inventario guardado = inventarioEjemplo();

            when(repository.save(nuevo)).thenReturn(guardado);

            Inventario resultado = service.guardar(nuevo);

            assertThat(resultado.getId()).isEqualTo(1);
            assertThat(resultado.getCantidadDisponible()).isEqualByComparingTo(new BigDecimal("100"));
            verify(repository, times(1)).save(nuevo);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // actualizar()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("actualizar()")
    class Actualizar {

        @Test
        @DisplayName("actualiza todos los campos cuando el inventario existe")
        void deberiaActualizarTodosCamposCuandoExiste() {
            Inventario existente = inventarioEjemplo();
            LocalDateTime nuevaFecha = LocalDateTime.of(2026, 6, 1, 12, 0);
            Inventario nuevaData = new Inventario(null, 20, 8, new BigDecimal("200"), nuevaFecha);

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(Inventario.class))).thenAnswer(inv -> inv.getArgument(0));

            Inventario resultado = service.actualizar(1, nuevaData);

            assertThat(resultado.getCentroAcopioId()).isEqualTo(20);
            assertThat(resultado.getTipoRecursoId()).isEqualTo(8);
            assertThat(resultado.getCantidadDisponible()).isEqualByComparingTo(new BigDecimal("200"));
            assertThat(resultado.getActualizadoEn()).isEqualTo(nuevaFecha);
            verify(repository, times(1)).save(existente);
        }

        @Test
        @DisplayName("retorna null cuando el inventario no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            Inventario resultado = service.actualizar(99, new Inventario());

            assertThat(resultado).isNull();
            verify(repository, never()).save(any());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // actualizarParcial()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("actualizarParcial()")
    class ActualizarParcial {

        @Test
        @DisplayName("actualiza solo los campos no nulos")
        void deberiaActualizarSoloCamposNoNulos() {
            Inventario existente = inventarioEjemplo();
            Inventario parcial = new Inventario();
            parcial.setCantidadDisponible(new BigDecimal("999"));
            // resto null → no deben cambiar

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(Inventario.class))).thenAnswer(inv -> inv.getArgument(0));

            Inventario resultado = service.actualizarParcial(1, parcial);

            assertThat(resultado.getCantidadDisponible()).isEqualByComparingTo(new BigDecimal("999"));
            assertThat(resultado.getCentroAcopioId()).isEqualTo(10);  // sin cambio
            assertThat(resultado.getTipoRecursoId()).isEqualTo(5);    // sin cambio
        }

        @Test
        @DisplayName("no modifica ningún campo si todos vienen null")
        void deberiaNoModificarNadaSiTodoEsNull() {
            Inventario existente = inventarioEjemplo();

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(Inventario.class))).thenAnswer(inv -> inv.getArgument(0));

            Inventario resultado = service.actualizarParcial(1, new Inventario());

            assertThat(resultado.getCantidadDisponible()).isEqualByComparingTo(new BigDecimal("100"));
            assertThat(resultado.getCentroAcopioId()).isEqualTo(10);
            assertThat(resultado.getTipoRecursoId()).isEqualTo(5);
        }

        @Test
        @DisplayName("retorna null cuando el inventario no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            Inventario resultado = service.actualizarParcial(99, new Inventario());

            assertThat(resultado).isNull();
            verify(repository, never()).save(any());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // eliminar()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("eliminar()")
    class Eliminar {

        @Test
        @DisplayName("elimina el inventario cuando existe")
        void deberiaEliminarCuandoExiste() {
            Inventario existente = inventarioEjemplo();
            when(repository.findById(1)).thenReturn(Optional.of(existente));

            service.eliminar(1);

            verify(repository, times(1)).delete(existente);
        }

        @Test
        @DisplayName("lanza excepción cuando el inventario no existe")
        void deberiaLanzarExcepcionCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.eliminar(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Inventario no encontrado");

            verify(repository, never()).delete(any());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // porCentro()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("porCentro()")
    class PorCentro {

        @Test
        @DisplayName("retorna inventarios filtrados por centro de acopio")
        void deberiaRetornarInventariosPorCentro() {
            List<Inventario> lista = List.of(inventarioEjemplo());
            when(repository.findByCentroAcopioId(10)).thenReturn(lista);

            List<Inventario> resultado = service.porCentro(10);

            assertThat(resultado).hasSize(1);
            verify(repository, times(1)).findByCentroAcopioId(10);
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay inventarios en ese centro")
        void deberiaRetornarListaVaciaCuandoNoHayCoincidencias() {
            when(repository.findByCentroAcopioId(99)).thenReturn(Collections.emptyList());

            List<Inventario> resultado = service.porCentro(99);

            assertThat(resultado).isEmpty();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // porTipoRecurso()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("porTipoRecurso()")
    class PorTipoRecurso {

        @Test
        @DisplayName("retorna inventarios filtrados por tipo de recurso")
        void deberiaRetornarInventariosPorTipoRecurso() {
            List<Inventario> lista = List.of(inventarioEjemplo());
            when(repository.findByTipoRecursoId(5)).thenReturn(lista);

            List<Inventario> resultado = service.porTipoRecurso(5);

            assertThat(resultado).hasSize(1);
            verify(repository, times(1)).findByTipoRecursoId(5);
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay inventarios para ese tipo")
        void deberiaRetornarListaVaciaCuandoNoHayCoincidencias() {
            when(repository.findByTipoRecursoId(99)).thenReturn(Collections.emptyList());

            List<Inventario> resultado = service.porTipoRecurso(99);

            assertThat(resultado).isEmpty();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // agregarStock()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("agregarStock()")
    class AgregarStock {

        @Test
        @DisplayName("suma cantidad al stock existente")
        void deberiaSumarCantidadAlStockExistente() {
            Inventario existente = inventarioEjemplo(); // stock: 100

            when(repository.findByCentroAcopioIdAndTipoRecursoId(10, 5))
                .thenReturn(Optional.of(existente));
            when(repository.save(any(Inventario.class))).thenAnswer(inv -> inv.getArgument(0));

            Inventario resultado = service.agregarStock(10, 5, new BigDecimal("30"));

            assertThat(resultado.getCantidadDisponible()).isEqualByComparingTo(new BigDecimal("130"));
            verify(repository, times(1)).save(existente);
        }

        @Test
        @DisplayName("crea nuevo inventario con la cantidad cuando no existe")
        void deberiaCrearNuevoInventarioCuandoNoExiste() {
            when(repository.findByCentroAcopioIdAndTipoRecursoId(10, 5))
                .thenReturn(Optional.empty());
            when(repository.save(any(Inventario.class))).thenAnswer(inv -> inv.getArgument(0));

            Inventario resultado = service.agregarStock(10, 5, new BigDecimal("50"));

            assertThat(resultado.getCantidadDisponible()).isEqualByComparingTo(new BigDecimal("50"));
            assertThat(resultado.getCentroAcopioId()).isEqualTo(10);
            assertThat(resultado.getTipoRecursoId()).isEqualTo(5);
            verify(repository, times(1)).save(any(Inventario.class));
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // restarStock()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("restarStock()")
    class RestarStock {

        @Test
        @DisplayName("resta la cantidad cuando hay stock suficiente")
        void deberiaRestarCantidadCuandoHayStockSuficiente() {
            Inventario existente = inventarioEjemplo(); // stock: 100

            when(repository.findByCentroAcopioIdAndTipoRecursoId(10, 5))
                .thenReturn(Optional.of(existente));
            when(repository.save(any(Inventario.class))).thenAnswer(inv -> inv.getArgument(0));

            Inventario resultado = service.restarStock(10, 5, new BigDecimal("40"));

            assertThat(resultado.getCantidadDisponible()).isEqualByComparingTo(new BigDecimal("60"));
            verify(repository, times(1)).save(existente);
        }

        @Test
        @DisplayName("lanza excepción cuando no hay inventario para ese recurso")
        void deberiaLanzarExcepcionCuandoNoHayInventario() {
            when(repository.findByCentroAcopioIdAndTipoRecursoId(10, 5))
                .thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.restarStock(10, 5, new BigDecimal("10")))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No hay inventario para ese recurso");

            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("lanza excepción cuando el stock es insuficiente")
        void deberiaLanzarExcepcionCuandoStockEsInsuficiente() {
            Inventario existente = inventarioEjemplo(); // stock: 100

            when(repository.findByCentroAcopioIdAndTipoRecursoId(10, 5))
                .thenReturn(Optional.of(existente));

            assertThatThrownBy(() -> service.restarStock(10, 5, new BigDecimal("150")))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Stock insuficiente");

            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("permite restar exactamente todo el stock disponible")
        void deberiaPermitirRestarTodoElStock() {
            Inventario existente = inventarioEjemplo(); // stock: 100

            when(repository.findByCentroAcopioIdAndTipoRecursoId(10, 5))
                .thenReturn(Optional.of(existente));
            when(repository.save(any(Inventario.class))).thenAnswer(inv -> inv.getArgument(0));

            Inventario resultado = service.restarStock(10, 5, new BigDecimal("100"));

            assertThat(resultado.getCantidadDisponible()).isEqualByComparingTo(BigDecimal.ZERO);
        }
    }
}