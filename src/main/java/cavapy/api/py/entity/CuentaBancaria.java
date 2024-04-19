package cavapy.api.py.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "cuenta_bancaria", schema = "public")
public class CuentaBancaria {

    @Column(name = "numero_cuenta")
    private String numeroCuenta;

    @Column(name = "denominacion")
    private String denominacion;

    @Column(name = "moneda")
    private String moneda;

    @Column(name = "tipo")
    private String tipo;

    @Id
    @Column(name = "hash")
    private String hash;

    @Column(name = "id_banco_fk")
    private Integer idBancoFk;

    @Column(name = "estado")
    private String estado;

    @Column(name = "fecha_alta")
    private Date fechaAlta;

    @Column(name = "usu_alta")
    private String usu_alta;

    @Column(name = "fecha_ultmod")
    private Date fechaUltmod;

    @Column(name = "usu_ultmod")
    private String usuUltmod;

}