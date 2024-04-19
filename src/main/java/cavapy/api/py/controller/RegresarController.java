package cavapy.api.py.controller;

import cavapy.api.py.model.FiltrosDeBusqueda;
import cavapy.api.py.responses.BuscarResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RegresarController {

    @RequestMapping(value = "/regresarHome")
    public String regresarHome(RedirectAttributes redirectAttributes) {
        FiltrosDeBusqueda filtrosDeBusqueda = new FiltrosDeBusqueda();
        redirectAttributes.addFlashAttribute("filtrosDeBusqueda", filtrosDeBusqueda);
        List<BuscarResponse> buscarResponses = new ArrayList<>();
        redirectAttributes.addFlashAttribute("buscarResponses", buscarResponses);
        return "redirect:/home";
    }
}
