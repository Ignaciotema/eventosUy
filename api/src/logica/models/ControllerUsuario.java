package logica.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import excepciones.EmailRepetido;
import excepciones.NombreInstiExistente;
import excepciones.NombreUsuarioExistente;
import excepciones.UsuarioNoEncontrado;
import logica.controllers.IControllerUsuario;
import logica.data_types.DTAsistente;
import logica.data_types.DTOrganizador;
import logica.data_types.DataUsuario;
import logica.data_types.DataUsuario.TipoUsuario;
import logica.manejadores.ManejadorInstitucion;
import logica.manejadores.ManejadorUsuario;

//TODO: implementar excepciones para manejar campos vacíos en las altas salvo la web de organizador ya que es opcional
public class ControllerUsuario implements IControllerUsuario {

	@Override
	public void ingresarAsistente(String nickname, String nombre, String email, String password, String apellido,
			LocalDate fechaNac) throws NombreUsuarioExistente, EmailRepetido, Exception {
		ManejadorUsuario mUser = ManejadorUsuario.getInstance();
		if (mUser.existeNickname(nickname)) {
			throw new NombreUsuarioExistente("Ya existe un usuario con este nickname");
		} else if (mUser.existeEmail(email)) {
			throw new EmailRepetido("Ya existe un usuario con este email");
		} else {
			Asistente user = new Asistente(nickname, nombre, email, password, apellido, fechaNac);
			mUser.agregarUsuario(user);
		}
	}

	@Override
	public Set<String> listarInstituciones() {
		ManejadorInstitucion mInsti = ManejadorInstitucion.getInstance();
		return mInsti.obtenerInstituciones();
	}

	@Override
	public void agregarAsistente(String nicknameAsistente, String nombreInstitucion) {
		ManejadorUsuario mUser = ManejadorUsuario.getInstance();
		ManejadorInstitucion mInsti = ManejadorInstitucion.getInstance();
		Asistente asistente = mUser.obtenerAsistente(nicknameAsistente);
		asistente.setInstitucion(mInsti.obtenerInstitucion(nombreInstitucion));

	}

	@Override
	public void ingresarOrganizador(String nickname, String nombre, String email, String password, String descripcion, String web) throws NombreUsuarioExistente, EmailRepetido, Exception {
		ManejadorUsuario mUser = ManejadorUsuario.getInstance();
		if (mUser.existeNickname(nickname)) {
			throw new NombreUsuarioExistente("Ya existe un usuario con este nickname");
		} else if (mUser.existeEmail(email)) {
			throw new EmailRepetido("Ya existe un usuario con este email");
		} else {
			Organizador user = new Organizador(nickname, nombre, email, password, descripcion, web);
			mUser.agregarUsuario(user);
		}
	}

	@Override
	public Set<String> listarUsuarios() {
		return ManejadorUsuario.getInstance().obtenerUsuarios();
	}
	
	
	public Set<String> listarOrganizadores() {
		ManejadorUsuario mUser = ManejadorUsuario.getInstance();
		Set<String> organizadores = new HashSet<>();
		for (String nick: mUser.obtenerUsuarios()) {
			if (mUser.obtenerUsuario(nick) instanceof Organizador) {
				organizadores.add(nick);
			}
		}
		return organizadores;
	}
	

	@Override
	public Set<String> listarAsistentes() {
		return ManejadorUsuario.getInstance().obtenerAsistentes();
	}
	
	@Override
	public DataUsuario infoUsuario(String nickname) throws UsuarioNoEncontrado {
		ManejadorUsuario mUser = ManejadorUsuario.getInstance();
		Usuario user = mUser.obtenerUsuario(nickname);
		if (user != null) {
			TipoUsuario tipo;
			if ( user instanceof Asistente) {
				tipo = TipoUsuario.ASISTENTE;
			} else {
				tipo = TipoUsuario.ORGANIZADOR;
			}
			
			return new DataUsuario(user.getNickname(), user.getNombre(), user.getEmail(), tipo);
		} else {
			throw new UsuarioNoEncontrado("No existe un usuario con este nickname");
		}
		
	}

	
	
	@Override
	public Set<String> listarRegistrosAEventos(String nickname) {
		ManejadorUsuario mInsti = ManejadorUsuario.getInstance();
		Asistente asistente = mInsti.obtenerAsistente(nickname);
		Set<Registro> regs = asistente.getRegistros();
		Set<String> nombresRegistros= new HashSet<>();
		for (Registro reg: regs) {
			nombresRegistros.add((reg.getEdicion()).getNombre());
		}
		return nombresRegistros;
	}
	
	@Override
	public Set<String> listarEdicionesOrganizadas(String nickname) {
		ManejadorUsuario mInsti = ManejadorUsuario.getInstance();
		Organizador org = mInsti.obtenerOrganizador(nickname);
		return org.getEdiciones();
	}
	

	
	
	@Override
	public DTAsistente infoAsistente(String nickname) {
		ManejadorUsuario mUser = ManejadorUsuario.getInstance();
		Asistente user = mUser.obtenerAsistente(nickname);
		DTAsistente dtA;
		if (user.getInstitucion() != null) {
			dtA = new DTAsistente(user.getNickname(), user.getNombre(), user.getEmail(), user.getApellido(), user.getFechaNacimiento(), user.getInstitucion().getNombre());
		} else {
			dtA = new DTAsistente(user.getNickname(), user.getNombre(), user.getEmail(), user.getApellido(), user.getFechaNacimiento(), null);
		}
		return dtA;
	}
	
	
	@Override
	public DTOrganizador infoOrganizador(String nickname) {
		ManejadorUsuario mUser = ManejadorUsuario.getInstance();
		Organizador user = mUser.obtenerOrganizador(nickname);
		DTOrganizador dtO = new DTOrganizador(user.getNickname(), user.getNombre(), user.getEmail(), user.getDescripcion(), user.getWeb());
		return dtO;
	}
	@Override
	public void editarAsistente(String nick, String nom, String apellido, LocalDate fdef) {
		ManejadorUsuario mUser = ManejadorUsuario.getInstance();
		Asistente user = mUser.obtenerAsistente(nick);
		user.setApellido(apellido);
		user.setFechaNacimiento(fdef);
		user.setNombre(nom);
	}
	
	@Override
	public void editarOrganizador(String nick, String nom, String descripcion, String web) {
		ManejadorUsuario mUser = ManejadorUsuario.getInstance();
		Organizador user = mUser.obtenerOrganizador(nick);
		user.setDescripcion(descripcion);
		user.setWeb(web);
		user.setNombre(nom);
		}
	
		
	
	


   @Override
   public void altaInstitucion(String nombre, String descripcion, String web) throws NombreInstiExistente, Exception {
	   ManejadorInstitucion mInsti = ManejadorInstitucion.getInstance();
	   if (mInsti.obtenerInstitucion(nombre) != null) {
		   throw new NombreInstiExistente("Ya existe una institucion con este nombre");
	   } else {
		   Institucion institucion = new Institucion(nombre, descripcion, web);
		   mInsti.agregarInstitucion(institucion);
	   }
   
	
   }
   
  @Override
  public DataUsuario  iniciarSesionNickname(String nickname, String password) {
	  ManejadorUsuario mUser = ManejadorUsuario.getInstance();
	  Usuario user = mUser.obtenerUsuario(nickname);
	  if (user != null && user.getPassword().equals(password)) {
		  return new DataUsuario(user.getNickname(), user.getNombre(), user.getEmail(), (user instanceof Asistente) ? TipoUsuario.ASISTENTE : TipoUsuario.ORGANIZADOR);
	  }
	  return null;
  }
  
  @Override
  public DataUsuario iniciarSesionEmail(String email, String password) {
	  ManejadorUsuario mUser = ManejadorUsuario.getInstance();
	  Usuario user = mUser.obtenerUsuarioPorEmail(email);
	  if (user != null && user.getPassword().equals(password)) {
		  return new DataUsuario(user.getNickname(), user.getNombre(), user.getEmail(), (user instanceof Asistente) ? TipoUsuario.ASISTENTE : TipoUsuario.ORGANIZADOR);
	  }
	  return null;
  }
  
  @Override
  public String obtenerInstitucionAsistente(String nickname) {
	  ManejadorUsuario mUser = ManejadorUsuario.getInstance();
	  Asistente as = mUser.obtenerAsistente(nickname);
	  if (as.getInstitucion() != null) {
		  return as.getInstitucion().getNombre();
	  } else {
		  return null;
	  }



}
  @Override
  public boolean existeNickname(String nickname) {
	  ManejadorUsuario mUser = ManejadorUsuario.getInstance();
	  return mUser.existeNickname(nickname);
  }
  
  @Override
  public boolean existeEmail(String email) {
	  ManejadorUsuario mUser = ManejadorUsuario.getInstance();
	  return mUser.existeEmail(email);
  }
  @Override
  public void seguirUsuario(String seguidor, String seguido) throws UsuarioNoEncontrado {
	  ManejadorUsuario mUser = ManejadorUsuario.getInstance();
	  Usuario userSeguidor = mUser.obtenerUsuario(seguidor);
	  Usuario userSeguido = mUser.obtenerUsuario(seguido);
	  if (userSeguidor == null) {
		  throw new UsuarioNoEncontrado("No existe un usuario con el nickname: " + seguidor);
	  }
	  if (userSeguido == null) {
		  throw new UsuarioNoEncontrado("No existe un usuario con el nickname: " + seguido);
	  }
	  userSeguidor.agregarSeguido(userSeguido);}
  
  @Override
  public int cantidadSeguidores(String nickname) throws UsuarioNoEncontrado {
	  ManejadorUsuario mUser = ManejadorUsuario.getInstance();
	  Usuario user = mUser.obtenerUsuario(nickname);
	  if (user == null) {
		  throw new UsuarioNoEncontrado("No existe un usuario con el nickname: " + nickname);
	  }
	  if (user.getSeguidores() == null){
		  return 0;
	  }else {
	  return user.getSeguidores().size();}}
  @Override
  public int cantidadSeguidos(String nickname) throws UsuarioNoEncontrado {
	  ManejadorUsuario mUser = ManejadorUsuario.getInstance();
	  Usuario user = mUser.obtenerUsuario(nickname);
	  if (user == null) {
		  throw new UsuarioNoEncontrado("No existe un usuario con el nickname: " + nickname);
	  }
	  if (user.getSeguidos() == null){
		  return 0;}else {
	  return user.getSeguidos().size();}}
  
  
  @Override
  public void dejarDeSeguirUsuario(String seguidor, String seguido) throws UsuarioNoEncontrado {
	  ManejadorUsuario mUser = ManejadorUsuario.getInstance();
	  Usuario userSeguidor = mUser.obtenerUsuario(seguidor);
	  Usuario userSeguido = mUser.obtenerUsuario(seguido);
	  if (userSeguidor == null) {
		  throw new UsuarioNoEncontrado("No existe un usuario con el nickname: " + seguidor);
	  }
	  if (userSeguido == null) {
		  throw new UsuarioNoEncontrado("No existe un usuario con el nickname: " + seguido);
	  }
	  userSeguidor.eliminarSeguido(userSeguido);}

  @Override
  public boolean esSeguidor(String seguidor, String seguido) throws UsuarioNoEncontrado {
	  ManejadorUsuario mUser = ManejadorUsuario.getInstance();
	  Usuario userSeguidor = mUser.obtenerUsuario(seguidor);
	  Usuario userSeguido = mUser.obtenerUsuario(seguido);
	  if (userSeguidor == null) {
		  throw new UsuarioNoEncontrado("No existe un usuario con el nickname: " + seguidor);
	  }
	  if (userSeguido == null) {
		  throw new UsuarioNoEncontrado("No existe un usuario con el nickname: " + seguido);
	  }
	  List<Usuario> seguidos = userSeguidor.getSeguidos();
	  if (seguidos == null) {
		  return false;
	  }else {
	  return seguidos.contains(userSeguido);}


}}
	


    

