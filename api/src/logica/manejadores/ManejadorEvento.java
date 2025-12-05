package logica.manejadores;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import logica.models.Evento;


public class ManejadorEvento{
	
	private static ManejadorEvento instance = null;
	private Map<String, Evento> eventos;
	
	public static ManejadorEvento getInstance() {
		if (instance == null) {
			instance = new ManejadorEvento();
		}
		return instance;
	}
	
	private ManejadorEvento() {
		eventos = new HashMap<String, Evento>();
	}
	
	
	public Map<String, Evento> obtenerEventos() {
		return eventos;
	}
	
	public Set<String> obtenerNombresEventos() {
		return new HashSet<String>(eventos.keySet());
	}
	
	public Evento obtenerEvento(String nombreEvento) {
		return eventos.get(nombreEvento);
	}
	
	public void agregarEvento(Evento eve) {
		eventos.put(eve.getNombre(), eve);
	}
	public boolean existeEvento(String nombreEvento) {
		return eventos.containsKey(nombreEvento);
	}
}
