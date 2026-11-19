package eus.birt.dam.ad05.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/*	Captura RecursoNoEncontradoException en CUALQUIER controller de la app
* 	y la convierte en una pagina de error legible, en vez de dejar que
* 	Spring muestre su pagina de error generica. 
*/
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RecursoNoEncontradoException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String manejarRecursoNoEncontrado(RecursoNoEncontradoException ex, Model model) {
		model.addAttribute("mensaje", ex.getMessage());
		return "error/404";
	}

}
