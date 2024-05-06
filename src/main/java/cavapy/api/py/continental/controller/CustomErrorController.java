package cavapy.api.py.continental.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

@ControllerAdvice
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleError(Model model, HttpRequestMethodNotSupportedException ex) {

        model.addAttribute("errorDetails", ex.getMessage());
        return "error"; // Esto redirigirá a la página error.html en Thymeleaf
    }

}
