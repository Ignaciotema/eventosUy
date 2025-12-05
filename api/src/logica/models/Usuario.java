package logica.models;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
	
	private String nickname;
	private String nombre;
	private String email;
	private String password;
	private List<Usuario> seguidores;
	private List<Usuario> seguidos;

	public String getNickname() {
		return nickname;
	}
	
	public List<Usuario> getSeguidores() {
		return seguidores;
	}
	public List<Usuario> getSeguidos() {
		return seguidos;
	}
	
	
	
	public  String getNombre() {
		return nombre;
	}
	
	public String getEmail() {
		return email;
	}
	public  String getPassword() {
		return password;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Usuario(String nickname, String nombre, String email, String password) {
		this.nickname = nickname;
		this.nombre = nombre;
		this.email = email;
		this.password = password;
		
		
	}

	public void agregarSeguido(Usuario userSeguido) {
		if (seguidos == null) {
			seguidos = new ArrayList<Usuario>();
		}
		// Evitar duplicados
		if (!seguidos.contains(userSeguido)) {
			seguidos.add(userSeguido);
			
			// Actualizar la relación bidireccional: añadir este usuario a los seguidores del usuario seguido
			if (userSeguido.seguidores == null) {
				userSeguido.seguidores = new ArrayList<Usuario>();
			}
			if (!userSeguido.seguidores.contains(this)) {
				userSeguido.seguidores.add(this);
			}
		}
	}

	public void eliminarSeguido(Usuario userSeguido) {
		if (seguidos != null) {
			seguidos.remove(userSeguido);
			
			// Actualizar la relación bidireccional: remover este usuario de los seguidores del usuario que se deja de seguir
			if (userSeguido.seguidores != null) {
				userSeguido.seguidores.remove(this);
			}
		}
	}

	

}