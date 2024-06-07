package cavapy.api.py.continental.controller;

import cavapy.api.py.continental.entity.CuentaBancaria;
import cavapy.api.py.continental.entity.Movimientos;
import cavapy.api.py.continental.entity.ReferenciaDetalle;
import cavapy.api.py.continental.model.BankType;
import cavapy.api.py.continental.model.FiltrosDeBusqueda;
import cavapy.api.py.continental.repository.BuscarResponseRepository;
import cavapy.api.py.continental.repository.CuentaBancariaRepository;
import cavapy.api.py.continental.repository.MovimientosRepository;
import cavapy.api.py.continental.repository.ReferenciaDetalleRepository;
import cavapy.api.py.continental.responses.BuscarResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
public class BuscarController {

    Logger logger = Logger.getLogger(BuscarResponse.class.getName());

    private final CuentaBancariaRepository cuentaBancariaRepository;

    private final MovimientosRepository movimientosRepository;

    private final ReferenciaDetalleRepository referenciaDetalleRepository;

    private final RestTemplate restTemplate;

    MainController mainController;

    private final String CORE_URL = "http://localhost:8383/api/core/banks/getAll";

    @Autowired
    public BuscarController(CuentaBancariaRepository cuentaBancariaRepository, BuscarResponseRepository buscarResponseRepository, MainController mainController,
                            RestTemplate restTemplate, MovimientosRepository movimientosRepository, ReferenciaDetalleRepository referenciaDetalleRepository) {
        this.cuentaBancariaRepository = cuentaBancariaRepository;
        this.buscarResponseRepository = buscarResponseRepository;
        this.mainController = mainController;
        this.restTemplate = restTemplate;
        this.movimientosRepository = movimientosRepository;
        this.referenciaDetalleRepository = referenciaDetalleRepository;
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
        BankType[] response = restTemplate.getForObject(CORE_URL, BankType[].class);
        for (BuscarResponse br : buscarResponseList) {

            for (BankType bt : response) {

                String cadena = removerCeros(br.getNumeroDeCuenta());

                if (isSubsequence(cadena, bt.getAccountNumber())) {
                    br.setNumeroDeDocumento(bt.getDocumentNumber());

                    ReferenciaDetalle referenciaDetalle = referenciaDetalleRepository.findById(br.getReferencia()).orElse(null);
                    if (referenciaDetalle != null) {
                        referenciaDetalle.setNumeroDeCuenta(cadena);
                        referenciaDetalle.setNumeroDeDocumento(bt.getDocumentNumber());
                        referenciaDetalleRepository.save(referenciaDetalle);
                    }
                    break;
                }

            }

            double numeroDouble = Double.parseDouble(br.getMonto()); // Convertir el String a un número double

            // Crear un formato con el patrón deseado (en este caso, con punto para los miles y coma para los decimales)
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols(Locale.getDefault());
            simbolos.setDecimalSeparator(',');
            simbolos.setGroupingSeparator('.');
            DecimalFormat formatoDecimal = new DecimalFormat("#,###.##", simbolos);

            // Aplicar el formato al número double
            String numeroFormateado = formatoDecimal.format(numeroDouble);
            br.setMonto(numeroFormateado);
            br.setMoneda(br.getMoneda().equals("MONEDA NACIONAL")?"PYG":"USD");
            br.setNumeroDeCuenta(removerCeros(br.getNumeroDeCuenta()));
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

    private boolean isSubsequence(String A, String B) {
        int j = 0; // Índice para la cadena A

        for (int i = 0; i < B.length() && j < A.length(); i++) {
            if (A.charAt(j) == B.charAt(i)) {
                j++;
            }
        }

        return j == A.length();
    }

    private String removerCeros(String accountNumber) {
        boolean sw = false;
        String retorno = "";
        for (int i = 0; i < accountNumber.length(); i++) {
            if (accountNumber.charAt(i) != '0') {
                sw = true;
            }
            if (accountNumber.charAt(i) == '-') {
                sw = false;
                continue;
            }
            if (sw) {
                retorno += accountNumber.charAt(i);
            }
        }
        return retorno;
    }

    private String formatDate(String date) {
        String [] string = date.split("-");

        return  string[2] +
                "-" + string[1] +
                "-" + string[0];
    }
}
