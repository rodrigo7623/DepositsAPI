package cavapy.api.py.model;

import cavapy.api.py.entity.CuentaBancaria;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
public class FiltrosDeBusqueda {

    private String fechaInicio;

    private String fechaFin;

    private String cuentaSeleccionada;
}
