package cavapy.api.py.responses;

import lombok.Data;

import java.sql.Date;

@Data
public class GetMovementResponse {

    private String cuenta;

    private String moneda;

    private String tipoOperacion;

    private String FechaInicial;

    private String fechaFin;

    private MovimientosDetalleTO [] detalle;
}
