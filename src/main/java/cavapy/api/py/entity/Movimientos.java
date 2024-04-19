package cavapy.api.py.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

@Data
@Entity
@Table(name = "movimientos")
public class Movimientos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimientos_pk")
    private  Integer idMovimientosPk;
    @Basic
    @Column(name = "cuenta")
    private String cuenta;
    @Basic
    @Column(name = "saldo_mes_anterior")
    private BigDecimal saldoMesAnterior;
    @Basic
    @Column(name = "saldo_contable")
    private BigDecimal saldoContable;
    @Basic
    @Column(name = "saldo_retenido")
    private BigDecimal saldoRetenido;
    @Basic
    @Column(name = "saldo_bloqueado")
    private BigDecimal saldoBloqueado;
    @Basic
    @Column(name = "saldo_disponible")
    private BigDecimal saldoDisponible;
    @Basic
    @Column(name = "saldo_inicial")
    private BigDecimal saldoInicial;
    @Basic
    @Column(name = "monto_sobregiro")
    private BigDecimal montoSobregiro;
    @Basic
    @Column(name = "moneda")
    private String moneda;
    @Basic
    @Column(name = "funcionario")
    private String funcionario;
    @Basic
    @Column(name = "retenido_dia")
    private BigDecimal retenidoDia;
    @Basic
    @Column(name = "retenido_24")
    private BigDecimal retenido24;
    @Basic
    @Column(name = "retenido_48")
    private BigDecimal retenido48;
    @Basic
    @Column(name = "retenido_mas48")
    private BigDecimal retenidoMas48;
    @Basic
    @Column(name = "tipo_operacion")
    private String tipoOperacion;
    @Basic
    @Column(name = "fecha_inicial")
    private String fechaInicial;
    @Basic
    @Column(name = "fecha_fin")
    private String fechaFin;
    @Basic
    @Column(name = "dep_tauser_efe")
    private BigDecimal depTauserEfe;
    @Basic
    @Column(name = "dep_tauser_chq")
    private BigDecimal depTauserChq;

    @Column(name = "hash")
    private String hash;

}
