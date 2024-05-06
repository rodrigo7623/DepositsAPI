package cavapy.api.py.continental.controller;

import cavapy.api.py.continental.repository.CuentaBancariaRepository;
import cavapy.api.py.continental.util.AccessToken;
import cavapy.api.py.continental.responses.BuscarResponse;
import cavapy.api.py.continental.repository.BuscarResponseRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class RecaudarController {


    private final CuentaBancariaRepository cuentaBancariaRepository;

    @Autowired
    public RecaudarController(CuentaBancariaRepository cuentaBancariaRepository, MainController mainController, BuscarResponseRepository buscarResponseRepository) {
        this.cuentaBancariaRepository = cuentaBancariaRepository;
        this.mainController = mainController;
        this.buscarResponseRepository = buscarResponseRepository;
    }

    final
    MainController mainController;
    final
    BuscarResponseRepository buscarResponseRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    public RecaudarController(CuentaBancariaRepository cuentaBancariaRepository, BuscarResponseRepository buscarResponseRepository, MainController mainController) {
        this.cuentaBancariaRepository = cuentaBancariaRepository;
        this.buscarResponseRepository = buscarResponseRepository;
        this.mainController = mainController;
    }

    @PostMapping("/recaudar")
    public String recaudar(RedirectAttributes redirectAttributes, HttpServletRequest request) {
        List<BuscarResponse> buscarResponseList;
        ResponseEntity<?> accessTokeResponse = mainController.getAccessToken();
        accessTokeResponse = null;
        if (accessTokeResponse == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al generar el token de acceso.");
            /*List<CuentaBancaria> bankAccounts = (List<CuentaBancaria>) cuentaBancariaRepository.findAll();
            List<String> data = new ArrayList<>();

            Filtros filtros = new Filtros();

            String selectedAccount = "";

            String startDate = "";

            String endDate = "";

            String resultsForPage = "";

            model.addAttribute("accounts",  bankAccounts);
            model.addAttribute("selectedAccount", selectedAccount);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("resultsForPage", resultsForPage);
            model.addAttribute("data", data);
            model.addAttribute(filtros);*/
            return "redirect:/home";
        }
        try {
            AccessToken accessToken = objectMapper.readValue(accessTokeResponse.getBody().toString(), AccessToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
