package eus.birt.dam.ad05.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "cuaderno")
public class Cuaderno {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "El titulo es obligatorio")
	@Size(max = 150, message = "El titulo no puede superar los 150 caracteres")
	@Column(name = "titulo", length = 100, nullable = false)
	private String titulo;

	@NotBlank(message = "La materia es obligatoria")
	@Size(max = 60, message = "La materia no puede superar los 60 caracteres")
	@Column(name = "materia", length = 60, nullable = false)
	private String materia;

	@OneToMany(mappedBy = "cuaderno", cascade = CascadeType.REMOVE)
	private List<Nota> notas = new ArrayList<Nota>();

	// Constructores

	protected Cuaderno() {
	}

	public Cuaderno(String titulo, String materia) {
		this.titulo = titulo;
		this.materia = materia;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getMateria() {
		return materia;
	}

	public void setMateria(String materia) {
		this.materia = materia;
	}

	public Long getId() {
		return id;
	}

	public List<Nota> getNotas() {
		return notas;
	}

	@Override
	public String toString() {
		return "Cuaderno [id=" + id + ", titulo=" + titulo + ", materia=" + materia + "]";
	}

}
