package logica.controllers;

import excepciones.AsistenteYaRegistrado;
import excepciones.CupoLLeno;
import excepciones.EventoFinalizadoExcepcion;
import excepciones.FechaInicioPOSTFINAL;
import excepciones.FechaInicioPREALTA;
import excepciones.FechaRegPREALTA;
import excepciones.NombreEdicionExistenteExcepcion;
import excepciones.NombreEventoExcepcion;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import logica.data_types.DTAsistente;
import logica.data_types.DTDetalleEdicion;
import logica.data_types.DTDetalleEvento;
import logica.data_types.DTPatrocinio;
import logica.data_types.DTRegistro;
import logica.data_types.DTTipoRegistro;
import logica.enumerators.NivelPatrocinio;

/**. 
 * Interfaz de ControllerEvento 
 * */
public interface IControllerEvento {
	
	public void altaEvento(String nombre, String sigla, LocalDate fechaAlta, String descripcion, Set<String> categorias,String url)throws NombreEventoExcepcion, Exception;
	public Set<String> listarEventos();
	public Set<String> listarCategorias();
	public Set<String> listarEdiciones(String nombreEvento);
	public Set<String> listarPatrocinios(String nombreEdi);
	public DTPatrocinio obtenerPatrocinio(String nombreEdi, String nombreInstitucion);
	public List<DTDetalleEvento> obtenerEventosRecientes();
	public DTDetalleEdicion mostrarDetallesEdicion(String nombreEdi);
	public DTTipoRegistro verDetalleTRegistro(String nombreEdi, String nomTRegistro);
	public void altaTipoDeRegistro(String nombreEdi, String nombre, String descripcion, Float costo, int cupo) throws excepciones.TipoRegistroExistenteExcepcion, Exception;
	public DTDetalleEvento verDetalleEvento(String nombreEvento) throws EventoFinalizadoExcepcion;
	public Set<String> listarTiposDeRegistro( String nombreEdicion);
	public void altaEdicionDeEvento(String nombreEvento, String nicknameOrganizador, String nombre, String sigla, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta, String ciudad, String pais,String url) throws NombreEdicionExistenteExcepcion, FechaInicioPOSTFINAL, FechaInicioPREALTA, Exception;
	public void ingresarCategoria(String string);
	public DTRegistro infoRegistro(String edicion, String usuario);
	List<DTAsistente> listarAsistentesAEdicionDeEvento(String nomEdi);
	void elegirAsistenteYTipoRegistro(String nickAsistente, String tipoReg, String nomEdi,boolean esGratis) throws FechaRegPREALTA, CupoLLeno, AsistenteYaRegistrado, Exception;
	void altaRegistro(String nickAsistente, String tipoReg, String nombreEdi,boolean esGratis);
	void altaPatrocinio(String nombreEdi, String institucion, NivelPatrocinio nivel, double aporteEconomico, String tipoRegistroGratis, int cantidadGratis, String codigo);
	public LocalDate getFechaSistema();
	public LocalDate setFechaSistema(LocalDate fechaNueva);
	public String nomEvPorEd(String nomEdi);
	public void aceptarEdicion(String nomEdi, String nomEv);
	public void rechazarEdicion(String nomEdi, String nomEv);
	Set<String> listarEdicionesTodas();
	Set<String> listarEdicionesConfirmadas(String nombreEvento);
	Set<String> listarEdicionesPendientes(String nombreEvento);
	public void finalizarEvento(String nombreEvento);
    void confirmarAsistencia(String nombreEdi, String nickAsistente);
	Set<String> listarEventosConFinalizados();
    
    // Métodos para estadísticas de eventos visitados
    void registrarVisitaEvento(String nombreEvento);
    List<Map<String, Object>> obtenerTop5EventosMasVisitados();
    Map<String, Long> obtenerEstadisticasVisitas();
	
	
}