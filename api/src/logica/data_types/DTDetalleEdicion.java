package logica.data_types;

import java.time.LocalDate;
import java.util.Set;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import logica.enumerators.EstadoEdicion;
import webservices.LocalDateAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class DTDetalleEdicion {
	private String nombre;
	private String sigla;
	
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	@XmlSchemaType(name = "date")
	private LocalDate fechaInicio;
	
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	@XmlSchemaType(name = "date")
	private LocalDate fechaFin;
	
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	@XmlSchemaType(name = "date")
	private LocalDate fechaAlta;
	
	private String ciudad;
	private String pais;
	private String organizador;
	private EstadoEdicion estado;
	
    private final Set<String> nombresTiposRegistros;
	private final Set<String> nombresInstituciones;
	private String videourl;
	
	public DTDetalleEdicion(String nombre, String sigla, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta,
			String ciudad, String pais, String organizador, Set<String> tiposRegistro, Set<String> instituciones, EstadoEdicion estado) {
		super();
		this.nombre = nombre;
		this.sigla = sigla;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.fechaAlta = fechaAlta;
		this.ciudad = ciudad;
		this.pais = pais;
		this.organizador = organizador;
		this.nombresTiposRegistros = tiposRegistro;
		this.nombresInstituciones = instituciones;
		this.estado = estado;
		this.videourl = "";
	}
	
	public DTDetalleEdicion(String nombre, String sigla, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta,
			String ciudad, String pais, String organizador, Set<String> tiposRegistro, Set<String> instituciones, EstadoEdicion estado, String videourl) {
		super();
		this.nombre = nombre;
		this.sigla = sigla;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.fechaAlta = fechaAlta;
		this.ciudad = ciudad;
		this.pais = pais;
		this.organizador = organizador;
		this.nombresTiposRegistros = tiposRegistro;
		this.nombresInstituciones = instituciones;
		this.estado = estado;
		this.videourl = videourl;
	}
	
	
	

	public DTDetalleEdicion() {
		this.nombresTiposRegistros = null;
		this.nombresInstituciones = null;

	}

	public String getNombre() {
		return nombre;
	}

	public String getSigla() {
		return sigla;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public LocalDate getFechaAlta() {
		return fechaAlta;
	}

	public String getCiudad() {
		return ciudad;
	}

	public String getPais() {
		return pais;
	}

	public String getOrganizador() {
		return organizador;
	}
	
	public EstadoEdicion getEstado() {
		return estado;
	}



	public Set<String> getNombresTiposRegistros() {
		return nombresTiposRegistros;
	}

	public Set<String> getNombresInstituciones() {
		return nombresInstituciones;
	}

	public void setOrganizador(Object object) {
		
		this.organizador = (String) object;
		
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public void setSigla(String sigla) {
		this.sigla = sigla;
	}


	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}


	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}


	public void setFechaAlta(LocalDate fechaAlta) {
		this.fechaAlta = fechaAlta;
	}


	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}


	public void setPais(String pais) {
		this.pais = pais;
	}


	public void setOrganizador(String organizador) {
		this.organizador = organizador;
	}


	public void setEstado(EstadoEdicion estado) {
		this.estado = estado;
	}
	
	public String getVideourl() {
		return videourl;
	}
	
	public void setVideourl(String videourl) {
		this.videourl = videourl;
	}
	

	
	
	
	
	
}