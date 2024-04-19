package cavapy.api.py.controller;

import cavapy.api.py.entity.CuentaBancaria;
import cavapy.api.py.model.FiltrosDeBusqueda;
import cavapy.api.py.repository.CuentaBancariaRepository;
import cavapy.api.py.responses.BuscarResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private final CuentaBancariaRepository cuentaBancariaRepository;

    @Autowired
    public HomeController(CuentaBancariaRepository cuentaBancariaRepository) {
        this.cuentaBancariaRepository = cuentaBancariaRepository;
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        List<CuentaBancaria> bankAccounts = (List<CuentaBancaria>) cuentaBancariaRepository.findAll();
        model.addAttribute("accounts",  bankAccounts);
        return "home";
    }

    @PostMapping("/home")
    public String homePost(Model model, HttpSession session) {
        List<CuentaBancaria> bankAccounts = (List<CuentaBancaria>) cuentaBancariaRepository.findAll();
        List<BuscarResponse> buscarResponses = new ArrayList<>();
        FiltrosDeBusqueda filtrosDeBusqueda = new FiltrosDeBusqueda();
        model.addAttribute("accounts",  bankAccounts);
        model.addAttribute("filtrosDeBusqueda", filtrosDeBusqueda);
        model.addAttribute("buscarResponses", buscarResponses);

        if (session.getAttribute("userName") != null) {
            model.addAttribute("userName", session.getAttribute("userName"));
        }

        return "home";
    }

    @PostMapping("/save-session")
    public String saveSession(String name, HttpSession session) {
        session.setAttribute("userName", name);
        return "redirect:/home";
    }

}