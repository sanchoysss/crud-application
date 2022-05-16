package by.mironovich.application.crudapplication.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/error")
    public String error(Model model, HttpStatus status) {
        System.out.println(status.value());
        model.addAttribute("errorNum", status.value());
        model.addAttribute("errorText", status.toString());
        return "error";
    }
}
