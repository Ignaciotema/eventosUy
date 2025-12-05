package logica.models;

import logica.data_types.DTTipoRegistro;

public class TipoRegistro {
	private String nombre;
	private String descripcion;
	private float costo;
	private int cupo;
	
	public TipoRegistro(String nombre, String descripcion, float costo, int cupo) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.costo = costo;
		this.cupo = cupo;
	}
	
	public String getNombre() {
		return nombre;
	}
	/*
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}*/
	public float getCosto() {
		return costo;
	}
	
	/*public void setCosto(float costo) {
		this.costo = costo;
	}

	public void setCupo(int cupo) {
		this.cupo = cupo;
	}
	*/
	public void restarCupo() {
		this.cupo--;
	}
	
	public int getCupo() {
		return cupo;
	}

	public boolean verificarCupo() {
		return !(this.cupo == 0);
	}
	
	public DTTipoRegistro infoTipoRegistro() {
		
		DTTipoRegistro dtTReg = new DTTipoRegistro(nombre, descripcion, costo, cupo);
		return dtTReg;
		
	}
	
}
