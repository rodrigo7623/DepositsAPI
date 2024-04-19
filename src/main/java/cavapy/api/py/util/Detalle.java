package cavapy.api.py.util;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Detalle {

    private String fechaContable;

    private String hora;

    private String fecha;

    private Integer transaccion;

    private String tipoDeMovimiento;

    private BigDecimal montoCredito;

    private BigDecimal montoDebito;

    private BigDecimal monto;

    private String concepto;

    private Integer serie;

    private String comprobante;

    private String usuario;

    private String origen;

    private String siglasSucursal;

    private String tipoDetalle;

    private String idReferenciaDetalle;

    private String location;

}
