package eus.birt.dam.ad05.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	// Redirige la raiz de la aplicacion al listado de cuadernos
	@GetMapping("/")
	public String raiz() {
		return "redirect:/cuadernos";
	}

}
