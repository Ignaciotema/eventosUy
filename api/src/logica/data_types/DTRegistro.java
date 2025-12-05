package logica.data_types;

import java.time.LocalDate;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import webservices.LocalDateAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class DTRegistro {
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	@XmlSchemaType(name = "date")
	private LocalDate fechaRegistro;
	private String nombreEdicion;
	private String nombreAsistente;
	private String tipoRegistro;
	private float costo;
	private boolean asistencia;
	
	public DTRegistro(LocalDate fechaRegistro, String nombreEdicion, String nombreAsistente, float costo, String tipoReg, boolean asistencia) {
		this.fechaRegistro = fechaRegistro;
		this.nombreEdicion = nombreEdicion;
		this.nombreAsistente = nombreAsistente;
		this.costo = costo;
		this.tipoRegistro = tipoReg;
		this.asistencia = asistencia;
	}
	
	public DTRegistro() {
	}

	public LocalDate getFechaRegistro() {
		return fechaRegistro;
	}
	
	public String getNombreEdicion() {
		return nombreEdicion;
	}
	
	public void setFechaRegistro(LocalDate fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public void setNombreEdicion(String nombreEdicion) {
		this.nombreEdicion = nombreEdicion;
	}

	public void setNombreAsistente(String nombreAsistente) {
		this.nombreAsistente = nombreAsistente;
	}

	public void setCosto(float costo) {
		this.costo = costo;
	}

	public String getNombreAsistente() {
		return nombreAsistente;
	}
	
	public float getCosto() {
		return costo;
	}

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
	
	public boolean getAsistencia() {
		return asistencia;
	}
	public void setAsistencia(boolean asistencia) {
		this.asistencia = asistencia;
	}


	public void confirmarAsistencia(String asistente) {
		if (!this.asistencia){
			this.asistencia = true;
			//TODO: generar pdf ...	
		}
		
	}
	
}
