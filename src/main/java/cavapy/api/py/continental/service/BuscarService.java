package cavapy.api.py.continental.service;

import cavapy.api.py.continental.model.FiltrosDeBusqueda;
import org.springframework.stereotype.Service;

@Service
public class BuscarService {

    public boolean existenMovimientos(FiltrosDeBusqueda filtrosDeBusqueda) {
        String starDateRequest = formatDateToCallApi(filtrosDeBusqueda.getFechaInicio());
        String endDateRequest = formatDateToCallApi(filtrosDeBusqueda.getFechaFin());
        return false;
    }

    private String formatDateToCallApi(String date) {
        String [] string = date.split("-");
        return  string[2] + "-" + string[1] + "-" + string[0];
    }
}
