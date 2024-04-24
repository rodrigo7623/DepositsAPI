package cavapy.api.py.controller;

import cavapy.api.py.entity.CuentaBancaria;
import cavapy.api.py.entity.Movimientos;
import cavapy.api.py.entity.MovimientosDetalle;
import cavapy.api.py.entity.ReferenciaDetalle;
import cavapy.api.py.repository.CuentaBancariaRepository;
import cavapy.api.py.repository.MovimientosDetalleRepository;
import cavapy.api.py.repository.MovimientosRepository;
import cavapy.api.py.repository.ReferenciaDetalleRepository;
import cavapy.api.py.responses.*;
import cavapy.api.py.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
public class MainController {

    Logger logger = Logger.getLogger(MainController.class.getName());

    private final HttpHeaders headers = new HttpHeaders();

    HttpEntity<?> httpEntity = null;

    @Autowired
    public MainController(RestTemplate restTemplate, CuentaBancariaRepository cuentaBancariaRepository,
                          MovimientosRepository movimientosRepository,
                          MovimientosDetalleRepository movimientosDetalleRepository, ReferenciaDetalleRepository referenciaDetalleRepository) {
        this.restTemplate = restTemplate;
        this.cuentaBancariaRepository = cuentaBancariaRepository;
        this.movimientosRepository = movimientosRepository;
        this.movimientosDetalleRepository = movimientosDetalleRepository;
        this.referenciaDetalleRepository = referenciaDetalleRepository;
    }

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public final CuentaBancariaRepository cuentaBancariaRepository;

    private final MovimientosRepository movimientosRepository;

    private final ReferenciaDetalleRepository referenciaDetalleRepository;

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

    @Value("${pradera.api.authorization}")
    private String praderaToken;


    @Value("${pradera.uat.url.deposit}")
    private String depositUrl;

    @RequestMapping(value = "/getAccessToken", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAccessToken() {
        logger.info("POST Request: " + accessTokenUrl);
        if (!headers.isEmpty()) {
            headers.clear();
        }
        headers.add("Usuario", usuario);
        headers.add("Password", password);
        headers.add("RUC", ruc);
        headers.add("Subscription-key", subscriptionKey);
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(accessTokenUrl, HttpMethod.POST, httpEntity, String.class);
        ApiResponse apiResponse = new ApiResponse();
        try {
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                AccessTokenResponse accessTokenResponse = objectMapper.readValue(responseEntity.getBody(), AccessTokenResponse.class);
                return new ResponseEntity<>(new AccessToken(accessTokenResponse.getAccessToken()), HttpStatus.OK);
            } else if (responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value()) {
                apiResponse = objectMapper.readValue(responseEntity.getBody(), ApiResponse.class);
                return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
            } else if (responseEntity.getStatusCodeValue() == HttpStatus.GATEWAY_TIMEOUT.value()) {
                apiResponse.setStatusCode(HttpStatus.GATEWAY_TIMEOUT.value());
                apiResponse.setMessage(HttpStatus.GATEWAY_TIMEOUT.getReasonPhrase());
                return new ResponseEntity<>(apiResponse, HttpStatus.GATEWAY_TIMEOUT);
            } else {
                ErrorResponse errorResponse = objectMapper.readValue(responseEntity.getBody(), ErrorResponse.class);
                apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
                apiResponse.setMessage(errorResponse.getErrorDescription());
                return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
            }
        } catch (JsonProcessingException ex) {
            logger.severe(ex.getMessage());
            apiResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            apiResponse.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getBankAccount", method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBankAccount() {
        ResponseEntity<?> responseEntity = setGetRequest();
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

    private final MovimientosDetalleRepository movimientosDetalleRepository;

    @RequestMapping(value = "/getMovements/{hash}/{start}/{end}", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMovements(@Parameter(description = "Cuenta hash", required = true)
                                          @PathVariable String hash,
                                          @Parameter(description = "Fecha Inicio en formato DD/MM/YYYY", required = true)
                                          @PathVariable String start,
                                          @Parameter(description = "Fecha Fin DD/MM/YYYY", required = true)
                                          @PathVariable String end) {

        ResponseEntity<?> responseEntity = setGetRequest();

        if (hash.contains("/")) {
            try {
                hash = URLEncoder.encode(hash, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                logger.severe(e.getMessage());
                return new ResponseEntity<>(new ErrorResponse(e.getCause().getMessage(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        start = formatDate(start);
        end = formatDate(end);
        movementsUrl = formatMovementsUrl(hash, start, end);

        logger.info("{hash: " + hash + ", fecha de inicio: " + start + ", fecha fin: " + end + "}");

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            return badRequestHandler(responseEntity.getStatusCode().getReasonPhrase(), responseEntity.getStatusCode());
        }

        ResponseEntity<?> responseEntityMovimientos = getMovimientosResponse();
        
        if (responseEntityMovimientos == null) {
            return badRequestHandler(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (!responseEntityMovimientos.getStatusCode().is2xxSuccessful()) {
            return responseEntityMovimientos;
        }

        MovimientosResponse movimientosResponse = (MovimientosResponse) responseEntityMovimientos.getBody();

        if (movimientosResponse == null) {
            return badRequestHandler(Messages.MOVEMENTS_EMPTY.getErrorDescription(), HttpStatus.NOT_FOUND);
        }

        if (movimientosResponse.getMovimientos().length == 0) {
            return badRequestHandler(Messages.MOVEMENTS_EMPTY.getErrorDescription(), HttpStatus.NOT_FOUND);
        }

        Movimientos movimientos = saveMovimientos(movimientosResponse, hash);

        if (movimientos == null) {
            return badRequestHandler(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<MovimientosDetalle> movimientosDetalleList = saveMovimientosDetalle(movimientosResponse, movimientos);

        if (movimientosDetalleList == null || movimientosDetalleList.isEmpty()) {
            return badRequestHandler(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<ReferenciaDetalle> referenciaDetalle = saveReferenciaDetalle(movimientosDetalleList);

        if (referenciaDetalle.isEmpty()) {
            return badRequestHandler(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        MovementRequestResult movementRequestResult = new MovementRequestResult();
        movementRequestResult.setCuenta(movimientos.getCuenta());
        movementRequestResult.setMoneda(movimientos.getMoneda());
        movementRequestResult.setTipoOperacion(movimientos.getTipoOperacion());
        movementRequestResult.setFechaInicial(movimientos.getFechaInicial());
        movementRequestResult.setFechaFin(movimientos.getFechaFin());

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
        movementRequestResult.setDetalle(movimientosDetalleTOList);
        return new ResponseEntity<>(movementRequestResult, HttpStatus.OK);
       
    }

    private List<ReferenciaDetalle> saveReferenciaDetalle(List<MovimientosDetalle> movimientosDetalleList) {
        List<ReferenciaDetalle> referenciaDetalleNotSaved = new ArrayList<>();
        List<ReferenciaDetalle> referenciaDetalleInserted = new ArrayList<>();
        for (MovimientosDetalle movimientosDetalle : movimientosDetalleList) {
            ReferenciaDetalle referenciaDetalle = new ReferenciaDetalle();
            ResponseEntity<?> responseEntityToken = setGetRequest();
            if (responseEntityToken.getStatusCode().is2xxSuccessful()) {
                try {
                    referenceUrl = referenceUrl.replace("referencia", movimientosDetalle.getIdReferenciaDetalle());
                    logger.info("GET request: " + referenceUrl);
                    logger.info("Reference requested:");
                    logger.info(referenciaDetalle.toString());
                    ResponseEntity<String> responseEntity = restTemplate.exchange(referenceUrl,
                            HttpMethod.GET, httpEntity, String.class);
                    if (responseEntity.getStatusCode().is2xxSuccessful()) {
                        referenciaDetalleRepository.save(referenciaDetalle);
                        referenciaDetalleInserted.add(referenciaDetalle);
                    } else {
                        referenciaDetalleNotSaved.add(referenciaDetalle);
                    }
                } catch (Exception ex) {
                    logger.severe(ex.getMessage());
                    referenciaDetalleNotSaved.add(referenciaDetalle);
                }
            } else {
                referenciaDetalleNotSaved.add(referenciaDetalle);
            }
        }
        if (!referenciaDetalleNotSaved.isEmpty()) {
            logger.warning("References not saved:");
            for (ReferenciaDetalle rd : referenciaDetalleNotSaved) {
                logger.warning(rd.toString());
            }
        }
        return referenciaDetalleInserted;
    }

    private List<MovimientosDetalle> saveMovimientosDetalle(MovimientosResponse movimientosResponse, Movimientos movimientos) {
        List<MovimientosDetalle> movimientosDetalleList = new ArrayList<>();
        int position = 1;
        for (Detalle detalle : movimientosResponse.getMovimientos()) {
            if (detalle.getTipoDeMovimiento().equalsIgnoreCase("Ingreso")
                    && detalle.getTipoDetalle().contains("SIPAP")) {
                MovimientosDetalle movimientosDetalle = new MovimientosDetalle();
                movimientosDetalle.setIdMovimientosFk(movimientos.getIdMovimientosPk());
                movimientosDetalle.setPosicion(position);
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
                position += 1;
            }
        }
        try {
            return (List<MovimientosDetalle>) movimientosDetalleRepository.saveAll(movimientosDetalleList);
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
            return null;
        }
    }

    private Movimientos saveMovimientos(MovimientosResponse movimientosResponse, String hash) {
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
            return movimientosRepository.save(movimientos);
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
            return null;
        }
    }

    private ResponseEntity<?> badRequestHandler(String message, HttpStatus httpStatus) {
        return new ResponseEntity<>(new ApiResponse(httpStatus.value(), message), httpStatus);
    }

    private ResponseEntity<?> getMovimientosResponse() {

        try {
            logger.info("POST Request: " + movementsUrl);
            ResponseEntity<String> responseEntity = restTemplate.exchange(movementsUrl,
                    HttpMethod.GET,
                    httpEntity,
                    String.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                MovimientosResponse movimientosResponse = objectMapper.readValue(responseEntity.getBody(), MovimientosResponse.class);
                return new ResponseEntity<>(movimientosResponse, HttpStatus.OK);
            } else if (responseEntity.getStatusCodeValue() == HttpStatus.FORBIDDEN.value()) {
                ApiResponse apiResponse = objectMapper.readValue(responseEntity.getBody(), ApiResponse.class);
                return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
            } else {
                ErrorResponse errorResponse = objectMapper.readValue(responseEntity.getBody(), ErrorResponse.class);
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
            return null;
        }
    }

    private String formatMovementsUrl(String hash, String start, String end) {
        return movementsUrl
                .replace("account_number", hash)
                .replace("start", start)
                .replace("end", end);
    }

    private String formatDate(String date) {
        String [] newDate = date.split("-");
        return newDate[2] +
                "/" +
                newDate[1] +
                "/" +
                newDate[0];
    }

    @RequestMapping(value = "/apiFundsOperations/deposit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deposit(@RequestBody DepositRequest depositRequest) {




        if (!headers.isEmpty()) {
            headers.clear();
        }

        headers.add("Authorization", "Bearer " + praderaToken);

        httpEntity = new HttpEntity<>(headers);


        ObjectMapper objectMapper = new ObjectMapper();

        // Define the request body you want to send
        String requestBody; // This is just an example JSON string
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
        ResponseEntity<DepositResponse> responseEntity = restTemplate.exchange(depositUrl, HttpMethod.POST, requestEntity, DepositResponse.class);

        // Retrieve the response body and status code
        DepositResponse responseBody = responseEntity.getBody();
        HttpStatus statusCode = responseEntity.getStatusCode();

        // Handle the response as needed
        System.out.println("Response Body: " + responseEntity);
        System.out.println("Status Code: " + statusCode);

        String r;
        try {
            r = objectMapper.writeValueAsString(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    private ResponseEntity<?> setGetRequest() {
        ResponseEntity<?> responseEntity = getAccessToken();
        if (responseEntity.getStatusCodeValue() != HttpStatus.OK.value()) {
            return responseEntity;
        }
        if (!headers.isEmpty()) {
            headers.clear();
        }
        AccessToken accessToken = (AccessToken) responseEntity.getBody();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Subscription-key", subscriptionKey);
        httpEntity = new HttpEntity<>(headers);
        return responseEntity;
    }

}