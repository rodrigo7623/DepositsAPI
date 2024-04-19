package cavapy.api.py.responses;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MovimientosDetalleTO {

    private String comprobante;

    private String fechaContable;

    private String Concepto;

    private BigDecimal monto;

    private String TipoDetalle;

}
