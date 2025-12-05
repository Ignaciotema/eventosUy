package webservices;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import excepciones.AsistenteYaRegistrado;
import excepciones.CupoLLeno;
import excepciones.EventoFinalizadoExcepcion;
import excepciones.FechaInicioPOSTFINAL;
import excepciones.FechaInicioPREALTA;
import excepciones.NombreEdicionExistenteExcepcion;
import excepciones.NombreEventoExcepcion;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.ws.Endpoint;
import logica.controllers.IControllerEvento;
import logica.data_types.DTAsistente;
import logica.data_types.DTDetalleEdicion;
import logica.data_types.DTDetalleEvento;
import logica.data_types.DTPatrocinio;
import logica.data_types.DTRegistro;
import logica.data_types.DTTipoRegistro;
import logica.enumerators.NivelPatrocinio;
import logica.models.Factory;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class publicadorEvento {
	private Endpoint endpoint = null;
	IControllerEvento ICE = Factory.getInstance().getControllerEvento();
	
    //Constructor
    public publicadorEvento(){}

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
		endpoint = Endpoint.publish(props.getProperty("server.url") + ":" + props.getProperty("server.port") + "/publicadorEvento", this);
    }
    
    @WebMethod(exclude = true)
    public Endpoint getEndpoint() {
            return endpoint;
    }
    
    @WebMethod
	public WrapperHashSet<String> listarEventos() {
		return new WrapperHashSet<String>(ICE.listarEventos());
	}
	
	@WebMethod
	public WrapperHashSet<String> listarEdicionesTodas(){
		return new WrapperHashSet<String>(ICE.listarEdicionesTodas());
	}
	
	public void altaEvento(String nombre, String sigla, String fechaAlta, String descripcion, WrapperHashSet<String> categorias,String url)throws NombreEventoExcepcion, Exception {
		LocalDate fechaA = LocalDate.parse(fechaAlta);
		ICE.altaEvento(nombre, sigla, fechaA, descripcion, categorias.toHashSet(), url);
		
	}
		
	
	@WebMethod
	public WrapperHashSet<String> listarCategorias() {
		return new WrapperHashSet<String>(ICE.listarCategorias());
	}
	

	@WebMethod
	public WrapperHashSet<String> listarEdiciones(String nombreEvento) {
		return new WrapperHashSet<String>(ICE.listarEdiciones(nombreEvento));
	}

	@WebMethod
	public WrapperHashSet<String> listarPatrocinios(String nombreEdi) {		
		return new WrapperHashSet<String>(ICE.listarPatrocinios(nombreEdi));
	}

	@WebMethod
	public DTPatrocinio obtenerPatrocinio(String nombreEdi, String nombreInstitucion) {
		return ICE.obtenerPatrocinio(nombreEdi, nombreInstitucion);
	}

	@WebMethod
	public DTDetalleEdicion mostrarDetallesEdicion(String nombreEdi) {
		return ICE.mostrarDetallesEdicion(nombreEdi);
	}

	@WebMethod
	public DTTipoRegistro verDetalleTRegistro(String nombreEdi, String nomTRegistro) {
		return ICE.verDetalleTRegistro(nombreEdi, nomTRegistro);
	}

	@WebMethod
	public void altaTipoDeRegistro(String nombreEdi, String nombre, String desc, Float costo, int cupo) throws excepciones.TipoRegistroExistenteExcepcion, Exception {
		ICE.altaTipoDeRegistro(nombreEdi, nombre, desc, costo, cupo);
	}

	@WebMethod
	public DTDetalleEvento verDetalleEvento(String nombreEvento) throws EventoFinalizadoExcepcion {
		return ICE.verDetalleEvento(nombreEvento);
	}
	
	@WebMethod
	public WrapperHashSet<String> listarTiposDeRegistro(String nombreEdi) {
		return new WrapperHashSet<String>(ICE.listarTiposDeRegistro(nombreEdi));
	}
	
	@WebMethod
	public DTRegistro infoRegistro(String edicion, String usuario) {
		return ICE.infoRegistro(edicion, usuario);
	}

	@WebMethod
	public void altaEdicionDeEvento(String nombreEvento, String nicknameOrganizador, String nombre, String sigla, String fechaInicio, String fechaFin, String fechaAlta, String ciudad, String pais,String url)throws NombreEdicionExistenteExcepcion, FechaInicioPOSTFINAL, FechaInicioPREALTA, Exception {
		LocalDate fInicio = LocalDate.parse(fechaInicio);
		LocalDate fFin = LocalDate.parse(fechaFin);
		LocalDate fAlta = LocalDate.parse(fechaAlta);
		ICE.altaEdicionDeEvento(nombreEvento, nicknameOrganizador, nombre, sigla, fInicio, fFin, fAlta, ciudad, pais,url);
		
	}
	
	@WebMethod
	public void ingresarCategoria(String string) {
		ICE.ingresarCategoria(string);
	}
	
	
	@WebMethod
	public WrapperHashSet<String> listarAsistentesAEdicionDeEvento(String nomEdi) {
	    List<DTAsistente> asistentes = ICE.listarAsistentesAEdicionDeEvento(nomEdi);
	    Set<String> nicknames = new HashSet<>();
	    
	    for (DTAsistente asistente : asistentes) {
	        nicknames.add(asistente.getNickname());
	    }
	    
	    return new WrapperHashSet<String>(nicknames);
	}

	
	@WebMethod
	public void elegirAsistenteYTipoRegistro(String nickAsistente, String tipoReg, String nomEdi,boolean esGratis) throws FechaInicioPREALTA, CupoLLeno, AsistenteYaRegistrado, Exception { 
		ICE.elegirAsistenteYTipoRegistro(nickAsistente, tipoReg, nomEdi, esGratis);
	}
	
	@WebMethod
	public void altaRegistro(String nickAsistente, String tipoReg, String nombreEdi,boolean esGratis) throws Exception {
		ICE.altaRegistro(nickAsistente, tipoReg, nombreEdi, esGratis);
	}
	
	@WebMethod
	public void altaPatrocinio(String nombreEdi, String institucion, NivelPatrocinio nivel, double aporteEconomico, String tipoRegistroGratis, int cantidadGratis, String codigo) {
	    ICE.altaPatrocinio(nombreEdi, institucion, nivel, aporteEconomico, tipoRegistroGratis, cantidadGratis, codigo);
	}

	@WebMethod
	public String getFechaSistema() {
		return ICE.getFechaSistema().toString();
	}

	@WebMethod
	public LocalDate setFechaSistema(String fechaNueva) {
		LocalDate fechaN = LocalDate.parse(fechaNueva);
		return ICE.setFechaSistema(fechaN);
	}
	
	public String nomEvPorEd(String nomEdi) {
		return ICE.nomEvPorEd(nomEdi);
	}
	
	@WebMethod
	public void aceptarEdicion(String nomedi, String nomev) {
		ICE.aceptarEdicion(nomedi, nomev);	
	}
	
	@WebMethod
	public void rechazarEdicion(String nomedi, String nomev) {
		ICE.rechazarEdicion(nomedi, nomev);
	}
	
	@WebMethod
	public WrapperHashSet<String> listarEdicionesConfirmadas(String nombreEvento) {
		return new WrapperHashSet<String>(ICE.listarEdicionesConfirmadas(nombreEvento));
	}
	
	@WebMethod
	public WrapperHashSet<String> listarEdicionesPendientes(String nombreEvento) {
		return new WrapperHashSet<String>(ICE.listarEdicionesPendientes(nombreEvento));
	}
	
	@WebMethod
	public WrapperHashSet<DTDetalleEvento> obtenerEventosRecientes() {
		return new WrapperHashSet<DTDetalleEvento>(new HashSet<DTDetalleEvento>(ICE.obtenerEventosRecientes()));
	}
	
	@WebMethod
	public void ConfirmarAsistencia(String nickAsistente, String nombreEdi) {
		ICE.confirmarAsistencia(nombreEdi, nickAsistente);
	}
   
    @WebMethod
    public byte[] getFile(@WebParam(name = "fileName") String name)
                    throws  IOException {
        byte[] byteArray = null;
        try {
                File f = new File("files/" + name);
                FileInputStream streamer = new FileInputStream(f);
                byteArray = new byte[streamer.available()];
                streamer.read(byteArray);
        } catch (IOException e) {
                throw e;
        }
        return byteArray;
    }
    @WebMethod
    public void finalizarEvento(String nombreEvento) {
		ICE.finalizarEvento(nombreEvento);
		return;
	}
    
    @WebMethod
    public void registrarVisitaEvento(@WebParam(name = "nombreEvento") String nombreEvento) {
        ICE.registrarVisitaEvento(nombreEvento);
    }
    
}