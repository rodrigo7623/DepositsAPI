package cavapy.api.py.controller;

import cavapy.api.py.entity.CuentaBancaria;
import cavapy.api.py.model.FiltrosDeBusqueda;
import cavapy.api.py.repository.BuscarResponseRepository;
import cavapy.api.py.repository.CuentaBancariaRepository;
import cavapy.api.py.responses.BuscarResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

@Controller
public class BuscarController {

    Logger logger = Logger.getLogger(BuscarResponse.class.getName());

    private final CuentaBancariaRepository cuentaBancariaRepository;

    @Autowired
    public BuscarController(CuentaBancariaRepository cuentaBancariaRepository, BuscarResponseRepository buscarResponseRepository) {
        this.cuentaBancariaRepository = cuentaBancariaRepository;
        this.buscarResponseRepository = buscarResponseRepository;
    }

    private final BuscarResponseRepository buscarResponseRepository;

    @RequestMapping(value = "/buscar", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String buscar(@ModelAttribute FiltrosDeBusqueda filtrosDeBusqueda, RedirectAttributes redirectAttributes,
                         Model model) {
        String selectedAccount = filtrosDeBusqueda.getCuentaSeleccionada();
        String startDate = filtrosDeBusqueda.getFechaInicio();
        String endDate = filtrosDeBusqueda.getFechaFin();
        logger.info("Invocación al método buscar");
        logger.info("Cuenta seleccionada: " + selectedAccount);
        logger.info("Fecha de inicio: " + startDate);
        logger.info("Fecha de fin: " + endDate);
        List<BuscarResponse> buscarResponseList = buscarResponseRepository.getAllByFechaInicialAndFechaFin(startDate, endDate, selectedAccount);
        for (BuscarResponse br : buscarResponseList) {
            double numeroDouble = Double.parseDouble(br.getMonto()); // Convertir el String a un número double

            // Crear un formato con el patrón deseado (en este caso, con punto para los miles y coma para los decimales)
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols(Locale.getDefault());
            simbolos.setDecimalSeparator(',');
            simbolos.setGroupingSeparator('.');
            DecimalFormat formatoDecimal = new DecimalFormat("#,###.##", simbolos);

            // Aplicar el formato al número double
            String numeroFormateado = formatoDecimal.format(numeroDouble);
            br.setMonto(numeroFormateado);
        }
        List<CuentaBancaria> bankAccounts = (List<CuentaBancaria>) cuentaBancariaRepository.findAll();
        if (buscarResponseList.isEmpty()) {
            redirectAttributes.addFlashAttribute("accounts", bankAccounts);
            redirectAttributes.addFlashAttribute("filtrosDeBusqueda", filtrosDeBusqueda);
            return "redirect:/home";
        } else {
            model.addAttribute("buscarResponseList", buscarResponseList);
            return "buscar";
        }

        //model.addAttribute("filtrosDeBusqueda", filtrosDeBusqueda);


    }

    private String formatDate(String date) {
        date = date.replace("-", "/");
        logger.info(date);
        return date;
    }
}
