package logica.manejadores;

import java.util.HashMap;
import java.util.Map;

import logica.enumerators.EstadoEdicion;
import logica.models.Edicion;

public class ManejadorEdicion {
	private static ManejadorEdicion instance;
	private Map<String, Edicion> colEdicionesIngresadas;
	private Map<String, Edicion> colEdicionesConfirmadas;
	private Map<String, Edicion> colEdicionesRechazadas;

	private ManejadorEdicion() {
		colEdicionesIngresadas = new HashMap<String, Edicion>();
		colEdicionesConfirmadas = new HashMap<String, Edicion>();
		colEdicionesRechazadas = new HashMap<String, Edicion>();
	}
	
	public static ManejadorEdicion getInstance() {
		if (instance == null) {
			instance = new ManejadorEdicion();
		}
		return instance;
	}

	// Verifica si existe una edición en alguna de las colecciones
	public boolean existeEdicion(String nombreEdicion) {
		return colEdicionesIngresadas.containsKey(nombreEdicion)
			|| colEdicionesConfirmadas.containsKey(nombreEdicion)
			|| colEdicionesRechazadas.containsKey(nombreEdicion);
	}

	// Obtener todas las ediciones Pendientes
	public Map<String, Edicion> obtenerEdicionesPendientes() {
		return colEdicionesIngresadas;
	}

	// Obtener todas las ediciones confirmadas
	public Map<String, Edicion> obtenerEdicionesConfirmadas() {
		return colEdicionesConfirmadas;
	}

	// Obtener todas las ediciones rechazadas
	public Map<String, Edicion> obtenerEdicionesRechazadas() {
		return colEdicionesRechazadas;
	}

	// Buscar una edición por nombre en todas las colecciones
	public Edicion encontrarEdicion(String nombreEdi) {
		if (colEdicionesIngresadas.containsKey(nombreEdi)) {
			return colEdicionesIngresadas.get(nombreEdi);
		} else if (colEdicionesConfirmadas.containsKey(nombreEdi)) {
			return colEdicionesConfirmadas.get(nombreEdi);
		} else if (colEdicionesRechazadas.containsKey(nombreEdi)) {
			return colEdicionesRechazadas.get(nombreEdi);
		}
		return null;
	}

	// Agregar edición a Pendientes
	public void agregarEdicionIngresada(Edicion nueva) {
		colEdicionesIngresadas.put(nueva.getNombre(), nueva);
	}

	public void cambioEstado(Edicion edi, EstadoEdicion nuevoestado) {
		colEdicionesIngresadas.remove(edi.getNombre());
		if (nuevoestado == EstadoEdicion.Confirmada) {
			colEdicionesConfirmadas.put(edi.getNombre(), edi);
			edi.setEstado(EstadoEdicion.Confirmada);
		} else if (nuevoestado == EstadoEdicion.Rechazada) {
			colEdicionesRechazadas.put(edi.getNombre(), edi);
			edi.setEstado(EstadoEdicion.Rechazada);
		}
		
	}
}