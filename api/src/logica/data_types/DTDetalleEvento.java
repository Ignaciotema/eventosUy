package logica.data_types;

import java.time.LocalDate;
import java.util.Set;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import webservices.LocalDateAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class DTDetalleEvento {
	private String nombre;
	private String sigla;
	
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	@XmlSchemaType(name = "date")
	private LocalDate fechaAlta;
	
	private String descripcion;
	private Set<String>  categorias;
	private Set<String> ediciones;
	private boolean finalizado;
	private String videourl;
	
	public DTDetalleEvento(String nombre, String sigla, LocalDate fecha, String descripcion, Set<String> categorias,
			Set<String> hashSet, boolean finalizado) {
		super();
		this.nombre = nombre;
		this.sigla = sigla;
		this.fechaAlta= fecha;
		this.descripcion = descripcion;
		this.categorias = categorias;
		this.ediciones = hashSet;
		this.finalizado = finalizado;
		this.videourl = "";
	}
	
	public DTDetalleEvento(String nombre, String sigla, LocalDate fecha, String descripcion, Set<String> categorias,
			Set<String> hashSet, boolean finalizado, String videourl) {
		super();
		this.nombre = nombre;
		this.sigla = sigla;
		this.fechaAlta= fecha;
		this.descripcion = descripcion;
		this.categorias = categorias;
		this.ediciones = hashSet;
		this.finalizado = finalizado;
		this.videourl = videourl;
	}
	
	public String getNombre() {
		return nombre;
	}
	public String getSigla() {
		return sigla;
	}
	public LocalDate getFechaAlta() {
		return fechaAlta;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public Set<String> getCategorias() {
		return categorias;
	}
	public Set<String> getEdiciones() {
		return ediciones;
	}
	
	public DTDetalleEvento() {

	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public void setFechaAlta(LocalDate fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setCategorias(Set<String> categorias) {
		this.categorias = categorias;
	}

	public void setEdiciones(Set<String> ediciones) {
		this.ediciones = ediciones;
	}
	
	public boolean getFinalizado() {
		return finalizado;
	}
	
	public void setFinalizado(boolean finalizado) {
		this.finalizado = finalizado;
	}
	public String getVideourl() {
		return videourl;
	}
	public void setVideourl(String videourl) {
		this.videourl = videourl;
	}
	
}