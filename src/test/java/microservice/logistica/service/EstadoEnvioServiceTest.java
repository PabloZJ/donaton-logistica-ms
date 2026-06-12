package microservice.logistica.service;

import microservice.logistica.model.EstadoEnvio;
import microservice.logistica.repository.EstadoEnvioRepository;
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
@DisplayName("EstadoEnvioService - Tests unitarios")
class EstadoEnvioServiceTest {

    @Mock
    private EstadoEnvioRepository repository;

    @InjectMocks
    private EstadoEnvioService service;

    // ─── Datos de prueba ─────────────────────────────────────────────────────

    private EstadoEnvio estadoEjemplo() {
        EstadoEnvio e = new EstadoEnvio();
        e.setId(1);
        e.setNombre("Pendiente");
        return e;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerTodos()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerTodos()")
    class ObtenerTodos {

        @Test
        @DisplayName("retorna lista con todos los estados")
        void deberiaRetornarTodosLosEstados() {
            List<EstadoEnvio> lista = List.of(
                estadoEjemplo(),
                new EstadoEnvio(2, "En tránsito")
            );
            when(repository.findAll()).thenReturn(lista);

            List<EstadoEnvio> resultado = service.obtenerTodos();

            assertThat(resultado).hasSize(2);
            assertThat(resultado.get(0).getNombre()).isEqualTo("Pendiente");
            assertThat(resultado.get(1).getNombre()).isEqualTo("En tránsito");
            verify(repository, times(1)).findAll();
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay estados")
        void deberiaRetornarListaVaciaCuandoNoHayEstados() {
            when(repository.findAll()).thenReturn(Collections.emptyList());

            List<EstadoEnvio> resultado = service.obtenerTodos();

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
        @DisplayName("retorna el estado cuando existe")
        void deberiaRetornarEstadoCuandoExiste() {
            when(repository.findById(1)).thenReturn(Optional.of(estadoEjemplo()));

            EstadoEnvio resultado = service.obtenerPorId(1);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1);
            assertThat(resultado.getNombre()).isEqualTo("Pendiente");
        }

        @Test
        @DisplayName("retorna null cuando no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            EstadoEnvio resultado = service.obtenerPorId(99);

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
        @DisplayName("guarda y retorna el estado con id asignado")
        void deberiaGuardarYRetornarEstadoConId() {
            EstadoEnvio nuevo = new EstadoEnvio(null, "Entregado");
            EstadoEnvio guardado = new EstadoEnvio(3, "Entregado");

            when(repository.save(nuevo)).thenReturn(guardado);

            EstadoEnvio resultado = service.guardar(nuevo);

            assertThat(resultado.getId()).isEqualTo(3);
            assertThat(resultado.getNombre()).isEqualTo("Entregado");
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
        @DisplayName("actualiza el nombre cuando el estado existe")
        void deberiaActualizarNombreCuandoExiste() {
            EstadoEnvio existente = estadoEjemplo();
            EstadoEnvio actualizado = new EstadoEnvio(1, "Cancelado");

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(EstadoEnvio.class))).thenReturn(actualizado);

            EstadoEnvio resultado = service.actualizar(1, new EstadoEnvio(null, "Cancelado"));

            assertThat(resultado.getNombre()).isEqualTo("Cancelado");
            verify(repository, times(1)).save(existente);
        }

        @Test
        @DisplayName("retorna null cuando el estado no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            EstadoEnvio resultado = service.actualizar(99, new EstadoEnvio(null, "X"));

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
            EstadoEnvio existente = estadoEjemplo();
            EstadoEnvio esperado = new EstadoEnvio(1, "En tránsito");

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(EstadoEnvio.class))).thenReturn(esperado);

            EstadoEnvio resultado = service.actualizarParcial(1, new EstadoEnvio(null, "En tránsito"));

            assertThat(resultado.getNombre()).isEqualTo("En tránsito");
            verify(repository, times(1)).save(existente);
        }

        @Test
        @DisplayName("no modifica el nombre cuando se envía null")
        void deberiaManternerNombreCuandoSeEnviaNulo() {
            EstadoEnvio existente = estadoEjemplo();

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(EstadoEnvio.class))).thenReturn(existente);

            EstadoEnvio resultado = service.actualizarParcial(1, new EstadoEnvio(null, null));

            assertThat(resultado.getNombre()).isEqualTo("Pendiente");
        }

        @Test
        @DisplayName("retorna null cuando el estado no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            EstadoEnvio resultado = service.actualizarParcial(99, new EstadoEnvio(null, "X"));

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
        @DisplayName("elimina el estado cuando existe")
        void deberiaEliminarCuandoExiste() {
            EstadoEnvio existente = estadoEjemplo();
            when(repository.findById(1)).thenReturn(Optional.of(existente));

            service.eliminar(1);

            verify(repository, times(1)).delete(existente);
        }

        @Test
        @DisplayName("lanza excepción cuando el estado no existe")
        void deberiaLanzarExcepcionCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.eliminar(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Estado no encontrado");

            verify(repository, never()).delete(any());
        }
    }
}