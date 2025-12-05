package logica.models;

import java.time.LocalDate;

import logica.controllers.IControllerEvento;

public class Registro {

	private Edicion edicion;
	private Asistente asistente;
	private float costo;
	private TipoRegistro tipoReg;
	private LocalDate fechaRegistro;
	private String constanciaUrl;
	private boolean asistencia;

	public LocalDate getFechaRegistro() {
		return fechaRegistro;
	}


	public void setFechaRegistro(LocalDate fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	//TODO: implementar caso en el que el costo sea 0 (ej: asistente de una institucion que patrocina)
	public Registro(Asistente asis, TipoRegistro tipoReg, Edicion edi, boolean asistencia,boolean esGratis) {
		super();
		this.asistente=asis;
		this.edicion=edi;
		this.setTipoReg(tipoReg);
		if (esGratis) {
			this.costo = 0;
		} else {
			this.costo = tipoReg.getCosto();
		}
		
		IControllerEvento cEve = new ControllerEvento();
		this.fechaRegistro = cEve.getFechaSistema();
		this.asistencia = asistencia;
		return;
	}

	public Edicion getEdicion() {
		return edicion;
	}
	
	/*public void setEdicion(Edicion edicion) {
		this.edicion = edicion;
	}*/
	
	public Asistente getAsistente() {
		return asistente;
	}
	
	/*public void setAsistente(Asistente asistente) {
		this.asistente = asistente;
	}*/
	
	public float getCosto() {
		return costo;
	}
	
	/*public void setCosto(int costo) {
		this.costo = costo;
	}*/


	public TipoRegistro getTipoReg() {
		return tipoReg;
	}


	public void setTipoReg(TipoRegistro tipoReg) {
		this.tipoReg = tipoReg;
	}
	
	public String getConstanciaUrl() {
		return constanciaUrl;
	}


	public void setConstanciaUrl(String constanciaUrl) {
		this.constanciaUrl = constanciaUrl;
	}
	
	public boolean getAsistencia() {
		return asistencia;
	}


	public void confirmarAsistencia() {
		if (!this.asistencia){
			this.asistencia = true;
			//TODO: generar pdf ...	
		}
		
	}
}
