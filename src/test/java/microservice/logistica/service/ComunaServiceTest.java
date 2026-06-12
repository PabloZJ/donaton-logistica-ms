package microservice.logistica.service;

import microservice.logistica.model.Comuna;
import microservice.logistica.model.Region;
import microservice.logistica.repository.ComunaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ComunaService - Tests unitarios")
class ComunaServiceTest {

    @Mock
    private ComunaRepository repository;

    @InjectMocks
    private ComunaService service;

    // ─── Datos de prueba ─────────────────────────────────────────────────────

    private Region regionEjemplo() {
        Region r = new Region();
        r.setId(3);
        r.setNombre("Metropolitana");
        return r;
    }

    private Comuna comunaEjemplo() {
        Comuna c = new Comuna();
        c.setId(1);
        c.setNombre("Santiago");
        c.setRegion(regionEjemplo());
        return c;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerComunas()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerComunas()")
    class ObtenerComunas {

        @Test
        @DisplayName("retorna lista con todas las comunas")
        void deberiaRetornarTodasLasComunas() {
            List<Comuna> lista = List.of(comunaEjemplo(), comunaEjemplo());
            when(repository.findAll()).thenReturn(lista);

            List<Comuna> resultado = service.obtenerComunas();

            assertThat(resultado).hasSize(2);
            verify(repository, times(1)).findAll();
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay comunas")
        void deberiaRetornarListaVaciaCuandoNoHayComunas() {
            when(repository.findAll()).thenReturn(Collections.emptyList());

            List<Comuna> resultado = service.obtenerComunas();

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
        @DisplayName("retorna la comuna cuando existe")
        void deberiaRetornarComunaCuandoExiste() {
            when(repository.findById(1)).thenReturn(Optional.of(comunaEjemplo()));

            Comuna resultado = service.obtenerPorId(1);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1);
            assertThat(resultado.getNombre()).isEqualTo("Santiago");
            assertThat(resultado.getRegion().getNombre()).isEqualTo("Metropolitana");
        }

        @Test
        @DisplayName("retorna null cuando no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            Comuna resultado = service.obtenerPorId(99);

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
        @DisplayName("guarda y retorna la comuna con id asignado")
        void deberiaGuardarYRetornarComunaConId() {
            Comuna nueva = new Comuna();
            nueva.setNombre("Providencia");
            nueva.setRegion(regionEjemplo());

            Comuna guardada = new Comuna();
            guardada.setId(2);
            guardada.setNombre("Providencia");
            guardada.setRegion(regionEjemplo());

            when(repository.save(nueva)).thenReturn(guardada);

            Comuna resultado = service.guardar(nueva);

            assertThat(resultado.getId()).isEqualTo(2);
            assertThat(resultado.getNombre()).isEqualTo("Providencia");
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
        @DisplayName("actualiza todos los campos cuando la comuna existe")
        void deberiaActualizarTodosCamposCuandoExiste() {
            Comuna existente = comunaEjemplo();

            Region nuevaRegion = new Region();
            nuevaRegion.setId(5);
            nuevaRegion.setNombre("Valparaíso");

            Comuna nuevaData = new Comuna();
            nuevaData.setNombre("Viña del Mar");
            nuevaData.setRegion(nuevaRegion);

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(Comuna.class))).thenAnswer(inv -> inv.getArgument(0));

            Comuna resultado = service.actualizar(1, nuevaData);

            assertThat(resultado.getNombre()).isEqualTo("Viña del Mar");
            assertThat(resultado.getRegion().getNombre()).isEqualTo("Valparaíso");
            verify(repository, times(1)).save(existente);
        }

        @Test
        @DisplayName("retorna null cuando la comuna no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            Comuna resultado = service.actualizar(99, new Comuna());

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
            Comuna existente = comunaEjemplo();

            Comuna parcial = new Comuna();
            parcial.setNombre("Ñuñoa");
            // region null → no debe cambiar

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(Comuna.class))).thenAnswer(inv -> inv.getArgument(0));

            Comuna resultado = service.actualizarParcial(1, parcial);

            assertThat(resultado.getNombre()).isEqualTo("Ñuñoa");
            assertThat(resultado.getRegion().getNombre()).isEqualTo("Metropolitana"); // sin cambio
        }

        @Test
        @DisplayName("no modifica ningún campo si todos vienen null")
        void deberiaNoModificarNadaSiTodoEsNull() {
            Comuna existente = comunaEjemplo();

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(Comuna.class))).thenAnswer(inv -> inv.getArgument(0));

            Comuna resultado = service.actualizarParcial(1, new Comuna());

            assertThat(resultado.getNombre()).isEqualTo("Santiago");
            assertThat(resultado.getRegion().getNombre()).isEqualTo("Metropolitana");
        }

        @Test
        @DisplayName("retorna null cuando la comuna no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            Comuna resultado = service.actualizarParcial(99, new Comuna());

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
        @DisplayName("elimina la comuna cuando existe")
        void deberiaEliminarCuandoExiste() {
            Comuna existente = comunaEjemplo();
            when(repository.findById(1)).thenReturn(Optional.of(existente));

            service.eliminar(1);

            verify(repository, times(1)).delete(existente);
        }

        @Test
        @DisplayName("lanza excepción cuando la comuna no existe")
        void deberiaLanzarExcepcionCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.eliminar(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Comuna no encontrada");

            verify(repository, never()).delete(any());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerPorRegion()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerPorRegion()")
    class ObtenerPorRegion {

        @Test
        @DisplayName("retorna comunas filtradas por region")
        void deberiaRetornarComunasPorRegion() {
            List<Comuna> lista = List.of(comunaEjemplo());
            when(repository.findByRegionId(3)).thenReturn(lista);

            List<Comuna> resultado = service.obtenerPorRegion(3);

            assertThat(resultado).hasSize(1);
            verify(repository, times(1)).findByRegionId(3);
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay comunas en esa region")
        void deberiaRetornarListaVaciaCuandoNoHayCoincidencias() {
            when(repository.findByRegionId(99)).thenReturn(Collections.emptyList());

            List<Comuna> resultado = service.obtenerPorRegion(99);

            assertThat(resultado).isEmpty();
        }
    }
}