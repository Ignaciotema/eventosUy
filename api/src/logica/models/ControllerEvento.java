package logica.models;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import excepciones.AsistenteYaRegistrado;
import excepciones.CupoLLeno;
import excepciones.EventoFinalizadoExcepcion;
import excepciones.FechaInicioPOSTFINAL;
import excepciones.FechaInicioPREALTA;
import excepciones.FechaRegPREALTA;
import excepciones.NombreEdicionExistenteExcepcion;
import excepciones.NombreEventoExcepcion;
import logica.controllers.IControllerEvento;
import logica.data_types.DTAsistente;
import logica.data_types.DTDetalleEdicion;
import logica.data_types.DTDetalleEvento;
import logica.data_types.DTPatrocinio;
import logica.data_types.DTRegistro;
import logica.data_types.DTTipoRegistro;
import logica.enumerators.EstadoEdicion;
import logica.enumerators.NivelPatrocinio;
import logica.manejadores.ManejadorCategoria;
import logica.manejadores.ManejadorEdicion;
import logica.manejadores.ManejadorEvento;
import logica.manejadores.ManejadorUsuario;


public class ControllerEvento implements IControllerEvento{
	
	private static LocalDate fechaSistema = LocalDate.now();
	
	@Override
	public Set<String> listarEventos() {
		ManejadorEvento mEventos = ManejadorEvento.getInstance();
		Map<String, Evento> eventos = mEventos.obtenerEventos(); 
		Set<String> nomEventos = new LinkedHashSet<>();
		for (Evento eve : eventos.values()) {
			if (eve.getFinalizado() == false)	nomEventos.add(eve.getNombre());

		}
		return nomEventos;
	}
	
	@Override
	public Set<String> listarEventosConFinalizados() {
		ManejadorEvento mEventos = ManejadorEvento.getInstance();
		Map<String, Evento> eventos = mEventos.obtenerEventos(); 
		Set<String> nomEventos = new LinkedHashSet<>();
		for (Evento eve : eventos.values()) {
			nomEventos.add(eve.getNombre());

		}
		return nomEventos;
	}
	
	@Override
	public Set<String> listarEdicionesTodas(){
		ManejadorEdicion mEdi = ManejadorEdicion.getInstance();
		Map<String, Edicion> ediciones = mEdi.obtenerEdicionesPendientes();
		ediciones.putAll(mEdi.obtenerEdicionesConfirmadas());
		ediciones.putAll(mEdi.obtenerEdicionesRechazadas());
		Set<String> nomEdiciones = new LinkedHashSet<>();
		for (Edicion edi : ediciones.values()) {
			nomEdiciones.add(edi.getNombre());
		}
		return nomEdiciones;
	}
	
	public void altaEvento(String nombre, String sigla, LocalDate fechaAlta, String descripcion, Set<String> categorias,String url)throws NombreEventoExcepcion, Exception {
		
		ManejadorEvento mEventos = ManejadorEvento.getInstance();
		if (mEventos.existeEvento(nombre)) {
			throw new Exception("El evento ya existe");
		}else {
			if(url=="") {
			Evento nuevoEvento= new Evento(nombre, sigla, fechaAlta, descripcion);
			ManejadorCategoria mCategoria = ManejadorCategoria.getInstance();
			for (String cat : categorias) {
				nuevoEvento.agregarCategoria(mCategoria.obtenerCategoria(cat));
			}
			mEventos.agregarEvento(nuevoEvento);}
			else {
				Evento nuevoEvento= new Evento(nombre, sigla, fechaAlta, descripcion,url);
				ManejadorCategoria mCategoria = ManejadorCategoria.getInstance();
				for (String cat : categorias) {
					nuevoEvento.agregarCategoria(mCategoria.obtenerCategoria(cat));
				}
				mEventos.agregarEvento(nuevoEvento);
			}
		}
	}
		
	
	@Override
	public Set<String> listarCategorias() {
		ManejadorCategoria mCategoria = ManejadorCategoria.getInstance();
		return mCategoria.obtenernombresCategorias();
	}
	

	@Override
	public Set<String> listarEdiciones(String nombreEvento) {
		
		ManejadorEvento h_evento = ManejadorEvento.getInstance();
		Evento eve = h_evento.obtenerEvento(nombreEvento);
		Set<String> ediciones = new LinkedHashSet<>();
		
		if (eve != null) {
		  ediciones = eve.getEdiciones();
		}
		return ediciones;
	}

	@Override
	public Set<String> listarPatrocinios(String nombreEdi) {		
		return ManejadorEdicion.getInstance().encontrarEdicion(nombreEdi).getPatrocinios();
	}

	@Override
	public DTPatrocinio obtenerPatrocinio(String nombreEdi, String nombreInstitucion) {
		ManejadorEdicion mEdi = ManejadorEdicion.getInstance();
		Edicion edi = mEdi.encontrarEdicion(nombreEdi);
		return edi.getPatrocinio(nombreInstitucion);
	}

	@Override
	public DTDetalleEdicion mostrarDetallesEdicion(String nombreEdi) {
		ManejadorEdicion mEdi = ManejadorEdicion.getInstance();
		Edicion edi = mEdi.encontrarEdicion(nombreEdi);
		System.out.println(edi);
		DTDetalleEdicion dtEdi = edi.devolverDT();
		Organizador org = edi.getOrganizador();
		dtEdi.setOrganizador(org != null ? org.getNickname() : null);
		return dtEdi;
	}

	@Override
	public DTTipoRegistro verDetalleTRegistro(String nombreEdi, String nomTRegistro) {
		ManejadorEdicion h_edicion = ManejadorEdicion.getInstance();
		Edicion edi = h_edicion.encontrarEdicion(nombreEdi);
		TipoRegistro tRegis = edi.getTipoRegistro(nomTRegistro);
		return tRegis.infoTipoRegistro();
	}

	@Override
	public void altaTipoDeRegistro(String nombreEdi, String nombre, String desc, Float costo, int cupo) throws excepciones.TipoRegistroExistenteExcepcion, Exception {
		
		ManejadorEdicion h_edicion = ManejadorEdicion.getInstance();
		Edicion edi = h_edicion.encontrarEdicion(nombreEdi);
		if (!edi.existeTipoRegistro(nombre)) {
			edi.crearTRegistro(nombre, desc, costo, cupo);
		} else {
			throw new excepciones.TipoRegistroExistenteExcepcion("Ya existe un tipo registro con este nombre");
		}
	
	}

	@Override
	public DTDetalleEvento verDetalleEvento(String nombreEvento) throws EventoFinalizadoExcepcion {
	  ManejadorEvento mEventos = ManejadorEvento.getInstance();
	  Evento eve = mEventos.obtenerEvento(nombreEvento);
	  
	 /* if (eve.getFinalizado()) {
		  throw new EventoFinalizadoExcepcion("El evento se encuentra finalizado");
	  } */
	  
	  DTDetalleEvento dtE = eve.devolverDT();
	  return dtE;		
	}
	
	@Override
	public Set<String> listarTiposDeRegistro(String nombreEdi) {
		ManejadorEdicion mEdi = ManejadorEdicion.getInstance();
		Edicion edi = mEdi.encontrarEdicion(nombreEdi);
	    Set<String> tiposReg = new HashSet<>();
	    if (edi == null) {
	        return tiposReg; // o lanzar una excepción si prefieres
	    }
	    for (TipoRegistro tipo_reg : edi.getTiposRegistro()) {
	    	tiposReg.add(tipo_reg.getNombre());
	    }
		return tiposReg;
	}
	
@Override
	public DTRegistro infoRegistro(String edicion, String usuario) {
		ManejadorEdicion mEdi = ManejadorEdicion.getInstance();
		Edicion edi = mEdi.encontrarEdicion(edicion);
		
		ManejadorUsuario mUsuer = ManejadorUsuario.getInstance();
		Asistente usu = mUsuer.obtenerAsistente(usuario);
		
		Registro reg = usu.getRegistro(edi);
		DTRegistro dtR = new DTRegistro(reg.getFechaRegistro(), edi.getNombre(), usu.getNickname(), reg.getCosto(), reg.getTipoReg().getNombre(), reg.getAsistencia());
		return dtR;
	}

	@Override
	public void altaEdicionDeEvento(String nombreEvento, String nicknameOrganizador, String nombre, String sigla, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta, String ciudad, String pais,String url)throws NombreEdicionExistenteExcepcion, FechaInicioPOSTFINAL, FechaInicioPREALTA, Exception {
		ManejadorEdicion mEdi = ManejadorEdicion.getInstance();
		if (mEdi.existeEdicion(nombre)) throw new NombreEdicionExistenteExcepcion("Ya existe una edicion con el nombre: " + nombre);
		if (fechaInicio.isAfter(fechaFin)) throw new FechaInicioPOSTFINAL("La fecha de inicio no puede ser posterior a la fecha de finalizacion");
		if (fechaInicio.isBefore(fechaAlta)) throw new FechaInicioPREALTA(" La fecha de inicio no puede ser anterior a la fecha de alta de edicion");
		
		ManejadorEvento mEve = ManejadorEvento.getInstance();
		ManejadorUsuario mUsuer = ManejadorUsuario.getInstance();
		Evento eve = mEve.obtenerEvento(nombreEvento);
		
		if  (eve == null) throw new IllegalArgumentException("No existe el evento: " + nombreEvento);
		if (fechaInicio.isBefore(eve.getFechaAlta())) throw new FechaInicioPREALTA(" La fecha de inicio no puede ser anterior a la fecha de alta del evento");
		if (fechaAlta.isBefore(eve.getFechaAlta())) throw new FechaInicioPREALTA(" La fecha de alta de edicion no puede ser anterior a la fecha de alta del evento");
		
		Organizador org = mUsuer.obtenerOrganizador(nicknameOrganizador);
		org.agregarEdicion(nombre);
        if (url=="") {
		Edicion nueva = new Edicion(nombre, sigla, fechaInicio, fechaFin, fechaAlta, ciudad, pais, eve, org);
		eve.agregarEdicion(nueva); 
		mEdi.agregarEdicionIngresada(nueva);}
        else {
			Edicion nueva = new Edicion(nombre, sigla, fechaInicio, fechaFin, fechaAlta, ciudad, pais, eve, org,url);
			eve.agregarEdicion(nueva); 
			mEdi.agregarEdicionIngresada(nueva);
		}
	
		
	}
	
	public void confirmarRechazarEdicion(String nombreEdi, boolean aceptar) {
		ManejadorEdicion mEdi = ManejadorEdicion.getInstance();
		Edicion edi = mEdi.encontrarEdicion(nombreEdi);
		if (aceptar && edi.getEstado() == EstadoEdicion.Ingresada) {
			edi.setEstado(EstadoEdicion.Confirmada);
			mEdi.agregarEdicionIngresada(edi);
		} else {
			edi.setEstado(EstadoEdicion.Rechazada);
			//evaluar si hay que eliminar de alguna coleccion
		}
	}
	
	@Override
	public void ingresarCategoria(String string) {
		ManejadorCategoria mCategoria = ManejadorCategoria.getInstance();
		mCategoria.agregarCategoria(new Categoria(string));
	}
	
	@Override
	public List<DTAsistente> listarAsistentesAEdicionDeEvento(String nomEdi) {
		ManejadorEdicion mEdi = ManejadorEdicion.getInstance();
		Edicion edi = mEdi.encontrarEdicion(nomEdi);
		return edi.obtenerAsistentes();
	}
	
	@Override
	public void elegirAsistenteYTipoRegistro(String nickAsistente, String tipoReg, String nomEdi,boolean esGratis) throws FechaInicioPREALTA, CupoLLeno, AsistenteYaRegistrado, Exception { 
		//asumo que nomEdi viene de la interfaz en memoria
			
		ManejadorEdicion mEdi = ManejadorEdicion.getInstance();
		Edicion edi = mEdi.encontrarEdicion(nomEdi);
		Evento eve = edi.getEvento();
		if (eve.getFechaAlta().isAfter(fechaSistema)) throw new FechaRegPREALTA("La edicion no se encuentra habilitada para registros.");
		
		
		if (!edi.verificarCupoTipoReg(tipoReg)) throw new CupoLLeno("No hay cupo disponible para el tipo de registro seleccionado.");
			
	    if (!edi.verificarRegistros(nickAsistente)) throw new AsistenteYaRegistrado("El asistente ya se encuentra registrado en la edicion seleccionada.");
		
		
		
		altaRegistro(nickAsistente, tipoReg, nomEdi,esGratis);
		
		
	}
	
	@Override
	public void altaRegistro(String nickAsistente, String tipoReg, String nombreEdi,boolean esGratis) {
		
		ManejadorEdicion mEdi = ManejadorEdicion.getInstance();
		Edicion edi = mEdi.encontrarEdicion(nombreEdi);
		
		ManejadorUsuario mUsuer = ManejadorUsuario.getInstance();
		Asistente asis = mUsuer.obtenerAsistente(nickAsistente);
		
		edi.crearRegistro(asis, tipoReg,esGratis);
		return;
	}
	
	@Override
	public void altaPatrocinio(String nombreEdi, String institucion, NivelPatrocinio nivel, double aporteEconomico, String tipoRegistroGratis, int cantidadGratis, String codigo) {
	    ManejadorEdicion mEdi = ManejadorEdicion.getInstance();
	    Edicion edi = mEdi.encontrarEdicion(nombreEdi);
	    TipoRegistro tipo_reg = edi.getTipoRegistro(tipoRegistroGratis);
	    
	    Patrocinio pat;
	    if (tipo_reg == null) {
	    	pat = new Patrocinio(
		            fechaSistema,
		            (int) Math.round(aporteEconomico),
		            codigo,
		            0,
		            nivel,
		            tipoRegistroGratis
		    );
	    } else {
		    pat = new Patrocinio(
		            fechaSistema,
		            (int) Math.round(aporteEconomico),
		            codigo,
		            cantidadGratis,
		            nivel,
		            tipoRegistroGratis
		    );
	    }
	    

	    edi.agregarPatrocinio(institucion, pat);
	}

	@Override
	public LocalDate getFechaSistema() {
		return fechaSistema;
	}

	@Override
	public LocalDate setFechaSistema(LocalDate fechaNueva) {
		return fechaSistema = fechaNueva;
	}
	
	public String nomEvPorEd(String nomEdi) {
		ManejadorEdicion mEdi = ManejadorEdicion.getInstance();
		Edicion edi = mEdi.encontrarEdicion(nomEdi);
		Evento eve = edi.getEvento();
		return eve.getNombre();
	}
	@Override
	public void aceptarEdicion(String nomedi, String nomev) {
		ManejadorEvento mEventos = ManejadorEvento.getInstance();
		Evento eve = mEventos.obtenerEvento(nomev);
		Edicion edi = eve.getEdicion(nomedi);
		edi.setEstado(EstadoEdicion.Confirmada);
		eve.cambioEstado(edi, EstadoEdicion.Confirmada);
		ManejadorEdicion mEdi = ManejadorEdicion.getInstance();
		mEdi.cambioEstado(edi, EstadoEdicion.Confirmada);
		
		
	}
	@Override
	public void rechazarEdicion(String nomedi, String nomev) {
		ManejadorEvento mEventos = ManejadorEvento.getInstance();
		Evento eve = mEventos.obtenerEvento(nomev);
		Edicion edi = eve.getEdicion(nomedi);
		edi.setEstado(EstadoEdicion.Rechazada);
		eve.cambioEstado(edi, EstadoEdicion.Rechazada);
		ManejadorEdicion mEdi = ManejadorEdicion.getInstance();
		mEdi.cambioEstado(edi, EstadoEdicion.Rechazada);
		
		
	}
	
	@Override
	public Set<String> listarEdicionesConfirmadas(String nombreEvento) {
		
		ManejadorEvento h_evento = ManejadorEvento.getInstance();
		Evento eve = h_evento.obtenerEvento(nombreEvento);
		Set<String> ediciones = new LinkedHashSet<>();
		
		if (eve != null) {
		  for (Edicion edi : eve.getColEdicionesConfirmadas()) {
			  ediciones.add(edi.getNombre());
		  }
		}
		return ediciones;
	}
	
	@Override
	public Set<String> listarEdicionesPendientes(String nombreEvento) {
		
		ManejadorEvento h_evento = ManejadorEvento.getInstance();
		Evento eve = h_evento.obtenerEvento(nombreEvento);
		Set<String> ediciones = new LinkedHashSet<>();
		
		if (eve != null) {
		  for (Edicion edi : eve.getColEdicionesIngresadas()) {
			  ediciones.add(edi.getNombre());
		  }
		}
		return ediciones;
	}

	@Override
	public List<DTDetalleEvento> obtenerEventosRecientes() {
		ManejadorEvento h_evento = ManejadorEvento.getInstance();
		Set<String> eventos = h_evento.obtenerEventos().keySet();
		List<DTDetalleEvento> recientes = new ArrayList<>();
		
		int count = 0;
		//me quedo con las primeras 4, despues hay que ver con cual nos quedamos
		for (String eve : eventos) {
		    if (count >= 4) break;
		    if (!h_evento.obtenerEvento(eve).getFinalizado()) { // saltar eventos finalizados
		    recientes.add(h_evento.obtenerEvento(eve).devolverDT());
		    count++;}
		}
		 
		return recientes;
	}

	@Override
	public void confirmarAsistencia(String nombreEdi, String nickAsistente) {
	    if (nombreEdi == null || nombreEdi.isBlank() ||
	        nickAsistente == null || nickAsistente.isBlank()) {
	        throw new IllegalArgumentException("Faltan parámetros: nombreEdi y/o nickAsistente");
	    }

	    Edicion edi = ManejadorEdicion.getInstance().encontrarEdicion(nombreEdi);
	    if (edi == null) throw new IllegalArgumentException("No existe la edición: " + nombreEdi);

	    Asistente asis = ManejadorUsuario.getInstance().obtenerAsistente(nickAsistente);
	    if (asis == null) throw new IllegalArgumentException("No existe el asistente: " + nickAsistente);

	    Registro reg = asis.getRegistro(edi);
	    if (reg == null) reg = edi.getRegistroDe(nickAsistente);

	    if (reg == null) {
	        throw new IllegalStateException("El asistente " + nickAsistente + " no tiene registro en " + nombreEdi);
	    }

	    if (!Boolean.TRUE.equals(reg.getAsistencia())) {
	        reg.confirmarAsistencia();
	    }
	}

	
	public void finalizarEvento(String nombreEvento) {
		ManejadorEvento mEventos = ManejadorEvento.getInstance();
		Evento eve = mEventos.obtenerEvento(nombreEvento);
		eve.setFinalizado(true);
		return;
	}
	
	// Métodos para estadísticas de eventos visitados
	@Override
	public void registrarVisitaEvento(String nombreEvento) {
		RastreadorVisitasEvento.obtenerInstancia().registrarVisita(nombreEvento);
	}
	
	@Override
	public List<Map<String, Object>> obtenerTop5EventosMasVisitados() {
		return RastreadorVisitasEvento.obtenerInstancia().obtenerTop5Eventos();
	}
	
	@Override
	public Map<String, Long> obtenerEstadisticasVisitas() {
		return RastreadorVisitasEvento.obtenerInstancia().obtenerTodosLosContadores();
	}
	
}