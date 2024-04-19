package cavapy.api.py.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class ConsultarController {

    @RequestMapping(value = "/consultar", method = RequestMethod.POST)
    private String consultar(@RequestParam("fechaInicio") String fechaInicio,
                                               @RequestParam("fechaFin") String fechaFin,
                                                Model model) {
        return "redirect:/home";
    }
}
