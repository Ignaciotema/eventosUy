package logica.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import logica.data_types.DTAsistente;

public class Asistente extends Usuario {
	
	private String apellido;
	private LocalDate fechaNacimiento;
	private Institucion institucion = null;
	private Set<Registro> registros = new HashSet<Registro>();
	
	public String getApellido() {
		return apellido;
	}
	
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}
	
	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	

	public Institucion getInstitucion() {
		return institucion;
	}
	
	public void setInstitucion(Institucion institucion) {
		this.institucion = institucion;
	}
	

	public Set<Registro> getRegistros() {
		return registros;
	}
	
	public void addRegistro(Registro reg) {
		this.registros.add(reg);
	}
	
	public Registro getRegistro(Edicion edicion) {
	    if (edicion == null) return null;
	    for (Registro reg : registros) {
	        Edicion e = reg.getEdicion();
	        if (e == edicion) return reg;
	        if (e != null && e.getNombre() != null && e.getNombre().equals(edicion.getNombre())) {
	            return reg;
	        }
	    }
	    return null;
	}


	public Asistente(String nickname, String nombre, String email, String password, String apellido, LocalDate fechaNacimiento) {
		super(nickname, nombre, email, password);
		this.apellido = apellido;
		this.fechaNacimiento = fechaNacimiento;
	}
	
	public DTAsistente infoAsist() {
		//para evitar los getters podriamos hacer que Usuario sea protected 
		DTAsistente dtasis = new DTAsistente(this.getNickname(), this.getNombre(), this.getEmail(), this.apellido, this.fechaNacimiento, (this.institucion != null) ? this.institucion.getNombre() : null);
		return dtasis;
	}
	
}
