package com.example.planaula;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/planaula")
public class loginController {

    @GetMapping("/")
    public String hello(){
        return "hello";
    }
}
