package eus.birt.dam.ad05.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "nota")
public class Nota {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "El titulo es obligatorio")
	@Size(max = 100, message = "El titulo no puede superar los 100 caracteres")
	@Column(name = "titulo", length = 100, nullable = false)
	private String titulo;

	@NotBlank(message = "El contenido es obligatorio")
	@Size(max = 2000, message = "El contenido no puede superar los 2000 caracteres")
	@Column(name = "contenido", length = 2000, nullable = false)
	private String contenido;

	@NotNull(message = "La fecha es obligatoria")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@Column(name = "fecha", nullable = false)
	private LocalDate fecha;

	@ManyToOne(optional = false)
	@JoinColumn(name = "cuaderno_id", nullable = false)
	private Cuaderno cuaderno;

	// Constructores

	public Nota() {
	}

	public Nota(String titulo, String contenido, LocalDate fecha, Cuaderno cuaderno) {
		this.titulo = titulo;
		this.contenido = contenido;
		this.fecha = fecha;
		this.cuaderno = cuaderno;
	}

	// Getters - Setters

	public Long getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public Cuaderno getCuaderno() {
		return cuaderno;
	}

	public void setCuaderno(Cuaderno cuaderno) {
		this.cuaderno = cuaderno;
	}

	@Override
	public String toString() {
		return "Nota [id=" + id + ", titulo=" + titulo + ", fecha=" + fecha
				+ ", cuadernoId=" + (cuaderno != null ? cuaderno.getId() : null) + "]";
	}

}
