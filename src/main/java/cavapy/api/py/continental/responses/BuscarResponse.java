package cavapy.api.py.continental.responses;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
