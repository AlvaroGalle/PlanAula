package com.example.planaula.security;

import com.example.planaula.dto.CentroDTO;
import com.example.planaula.services.CentrosService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("permisoService")
public class PermisoService {

	public final CentrosService centrosService;

    public PermisoService(CentrosService centrosService) {
        this.centrosService = centrosService;
    }

    public boolean tieneAccesoCentroFromPath(Authentication auth, Object handler) {
	    if (auth == null || !auth.isAuthenticated()) return false;
	    Object principal = auth.getPrincipal();
	    if (!(principal instanceof CustomUserDetails user)) return false;

	    RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
	    if (attrs instanceof ServletRequestAttributes sra) {
	        HttpServletRequest req = sra.getRequest();
	        String path = req.getRequestURI();
	        try {
	            int centroId = Integer.parseInt(path.split("/")[1]);
				List<CentroDTO> centros = centrosService.obtenerCentrosPorUsuario(centroId);
				Set<Integer> centrosPermitidos = centros.stream()
						.map(CentroDTO::getId)
						.collect(Collectors.toSet());
				return centrosPermitidos.contains(centroId);
	        } catch (Exception e) {
	            return false;
	        }
	    }
	    return false;
	}

}
