package microservice.logistica.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "inventario", schema = "logistica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "centro_acopio_id", nullable = false)
    private Integer centroAcopioId;

    @Column(name = "tipo_recurso_id", nullable = false)
    private Integer tipoRecursoId;

    @Column(name = "cantidad_disponible", nullable = false, precision = 18, scale = 2)
    private BigDecimal cantidadDisponible;

    @Column(name = "actualizado_en", nullable = false)
    private LocalDateTime actualizadoEn;
}