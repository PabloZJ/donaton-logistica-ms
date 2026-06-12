package microservice.logistica.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import microservice.logistica.model.Asignacion;
import microservice.logistica.repository.AsignacionRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("AsignacionService - Tests unitarios")
class AsignacionServiceTest {

    @Mock
    private AsignacionRepository repository;

    @InjectMocks
    private AsignacionService service;

    // ─── Datos de prueba ─────────────────────────────────────────────────────

    private Asignacion asignacionEjemplo() {
        Asignacion a = new Asignacion();
        a.setId(1);
        a.setNecesidadId(10);
        a.setCantidadAsignada(new BigDecimal("5"));
        a.setFechaAsignacion(LocalDateTime.of(2026, 1, 15, 10, 0));
        a.setResponsableUid("uid-resp-123");
        return a;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerTodas()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerTodas()")
    class ObtenerTodas {

        @Test
        @DisplayName("retorna lista con todas las asignaciones")
        void deberiaRetornarTodasLasAsignaciones() {
            List<Asignacion> lista = List.of(asignacionEjemplo(), asignacionEjemplo());
            when(repository.findAll()).thenReturn(lista);

            List<Asignacion> resultado = service.obtenerTodas();

            assertThat(resultado).hasSize(2);
            verify(repository, times(1)).findAll();
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay asignaciones")
        void deberiaRetornarListaVaciaCuandoNoHayAsignaciones() {
            when(repository.findAll()).thenReturn(Collections.emptyList());

            List<Asignacion> resultado = service.obtenerTodas();

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
        @DisplayName("retorna la asignacion cuando existe")
        void deberiaRetornarAsignacionCuandoExiste() {
            when(repository.findById(1)).thenReturn(Optional.of(asignacionEjemplo()));

            Asignacion resultado = service.obtenerPorId(1);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1);
            assertThat(resultado.getNecesidadId()).isEqualTo(10);
            assertThat(resultado.getResponsableUid()).isEqualTo("uid-resp-123");
        }

        @Test
        @DisplayName("retorna null cuando no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            Asignacion resultado = service.obtenerPorId(99);

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
        @DisplayName("guarda y retorna la asignacion con id asignado")
        void deberiaGuardarYRetornarAsignacionConId() {
            Asignacion nueva = new Asignacion();
            nueva.setNecesidadId(10);
            nueva.setCantidadAsignada(new BigDecimal("3"));
            nueva.setResponsableUid("uid-resp-456");

            Asignacion guardada = asignacionEjemplo();
            guardada.setId(2);

            when(repository.save(nueva)).thenReturn(guardada);

            Asignacion resultado = service.guardar(nueva);

            assertThat(resultado.getId()).isEqualTo(2);
            assertThat(resultado.getNecesidadId()).isEqualTo(10);
            verify(repository, times(1)).save(nueva);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // actualizar()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("actualizar()")
    class Actualizar {

        @Test
        @DisplayName("actualiza todos los campos cuando la asignacion existe")
        void deberiaActualizarTodosCamposCuandoExiste() {
            Asignacion existente = asignacionEjemplo();

            Asignacion nuevaData = new Asignacion();
            nuevaData.setNecesidadId(20);
            nuevaData.setCantidadAsignada(new BigDecimal("15"));
            nuevaData.setFechaAsignacion(LocalDateTime.of(2026, 6, 1, 9, 0));
            nuevaData.setResponsableUid("uid-nuevo-resp");

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(Asignacion.class))).thenAnswer(inv -> inv.getArgument(0));

            Asignacion resultado = service.actualizar(1, nuevaData);

            assertThat(resultado.getNecesidadId()).isEqualTo(20);
            assertThat(resultado.getCantidadAsignada()).isEqualByComparingTo(new BigDecimal("15"));
            assertThat(resultado.getResponsableUid()).isEqualTo("uid-nuevo-resp");
            assertThat(resultado.getFechaAsignacion()).isEqualTo(LocalDateTime.of(2026, 6, 1, 9, 0));
            verify(repository, times(1)).save(existente);
        }

        @Test
        @DisplayName("retorna null cuando la asignacion no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            Asignacion resultado = service.actualizar(99, new Asignacion());

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
            Asignacion existente = asignacionEjemplo();

            Asignacion parcial = new Asignacion();
            parcial.setCantidadAsignada(new BigDecimal("99"));
            // resto null → no deben cambiar

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(Asignacion.class))).thenAnswer(inv -> inv.getArgument(0));

            Asignacion resultado = service.actualizarParcial(1, parcial);

            assertThat(resultado.getCantidadAsignada()).isEqualByComparingTo(new BigDecimal("99"));
            assertThat(resultado.getNecesidadId()).isEqualTo(10);                              // sin cambio
            assertThat(resultado.getResponsableUid()).isEqualTo("uid-resp-123");               // sin cambio
            assertThat(resultado.getFechaAsignacion()).isEqualTo(LocalDateTime.of(2026, 1, 15, 10, 0)); // sin cambio
        }

        @Test
        @DisplayName("no modifica ningún campo si todos vienen null")
        void deberiaNoModificarNadaSiTodoEsNull() {
            Asignacion existente = asignacionEjemplo();

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(Asignacion.class))).thenAnswer(inv -> inv.getArgument(0));

            Asignacion resultado = service.actualizarParcial(1, new Asignacion());

            assertThat(resultado.getNecesidadId()).isEqualTo(10);
            assertThat(resultado.getResponsableUid()).isEqualTo("uid-resp-123");
            assertThat(resultado.getCantidadAsignada()).isEqualByComparingTo(new BigDecimal("5"));
        }

        @Test
        @DisplayName("retorna null cuando la asignacion no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            Asignacion resultado = service.actualizarParcial(99, new Asignacion());

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
        @DisplayName("llama a deleteById con el id correcto")
        void deberiaLlamarDeleteByIdConElIdCorrecto() {
            doNothing().when(repository).deleteById(1);

            service.eliminar(1);

            verify(repository, times(1)).deleteById(1);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerPorNecesidad()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerPorNecesidad()")
    class ObtenerPorNecesidad {

        @Test
        @DisplayName("retorna asignaciones filtradas por necesidad")
        void deberiaRetornarAsignacionesPorNecesidad() {
            List<Asignacion> lista = List.of(asignacionEjemplo());
            when(repository.findByNecesidadId(10)).thenReturn(lista);

            List<Asignacion> resultado = service.obtenerPorNecesidad(10);

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getNecesidadId()).isEqualTo(10);
            verify(repository, times(1)).findByNecesidadId(10);
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay asignaciones para esa necesidad")
        void deberiaRetornarListaVaciaCuandoNoHayCoincidencias() {
            when(repository.findByNecesidadId(99)).thenReturn(Collections.emptyList());

            List<Asignacion> resultado = service.obtenerPorNecesidad(99);

            assertThat(resultado).isEmpty();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerPorResponsable()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerPorResponsable()")
    class ObtenerPorResponsable {

        @Test
        @DisplayName("retorna asignaciones filtradas por responsable")
        void deberiaRetornarAsignacionesPorResponsable() {
            List<Asignacion> lista = List.of(asignacionEjemplo());
            when(repository.findByResponsableUid("uid-resp-123")).thenReturn(lista);

            List<Asignacion> resultado = service.obtenerPorResponsable("uid-resp-123");

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getResponsableUid()).isEqualTo("uid-resp-123");
            verify(repository, times(1)).findByResponsableUid("uid-resp-123");
        }

        @Test
        @DisplayName("retorna lista vacía cuando el responsable no tiene asignaciones")
        void deberiaRetornarListaVaciaCuandoNoHayCoincidencias() {
            when(repository.findByResponsableUid("uid-inexistente")).thenReturn(Collections.emptyList());

            List<Asignacion> resultado = service.obtenerPorResponsable("uid-inexistente");

            assertThat(resultado).isEmpty();
        }
    }
}