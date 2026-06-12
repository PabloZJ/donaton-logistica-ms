package microservice.logistica.service;

import microservice.logistica.model.Region;
import microservice.logistica.repository.RegionRepository;
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
@DisplayName("RegionService - Tests unitarios")
class RegionServiceTest {

    @Mock
    private RegionRepository repository;

    @InjectMocks
    private RegionService service;

    // ─── Datos de prueba ─────────────────────────────────────────────────────

    private Region regionEjemplo() {
        return new Region(1, "Metropolitana");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerRegiones()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerRegiones()")
    class ObtenerRegiones {

        @Test
        @DisplayName("retorna lista con todas las regiones")
        void deberiaRetornarTodasLasRegiones() {
            List<Region> lista = List.of(
                new Region(1, "Metropolitana"),
                new Region(2, "Valparaíso")
            );
            when(repository.findAll()).thenReturn(lista);

            List<Region> resultado = service.obtenerRegiones();

            assertThat(resultado).hasSize(2);
            assertThat(resultado.get(0).getNombre()).isEqualTo("Metropolitana");
            assertThat(resultado.get(1).getNombre()).isEqualTo("Valparaíso");
            verify(repository, times(1)).findAll();
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay regiones")
        void deberiaRetornarListaVaciaCuandoNoHayRegiones() {
            when(repository.findAll()).thenReturn(Collections.emptyList());

            List<Region> resultado = service.obtenerRegiones();

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
        @DisplayName("retorna la región cuando existe")
        void deberiaRetornarRegionCuandoExiste() {
            when(repository.findById(1)).thenReturn(Optional.of(regionEjemplo()));

            Region resultado = service.obtenerPorId(1);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1);
            assertThat(resultado.getNombre()).isEqualTo("Metropolitana");
        }

        @Test
        @DisplayName("retorna null cuando no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            Region resultado = service.obtenerPorId(99);

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
        @DisplayName("guarda y retorna la región con id asignado")
        void deberiaGuardarYRetornarRegionConId() {
            Region nueva = new Region(null, "Biobío");
            Region guardada = new Region(3, "Biobío");

            when(repository.save(nueva)).thenReturn(guardada);

            Region resultado = service.guardar(nueva);

            assertThat(resultado.getId()).isEqualTo(3);
            assertThat(resultado.getNombre()).isEqualTo("Biobío");
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
        @DisplayName("actualiza el nombre cuando la región existe")
        void deberiaActualizarNombreCuandoExiste() {
            Region existente = regionEjemplo();
            Region actualizada = new Region(1, "Región Metropolitana");

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(Region.class))).thenReturn(actualizada);

            Region resultado = service.actualizar(1, new Region(null, "Región Metropolitana"));

            assertThat(resultado.getNombre()).isEqualTo("Región Metropolitana");
            verify(repository, times(1)).save(existente);
        }

        @Test
        @DisplayName("retorna null cuando la región no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            Region resultado = service.actualizar(99, new Region(null, "Nueva"));

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
        @DisplayName("actualiza el nombre cuando se envía")
        void deberiaActualizarNombreCuandoSeEnvia() {
            Region existente = regionEjemplo();
            Region parcial = new Region(null, "RM");

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(Region.class))).thenReturn(existente);

            Region resultado = service.actualizarParcial(1, parcial);

            assertThat(resultado.getNombre()).isEqualTo("RM");
            verify(repository, times(1)).save(existente);
        }

        @Test
        @DisplayName("no modifica el nombre cuando se envía null")
        void deberiaMantenerNombreCuandoSeEnviaNulo() {
            Region existente = regionEjemplo();
            Region parcial = new Region(null, null);

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(Region.class))).thenReturn(existente);

            Region resultado = service.actualizarParcial(1, parcial);

            assertThat(resultado.getNombre()).isEqualTo("Metropolitana");
        }

        @Test
        @DisplayName("retorna null cuando la región no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            Region resultado = service.actualizarParcial(99, new Region(null, "X"));

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
        @DisplayName("elimina la región cuando existe")
        void deberiaEliminarCuandoExiste() {
            Region existente = regionEjemplo();
            when(repository.findById(1)).thenReturn(Optional.of(existente));

            service.eliminar(1);

            verify(repository, times(1)).delete(existente);
        }

        @Test
        @DisplayName("lanza excepción cuando la región no existe")
        void deberiaLanzarExcepcionCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.eliminar(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Región no encontrada");

            verify(repository, never()).delete(any());
        }
    }
}