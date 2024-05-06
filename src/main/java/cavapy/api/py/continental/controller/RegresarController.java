package cavapy.api.py.continental.controller;

import cavapy.api.py.continental.model.FiltrosDeBusqueda;
import cavapy.api.py.continental.responses.BuscarResponse;
import org.springframework.stereotype.Controller;
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
        List<BuscarResponse> buscarResponses = null;
        redirectAttributes.addFlashAttribute("buscarResponses", buscarResponses);
        return "redirect:/home";
    }
}
