package cavapy.api.py.responses;

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

    @Column(name = "id_movimientos_pk")
    private Integer idTrama;

    private String fecha;

/*    @Column(name = "tipo_de_movimiento")
    private String tipoDeMovimiento;*/

    private String monto;

    @Id
    private String comprobante;


    private String moneda;

    private String descripcion;
/*
    private String tipo;

    private String nombre;
*/

    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @Column(name = "numero_de_documento")
    private String numeroDeDocumento;

    private String banco;

    @Column(name = "fecha_inicial")
    private String fechaInicial;

    @Column(name = "fecha_fin")
    private String fechaFin;

    @Column(name = "ind_migracion")
    private String indMigracion;

    @Column(name = "numero_de_cuenta")
    private String numeroDeCuenta;
    /*
      <td th:text="${registro.banco}"></td>
                <td th:text="${registro.numeroDeDocumento}"></td>
                <td th:text="${registro.numeroDeCuenta}"></td>
                <td th:text="${registro.comprobante}"></td>
                <td th:text="${registro.monto}"></td>
                <td th:text="${registro.indMigracion}"></td>
                <td th:text="${registro.descripcion}"></td>*/

    public BuscarResponse(String banco, String nroDoc, String nroCta, String comp, String monto, String indMigracion, String descripcion) {

        this.banco = banco;

        this.numeroDeDocumento = nroDoc;

        this.numeroDeCuenta = nroCta;

        this.comprobante = comp;

        this.monto =  monto;

        this.indMigracion = indMigracion;

        this.descripcion = descripcion;



    }
}
