package logica.models;

import java.util.*;

/**
 * Clase para rastrear las visitas a los eventos y mantener estadísticas en memoria
 * Los datos se mantienen solo durante la sesión 
 */
public class RastreadorVisitasEvento {
    
    private static RastreadorVisitasEvento instancia = null;
    private Map<String, Long> contadoresVisitas;
    
    private RastreadorVisitasEvento() {
        contadoresVisitas = new HashMap<>();
    }
    
    
    public static RastreadorVisitasEvento obtenerInstancia() {
        if (instancia == null) {
            instancia = new RastreadorVisitasEvento();
        }
        return instancia;
    }
    
    
    public void registrarVisita(String nombreEvento) {
        if (nombreEvento != null && !nombreEvento.trim().isEmpty()) {
            contadoresVisitas.merge(nombreEvento.trim(), 1L, Long::sum);
        }
    }

   
    public List<Map<String, Object>> obtenerTop5Eventos() {
        // Convertir el map a lista de entries para poder ordenar
        List<Map.Entry<String, Long>> listaEntradas = new ArrayList<>();
        for (Map.Entry<String, Long> entry : contadoresVisitas.entrySet()) {
            listaEntradas.add(entry);
        }
        
        // bubble sort insano
        for (int i = 0; i < listaEntradas.size() - 1; i++) {
            for (int j = 0; j < listaEntradas.size() - 1 - i; j++) {
                if (listaEntradas.get(j).getValue() < listaEntradas.get(j + 1).getValue()) {
                    // Intercambiar elementos
                    Map.Entry<String, Long> temp = listaEntradas.get(j);
                    listaEntradas.set(j, listaEntradas.get(j + 1));
                    listaEntradas.set(j + 1, temp);
                }
            }
        }
        
        // Crear lista resultado con máximo 5 elementos
        List<Map<String, Object>> resultado = new ArrayList<>();
        int limite = Math.min(5, listaEntradas.size());
        
        for (int i = 0; i < limite; i++) {
            Map.Entry<String, Long> entrada = listaEntradas.get(i);
            Map<String, Object> infoEvento = new HashMap<>();
            infoEvento.put("nombre", entrada.getKey());
            infoEvento.put("visitas", entrada.getValue());
            resultado.add(infoEvento);
        }
        
        return resultado;
    }
    
   
    public Map<String, Long> obtenerTodosLosContadores() {
        return new HashMap<>(contadoresVisitas);
    }
    
    
    public void limpiarEstadisticas() {
        contadoresVisitas.clear();
    }
}