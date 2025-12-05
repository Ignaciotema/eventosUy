package casosPrueba;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import excepciones.NombreEventoExcepcion;
import logica.controllers.IControllerEvento;
import logica.data_types.DTDetalleEvento;
import logica.enumerators.EstadoEdicion;
import logica.manejadores.ManejadorCategoria;
import logica.manejadores.ManejadorEdicion;
import logica.manejadores.ManejadorEvento;
import logica.manejadores.ManejadorUsuario;
import logica.models.Categoria;
import logica.models.Edicion;
import logica.models.Evento;
import logica.models.Factory;
import logica.models.Organizador;

public class TestEstadoEdiciones {
	
	@Test
	public void testEstadoEdiciones() throws NombreEventoExcepcion, Exception {
		//Edicion(String nombre, String sigla, LocalDate fechaInicio, LocalDate fechaFin, 
		//LocalDate fechaAlta, String ciudad, String pais,Evento evento,Organizador organizador)
		
		
		
		ManejadorEdicion h_edicion = ManejadorEdicion.getInstance();
		IControllerEvento ICE = Factory.getInstance().getControllerEvento();
		Organizador org = new Organizador("Vegetta","Samuel","samu@gmail.com","a","desc1","www.vegetta.com");
		ManejadorUsuario.getInstance().agregarUsuario(org);
		HashSet<String> categorias = new HashSet<String>();
		categorias.add("cat1");
		Categoria cat = new Categoria("cat1");
		ManejadorCategoria.getInstance().agregarCategoria(cat);
		
		ICE.altaEvento("test", "T", LocalDate.of(1999, 1, 1), "habemus testing", categorias,"");
		Evento evento = ManejadorEvento.getInstance().obtenerEvento("test");
		assertTrue(evento != null); //checkeo por las dudas que se haya creado bien el evento
		
		// Ahora agregamos 3 ediciones: una ingresada, una rechazada y otra confirmada
		
		//Ingresada,
		//Confirmada,
		//Rechazada
		
		Edicion ed1 = new Edicion("edicion1", "E1", LocalDate.of(2024, 12, 1), LocalDate.of(2024, 12, 10), 
				LocalDate.of(2024, 6, 1), "ciudad1", "pais1", evento, org);
		Edicion ed2 = new Edicion("edicion2", "E2", LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 10), 
				LocalDate.of(2024, 5, 1), "ciudad2", "pais2", evento, org);
		Edicion ed3 = new Edicion("edicion3", "E3", LocalDate.of(2024, 11, 15), LocalDate.of(2024, 11, 25), 
				LocalDate.of(2024, 5, 15), "ciudad3", "pais3", evento, org);
		
		h_edicion.agregarEdicionIngresada(ed1);
	    h_edicion.agregarEdicionIngresada(ed2);
	    h_edicion.agregarEdicionIngresada(ed3);
	    
	    // Checkeamos que se agreguen bien
	    assertTrue(h_edicion.existeEdicion("edicion1"));
	    assertTrue(h_edicion.existeEdicion("edicion2"));
	    assertTrue(h_edicion.existeEdicion("edicion3"));
	    
	    // Ahora vamos fijarnos que sea posible obtenerlas y que efectivamente sean las mismas
	    Edicion e1 = h_edicion.encontrarEdicion("edicion1");
	    Edicion e2 = h_edicion.encontrarEdicion("edicion2");
	    Edicion e3 = h_edicion.encontrarEdicion("edicion3");
	    
	    assertTrue(e1 != null && e1.getNombre().equals("edicion1") && e1.getEstado() == EstadoEdicion.Ingresada);
	    assertTrue(e2 != null && e2.getNombre().equals("edicion2") && e2.getEstado() == EstadoEdicion.Ingresada);
	    assertTrue(e3 != null && e3.getNombre().equals("edicion3") && e3.getEstado() == EstadoEdicion.Ingresada);
		
		h_edicion.cambioEstado(e2, EstadoEdicion.Rechazada);
		h_edicion.cambioEstado(e3, EstadoEdicion.Confirmada);
		
		assertTrue(e1.getEstado() == EstadoEdicion.Ingresada);
		assertTrue(e2.getEstado() == EstadoEdicion.Rechazada);
		assertTrue(e3.getEstado() == EstadoEdicion.Confirmada);
		
		// Verificamos que las listas esten bien
		Edicion e2_buscada = h_edicion.encontrarEdicion("edicion2");
		Edicion e3_buscada = h_edicion.encontrarEdicion("edicion3");
		assertTrue(e2_buscada != null && e2_buscada.getEstado() == EstadoEdicion.Rechazada);
		assertTrue(e3_buscada != null && e3_buscada.getEstado() == EstadoEdicion.Confirmada);
		
		
		ICE.altaEdicionDeEvento("test", "Vegetta", "EdTest", "ET", LocalDate.of(2024, 10, 1), LocalDate.of(2024, 10, 10), 
				LocalDate.of(2024, 6, 15), "ciudadTest", "paisTest","");
		ICE.altaEdicionDeEvento("test", "Vegetta", "EdTest2", "ET2", LocalDate.of(2024, 10, 1), LocalDate.of(2024, 10, 10), 
				LocalDate.of(2024, 6, 15), "ciudadTest", "paisTest","");
		ICE.altaEdicionDeEvento("test", "Vegetta", "EdTest3", "ET3", LocalDate.of(2024, 10, 1), LocalDate.of(2024, 10, 10), 
				LocalDate.of(2024, 6, 15), "ciudadTest", "paisTest",""); //estado = ingresada
		
		//checkeamos que se obtenga correctamente el evento a partir de la edicion
		assertEquals("test", ICE.nomEvPorEd("EdTest"));
		
		//checkeamos que se pueda aceptar y rechazar una edicion
		ICE.aceptarEdicion("EdTest","test");
		ICE.rechazarEdicion("EdTest2","test");
		
		// verificamos los cambios de estado
		Edicion edTest = h_edicion.encontrarEdicion("EdTest");
		Edicion edTest2 = h_edicion.encontrarEdicion("EdTest2");
		assertTrue(edTest != null && edTest.getEstado() == EstadoEdicion.Confirmada);
		assertTrue(edTest2 != null && edTest2.getEstado() == EstadoEdicion.Rechazada);
		
		Set<String> confirmadas = ICE.listarEdicionesConfirmadas("test");
		Set<String> rechazadas = ICE.listarEdicionesPendientes("test");
		
		assertTrue(confirmadas.contains("EdTest"));
		assertTrue(rechazadas.contains("EdTest3"));
		
		//ahora probamos que se obtengan las ultimas 3 ediciones
		ICE.altaEvento("evento1", "E1", LocalDate.of(2023, 1, 1), "Descripción del evento 1", categorias,"");
		ICE.altaEvento("evento2", "E2", LocalDate.of(2023, 2, 1), "Descripción del evento 2", categorias,"");
		ICE.altaEvento("evento3", "E3", LocalDate.of(2023, 3, 1), "Descripción del evento 3", categorias,"");
		ICE.altaEvento("evento4", "E4", LocalDate.of(2023, 4, 1), "Descripción del evento 4", categorias,"");

		List<DTDetalleEvento> recientes = ICE.obtenerEventosRecientes();
		assertEquals(4, recientes.size());

		
		Set<String> nombresEventos = new HashSet<>();
		for (DTDetalleEvento ev : recientes) {
		    nombresEventos.add(ev.getNombre());
		}

		assertTrue(nombresEventos.size() == 4);
		
	}
   
}

