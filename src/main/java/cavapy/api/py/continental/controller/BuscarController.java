package cavapy.api.py.continental.controller;

import cavapy.api.py.continental.entity.CuentaBancaria;
import cavapy.api.py.continental.model.FiltrosDeBusqueda;
import cavapy.api.py.continental.repository.BuscarResponseRepository;
import cavapy.api.py.continental.repository.CuentaBancariaRepository;
import cavapy.api.py.continental.responses.BuscarResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

@Controller
public class BuscarController {

    Logger logger = Logger.getLogger(BuscarResponse.class.getName());

    private final CuentaBancariaRepository cuentaBancariaRepository;

    MainController mainController;

    @Autowired
    public BuscarController(CuentaBancariaRepository cuentaBancariaRepository, BuscarResponseRepository buscarResponseRepository, MainController mainController) {
        this.cuentaBancariaRepository = cuentaBancariaRepository;
        this.buscarResponseRepository = buscarResponseRepository;
        this.mainController = mainController;
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
/*        CuentaBancaria cuentaBancaria = cuentaBancariaRepository.findByNumeroCuenta(selectedAccount).orElse(null);
        if (cuentaBancaria!= null) {
            selectedAccount = cuentaBancaria.getHash();
        }*/

        String startDateRequest = formatDate(startDate);
        String endDateRequest = formatDate(endDate);

        mainController.getMovements(selectedAccount, startDateRequest, endDateRequest);
        CuentaBancaria cuentaBancaria = cuentaBancariaRepository.findByNumeroCuenta(selectedAccount).orElse(null);
        if (cuentaBancaria!= null) {
            selectedAccount = cuentaBancaria.getHash();
        }
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
        List<CuentaBancaria> bankAccounts = (List<CuentaBancaria>) cuentaBancariaRepository.findByEstado("AC").orElse(null);
        if (buscarResponseList.isEmpty()) {
            redirectAttributes.addFlashAttribute("accounts", bankAccounts);
            redirectAttributes.addFlashAttribute("filtrosDeBusqueda", filtrosDeBusqueda);
            redirectAttributes.addFlashAttribute("buscarResponses", buscarResponseList);
            return "redirect:/home";
        } else {
            model.addAttribute("buscarResponseList", buscarResponseList);
            return "buscar";
        }

        //model.addAttribute("filtrosDeBusqueda", filtrosDeBusqueda);


    }

    private String formatDate(String date) {
        String [] string = date.split("-");

        return  string[2] +
                "-" + string[1] +
                "-" + string[0];
    }
}
