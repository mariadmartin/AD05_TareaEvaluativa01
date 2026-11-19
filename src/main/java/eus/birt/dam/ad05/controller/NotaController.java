package eus.birt.dam.ad05.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import eus.birt.dam.ad05.entity.Cuaderno;
import eus.birt.dam.ad05.entity.Nota;
import eus.birt.dam.ad05.service.CuadernoService;
import eus.birt.dam.ad05.service.NotaService;
import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequestMapping("/notas")
public class NotaController {

	private final NotaService notaService;
	private final CuadernoService cuadernoService;

	public NotaController(NotaService notaService, CuadernoService cuadernoService) {
		this.notaService = notaService;
		this.cuadernoService = cuadernoService;
	}

	// ------- LISTAR --------
	// Read: listar las notas, todas o solo las de un cuaderno concreto (filtro opcional)
	@GetMapping
	public String list(@RequestParam(required = false) Long cuadernoId, Model model) {
		if (cuadernoId != null) {
			Cuaderno cuaderno = cuadernoService.buscarPorId(cuadernoId)
					.orElseThrow(() -> new RuntimeException("Cuaderno no encontrado: " + cuadernoId));
			model.addAttribute("notas", notaService.listarPorCuaderno(cuadernoId));
			model.addAttribute("cuadernoFiltro", cuaderno);
		} else {
			model.addAttribute("notas", notaService.listar());
		}
		return "notas/list";
	}

	// ------- CREAR --------
	// Create (paso 1/2): mostrar el formulario de alta vacio (GET)
	@GetMapping("/nueva")
	public String newNota(Model model, RedirectAttributes redirectAttributes) {
		List<Cuaderno> cuadernos = cuadernoService.listar();
		// si no hay ningun cuaderno, no se puede elegir uno: avisamos y no mostramos el formulario
		if (cuadernos.isEmpty()) {
			redirectAttributes.addFlashAttribute("mensaje", "Debes crear un cuaderno antes de poder anadir una nota.");
			return "redirect:/cuadernos";
		}
		// anadimos nota vacia y la lista de cuadernos (para el select) al modelo para la vista
		model.addAttribute("nota", new Nota());
		model.addAttribute("cuadernos", cuadernos);
		return "notas/form";
	}

	// Create (paso 2/2): procesar el formulario de alta (POST)
	@PostMapping("/nueva")
	public String create(@Valid @ModelAttribute Nota nota, BindingResult result,
			@RequestParam Long cuadernoId, Model model) {

		// comprobamos en el servidor que el cuaderno elegido en el select realmente existe
		boolean cuadernoValido = cuadernoService.buscarPorId(cuadernoId).isPresent();

		if (result.hasErrors() || !cuadernoValido) {
			model.addAttribute("cuadernos", cuadernoService.listar());
			model.addAttribute("cuadernoIdSeleccionado", cuadernoId);
			if (!cuadernoValido) {
				model.addAttribute("errorCuaderno", "El cuaderno seleccionado no existe. Elige uno de la lista.");
			}
			return "notas/form";
		}

		// creamos la nota nueva, asociada al cuaderno elegido
		notaService.crear(nota, cuadernoId);
		return "redirect:/notas";
	}

	// ------- EDITAR --------
	// Update (paso 1/2): mostrar el formulario con los datos actuales (GET)
	@GetMapping("/{id}/editar")
	public String editForm(@PathVariable Long id, Model model) {
		// buscamos la nota a editar por su id
		Nota nota = notaService.buscarPorId(id).orElseThrow(() -> new RuntimeException("Nota no encontrada: " + id));
		// anadimos nota, la lista de cuadernos (para el select) y el cuaderno actual al modelo
		model.addAttribute("nota", nota);
		model.addAttribute("cuadernos", cuadernoService.listar());
		model.addAttribute("cuadernoIdSeleccionado", nota.getCuaderno().getId());
		return "notas/form";
	}

	// Update (paso 2/2): procesar la edicion (POST)
	@PostMapping("/{id}/editar")
	public String update(@PathVariable Long id, @Valid @ModelAttribute Nota nota, BindingResult result,
			@RequestParam Long cuadernoId, Model model) {

		// comprobamos en el servidor que el cuaderno elegido en el select realmente existe
		boolean cuadernoValido = cuadernoService.buscarPorId(cuadernoId).isPresent();

		if (result.hasErrors() || !cuadernoValido) {
			model.addAttribute("cuadernos", cuadernoService.listar());
			model.addAttribute("cuadernoIdSeleccionado", cuadernoId);
			if (!cuadernoValido) {
				model.addAttribute("errorCuaderno", "El cuaderno seleccionado no existe. Elige uno de la lista.");
			}
			return "notas/form";
		}

		// actualizamos la nota existente (puede cambiar de cuaderno sin duplicarse ni eliminarse)
		notaService.actualizar(id, nota, cuadernoId);
		return "redirect:/notas";
	}

	// ------- ELIMINAR --------
	// Delete: eliminar una nota
	@PostMapping("/{id}/eliminar")
	public String delete(@PathVariable Long id) {
		notaService.eliminar(id);
		return "redirect:/notas";
	}

}
