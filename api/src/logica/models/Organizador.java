package logica.models;

import java.util.HashSet;
import java.util.Set;

public class Organizador extends Usuario {

    private String descripcion;
    private String web;
    private Set<String> ediciones = new HashSet<String>();
	
    //GETTERS Y SETTERS
    public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getWeb() {
		return web;
	}
	public void setWeb(String web) {
		this.web = web;
	}
	
    public Set<String> getEdiciones() {
		return ediciones;
	}
    
	/*public void setEdiciones(HashSet<String> ediciones) {
		this.ediciones = ediciones;
	}*/
	
	//METODOS DE LA COLECCION DE EDICIONES
	public void agregarEdicion(String nombreEdicion) {
		ediciones.add(nombreEdicion);
	}
	
	public boolean organizaEdicion(String nombreEdicion) {
		return ediciones.contains(nombreEdicion);
	}
	
	//CONSTRUCTOR
	public Organizador(String nickname, String nombre, String email, String password, String descripcion, String web) {
		super(nickname, nombre, email, password);
		this.descripcion = descripcion;
		this.web = web;
	}
	
}
