package cavapy.api.py.controller;

import cavapy.api.py.entity.CuentaBancaria;
import cavapy.api.py.entity.Movimientos;
import cavapy.api.py.entity.MovimientosDetalle;
import cavapy.api.py.entity.ReferenciaDetalle;
import cavapy.api.py.repository.BuscarResponseRepository;
import cavapy.api.py.repository.CuentaBancariaRepository;
import cavapy.api.py.repository.MovimientosDetalleRepository;
import cavapy.api.py.repository.MovimientosRepository;
import cavapy.api.py.responses.*;
import cavapy.api.py.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Repeatable;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

@RestController

public class MainController {

    Logger logger = Logger.getLogger(MainController.class.getName());

    private final HttpHeaders headers = new HttpHeaders();

    HttpEntity<?> httpEntity = null;

    @Autowired
    public MainController(RestTemplate restTemplate, CuentaBancariaRepository cuentaBancariaRepository) {
        this.restTemplate = restTemplate;
        this.cuentaBancariaRepository = cuentaBancariaRepository;
    }

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public final CuentaBancariaRepository cuentaBancariaRepository;

    @Value("${sandbox.api.usuario}")
    private String usuario;

    @Value("${sandbox.api.password}")
    private String password;

    @Value("${sandbox.api.ruc}")
    private String ruc;

    @Value("${sandbox.api.subscription-key}")
    private String subscriptionKey;

    @Value("${sandbox.api.token}")
    private String accessTokenUrl;

    @Value("${sandbox.api.accounts}")
    private String bankAccountUrl;

    @Value("${sandbox.api.extracts}")
    private String movementsUrl;

    @Value("${sandbox.api.reference}")
    private String referenceUrl;

    @RequestMapping(value = "/getAccessToken", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAccessToken(HttpServletRequest request) {
        logger.info("Se ha invocado el método para obtener el token de autenticación");
        logger.info("La dirección IP de la solicitud es: " + request.getRemoteAddr());
        if (!headers.isEmpty()) {
            headers.clear();
        }
        headers.add("Usuario", usuario);
        headers.add("Password", password);
        headers.add("RUC", ruc);
        headers.add("Subscription-key", subscriptionKey);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        AccessToken accessToken = new AccessToken();
        AccessTokenResponse accessTokenResponse;
        ErrorResponse errorResponse = new ErrorResponse();
        try {
            accessTokenResponse = restTemplate.postForObject(accessTokenUrl, httpEntity, AccessTokenResponse.class);
        } catch (HttpClientErrorException httpClientErrorException) {
            try {
                errorResponse = objectMapper.readValue(httpClientErrorException.getResponseBodyAsString(), ErrorResponse.class);
                return new ResponseEntity<>(errorResponse, httpClientErrorException.getStatusCode());
            } catch (JsonProcessingException jsonProcessingException) {
                errorResponse.setError(Messages.JSON_PROCESSING_ERROR.getError());
                errorResponse.setErrorDescription(Messages.JSON_PROCESSING_ERROR.getErrorDescription());
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
        } catch (HttpServerErrorException httpServerErrorException) {
            errorResponse.setError(Messages.GATEWAY_TIME_OUT.getError());
            errorResponse.setErrorDescription(Messages.GATEWAY_TIME_OUT.getErrorDescription());
            return new ResponseEntity<>(errorResponse, HttpStatus.GATEWAY_TIMEOUT);
        }
        if (accessTokenResponse != null) {
            if (accessTokenResponse.getAccessToken() != null) {
                accessToken = new AccessToken(accessTokenResponse.getAccessToken());
            }
        }
        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }

    @RequestMapping(value = "/getBankAccount", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBankAccount(HttpServletRequest request) {
        ResponseEntity<?> responseEntity = setGetRequest(request);
        if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
            BankAccount[] bankAccounts = restTemplate.exchange(bankAccountUrl, HttpMethod.GET, httpEntity, BankAccount[].class).getBody();
            if (bankAccounts == null) {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setError(Messages.BANK_ACCOUNT_EMPTY.getError());
                errorResponse.setErrorDescription(Messages.BANK_ACCOUNT_EMPTY.getErrorDescription());
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
            List<CuentaBancaria> listCuentaBancarias = new ArrayList<>();
            for (BankAccount ba : bankAccounts) {
                CuentaBancaria cuentaBancaria = new CuentaBancaria();
                cuentaBancaria.setNumeroCuenta(ba.getNumeroCuenta());
                cuentaBancaria.setDenominacion(ba.getDenominacion());
                cuentaBancaria.setMoneda(ba.getMoneda());
                cuentaBancaria.setTipo(ba.getTipo());
                cuentaBancaria.setHash(ba.getHash());
                cuentaBancaria.setIdBancoFk(Banks.CONTINENTAL.getIdBank());
                cuentaBancaria.setEstado(State.ACTIVO.getValue());
                cuentaBancaria.setFechaAlta(new Date(System.currentTimeMillis()));
                cuentaBancaria.setUsu_alta(usuario);
                cuentaBancaria.setFechaUltmod(new Date(System.currentTimeMillis()));
                cuentaBancaria.setUsuUltmod(usuario);
                listCuentaBancarias.add(cuentaBancaria);
            }
            try {
                for (CuentaBancaria cb : listCuentaBancarias) {
                    System.out.println(cb.toString());
                }
                listCuentaBancarias = (List<CuentaBancaria>) cuentaBancariaRepository.saveAll(listCuentaBancarias);
            } catch (Exception ex) {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setError(ex.getCause().getMessage());
                errorResponse.setErrorDescription(ex.getMessage());
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(listCuentaBancarias, HttpStatus.OK);
        } else {
            return null;
        }
    }

    AccessToken accessToken;

    @Autowired
    private MovimientosRepository movimientosRepository;

    @Autowired
    private MovimientosDetalleRepository movimientosDetalleRepository;

    @RequestMapping(value = "/getMovements/{hash}/{start}/{end}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMovements(@Parameter(description = "Cuenta hash", required = true) @PathVariable String hash,
                                          @Parameter(description = "Fecha Inicio en formato DD/MM/YYYY", required = true) @PathVariable String start,
                                          @Parameter(description = "Fecha Fin DD/MM/YYYY", required = true) @PathVariable String end,
                                          HttpServletRequest request) {

        ResponseEntity<?> responseEntity = setGetRequest(request);

        if (hash.contains("/")) {
            try {
                hash = URLEncoder.encode(hash, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        start = formatDate(start);

        end = formatDate(end);

        movementsUrl = movementsUrl.replace("account_number", hash);
        movementsUrl = movementsUrl.replace("start", start);
        movementsUrl = movementsUrl.replace("end", end);

        logger.info("HASH: " + hash);
        logger.info("fecha de inicio: " + start);
        logger.info("fecha fin: " + end);

        if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
            MovimientosResponse movimientosResponse = null;
            try {
                movimientosResponse = restTemplate.exchange(movementsUrl,
                        HttpMethod.GET, httpEntity, MovimientosResponse.class).getBody();
            } catch (Exception ex) {
                logger.info(ex.getMessage());
                return new ResponseEntity<>(new ErrorResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()),"HTTP REQUEST ERROR."), HttpStatus.BAD_REQUEST);
            }
            if (movimientosResponse == null) {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setError(Messages.MOVEMENTS_EMPTY.getError());
                errorResponse.setErrorDescription(Messages.MOVEMENTS_EMPTY.getErrorDescription());
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            if (movimientosResponse.getMovimientos().length == 0) {
                return new ResponseEntity<>(new GenericResponse("No existen movimientos en las fechas consultadas."), HttpStatus.NOT_FOUND);
            }

            Movimientos movimientos = new Movimientos();

            movimientos.setCuenta(movimientosResponse.getCabecera().getCuenta());
            movimientos.setSaldoMesAnterior(movimientosResponse.getCabecera().getSaldoMesAnterior());
            movimientos.setSaldoContable(movimientosResponse.getCabecera().getSaldoContable());
            movimientos.setSaldoRetenido(movimientosResponse.getCabecera().getSaldoRetenido());
            movimientos.setSaldoBloqueado(movimientosResponse.getCabecera().getSaldoBloqueado());
            movimientos.setSaldoDisponible(movimientosResponse.getCabecera().getSaldoDisponible());
            movimientos.setSaldoInicial(movimientosResponse.getCabecera().getSaldoInicial());
            movimientos.setMontoSobregiro(movimientosResponse.getCabecera().getMontoSobregiro());
            movimientos.setMoneda(movimientosResponse.getCabecera().getMoneda());
            movimientos.setFuncionario(movimientosResponse.getCabecera().getFuncionario());
            movimientos.setRetenidoDia(movimientosResponse.getCabecera().getRetenidoDia());
            movimientos.setRetenido24(movimientosResponse.getCabecera().getRetenido24());
            movimientos.setRetenido48(movimientosResponse.getCabecera().getRetenido48());
            movimientos.setRetenidoMas48(movimientosResponse.getCabecera().getRetenidoMas48());
            movimientos.setTipoOperacion(movimientosResponse.getCabecera().getTipoOperacion());
            movimientos.setFechaInicial(movimientosResponse.getCabecera().getFechaInicial());
            movimientos.setFechaFin(movimientosResponse.getCabecera().getFechaFin());
            movimientos.setDepTauserEfe(movimientosResponse.getCabecera().getDepTauserEfe());
            movimientos.setDepTauserChq(movimientosResponse.getCabecera().getDepTauserChq());
            movimientos.setHash(hash);

            try {
                movimientos = movimientosRepository.save(movimientos);
            } catch (Exception ex) {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setError(ex.getCause().getMessage());
                errorResponse.setErrorDescription(ex.getMessage());
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            List<MovimientosDetalle> movimientosDetalleList = new ArrayList<>();
            int posicion = 1;
            for (Detalle detalle : movimientosResponse.getMovimientos()) {

                if (detalle.getTipoDeMovimiento().equalsIgnoreCase("Ingreso")
                    && detalle.getTipoDetalle().contains("SIPAP")) {
                    MovimientosDetalle movimientosDetalle = new MovimientosDetalle();
                    movimientosDetalle.setIdMovimientosFk(movimientos.getIdMovimientosPk());
                    movimientosDetalle.setPosicion(posicion);
                    movimientosDetalle.setFechaContable(detalle.getFechaContable());
                    movimientosDetalle.setHora(detalle.getHora());
                    movimientosDetalle.setFecha(detalle.getFecha());
                    movimientosDetalle.setTransaccion(new BigDecimal(detalle.getTransaccion()));
                    movimientosDetalle.setTipoDeMovimiento(detalle.getTipoDeMovimiento());
                    movimientosDetalle.setMontoCredito(detalle.getMontoCredito());
                    movimientosDetalle.setMontoDebito(detalle.getMontoDebito());
                    movimientosDetalle.setMonto(detalle.getMonto());
                    movimientosDetalle.setConcepto(detalle.getConcepto());
                    movimientosDetalle.setSerie(new BigDecimal(detalle.getSerie()));
                    movimientosDetalle.setComprobante(detalle.getComprobante());
                    movimientosDetalle.setComprobante(detalle.getComprobante());
                    movimientosDetalle.setUsuario(detalle.getUsuario());
                    movimientosDetalle.setOrigen(detalle.getOrigen());
                    movimientosDetalle.setSiglasSucursal(detalle.getSiglasSucursal());
                    movimientosDetalle.setTipoDetalle(detalle.getTipoDetalle());
                    movimientosDetalle.setIdReferenciaDetalle(detalle.getIdReferenciaDetalle());
                    movimientosDetalle.setLocation(detalle.getLocation());
                    movimientosDetalleList.add(movimientosDetalle);
                    posicion += 1;
                }

            }
            try {
                movimientosDetalleList = (List<MovimientosDetalle>) movimientosDetalleRepository.saveAll(movimientosDetalleList);
            } catch (Exception ex) {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setError(ex.getCause().getMessage());
                errorResponse.setErrorDescription(ex.getMessage());
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            // TODO: 17/04/2024 implementar la persistencia de los datos para la taba 'referencia_detalle'
            for (MovimientosDetalle md : movimientosDetalleList) {
                ReferenciaDetalle referenciaDetalle = new ReferenciaDetalle();
                try {
                    movimientosResponse = restTemplate.exchange(movementsUrl,
                            HttpMethod.GET, httpEntity, MovimientosResponse.class).getBody();
                } catch (Exception ex) {
                    logger.info(ex.getMessage());
                    return new ResponseEntity<>(new ErrorResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()),"HTTP REQUEST ERROR."), HttpStatus.BAD_REQUEST);
                }

            }

            GetMovementResponse getMovementResponse = new GetMovementResponse();
            getMovementResponse.setCuenta(movimientos.getCuenta());
            getMovementResponse.setMoneda(movimientos.getMoneda());
            getMovementResponse.setTipoOperacion(movimientos.getTipoOperacion());
            getMovementResponse.setFechaInicial(movimientos.getFechaInicial());
            getMovementResponse.setFechaFin(movimientos.getFechaFin());

            MovimientosDetalleTO [] movimientosDetalleTOList = new MovimientosDetalleTO[movimientosDetalleList.size()];

            int i = 0;

            for (MovimientosDetalle detalle : movimientosDetalleList) {

                MovimientosDetalleTO movimientosDetalleTO = new MovimientosDetalleTO();
                movimientosDetalleTO.setComprobante(detalle.getComprobante());
                movimientosDetalleTO.setFechaContable(detalle.getFechaContable());
                movimientosDetalleTO.setConcepto(detalle.getConcepto());
                movimientosDetalleTO.setTipoDetalle(detalle.getTipoDetalle());
                movimientosDetalleTO.setMonto(detalle.getMonto());
                movimientosDetalleTOList[i] = movimientosDetalleTO;
                i+=1;
            }
            getMovementResponse.setDetalle(movimientosDetalleTOList);
            return new ResponseEntity<>(getMovementResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ErrorResponse(String.valueOf(responseEntity.getStatusCodeValue()),
                    "HTTP REQUEST ERROR."), HttpStatus.BAD_REQUEST);
        }

    }

    private String formatDate(String date) {
        String [] newDate = date.split("-");
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(newDate[2]);
        stringBuilder.append("/");
        stringBuilder.append(newDate[1]);
        stringBuilder.append("/");
        stringBuilder.append(newDate[0]);

        return stringBuilder.toString();
    }


    //Logger logger = Logger.getLogger(MainController.class.getName());

    @Value("${pradera.api.authorization}")
    private String praderaToken;


    @Value("${pradera.uat.url.deposit}")
    private String depositUrl;

    @RequestMapping(value = "/apiFundsOperations/deposit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deposit(@RequestBody DepositRequest depositRequest) {




        if (!headers.isEmpty()) {
            headers.clear();
        }

        headers.add("Authorization", "Bearer " + praderaToken);

        httpEntity = new HttpEntity<>(headers);

/*        DepositResponse depositResponse = restTemplate.exchange(depositUrl,
                HttpMethod.GET, httpEntity, DepositResponse.class).getBody();*/


        ObjectMapper objectMapper = new ObjectMapper();

        // Define the request body you want to send
        String requestBody = null; // This is just an example JSON string
        try {
            requestBody = objectMapper.writeValueAsString(depositRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Set headers if needed, such as Content-Type
        //HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create a request entity with the request body and headers
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);


        // Make the POST request with RestTemplate
        ResponseEntity<DepositResponse> responseEntity = restTemplate.exchange(depositUrl, HttpMethod.POST, requestEntity, DepositResponse.class);;

        // Retrieve the response body and status code
        DepositResponse responseBody = responseEntity.getBody();
        HttpStatus statusCode = responseEntity.getStatusCode();

        // Handle the response as needed
        System.out.println("Response Body: " + responseEntity.toString());
        System.out.println("Status Code: " + statusCode);

        String r ="";
        try {
            r = objectMapper.writeValueAsString(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    private ResponseEntity<?> setGetRequest(HttpServletRequest request) {

        ResponseEntity<?> responseEntity = getAccessToken(request);
        if (responseEntity.getStatusCodeValue() != HttpStatus.OK.value()) {
            return responseEntity;
        }
        if (!headers.isEmpty()) {
            headers.clear();
        }
        accessToken = (AccessToken) responseEntity.getBody();
        headers.add("Authorization", "Bearer " + accessToken.getAccessToken());
        headers.add("Subscription-key", subscriptionKey);
        httpEntity = new HttpEntity<>(headers);
        return responseEntity;
    }

}