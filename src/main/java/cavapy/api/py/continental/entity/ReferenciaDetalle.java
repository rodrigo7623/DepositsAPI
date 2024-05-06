package cavapy.api.py.continental.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "referencia_detalle")
public class ReferenciaDetalle {

    private String tipo;

    private String nombre;

    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @Column(name = "numero_de_documento")
    private String numeroDeDocumento;

    private String banco;

    @Column(name = "numero_de_cuenta")
    private String numeroDeCuenta;

    private String origen;

    @Column(name = "fechahora")
    private String fechaHora;

    private String motivo;

    @Id
    private String referencia;

    @Column(name = "tipo_detalle")
    private String tipoDetalle;

}
