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
import eus.birt.dam.ad05.entity.Nota;
import eus.birt.dam.ad05.service.CuadernoService;
import eus.birt.dam.ad05.service.NotaService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/cuadernos/{cuadernoId}/notas")
public class NotaController {

	private final NotaService notaService;
	private final CuadernoService cuadernoService;

	public NotaController(NotaService notaService, CuadernoService cuadernoService) {
		this.notaService = notaService;
		this.cuadernoService = cuadernoService;
	}

	// ------- LISTAR --------
	// Read: listar las notas de un cuaderno
	@GetMapping
	public String list(@PathVariable Long cuadernoId, Model model) {
		Cuaderno cuaderno = cuadernoService.buscarPorId(cuadernoId)
				.orElseThrow(() -> new RuntimeException("Cuaderno no encontrado: " + cuadernoId));
		model.addAttribute("cuaderno", cuaderno);
		model.addAttribute("notas", notaService.listarPorCuaderno(cuadernoId));
		return "notas/list";
	}

	// ------- CREAR --------
	// Create (paso 1/2): mostrar el formulario de alta vacio (GET)
	@GetMapping("/nueva")
	public String newNota(@PathVariable Long cuadernoId, Model model) {

		// anadimos nota al modelo para la vista
		model.addAttribute("nota", new Nota());
		return "notas/form";
	}

	// Create (paso 2/2): procesar el formulario de alta (POST)
	@PostMapping("/nueva")
	public String create(@PathVariable Long cuadernoId, @Valid @ModelAttribute Nota nota, BindingResult result) {

		if (result.hasErrors()) {
			return "notas/form";
		}

		// creamos la nota nueva
		notaService.crear(nota, cuadernoId);
		return "redirect:/cuadernos/" + cuadernoId + "/notas";
	}

	// ------- EDITAR --------
	// Update (paso 1/2): mostrar el formulario con los datos actuales (GET)
	@GetMapping("/{id}/editar")
	public String editForm(@PathVariable Long cuadernoId, @PathVariable Long id, Model model) {

		// buscamos la nota a editar por su id
		Nota nota = notaService.buscarPorId(id).orElseThrow(() -> new RuntimeException("Nota no encontrada: " + id));

		// si la nota no pertenece a ese cuaderno
		if (!nota.getCuaderno().getId().equals(cuadernoId)) {
			throw new RuntimeException("La nota no pertenece a ese cuaderno");
		}

		// anadimos nota al modelo para la vista
		model.addAttribute("nota", nota);
		return "notas/form";
	}

	// Update (paso 2/2): procesar la edicion (POST)
	@PostMapping("/{id}/editar")
	public String update(@PathVariable Long cuadernoId, @PathVariable Long id, @Valid @ModelAttribute Nota nota,
			BindingResult result) {

		if (result.hasErrors()) {
			return "notas/form";
		}

		// buscamos la nota a actualizar por su id
		Nota notaActual = notaService.buscarPorId(id)
				.orElseThrow(() -> new RuntimeException("Nota no encontrada: " + id));

		// si la nota no pertenece a este cuaderno
		if (!notaActual.getCuaderno().getId().equals(cuadernoId)) {
			throw new RuntimeException("La nota no pertenece a ese cuaderno");
		}

		notaService.actualizar(id, nota, cuadernoId);
		return "redirect:/cuadernos/" + cuadernoId + "/notas";
	}

	// ------- ELIMINAR --------
	// Delete: eliminar una nota
	@PostMapping("/{id}/eliminar")
	public String delete(@PathVariable Long cuadernoId, @PathVariable Long id) {

		// buscamos la nota a eliminar por su id
		Nota nota = notaService.buscarPorId(id)
				.orElseThrow(() -> new RuntimeException("Nota no encontrada: " + id));

		// si la nota no pertenece a ese cuaderno
		if (!nota.getCuaderno().getId().equals(cuadernoId)) {
			throw new RuntimeException("La nota no pertenece a ese cuaderno");
		}

		notaService.eliminar(id);
		return "redirect:/cuadernos/" + cuadernoId + "/notas";
	}

}
