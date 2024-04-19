package cavapy.api.py.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReferenciaDetalle {
    private String tipo;
    private String nombre;
    @Column(name = "tipo_de_documento")
    private String tipoDocumento;

    @Column(name = "numero_de_documento")
    private String numeroDeDocumento;

    private String banco;
    @Column(name = "numero_de_cuenta")
    private String numeroDeCuenta;

    private String origen;
    private String fechahora;
    private String motivo;

    @Id
    private String referencia;

    @Column(name = "tipo_detalle")
    private String tipoDetalle;
}
