package cavapy.api.py.continental.entity;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MovimientosDetallePk implements Serializable {

    @Column(name = "id_movimientos_fk")
    private Integer idMovimientosFk;

    @Column(name = "posicion")
    private Integer posicion;

}
