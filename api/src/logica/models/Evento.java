package logica.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import logica.data_types.DTDetalleEvento;
import logica.enumerators.EstadoEdicion;

public class Evento{
	private String nombre;
	private String sigla;
	private LocalDate fechaAlta;
	private String descripcion;
	private List<Edicion> colEdicionesConfirmadas;
	private List<Edicion> colEdicionesIngresadas;
	private List<Edicion> colEdicionesRechazadas;
	private List<Categoria> colCategorias;
	private boolean finalizado;
	private String videourl;

	public Evento(String nombre, String sigla,  LocalDate fecha, String descripcion) {
		this.nombre = nombre;
		this.fechaAlta = fecha;
		this.sigla = sigla;
		this.descripcion = descripcion;
		this.colEdicionesConfirmadas = new ArrayList<>();
		this.colEdicionesIngresadas = new ArrayList<>();
		this.colEdicionesRechazadas = new ArrayList<>();
		this.colCategorias = new ArrayList<>();
		this.finalizado = false;
		this.videourl = "";
	}
	
	public Evento(String nombre, String sigla,  LocalDate fecha, String descripcion, String videourl) {
		this.nombre = nombre;
		this.fechaAlta = fecha;
		this.sigla = sigla;
		this.descripcion = descripcion;
		this.colEdicionesConfirmadas = new ArrayList<>();
		this.colEdicionesIngresadas = new ArrayList<>();
		this.colEdicionesRechazadas = new ArrayList<>();
		this.colCategorias = new ArrayList<>();
		this.finalizado = false;
		this.videourl = videourl;
	}
	
	public String getVideourl() {
		return videourl;}

	public String getNombre() {
		return nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	
	public Edicion getEdicion(String nombreEdicion) {
		for (Edicion edi : this.colEdicionesConfirmadas) {
			if (nombreEdicion.equals(edi.getNombre())) {
				return edi;
			}
		}
		for (Edicion edi : this.colEdicionesIngresadas) {
			if (nombreEdicion.equals(edi.getNombre())) {
				return edi;
			}
		}
		for (Edicion edi : this.colEdicionesRechazadas) {
			if (nombreEdicion.equals(edi.getNombre())) {
				return edi;
			}
		}
		return null;
	}
	
	public  boolean getFinalizado() {
		return finalizado;
	}

	
	public void agregarEdicion(Edicion nueva) {
		if (nueva == null) throw new IllegalArgumentException("Edición vacía");
		if (getEdicion(nueva.getNombre()) != null) throw new IllegalArgumentException("Ya existe una edición con ese nombre");
		colEdicionesIngresadas.add(nueva); 
	}

	public void agregarCategoria(Categoria cat) {
		if (cat == null) throw new IllegalArgumentException("Categoría vacía");
		if (this.colCategorias.contains(cat)) throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
		colCategorias.add(cat);
	}

	
	public Set<String> getEdiciones() {
		Set<String> eds = new HashSet<String>();
		for (Edicion edi : this.colEdicionesConfirmadas) {
			eds.add(edi.getNombre());
		}
		for (Edicion edi : this.colEdicionesIngresadas) {
			eds.add(edi.getNombre());
		}
		for (Edicion edi : this.colEdicionesRechazadas) {
			eds.add(edi.getNombre());
		}
		return eds;
	}

	public Set<String> getCategorias() {
		Set<String> cats = new HashSet<String>();
		for (Categoria cat : this.colCategorias) {
			cats.add(cat.getNombre());
		}
		return cats;
	}

	public DTDetalleEvento devolverDT() {
		DTDetalleEvento dtE = new DTDetalleEvento(this.nombre, this.sigla, this.fechaAlta, this.descripcion, this.getCategorias(), this.getCategorias(), this.finalizado,this.videourl);
		return dtE;
	}

	public String getSigla() {
		return sigla;
	}

	public LocalDate getFechaAlta() {
		return fechaAlta;
	}

	// Devuelve las listas de ediciones según estado
	public List<Edicion> getColEdicionesConfirmadas() {
		return colEdicionesConfirmadas;
	}
	public List<Edicion> getColEdicionesIngresadas() {
		return colEdicionesIngresadas;
	}
	public List<Edicion> getColEdicionesRechazadas() {
		return colEdicionesRechazadas;
	}
	public List<Categoria> getColCategorias() {
		return colCategorias;
	}

	public void cambioEstado(Edicion edi, EstadoEdicion  nuevoestado) {
		colEdicionesIngresadas.remove(edi);
		if (nuevoestado == EstadoEdicion.Rechazada) colEdicionesRechazadas.add(edi);
		if (nuevoestado == EstadoEdicion.Confirmada)colEdicionesConfirmadas.add(edi);
		
	}
	
	public void setFinalizado(boolean fin) {
		this.finalizado = fin;
	}
}