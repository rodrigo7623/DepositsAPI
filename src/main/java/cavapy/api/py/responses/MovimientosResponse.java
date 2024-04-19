package cavapy.api.py.responses;

import cavapy.api.py.util.Cabecera;
import cavapy.api.py.util.Detalle;
import lombok.Data;

@Data
public class MovimientosResponse {

    private Cabecera cabecera;

    private Detalle [] movimientos;
}
