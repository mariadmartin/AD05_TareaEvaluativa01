package eus.birt.dam.ad05.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import eus.birt.dam.ad05.entity.Cuaderno;
import eus.birt.dam.ad05.repository.CuadernoRepository;

@Service
public class CuadernoService {

	private final CuadernoRepository cuadernoRepository;

	// constructor - inyeccion Repository
	public CuadernoService(CuadernoRepository cuadernoRepository) {
		this.cuadernoRepository = cuadernoRepository;
	}

	// Listar todos los cuadernos
	public List<Cuaderno> listar() {
		return cuadernoRepository.findAll();
	}

	// Buscar un cuaderno por su id
	public Optional<Cuaderno> buscarPorId(Long id) {
		return cuadernoRepository.findById(id);
	}

	// Crear un cuaderno nuevo
	public Cuaderno crear(Cuaderno cuaderno) {
		return cuadernoRepository.save(cuaderno);
	}

	// Actualizar un cuaderno existente
	public Cuaderno actualizar(Long id, Cuaderno datos) {
		Cuaderno cuaderno = buscarPorId(id).orElseThrow(() -> new RuntimeException("Cuaderno no encontrado: " + id));
		cuaderno.setTitulo(datos.getTitulo());
		cuaderno.setMateria(datos.getMateria());

		return cuadernoRepository.save(cuaderno);
	}

	// Eliminar un cuaderno (y sus notas, por cascada)
	public void eliminar(Long id) {
		cuadernoRepository.deleteById(id);
	}

}
