package cavapy.api.py.continental.responses;

import lombok.Data;

@Data
public class MovementRequestResult {

    private String cuenta;

    private String moneda;

    private String tipoOperacion;

    private String FechaInicial;

    private String fechaFin;

    private MovimientosDetalleTO [] detalle;
}
