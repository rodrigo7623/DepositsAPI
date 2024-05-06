package cavapy.api.py.continental.controller;

import cavapy.api.py.continental.entity.CuentaBancaria;
import cavapy.api.py.continental.model.FiltrosDeBusqueda;
import cavapy.api.py.continental.repository.CuentaBancariaRepository;
import cavapy.api.py.continental.responses.BuscarResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    List<CuentaBancaria> bankAccounts = new ArrayList<>();

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
       /* List<CuentaBancaria> bankAccounts = (List<CuentaBancaria>) cuentaBancariaRepository.findAll();
        model.addAttribute("accounts",  bankAccounts);*/
        return getString(model, session);
    }

    private String getString(Model model, HttpSession session) {

        List<BuscarResponse> buscarResponses = null;
        FiltrosDeBusqueda filtrosDeBusqueda = new FiltrosDeBusqueda();

        if (model.getAttribute("accounts")!=null) {
            bankAccounts = (List<CuentaBancaria>) model.getAttribute("accounts");
        } else {
            bankAccounts = cuentaBancariaRepository.findByEstado("AC").orElse(null);
        }

        if (model.getAttribute("buscarResponses") != null) {
            buscarResponses = (List<BuscarResponse>) model.getAttribute("buscarResponses");
        }

        if (model.getAttribute("filtrosDeBusqueda") != null) {
            filtrosDeBusqueda = (FiltrosDeBusqueda) model.getAttribute("filtrosDeBusqueda");
        }

        model.addAttribute("accounts",  bankAccounts);
        model.addAttribute("filtrosDeBusqueda", filtrosDeBusqueda);
        model.addAttribute("buscarResponses", buscarResponses);
        if (session.getAttribute("userName") != null) {
            model.addAttribute("userName", session.getAttribute("userName"));
        }

        return "home";
    }

    @PostMapping("/home")
    public String homePost(Model model, HttpSession session) {
        return getString(model, session);
    }

    @PostMapping("/save-session")
    public String saveSession(String name, HttpSession session) {
        session.setAttribute("userName", name);
        return "redirect:/home";
    }

}