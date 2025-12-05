package logica.controllers;

import java.time.LocalDate;
import java.util.Set;

import excepciones.EmailRepetido;
import excepciones.NombreInstiExistente;
import excepciones.NombreUsuarioExistente;
import excepciones.UsuarioNoEncontrado;
import logica.data_types.DTAsistente;
import logica.data_types.DTOrganizador;
import logica.data_types.DataUsuario;
import logica.models.Usuario;

public interface IControllerUsuario {
	 
	//ALTAS
	public void ingresarAsistente(String nickname, 
			String nombre, String email, String password,
			String apellido, LocalDate fechaNac) throws NombreUsuarioExistente, EmailRepetido, Exception;
	
	public void ingresarOrganizador(String nickname, String nombre, 
			String email, String password, String descripcion, String web) throws NombreUsuarioExistente, EmailRepetido, Exception;
	

	
	
	//LISTAS
	public Set<String> listarInstituciones();
	
	public Set<String> listarUsuarios();

	public Set<String> listarAsistentes();
	
	public Set<String> listarOrganizadores();
	
	public DataUsuario infoUsuario(String nickname) throws UsuarioNoEncontrado;

	public Set<String> listarRegistrosAEventos(String nickname);

	public Set<String> listarEdicionesOrganizadas(String nickname);

	
	
	//EDITAR
	public void agregarAsistente(String nicknameAsistente, String nombreInstitucion);
	
	//public void editarDatos(String nickname, String nombre, 
			//String descripcion, String URL, String apellido, LocalDate fechaNac);

	

	public void editarAsistente(String nick, String nombre, String apellido, LocalDate fechaNac);
	public void editarOrganizador(String nick, String nombre,  String descripcion, String web);
	
	
	
	
	public DTAsistente infoAsistente(String nickname);
	public DTOrganizador infoOrganizador(String nickname);
	public void altaInstitucion(String nombre, String descripcion, String web) throws NombreInstiExistente, Exception;

	public DataUsuario iniciarSesionNickname(String nickname, String password);

	public DataUsuario iniciarSesionEmail(String email, String password);

	public String obtenerInstitucionAsistente(String nickname);
	public boolean existeNickname(String nickname);
	public boolean existeEmail(String email);
	public void seguirUsuario(String nicknameSeguidor, String nicknameSeguido) throws UsuarioNoEncontrado;
	public int cantidadSeguidores(String nickname) throws UsuarioNoEncontrado;
	public int cantidadSeguidos(String nickname) throws UsuarioNoEncontrado;
	public void dejarDeSeguirUsuario(String nicknameSeguidor, String nicknameSeguido) throws UsuarioNoEncontrado;
    public boolean esSeguidor(String nicknameSeguidor, String nicknameSeguido) throws UsuarioNoEncontrado;

}