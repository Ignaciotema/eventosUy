package webservices;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Properties;

import excepciones.EmailRepetido;
import excepciones.NombreInstiExistente;
import excepciones.NombreUsuarioExistente;
import excepciones.UsuarioNoEncontrado;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.xml.ws.Endpoint;
import logica.controllers.IControllerUsuario;
import logica.data_types.DTAsistente;
import logica.data_types.DTOrganizador;
import logica.data_types.DataUsuario;
import logica.models.Factory;
import logica.models.Usuario;



@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class publicadorUsuario {
	 private Endpoint endpoint = null;
	 private IControllerUsuario ICU;
	    //Constructor
	    public publicadorUsuario(){
	     ICU = Factory.getInstance().getControllerUsuario();
	    }

	    //Operaciones las cuales quiero publicar

	    @WebMethod(exclude = true)
	    public void publicar(){
	    	
		    Properties props = new Properties();
	    	
	        FileInputStream fis;
			try {
				// Buscar application.properties en el home del usuario (según Sección 7.9)
				String userHome = System.getProperty("user.home");
				String configPath = userHome + "/application.properties";
				fis = new FileInputStream(configPath);
				props.load(fis);
			} catch (Exception e) {
				e.printStackTrace();
			}
	         endpoint = Endpoint.publish(props.getProperty("server.url") + ":" + props.getProperty("server.port") + "/publicadorUsuario", this);
	    }
	    
	    @WebMethod(exclude = true)
	    public Endpoint getEndpoint() {
	            return endpoint;
	    }
	    
	    @WebMethod
		public void ingresarAsistente(String nickname, 
				String nombre, String email, String password,
				String apellido, String fechaNac) throws NombreUsuarioExistente, EmailRepetido, Exception{
			LocalDate fechaNacLD = LocalDate.parse(fechaNac);
			
			ICU.ingresarAsistente( nickname, 
					 nombre, email, password,
					 apellido, fechaNacLD);
		}
		
		@WebMethod
		public void ingresarOrganizador(String nickname, String nombre, 
				String email, String password, String descripcion, String web) throws NombreUsuarioExistente, EmailRepetido, Exception{
			ICU.ingresarOrganizador( nickname, nombre, 
					 email, password, descripcion, web);
		}
		

		
		
		
		@WebMethod
		public WrapperHashSet<String> listarInstituciones(){
			return new WrapperHashSet<String>(ICU.listarInstituciones());
		}
		
		
		
		
		
		 @WebMethod
		public WrapperHashSet<String> listarUsuarios(){
			 return new WrapperHashSet<String>(ICU.listarUsuarios());
		 }
		 @WebMethod
		public WrapperHashSet<String> listarAsistentes(){
			 return new WrapperHashSet<String>(ICU.listarAsistentes());
		 }
		 @WebMethod
		public WrapperHashSet<String> listarOrganizadores(){
			 return new WrapperHashSet<String>(ICU.listarOrganizadores());
		 }
		 @WebMethod
		public DataUsuario infoUsuario(String nickname) throws UsuarioNoEncontrado{
			 return ICU.infoUsuario(nickname);
		 }
		 @WebMethod
		public WrapperHashSet<String> listarRegistrosAEventos(String nickname){
			 return new WrapperHashSet<String>(ICU.listarRegistrosAEventos(nickname));
		 }
		 @WebMethod
		public WrapperHashSet<String> listarEdicionesOrganizadas(String nickname){
			 return new WrapperHashSet<String>(ICU.listarEdicionesOrganizadas(nickname));
		 }

		
		
		 @WebMethod
		public void agregarAsistente(String nicknameAsistente, String nombreInstitucion) {
			 ICU.agregarAsistente(nicknameAsistente, nombreInstitucion);
		 }
		
		
	
		 @WebMethod
		public void editarAsistente(String nick, String nombre, String apellido, String fechaNac) {
			 LocalDate fechaNacLD = LocalDate.parse(fechaNac);
			 ICU.editarAsistente(nick, nombre, apellido, fechaNacLD);
		 }
		 @WebMethod
		public void editarOrganizador(String nick, String nombre,  String descripcion, String web) {
			 ICU.editarOrganizador(nick, nombre, descripcion, web);
		 }
		
		
		 @WebMethod
		public DTAsistente infoAsistente(String nickname) {
			 return ICU.infoAsistente(nickname);
		 }
		 @WebMethod
		public DTOrganizador infoOrganizador(String nickname) {
			 return ICU.infoOrganizador(nickname);
		 }
		 @WebMethod
		public void altaInstitucion(String nombre, String descripcion, String web) throws NombreInstiExistente, Exception{
			 ICU.altaInstitucion(nombre, descripcion, web);
		 }
		 @WebMethod
		public DataUsuario iniciarSesionNickname(String nickname, String password) {
			DataUsuario result = ICU.iniciarSesionNickname(nickname, password);
			
			
			return result;
		 }
		 
		 @WebMethod
		public DataUsuario iniciarSesionEmail(String email, String password)  {
			DataUsuario result = ICU.iniciarSesionEmail(email, password);
		
			return result;
		 }
		 @WebMethod
		public String obtenerInstitucionAsistente(String nickname) {
			 return ICU.obtenerInstitucionAsistente(nickname);}
	    
		 @WebMethod
		 public boolean existeNickname(String nickname) {
			 return ICU.existeNickname(nickname);
		 }
		 
		 @WebMethod
		 public boolean existeEmail(String email) {
			 return ICU.existeEmail(email);
		 }
	    
	    
	    

        @WebMethod
        public void seguirUsuario(String followerNickname, String followedNickname) throws UsuarioNoEncontrado {
			ICU.seguirUsuario(followerNickname, followedNickname);
		}
	    
	    @WebMethod
	    public int cantidadSeguidores(String nickname) throws UsuarioNoEncontrado {
	    	return ICU.cantidadSeguidores(nickname);
	    }
	    
	    @WebMethod
	    public int cantidadSeguidos(String nickname) throws UsuarioNoEncontrado {
	    	return ICU.cantidadSeguidos(nickname);
	    }
	    
	    @WebMethod
	    public void dejarDeSeguirUsuario(String followerNickname, String followedNickname) throws UsuarioNoEncontrado {
	    	ICU.dejarDeSeguirUsuario(followerNickname, followedNickname);
	    }
	    
	    @WebMethod
	    public boolean esSeguidor(String followerNickname, String followedNickname) throws UsuarioNoEncontrado {
	    	return ICU.esSeguidor(followerNickname, followedNickname);
	    }
	
	

	
	
	
	
	
	
	
	
	
}