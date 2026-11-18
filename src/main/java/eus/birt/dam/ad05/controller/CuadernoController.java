package eus.birt.dam.ad05.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import eus.birt.dam.ad05.entity.Cuaderno;
import eus.birt.dam.ad05.service.CuadernoService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/cuadernos")
public class CuadernoController {

	private final CuadernoService cuadernoService;

	public CuadernoController(CuadernoService cuadernoService) {
		this.cuadernoService = cuadernoService;
	}

	// ------- LISTAR --------
	// Read: listar todos los cuadernos
	@GetMapping
	public String list(Model model) {
		// anadimos cuadernos al modelo para la vista
		model.addAttribute("cuadernos", cuadernoService.listar());
		return "cuadernos/list";
	}

	// ------- CREAR --------
	// Create (paso 1/2): mostrar el formulario de alta vacio (GET)
	@GetMapping("/nuevo")
	public String newCuaderno(Model model) {
		// anadimos cuaderno vacio al modelo para la vista
		model.addAttribute("cuaderno", new Cuaderno());
		return "cuadernos/form";
	}

	// Create (paso 2/2): procesar el formulario de alta (POST)
	@PostMapping("/nuevo")
	public String create(@Valid @ModelAttribute Cuaderno cuaderno, BindingResult result) {
		if (result.hasErrors()) {
			return "cuadernos/form";
		}
		// creamos el cuaderno nuevo
		cuadernoService.crear(cuaderno);
		return "redirect:/cuadernos";
	}

	// ------- EDITAR --------
	// Update (paso 1/2): mostrar el formulario con los datos actuales (GET)
	@GetMapping("/{id}/editar")
	public String editForm(@PathVariable Long id, Model model) {
		// buscamos el cuaderno a editar por su id
		Cuaderno cuaderno = cuadernoService.buscarPorId(id)
				.orElseThrow(() -> new RuntimeException("Cuaderno no encontrado: " + id));
		// anadimos cuaderno al modelo para la vista
		model.addAttribute("cuaderno", cuaderno);
		return "cuadernos/form";
	}

	// Update (paso 2/2): procesar la edicion (POST)
	@PostMapping("/{id}/editar")
	public String update(@PathVariable Long id, @Valid @ModelAttribute Cuaderno cuaderno, BindingResult result) {
		if (result.hasErrors()) {
			return "cuadernos/form";
		}
		cuadernoService.actualizar(id, cuaderno);
		return "redirect:/cuadernos";
	}

	// ------- ELIMINAR --------
	// Delete: eliminar un cuaderno (y sus notas, por cascada)
	@PostMapping("/{id}/eliminar")
	public String delete(@PathVariable Long id) {
		cuadernoService.eliminar(id);
		return "redirect:/cuadernos";
	}

}
