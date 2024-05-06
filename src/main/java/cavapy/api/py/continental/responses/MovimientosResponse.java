package cavapy.api.py.continental.responses;

import cavapy.api.py.continental.util.Detalle;
import cavapy.api.py.continental.util.Cabecera;
import lombok.Data;

@Data
public class MovimientosResponse {

    private Cabecera cabecera;

    private Detalle[] movimientos;
}
