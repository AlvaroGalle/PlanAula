package com.example.planaula.controllers;

import com.example.planaula.services.AsignaturasService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Connection;
import java.sql.DriverManager;

@Controller
@RequestMapping("/planaula")
public class loginController {

    private final AsignaturasService asignaturasService;

    public loginController(AsignaturasService asignaturasService) {
        this.asignaturasService = asignaturasService;
    }

    @GetMapping("")
    public String hello(Model model) {
        String url = "jdbc:ucanaccess://C:/Users/51178456/Desktop/workspace/PlanAula/Access/PNS_Fesd2425.accdb";
        try (Connection conn = DriverManager.getConnection(url)) {
            model.addAttribute("asignaturas", asignaturasService.findAllAsignaturas());
            System.out.println("Conexión exitosa");
        } catch (Exception e) {
            e.getMessage();
        }
        return "hello";
    }
}
