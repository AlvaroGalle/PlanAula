package com.example.planaula.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Service("permisoService")
public class PermisoService {

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
	            return user.getCentrosPermitidos().contains(centroId);
	        } catch (Exception e) {
	            return false;
	        }
	    }
	    return false;
	}

}
