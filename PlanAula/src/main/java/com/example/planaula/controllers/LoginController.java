package com.example.planaula.controllers;

import com.example.planaula.Dto.UsuarioDTO;
import com.example.planaula.services.UserService;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("newUser", new UsuarioDTO());
        LocalDate localDate = LocalDate.now();
        model.addAttribute("anio", localDate.getYear());
        return "login";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UsuarioDTO usuarioDTO) {
        String sts = "";
        try{
            userService.createUser(usuarioDTO);
            sts = "Success";
        }catch (Exception e){
            System.out.println(e.getMessage());
            sts = "Error";
        }
        return "redirect:/login?sts=" + sts;
    }
}
