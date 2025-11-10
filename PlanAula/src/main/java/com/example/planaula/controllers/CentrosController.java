package com.example.planaula.controllers;
import com.example.planaula.Dto.CentroDTO;
import com.example.planaula.security.CustomUserDetails;
import com.example.planaula.services.*;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
        model.addAttribute("centroDTO", new CentroDTO());
        return "centros";
    }
    
    @PostMapping("")
    public String accionesCentro(@AuthenticationPrincipal CustomUserDetails user,
    						  @ModelAttribute(name="centroDTO") CentroDTO centroDTO) {
    	 byte[] logoBytes = null;
    	    if (centroDTO.getLogo() != null && !centroDTO.getLogo().isEmpty()) {
    	        try {
					logoBytes = centroDTO.getLogo().getBytes();
				} catch (IOException e) {
					e.printStackTrace();
				}
    	    }
    	 if(centroDTO.getId() == null || centroDTO.getId() == 0) {
    		 centrosService.crearCentro(user.getId(), centroDTO, logoBytes);
    	 }else {
    		 centrosService.editarCentro(centroDTO, logoBytes);
    	 }
        
        return "redirect:/centros";
    }
    
    @PostMapping("/fav")
    public String togglefav(@AuthenticationPrincipal CustomUserDetails user,
    						@RequestParam(name="idCentro", required = false, defaultValue = "0") int idCentro,
            		        @RequestParam(name="fav", required = false, defaultValue = "false") boolean fav) {
        centrosService.modificarCentrosFav(user.getId(), idCentro, fav);
        return "redirect:/centros";
    }
    
    @GetMapping("/{idCentro}/logo")
    @ResponseBody
    public ResponseEntity<byte[]> obtenerLogo(@PathVariable(name="idCentro") Integer idCentro) {
        byte[] imagen = centrosService.obtenerLogoPorId(idCentro);

        if (imagen == null || imagen.length == 0) {
            ClassPathResource imgFile = new ClassPathResource("static/images/centro_sin_logo.png");
            try {
				imagen = StreamUtils.copyToByteArray(imgFile.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }

    @GetMapping("{idCentro}")
    @ResponseBody
    public ResponseEntity<CentroDTO> obtenerCentro(@PathVariable(name="idCentro") Integer idCentro) {
        CentroDTO centro = centrosService.obtenerCentroPorId(idCentro);

        if (centro == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(centro);
    }

}
