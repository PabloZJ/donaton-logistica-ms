package microservice.logistica.service;

import microservice.logistica.model.CentroAcopio;
import microservice.logistica.repository.CentroAcopioRepository;
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
@DisplayName("CentroAcopioService - Tests unitarios")
class CentroAcopioServiceTest {

    @Mock
    private CentroAcopioRepository repository;

    @InjectMocks
    private CentroAcopioService service;

    // ─── Datos de prueba ─────────────────────────────────────────────────────

    private CentroAcopio centroEjemplo() {
        CentroAcopio c = new CentroAcopio();
        c.setId(1);
        c.setNombre("Centro Norte");
        c.setDireccion("Av. Principal 100");
        c.setRegion(new microservice.logistica.model.Region(3, "Metropolitana"));
        c.setActivo(true);
        return c;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerCentros()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerCentros()")
    class ObtenerCentros {

        @Test
        @DisplayName("retorna lista con todos los centros")
        void deberiaRetornarTodosLosCentros() {
            List<CentroAcopio> lista = List.of(centroEjemplo(), centroEjemplo());
            when(repository.findAll()).thenReturn(lista);

            List<CentroAcopio> resultado = service.obtenerCentros();

            assertThat(resultado).hasSize(2);
            verify(repository, times(1)).findAll();
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay centros")
        void deberiaRetornarListaVaciaCuandoNoHayCentros() {
            when(repository.findAll()).thenReturn(Collections.emptyList());

            List<CentroAcopio> resultado = service.obtenerCentros();

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
        @DisplayName("retorna el centro cuando existe")
        void deberiaRetornarCentroCuandoExiste() {
            when(repository.findById(1)).thenReturn(Optional.of(centroEjemplo()));

            CentroAcopio resultado = service.obtenerPorId(1);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1);
            assertThat(resultado.getNombre()).isEqualTo("Centro Norte");
            assertThat(resultado.getDireccion()).isEqualTo("Av. Principal 100");
        }

        @Test
        @DisplayName("retorna null cuando no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            CentroAcopio resultado = service.obtenerPorId(99);

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
        @DisplayName("guarda y retorna el centro con id asignado")
        void deberiaGuardarYRetornarCentroConId() {
            CentroAcopio nuevo = new CentroAcopio();
            nuevo.setNombre("Centro Sur");
            nuevo.setDireccion("Calle Nueva 200");
            nuevo.setActivo(true);

            CentroAcopio guardado = centroEjemplo();
            guardado.setId(2);
            guardado.setNombre("Centro Sur");

            when(repository.save(nuevo)).thenReturn(guardado);

            CentroAcopio resultado = service.guardar(nuevo);

            assertThat(resultado.getId()).isEqualTo(2);
            assertThat(resultado.getNombre()).isEqualTo("Centro Sur");
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
        @DisplayName("actualiza todos los campos cuando el centro existe")
        void deberiaActualizarTodosCamposCuandoExiste() {
            CentroAcopio existente = centroEjemplo();

            CentroAcopio nuevaData = new CentroAcopio();
            nuevaData.setNombre("Centro Actualizado");
            nuevaData.setDireccion("Nueva Dirección 999");
            nuevaData.setRegion(new microservice.logistica.model.Region(5, "Valparaíso"));
            nuevaData.setActivo(false);

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(CentroAcopio.class))).thenAnswer(inv -> inv.getArgument(0));

            CentroAcopio resultado = service.actualizar(1, nuevaData);

            assertThat(resultado.getNombre()).isEqualTo("Centro Actualizado");
            assertThat(resultado.getDireccion()).isEqualTo("Nueva Dirección 999");
            assertThat(resultado.getActivo()).isFalse();
            verify(repository, times(1)).save(existente);
        }

        @Test
        @DisplayName("retorna null cuando el centro no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            CentroAcopio resultado = service.actualizar(99, new CentroAcopio());

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
            CentroAcopio existente = centroEjemplo();

            CentroAcopio parcial = new CentroAcopio();
            parcial.setNombre("Nuevo Nombre");
            // resto null → no deben cambiar

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(CentroAcopio.class))).thenAnswer(inv -> inv.getArgument(0));

            CentroAcopio resultado = service.actualizarParcial(1, parcial);

            assertThat(resultado.getNombre()).isEqualTo("Nuevo Nombre");
            assertThat(resultado.getDireccion()).isEqualTo("Av. Principal 100"); // sin cambio
            assertThat(resultado.getActivo()).isTrue();                          // sin cambio
        }

        @Test
        @DisplayName("no modifica ningún campo si todos vienen null")
        void deberiaNoModificarNadaSiTodoEsNull() {
            CentroAcopio existente = centroEjemplo();

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(CentroAcopio.class))).thenAnswer(inv -> inv.getArgument(0));

            CentroAcopio resultado = service.actualizarParcial(1, new CentroAcopio());

            assertThat(resultado.getNombre()).isEqualTo("Centro Norte");
            assertThat(resultado.getDireccion()).isEqualTo("Av. Principal 100");
            assertThat(resultado.getActivo()).isTrue();
        }

        @Test
        @DisplayName("retorna null cuando el centro no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            CentroAcopio resultado = service.actualizarParcial(99, new CentroAcopio());

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
        @DisplayName("elimina el centro cuando existe")
        void deberiaEliminarCuandoExiste() {
            CentroAcopio existente = centroEjemplo();
            when(repository.findById(1)).thenReturn(Optional.of(existente));

            service.eliminar(1);

            verify(repository, times(1)).delete(existente);
        }

        @Test
        @DisplayName("lanza excepción cuando el centro no existe")
        void deberiaLanzarExcepcionCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.eliminar(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Centro de acopio no encontrado");

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
        @DisplayName("retorna centros filtrados por region")
        void deberiaRetornarCentrosPorRegion() {
            List<CentroAcopio> lista = List.of(centroEjemplo());
            when(repository.findByRegionId(3)).thenReturn(lista);

            List<CentroAcopio> resultado = service.obtenerPorRegion(3);

            assertThat(resultado).hasSize(1);
            verify(repository, times(1)).findByRegionId(3);
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay centros en esa region")
        void deberiaRetornarListaVaciaCuandoNoHayCoincidencias() {
            when(repository.findByRegionId(99)).thenReturn(Collections.emptyList());

            List<CentroAcopio> resultado = service.obtenerPorRegion(99);

            assertThat(resultado).isEmpty();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerActivos()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerActivos()")
    class ObtenerActivos {

        @Test
        @DisplayName("retorna solo los centros activos")
        void deberiaRetornarSoloCentrosActivos() {
            List<CentroAcopio> lista = List.of(centroEjemplo());
            when(repository.findByActivo(true)).thenReturn(lista);

            List<CentroAcopio> resultado = service.obtenerActivos();

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getActivo()).isTrue();
            verify(repository, times(1)).findByActivo(true);
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay centros activos")
        void deberiaRetornarListaVaciaCuandoNoHayActivos() {
            when(repository.findByActivo(true)).thenReturn(Collections.emptyList());

            List<CentroAcopio> resultado = service.obtenerActivos();

            assertThat(resultado).isEmpty();
        }
    }
}