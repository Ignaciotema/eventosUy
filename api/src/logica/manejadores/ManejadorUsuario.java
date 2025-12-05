package logica.manejadores;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import logica.models.Asistente;
import logica.models.Organizador;
import logica.models.Usuario;

public class ManejadorUsuario {
	
	private static ManejadorUsuario instance = null;
	private Map<String, Usuario> usuarios;
	private Set<String> emails;
	
	public static ManejadorUsuario getInstance() {
		if (instance == null) {
			instance = new ManejadorUsuario();
		}
		return instance;
	}
	
	private ManejadorUsuario() {
		usuarios = new HashMap<String, Usuario>();
		emails = new HashSet<String>();
	}
	
	public void agregarUsuario(Usuario user) {
		usuarios.put(user.getNickname(), user);
		emails.add(user.getEmail());
	}
	
	/**Retorna true si existe un usuario con el atributo nickname
	 **/
	public boolean existeNickname(String nickname) {
		return usuarios.containsKey(nickname);
	}
	
	/**Retorna true si existe un usuario con el atributo email
	 **/
	public boolean existeEmail(String email) {
		return emails.contains(email);
	}
	
	public Asistente obtenerAsistente(String nickname) {
	    Usuario u = usuarios.get(nickname);
	    if (u == null) {
	        throw new IllegalArgumentException("No existe usuario: " + nickname);
	    }
	    if (!(u instanceof Asistente)) {
	        throw new IllegalStateException("El usuario " + nickname + " no es Asistente.");
	    }
	    return (Asistente) u;
	}


	public Organizador obtenerOrganizador(String nickname) {
		return (Organizador) usuarios.get(nickname);
	}
	
	public Usuario obtenerUsuario(String nickname) {
	    if (!existeNickname(nickname)) {
	        throw new IllegalArgumentException("No existe un usuario con este nickname");
	    }
	    return usuarios.get(nickname);
	}
	
	public Set<String> obtenerUsuarios() {
		return new HashSet<String>(usuarios.keySet());
	}

	public Set<String> obtenerAsistentes() {
		Set<String> asistentes = new HashSet<String>();
		for (Usuario u : usuarios.values()) {
			if (u instanceof Asistente) {
				asistentes.add(u.getNickname());
			}
		}
		return asistentes;
	}
	
	public Set<String> obtenerOrganizadores() {
		Set<String> organizadores = new HashSet<String>();
		for (Usuario u : usuarios.values()) {
			if (u instanceof Organizador) {
				organizadores.add(u.getNickname());
			}
		}
		return organizadores;
	}
   public Usuario obtenerUsuarioPorEmail(String email) {
		for (Usuario u : usuarios.values()) {
			if (u.getEmail().equals(email)) {
				return u;
			}
		}
		return null; // Retorna null si no se encuentra ningún usuario con el email dado

	
}}
