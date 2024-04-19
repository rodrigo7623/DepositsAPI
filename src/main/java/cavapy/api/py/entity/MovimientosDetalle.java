package cavapy.api.py.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "MOVIMIENTOS_DETALLE", schema = "public", catalog = "")
//@IdClass(MovimientosDetallePK.class)
public class MovimientosDetalle {

    @Column(name = "id_movimientos_fk")
    private Integer idMovimientosFk;

    @Column(name = "posicion")
    private Integer posicion;

    @Basic
    @Column(name = "FECHA_CONTABLE")
    private String fechaContable;
    @Basic
    @Column(name = "HORA")
    private String hora;
    @Basic
    @Column(name = "FECHA")
    private String fecha;
    @Basic
    @Column(name = "TRANSACCION")
    private BigDecimal transaccion;
    @Basic
    @Column(name = "TIPO_DE_MOVIMIENTO")
    private String tipoDeMovimiento;
    @Basic
    @Column(name = "MONTO_CREDITO")
    private BigDecimal montoCredito;
    @Basic
    @Column(name = "MONTO_DEBITO")
    private BigDecimal montoDebito;
    @Basic
    @Column(name = "MONTO")
    private BigDecimal monto;
    @Basic
    @Column(name = "CONCEPTO")
    private String concepto;
    @Basic
    @Column(name = "SERIE")
    private BigDecimal serie;

    @Id
    @Basic
    @Column(name = "COMPROBANTE")
    private String comprobante;
    @Basic
    @Column(name = "USUARIO")
    private String usuario;
    @Basic
    @Column(name = "ORIGEN")
    private String origen;
    @Basic
    @Column(name = "SIGLAS_SUCURSAL")
    private String siglasSucursal;
    @Basic
    @Column(name = "TIPO_DETALLE")
    private String tipoDetalle;
    @Basic
    @Column(name = "ID_REFERENCIA_DETALLE")
    private String idReferenciaDetalle;
    @Basic
    @Column(name = "LOCATION")
    private String location;

    @Column(name = "ind_migracion")
    private String indMigracion;

    @Column
    private String descripcion;

}
