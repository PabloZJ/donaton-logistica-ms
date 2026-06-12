package microservice.logistica.service;

import microservice.logistica.model.CentroAcopio;
import microservice.logistica.model.Comuna;
import microservice.logistica.model.Envio;
import microservice.logistica.model.EstadoEnvio;
import microservice.logistica.repository.EnvioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EnvioService - Tests unitarios")
class EnvioServiceTest {

    @Mock
    private EnvioRepository repository;

    @InjectMocks
    private EnvioService service;

    // ─── Datos de prueba ─────────────────────────────────────────────────────

    private CentroAcopio centroEjemplo() {
        CentroAcopio c = new CentroAcopio();
        c.setId(10);
        c.setNombre("Centro Norte");
        return c;
    }

    private Comuna comunaEjemplo() {
        Comuna c = new Comuna();
        c.setId(5);
        c.setNombre("Santiago");
        return c;
    }

    private EstadoEnvio estadoEjemplo() {
        EstadoEnvio e = new EstadoEnvio();
        e.setId(1);
        e.setNombre("Pendiente");
        return e;
    }

    private Envio envioEjemplo() {
        Envio e = new Envio();
        e.setId(1);
        e.setCentroAcopio(centroEjemplo());
        e.setDestino("Av. Siempreviva 123");
        e.setComuna(comunaEjemplo());
        e.setFechaPlanificada(LocalDate.of(2026, 6, 15));
        e.setFechaEntrega(LocalDate.of(2026, 6, 16));
        e.setEstado(estadoEjemplo());
        e.setResponsableUid("uid-resp-123");
        return e;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerTodos()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerTodos()")
    class ObtenerTodos {

        @Test
        @DisplayName("retorna lista con todos los envios")
        void deberiaRetornarTodosLosEnvios() {
            List<Envio> lista = List.of(envioEjemplo(), envioEjemplo());
            when(repository.findAll()).thenReturn(lista);

            List<Envio> resultado = service.obtenerTodos();

            assertThat(resultado).hasSize(2);
            verify(repository, times(1)).findAll();
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay envios")
        void deberiaRetornarListaVaciaCuandoNoHayEnvios() {
            when(repository.findAll()).thenReturn(Collections.emptyList());

            List<Envio> resultado = service.obtenerTodos();

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
        @DisplayName("retorna el envio cuando existe")
        void deberiaRetornarEnvioCuandoExiste() {
            when(repository.findById(1)).thenReturn(Optional.of(envioEjemplo()));

            Envio resultado = service.obtenerPorId(1);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1);
            assertThat(resultado.getDestino()).isEqualTo("Av. Siempreviva 123");
            assertThat(resultado.getResponsableUid()).isEqualTo("uid-resp-123");
        }

        @Test
        @DisplayName("retorna null cuando no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            Envio resultado = service.obtenerPorId(99);

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
        @DisplayName("guarda y retorna el envio con id asignado")
        void deberiaGuardarYRetornarEnvioConId() {
            Envio nuevo = new Envio();
            nuevo.setDestino("Calle Nueva 456");
            nuevo.setResponsableUid("uid-nuevo");

            Envio guardado = envioEjemplo();
            guardado.setId(2);

            when(repository.save(nuevo)).thenReturn(guardado);

            Envio resultado = service.guardar(nuevo);

            assertThat(resultado.getId()).isEqualTo(2);
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
        @DisplayName("actualiza todos los campos cuando el envio existe")
        void deberiaActualizarTodosCamposCuandoExiste() {
            Envio existente = envioEjemplo();

            CentroAcopio nuevoCentro = new CentroAcopio();
            nuevoCentro.setId(20);
            nuevoCentro.setNombre("Centro Sur");

            Comuna nuevaComuna = new Comuna();
            nuevaComuna.setId(8);
            nuevaComuna.setNombre("Maipú");

            EstadoEnvio nuevoEstado = new EstadoEnvio();
            nuevoEstado.setId(2);
            nuevoEstado.setNombre("En tránsito");

            Envio nuevaData = new Envio();
            nuevaData.setCentroAcopio(nuevoCentro);
            nuevaData.setDestino("Nueva Dirección 999");
            nuevaData.setComuna(nuevaComuna);
            nuevaData.setFechaPlanificada(LocalDate.of(2026, 7, 1));
            nuevaData.setFechaEntrega(LocalDate.of(2026, 7, 2));
            nuevaData.setEstado(nuevoEstado);
            nuevaData.setResponsableUid("uid-nuevo-resp");

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(Envio.class))).thenAnswer(inv -> inv.getArgument(0));

            Envio resultado = service.actualizar(1, nuevaData);

            assertThat(resultado.getCentroAcopio().getId()).isEqualTo(20);
            assertThat(resultado.getDestino()).isEqualTo("Nueva Dirección 999");
            assertThat(resultado.getComuna().getNombre()).isEqualTo("Maipú");
            assertThat(resultado.getEstado().getNombre()).isEqualTo("En tránsito");
            assertThat(resultado.getResponsableUid()).isEqualTo("uid-nuevo-resp");
            assertThat(resultado.getFechaPlanificada()).isEqualTo(LocalDate.of(2026, 7, 1));
            verify(repository, times(1)).save(existente);
        }

        @Test
        @DisplayName("retorna null cuando el envio no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            Envio resultado = service.actualizar(99, new Envio());

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
            Envio existente = envioEjemplo();

            Envio parcial = new Envio();
            parcial.setDestino("Destino Nuevo");
            parcial.setResponsableUid("uid-parcial");
            // resto null → no deben cambiar

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(Envio.class))).thenAnswer(inv -> inv.getArgument(0));

            Envio resultado = service.actualizarParcial(1, parcial);

            assertThat(resultado.getDestino()).isEqualTo("Destino Nuevo");
            assertThat(resultado.getResponsableUid()).isEqualTo("uid-parcial");
            assertThat(resultado.getCentroAcopio().getId()).isEqualTo(10);       // sin cambio
            assertThat(resultado.getComuna().getNombre()).isEqualTo("Santiago"); // sin cambio
            assertThat(resultado.getEstado().getNombre()).isEqualTo("Pendiente"); // sin cambio
        }

        @Test
        @DisplayName("no modifica ningún campo si todos vienen null")
        void deberiaNoModificarNadaSiTodoEsNull() {
            Envio existente = envioEjemplo();

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(Envio.class))).thenAnswer(inv -> inv.getArgument(0));

            Envio resultado = service.actualizarParcial(1, new Envio());

            assertThat(resultado.getDestino()).isEqualTo("Av. Siempreviva 123");
            assertThat(resultado.getResponsableUid()).isEqualTo("uid-resp-123");
            assertThat(resultado.getCentroAcopio().getId()).isEqualTo(10);
        }

        @Test
        @DisplayName("retorna null cuando el envio no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            Envio resultado = service.actualizarParcial(99, new Envio());

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
        @DisplayName("elimina el envio cuando existe")
        void deberiaEliminarCuandoExiste() {
            Envio existente = envioEjemplo();
            when(repository.findById(1)).thenReturn(Optional.of(existente));

            service.eliminar(1);

            verify(repository, times(1)).delete(existente);
        }

        @Test
        @DisplayName("lanza excepción cuando el envio no existe")
        void deberiaLanzarExcepcionCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.eliminar(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Envio no encontrado");

            verify(repository, never()).delete(any());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // porEstado()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("porEstado()")
    class PorEstado {

        @Test
        @DisplayName("retorna envios filtrados por estado")
        void deberiaRetornarEnviosPorEstado() {
            List<Envio> lista = List.of(envioEjemplo());
            when(repository.findByEstadoId(1)).thenReturn(lista);

            List<Envio> resultado = service.porEstado(1);

            assertThat(resultado).hasSize(1);
            verify(repository, times(1)).findByEstadoId(1);
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay envios con ese estado")
        void deberiaRetornarListaVaciaCuandoNoHayCoincidencias() {
            when(repository.findByEstadoId(99)).thenReturn(Collections.emptyList());

            List<Envio> resultado = service.porEstado(99);

            assertThat(resultado).isEmpty();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // porCentro()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("porCentro()")
    class PorCentro {

        @Test
        @DisplayName("retorna envios filtrados por centro de acopio")
        void deberiaRetornarEnviosPorCentro() {
            List<Envio> lista = List.of(envioEjemplo());
            when(repository.findByCentroAcopioId(10)).thenReturn(lista);

            List<Envio> resultado = service.porCentro(10);

            assertThat(resultado).hasSize(1);
            verify(repository, times(1)).findByCentroAcopioId(10);
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay envios en ese centro")
        void deberiaRetornarListaVaciaCuandoNoHayCoincidencias() {
            when(repository.findByCentroAcopioId(99)).thenReturn(Collections.emptyList());

            List<Envio> resultado = service.porCentro(99);

            assertThat(resultado).isEmpty();
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // porComuna()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("porComuna()")
    class PorComuna {

        @Test
        @DisplayName("retorna envios filtrados por comuna")
        void deberiaRetornarEnviosPorComuna() {
            List<Envio> lista = List.of(envioEjemplo());
            when(repository.findByComunaId(5)).thenReturn(lista);

            List<Envio> resultado = service.porComuna(5);

            assertThat(resultado).hasSize(1);
            verify(repository, times(1)).findByComunaId(5);
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay envios en esa comuna")
        void deberiaRetornarListaVaciaCuandoNoHayCoincidencias() {
            when(repository.findByComunaId(99)).thenReturn(Collections.emptyList());

            List<Envio> resultado = service.porComuna(99);

            assertThat(resultado).isEmpty();
        }
    }
}