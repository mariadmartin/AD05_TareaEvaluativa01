package eus.birt.dam.ad05.exception;

/* 	Se lanza cuando se pide por id un Cuaderno o una Nota que no existe.
* 	La captura GlobalExceptionHandler (@ControllerAdvice) y la convierte
* en una pagina de error 404 legible.
*/
public class RecursoNoEncontradoException extends RuntimeException {

	// funcion anonima
	public RecursoNoEncontradoException(String mensaje) {
		super(mensaje);
	}

}
