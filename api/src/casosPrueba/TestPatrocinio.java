package casosPrueba;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import excepciones.FechaInicioPOSTFINAL;
import excepciones.FechaInicioPREALTA;
import excepciones.NombreEdicionExistenteExcepcion;
import excepciones.NombreEventoExcepcion;
import logica.controllers.IControllerEvento;
import logica.data_types.DTDetalleEdicion;
import logica.data_types.DTPatrocinio;
import logica.enumerators.EstadoEdicion;
import logica.manejadores.ManejadorCategoria;
import logica.manejadores.ManejadorEdicion;
import logica.models.Edicion;
import logica.models.Factory;

public class TestPatrocinio {
	@Test
	public void testeopat() {
		
		
		try {
			CargaDatos.cargarDatos();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ManejadorCategoria mc = ManejadorCategoria.getInstance();
		Set<String> cats = mc.obtenernombresCategorias();
		
		
		IControllerEvento ICE = Factory.getInstance().getControllerEvento();
		try {
			ICE.altaEvento("Evo1", "E90", java.time.LocalDate.of(2024, 11, 30), "Evento 90 aniversario", cats,"");
		} catch (NombreEventoExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ICE.altaEdicionDeEvento("Evo1", "imm", "E90", "Evo1", java.time.LocalDate.of(2024, 12, 2), java.time.LocalDate.of(2024, 12, 30), java.time.LocalDate.of(2024, 12, 1), "CiudadX", "PaisY","");
		} catch (NombreEdicionExistenteExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FechaInicioPOSTFINAL e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FechaInicioPREALTA e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			ICE.altaTipoDeRegistro( "E90", "pinguinesco", "DescTipoRegGratis", 0.0f, 100);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		ICE.altaPatrocinio("E90", "ORT Uruguay", logica.enumerators.NivelPatrocinio.Oro, 1000.0,"pinguinesco", 0, "COD123");
	
	    ManejadorEdicion me = ManejadorEdicion.getInstance();
	    Edicion ed = me.encontrarEdicion("E90");
	    DTPatrocinio pat = ed.getPatrocinio("ORT Uruguay");
	    System.out.println(pat.getCodigo());
	    System.out.println(pat.getFecha());
	    System.out.println(pat.getMonto());
	    System.out.println(pat.getNivelPatrocinio());
	    
	    assertEquals(true, pat.getFecha().equals(ICE.getFechaSistema()));
	    assertEquals(true, pat.getCodigo().equals("COD123"));
	    assertEquals(true, pat.getNivelPatrocinio().equals(logica.enumerators.NivelPatrocinio.Oro));
	    assertEquals(true, pat.getMonto() == 1000.0);
	    assertEquals(true, pat.getTipoRegistroGratis().equals("pinguinesco"));
	    assertEquals(true, pat.getCantRegsGratis() == 0);
	    DTPatrocinio dtpa=ICE.obtenerPatrocinio("E90", "ORT Uruguay");
	   
	    
	    
	    
	    
	    
	    
	    assertEquals(true, dtpa.getFecha().equals(pat.getFecha()));
	    assertEquals(true, dtpa.getCodigo().equals(pat.getCodigo()));
	    assertEquals(true, dtpa.getNivelPatrocinio().equals(pat.getNivelPatrocinio()));
	    assertEquals(true, dtpa.getMonto() == pat.getMonto());
	    assertEquals(true, dtpa.getTipoRegistroGratis().equals(pat.getTipoRegistroGratis ()));
	    assertEquals(true, dtpa.getCantRegsGratis() == pat.getCantRegsGratis());		
	    
	    
	    
	    Set<String>ins= new HashSet<>();
	    ins.add("ORT Uruguay");
	    
	  
	    DTDetalleEdicion dte= new DTDetalleEdicion("E90", "Evo1", java.time.LocalDate.of(2024, 12, 2), java.time.LocalDate.of(2024, 12, 30), java.time.LocalDate.of(2024, 12, 1), "CiudadX", "PaisY", "imm", cats, ins,EstadoEdicion.Ingresada);
	    DTDetalleEdicion dted=ICE.mostrarDetallesEdicion("E90");
	    
	    System.out.println(dted.getNombre());
	    System.out.println(dted.getSigla());
	    System.out.println(dted.getCiudad());
	    System.out.println(dted.getPais());
	    System.out.println(dted.getFechaAlta());
	    System.out.println(dted.getFechaFin());
	    System.out.println(dted.getFechaInicio());
	    System.out.println(dted.getOrganizador());
	    System.out.println(dted.getNombresInstituciones());
	   
	    
	    System.out.println(dte.getNombre());
	    System.out.println(dte.getSigla());
	    System.out.println(dte.getCiudad());
	    System.out.println(dte.getPais());
	    System.out.println(dte.getFechaAlta());
	    System.out.println(dte.getFechaFin());
	    System.out.println(dte.getFechaInicio());
	    System.out.println(dte.getOrganizador());
	    System.out.println(dte.getNombresInstituciones());
	    
	    
	    
	    
	    
	    
	    assertEquals(true, dted.getSigla().equals(dte.getSigla()));
	    assertEquals(true, dted.getCiudad().equals(dte.getCiudad()));
	    assertEquals(true, dted.getPais().equals(dte.getPais()));
	    assertEquals(true, dted.getFechaAlta().equals(dte.getFechaAlta()));
	    assertEquals(true, dted.getFechaFin().equals(dte.getFechaFin()));
	    assertEquals(true, dted.getFechaInicio().equals(dte.getFechaInicio()));
	    assertEquals(true, dted.getNombre().equals(dte.getNombre()));
	    assertEquals(true, dted.getNombresInstituciones().equals(dte.getNombresInstituciones()));
	    
	    
	    
	    
	    
	    
	
	
	}
	
	
	
	
}