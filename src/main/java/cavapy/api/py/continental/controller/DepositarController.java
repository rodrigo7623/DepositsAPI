package cavapy.api.py.continental.controller;

import cavapy.api.py.continental.entity.MovimientosDetalle;
import cavapy.api.py.continental.repository.BuscarResponseRepository;
import cavapy.api.py.continental.repository.MovimientosDetalleRepository;
import cavapy.api.py.continental.responses.BuscarResponse;
import cavapy.api.py.continental.responses.DepositResponse;
import cavapy.api.py.continental.util.Deposit;
import cavapy.api.py.continental.util.DepositRequest;
import cavapy.api.py.continental.util.IncorrectDeposit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

@Controller
public class DepositarController {

    @Autowired
    private BuscarResponseRepository buscarResponseRepository;

    @Autowired
    private MovimientosDetalleRepository movimientosDetalleRepository;

    @Value("${pradera.uat.url.deposit}")
    private String depositUrl;


    @Value("${pradera.api.authorization}")
    private String bearer;

    private final RestTemplate restTemplate;

    Logger logger = Logger.getLogger(DepositarController.class.getName());

    public DepositarController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @PostMapping(value = "/depositar")
    public String depositarSeleccionados(@RequestParam("seleccionados") List<String> seleccionados,
                                         Model model) {

        List<BuscarResponse> buscarResponseList = new ArrayList<>();

        Deposit [] depositRequest = new Deposit[seleccionados.size()];

        int indice = 0;
        for (String comprobante : seleccionados) {
            BuscarResponse buscarResponse = buscarResponseRepository.getBuscarResponseById(comprobante);
            buscarResponseList.add(buscarResponse);
            Deposit deposit = new Deposit();
            deposit.setRuc(buscarResponse.getNumeroDeDocumento());
            deposit.setCurrency(buscarResponse.getMoneda().equals("MONEDA NACIONAL")?"PYG":"USD");
            deposit.setAccountNumber(buscarResponse.getNumeroDeCuenta());
            deposit.setOperationAmount(String.valueOf(buscarResponse.getMonto()));
            depositRequest[indice] = deposit;
            indice ++;
        }

        DepositRequest requestBody = new DepositRequest();

        requestBody.setDeposit(depositRequest);

        ObjectMapper json = new ObjectMapper();


        String postBody = "";

        try {
            postBody = json.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", bearer);

        HttpEntity<?> httpEntity = new HttpEntity<>(postBody, headers);

        DepositResponse response = null;

        try {
            response = restTemplate. exchange(depositUrl,
                    HttpMethod.POST, httpEntity, DepositResponse.class).getBody();
        } catch (Exception ex) {
            logger.info(ex.getMessage());
        }

        for (Deposit correct : response.getCorrectDeposit()) {
            for (BuscarResponse br : buscarResponseList) {
                if (correct.getRuc().equals(br.getNumeroDeDocumento())
                && correct.getAccountNumber().equals(br.getNumeroDeCuenta())
                && correct.getCurrency().equals(br.getMoneda().equals("MONEDA NACIONAL")?"PYG":"USD")
                && correct.getOperationAmount().equals(br.getMonto())) {
                    br.setIndMigracion("MIGRADO");
                    br.setDescripcion("Migración exitosa");
                    break;
                }
            }
        }

        if (response.getIncorrectDeposit() != null || response.getIncorrectDeposit().length > 0) {
            for (BuscarResponse br: buscarResponseList) {
                if (!br.getIndMigracion().equals("MIGRADO")) {
                    br.setIndMigracion("FALLIDO");
                    IncorrectDeposit [] incorrectDeposit = response.getIncorrectDeposit();
                    for (int i = 0; i < incorrectDeposit.length; i++) {
                        if (incorrectDeposit[i].getRuc().equals(br.getNumeroDeDocumento())
                                && incorrectDeposit[i].getAccountNumber().equals(br.getNumeroDeCuenta())
                                && incorrectDeposit[i].getCurrency().equals(br.getMoneda().equals("MONEDA NACIONAL")?"PYG":"USD")
                                && incorrectDeposit[i].getOperationAmount().equals(br.getMonto())) {

                            br.setDescripcion(incorrectDeposit[i].getErrorMessage());
                            break;
                        }
                    }
                }
            }
        }


        for (BuscarResponse br: buscarResponseList) {

            MovimientosDetalle md = new MovimientosDetalle();

            md = movimientosDetalleRepository.findById(br.getComprobante()).get();


            md.setIndMigracion(br.getIndMigracion());
            md.setDescripcion(br.getDescripcion());

            movimientosDetalleRepository.save(md);

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


        model.addAttribute("buscarResponseList", buscarResponseList);
        return "buscar";
    }

    @GetMapping(value = "/depositar")
    public String depositar() {
        return "/depositar";
    }
}
