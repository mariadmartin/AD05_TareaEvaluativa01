package eus.birt.dam.ad05.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import eus.birt.dam.ad05.entity.Cuaderno;
import eus.birt.dam.ad05.entity.Nota;
import eus.birt.dam.ad05.repository.CuadernoRepository;
import eus.birt.dam.ad05.repository.NotaRepository;

@Service
public class NotaService {

	private final NotaRepository notaRepository;
	private final CuadernoRepository cuadernoRepository;

	// constructor - inyeccion de los dos repositorios
	public NotaService(NotaRepository notaRepository, CuadernoRepository cuadernoRepository) {
		this.notaRepository = notaRepository;
		this.cuadernoRepository = cuadernoRepository;
	}

	// Listar las notas de un cuaderno concreto
	public List<Nota> listarPorCuaderno(Long cuadernoId) {
		return notaRepository.findByCuadernoId(cuadernoId);
	}

	// Buscar una nota por su id
	public Optional<Nota> buscarPorId(Long id) {
		return notaRepository.findById(id);
	}

	// Crear una nota nueva, asociada a un cuaderno
	public Nota crear(Nota nota, Long cuadernoId) {
		Cuaderno cuaderno = cuadernoRepository.findById(cuadernoId)
				.orElseThrow(() -> new RuntimeException("Cuaderno no encontrado: " + cuadernoId));
		nota.setCuaderno(cuaderno);
		return notaRepository.save(nota);
	}

	// Actualizar una nota existente (incluye posible cambio de cuaderno)
	public Nota actualizar(Long id, Nota datos, Long cuadernoId) {
		Nota nota = buscarPorId(id).orElseThrow(() -> new RuntimeException("Nota no encontrada: " + id));
		Cuaderno cuaderno = cuadernoRepository.findById(cuadernoId)
				.orElseThrow(() -> new RuntimeException("Cuaderno no encontrado: " + cuadernoId));

		nota.setTitulo(datos.getTitulo());
		nota.setContenido(datos.getContenido());
		nota.setFecha(datos.getFecha());
		nota.setCuaderno(cuaderno);

		return notaRepository.save(nota);
	}

	// Eliminar una nota
	public void eliminar(Long id) {
		notaRepository.deleteById(id);
	}

}
