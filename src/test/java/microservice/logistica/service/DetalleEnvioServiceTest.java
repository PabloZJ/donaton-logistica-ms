package microservice.logistica.service;

import microservice.logistica.model.DetalleEnvio;
import microservice.logistica.model.Envio;
import microservice.logistica.repository.DetalleEnvioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DetalleEnvioService - Tests unitarios")
class DetalleEnvioServiceTest {

    @Mock
    private DetalleEnvioRepository repository;

    @InjectMocks
    private DetalleEnvioService service;

    // ─── Datos de prueba ─────────────────────────────────────────────────────

    private Envio envioEjemplo() {
        Envio e = new Envio();
        e.setId(10);
        return e;
    }

    private DetalleEnvio detalleEjemplo() {
        DetalleEnvio d = new DetalleEnvio();
        d.setId(1);
        d.setEnvio(envioEjemplo());
        d.setTipoRecursoId(5);
        d.setCantidad(new BigDecimal("20"));
        return d;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // obtenerTodos()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("obtenerTodos()")
    class ObtenerTodos {

        @Test
        @DisplayName("retorna lista con todos los detalles")
        void deberiaRetornarTodosLosDetalles() {
            List<DetalleEnvio> lista = List.of(detalleEjemplo(), detalleEjemplo());
            when(repository.findAll()).thenReturn(lista);

            List<DetalleEnvio> resultado = service.obtenerTodos();

            assertThat(resultado).hasSize(2);
            verify(repository, times(1)).findAll();
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay detalles")
        void deberiaRetornarListaVaciaCuandoNoHayDetalles() {
            when(repository.findAll()).thenReturn(Collections.emptyList());

            List<DetalleEnvio> resultado = service.obtenerTodos();

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
        @DisplayName("retorna el detalle cuando existe")
        void deberiaRetornarDetalleCuandoExiste() {
            when(repository.findById(1)).thenReturn(Optional.of(detalleEjemplo()));

            DetalleEnvio resultado = service.obtenerPorId(1);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1);
            assertThat(resultado.getTipoRecursoId()).isEqualTo(5);
            assertThat(resultado.getCantidad()).isEqualByComparingTo(new BigDecimal("20"));
        }

        @Test
        @DisplayName("retorna null cuando no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            DetalleEnvio resultado = service.obtenerPorId(99);

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
        @DisplayName("guarda y retorna el detalle con id asignado")
        void deberiaGuardarYRetornarDetalleConId() {
            DetalleEnvio nuevo = new DetalleEnvio();
            nuevo.setEnvio(envioEjemplo());
            nuevo.setTipoRecursoId(3);
            nuevo.setCantidad(new BigDecimal("10"));

            DetalleEnvio guardado = detalleEjemplo();
            guardado.setId(2);

            when(repository.save(nuevo)).thenReturn(guardado);

            DetalleEnvio resultado = service.guardar(nuevo);

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
        @DisplayName("actualiza todos los campos cuando el detalle existe")
        void deberiaActualizarTodosCamposCuandoExiste() {
            DetalleEnvio existente = detalleEjemplo();

            Envio nuevoEnvio = new Envio();
            nuevoEnvio.setId(20);

            DetalleEnvio nuevaData = new DetalleEnvio();
            nuevaData.setEnvio(nuevoEnvio);
            nuevaData.setTipoRecursoId(8);
            nuevaData.setCantidad(new BigDecimal("50"));

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(DetalleEnvio.class))).thenAnswer(inv -> inv.getArgument(0));

            DetalleEnvio resultado = service.actualizar(1, nuevaData);

            assertThat(resultado.getEnvio().getId()).isEqualTo(20);
            assertThat(resultado.getTipoRecursoId()).isEqualTo(8);
            assertThat(resultado.getCantidad()).isEqualByComparingTo(new BigDecimal("50"));
            verify(repository, times(1)).save(existente);
        }

        @Test
        @DisplayName("retorna null cuando el detalle no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            DetalleEnvio resultado = service.actualizar(99, new DetalleEnvio());

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
            DetalleEnvio existente = detalleEjemplo();

            DetalleEnvio parcial = new DetalleEnvio();
            parcial.setCantidad(new BigDecimal("99"));
            // envio y tipoRecursoId null → no deben cambiar

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(DetalleEnvio.class))).thenAnswer(inv -> inv.getArgument(0));

            DetalleEnvio resultado = service.actualizarParcial(1, parcial);

            assertThat(resultado.getCantidad()).isEqualByComparingTo(new BigDecimal("99"));
            assertThat(resultado.getTipoRecursoId()).isEqualTo(5);       // sin cambio
            assertThat(resultado.getEnvio().getId()).isEqualTo(10);      // sin cambio
        }

        @Test
        @DisplayName("no modifica ningún campo si todos vienen null")
        void deberiaNoModificarNadaSiTodoEsNull() {
            DetalleEnvio existente = detalleEjemplo();

            when(repository.findById(1)).thenReturn(Optional.of(existente));
            when(repository.save(any(DetalleEnvio.class))).thenAnswer(inv -> inv.getArgument(0));

            DetalleEnvio resultado = service.actualizarParcial(1, new DetalleEnvio());

            assertThat(resultado.getTipoRecursoId()).isEqualTo(5);
            assertThat(resultado.getCantidad()).isEqualByComparingTo(new BigDecimal("20"));
            assertThat(resultado.getEnvio().getId()).isEqualTo(10);
        }

        @Test
        @DisplayName("retorna null cuando el detalle no existe")
        void deberiaRetornarNullCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            DetalleEnvio resultado = service.actualizarParcial(99, new DetalleEnvio());

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
        @DisplayName("elimina el detalle cuando existe")
        void deberiaEliminarCuandoExiste() {
            DetalleEnvio existente = detalleEjemplo();
            when(repository.findById(1)).thenReturn(Optional.of(existente));

            service.eliminar(1);

            verify(repository, times(1)).delete(existente);
        }

        @Test
        @DisplayName("lanza excepción cuando el detalle no existe")
        void deberiaLanzarExcepcionCuandoNoExiste() {
            when(repository.findById(99)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.eliminar(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Detalle no encontrado");

            verify(repository, never()).delete(any());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // porEnvio()
    // ═══════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("porEnvio()")
    class PorEnvio {

        @Test
        @DisplayName("retorna detalles filtrados por envio")
        void deberiaRetornarDetallesPorEnvio() {
            List<DetalleEnvio> lista = List.of(detalleEjemplo());
            when(repository.findByEnvioId(10)).thenReturn(lista);

            List<DetalleEnvio> resultado = service.porEnvio(10);

            assertThat(resultado).hasSize(1);
            verify(repository, times(1)).findByEnvioId(10);
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay detalles para ese envio")
        void deberiaRetornarListaVaciaCuandoNoHayCoincidencias() {
            when(repository.findByEnvioId(99)).thenReturn(Collections.emptyList());

            List<DetalleEnvio> resultado = service.porEnvio(99);

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
        @DisplayName("retorna detalles filtrados por tipo de recurso")
        void deberiaRetornarDetallesPorTipoRecurso() {
            List<DetalleEnvio> lista = List.of(detalleEjemplo());
            when(repository.findByTipoRecursoId(5)).thenReturn(lista);

            List<DetalleEnvio> resultado = service.porTipoRecurso(5);

            assertThat(resultado).hasSize(1);
            verify(repository, times(1)).findByTipoRecursoId(5);
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay detalles para ese tipo")
        void deberiaRetornarListaVaciaCuandoNoHayCoincidencias() {
            when(repository.findByTipoRecursoId(99)).thenReturn(Collections.emptyList());

            List<DetalleEnvio> resultado = service.porTipoRecurso(99);

            assertThat(resultado).isEmpty();
        }
    }
}