package logica.data_types;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class DTOrganizador extends DataUsuario {
	
	private String descripcion;
	private String web;
	
	public DTOrganizador(String nickname, String nombre, String email, String descripcion, String web) {
		super(nickname, nombre, email, TipoUsuario.ORGANIZADOR);
		this.descripcion = descripcion;
		this.web = web;
	}
	
	public DTOrganizador() {
		super();
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public String getWeb() {
		return web;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setWeb(String web) {
		this.web = web;
	}
}