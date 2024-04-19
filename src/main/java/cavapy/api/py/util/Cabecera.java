package cavapy.api.py.util;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

@Data
public class Cabecera {

    private String cuenta;

    private BigDecimal saldoMesAnterior;

    private BigDecimal saldoContable;

    private BigDecimal saldoRetenido;

    private BigDecimal  saldoBloqueado;
             
    private BigDecimal saldoDisponible;

    private BigDecimal saldoInicial;

    private BigDecimal montoSobregiro;
    private String moneda;

    private String funcionario;

    private BigDecimal retenidoDia;

    private BigDecimal retenido24;

    private BigDecimal retenido48;

    private BigDecimal retenidoMas48;

    private String tipoOperacion;

    private String fechaInicial;

    private String fechaFin;
    private BigDecimal depTauserEfe;
    private BigDecimal depTauserChq;

}
