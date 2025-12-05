package logica.models;

public class Institucion {
	
	private String nombre;
	private String descripcion;
	private String web;
	
	public String getNombre() {
		return nombre;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public String getWeb() {
		return web;
	}
	
	
	public Institucion(String nombre, String descripcion, String web) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.web = web;
	}
	
	
	
}
