package cavapy.api.py.continental.controller;

import cavapy.api.py.continental.entity.CuentaBancaria;
import cavapy.api.py.continental.entity.ReferenciaDetalle;
import cavapy.api.py.continental.model.BankType;
import cavapy.api.py.continental.model.FiltrosDeBusqueda;
import cavapy.api.py.continental.repository.BuscarResponseRepository;
import cavapy.api.py.continental.repository.CuentaBancariaRepository;
import cavapy.api.py.continental.repository.MovimientosRepository;
import cavapy.api.py.continental.repository.ReferenciaDetalleRepository;
import cavapy.api.py.continental.responses.BuscarResponse;
import cavapy.api.py.continental.service.BuscarService;
import cavapy.api.py.continental.service.ValidationService;
import cavapy.api.py.continental.util.PropertiesConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

@Controller
@Configuration
public class BuscarController {

    Logger logger = Logger.getLogger(BuscarResponse.class.getName());

    private BuscarService buscarService;

    private ValidationService validationService;

    @Autowired
    public BuscarController(BuscarService buscarService, ValidationService validationService) {
        this.buscarService = buscarService;
        this.validationService = validationService;
    }

    @PostMapping(value = "/buscar")
    public String buscar(@ModelAttribute FiltrosDeBusqueda filtrosDeBusqueda, RedirectAttributes redirectAttributes,
                         Model model) {
        logger.info("Cuenta seleccionada: " + filtrosDeBusqueda.getCuentaSeleccionada() +
                " Fecha de inicio: " + filtrosDeBusqueda.getFechaInicio() +
                " Fecha de fin: " + filtrosDeBusqueda.getFechaFin());
        if (!validationService.isValidDate(filtrosDeBusqueda.getFechaInicio())) {
            return "redirect:/home?errorStartDate";
        }
        if (buscarService.existenMovimientos(filtrosDeBusqueda)) {
            return "buscar";
        } else {
            return "redirect:/home";
        }
    }
}
