package microservice.logistica.model;

import java.time.LocalDate;

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
@Table(name = "envio", schema = "logistica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "centro_acopio_id", nullable = false)
    private CentroAcopio centroAcopio;

    @Column(nullable = false, length = 255)
    private String destino;

    @ManyToOne
    @JoinColumn(name = "comuna_id", nullable = false)
    private Comuna comuna;

    @Column(name = "fecha_planificada", nullable = false)
    private LocalDate fechaPlanificada;

    @Column(name = "fecha_entrega")
    private LocalDate fechaEntrega;

    @ManyToOne
    @JoinColumn(name = "estado_id", nullable = false)
    private EstadoEnvio estado;

    @Column(name = "responsable_uid", nullable = false, length = 128)
    private String responsableUid;
}