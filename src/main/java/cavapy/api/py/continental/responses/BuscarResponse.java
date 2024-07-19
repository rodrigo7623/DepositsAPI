package cavapy.api.py.continental.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BuscarResponse {

    private String fecha;

    private String monto;

    @Id
    private String comprobante;

    @Column(name = "numero_de_documento")
    private String numeroDeDocumento;

    private String banco;

    @Column(name = "ind_migracion")
    private String indMigracion;

    @Column(name = "numero_de_cuenta")
    private String numeroDeCuenta;

    private String moneda;

    private String descripcion;

    @Column(name = "referencia")
    private String referencia;

}
