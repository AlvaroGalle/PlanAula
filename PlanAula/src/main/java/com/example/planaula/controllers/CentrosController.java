package com.example.planaula.controllers;
import com.example.planaula.security.CustomUserDetails;
import com.example.planaula.services.*;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/centros")
public class CentrosController {

    public final CentrosService centrosService;

    public CentrosController(CentrosService centrosService) {
    	this.centrosService = centrosService;
    }

    @GetMapping("")
    public String centros(@AuthenticationPrincipal CustomUserDetails user,
    					  @RequestParam(name="curso", required = false, defaultValue = "0") int curso,
            		      @RequestParam(name="profesor", required = false, defaultValue = "0") int profesor, 
            		   Model model) {
        model.addAttribute("centros", centrosService.obtenerCentrosPorUsuario(user.getId()));
        return "centros";
    }
    
    @PostMapping("/fav")
    public String togglefav(@AuthenticationPrincipal CustomUserDetails user,
    						@RequestParam(name="idCentro", required = false, defaultValue = "0") int idCentro,
            		        @RequestParam(name="fav", required = false, defaultValue = "false") boolean fav) {
        centrosService.modificarCentrosFav(user.getId(), idCentro, fav);
        return "redirect:/centros";
    }
}
