package microservice.logistica.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "detalle_envio", schema = "logistica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleEnvio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "envio_id", nullable = false)
    private Envio envio;

    @Column(name = "tipo_recurso_id", nullable = false)
    private Integer tipoRecursoId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad;
}