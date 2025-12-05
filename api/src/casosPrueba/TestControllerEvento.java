package casosPrueba;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.HashSet;

import org.junit.Test;

import excepciones.EmailRepetido;
import excepciones.NombreUsuarioExistente;
import logica.controllers.IControllerEvento;
import logica.controllers.IControllerUsuario;
import logica.manejadores.ManejadorEvento;
import logica.models.Factory;

public class TestControllerEvento {
	
	
	
	@Test
	public void testAltaEventoEdicionCategoria()  {
		
			
	    IControllerUsuario ICU = Factory.getInstance().getControllerUsuario();
		IControllerEvento ICE = Factory.getInstance().getControllerEvento();
		ManejadorEvento mE = ManejadorEvento.getInstance();
		
		ICE.ingresarCategoria("Categoria1");
		ICE.ingresarCategoria("Categoria2");
		ICE.ingresarCategoria("Categoria2");
		
		HashSet<String> categoriasSet = new HashSet<String>();
		categoriasSet.add("Categoria1");
		
		//TEST ALTA DE EVENTO
		try {
			ICE.altaEvento("Evento1", "E1", LocalDate.of(2018, 02, 13), "Descripcion del evento 1", categoriasSet,"");
			ICE.altaEvento("Evento1", "E1", LocalDate.of(2022, 16, 11), "Descripcion del evento 2", categoriasSet,"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(true, mE.existeEvento("Evento1"));
		assertEquals(false, mE.existeEvento("Evento2"));
		assertEquals(mE.obtenerEvento("Evento1").getDescripcion(), "Descripcion del evento 1");
		assertEquals(mE.obtenerEvento("Evento1").getCategorias().size(), 1);
		
		//TEST LISTAR EVENTOS
		HashSet<String> eventosSet = new HashSet<String>();
		eventosSet.add("Evento1");
		assertEquals(true, ICE.listarEventos().equals(eventosSet));
		
		
		//TEST LISTAR CATEGORIAS
		categoriasSet.add("Categoria2");
		assertEquals(true, ICE.listarCategorias().equals(categoriasSet));
		try {
			ICU.ingresarOrganizador("Vegetta", "Samuel", "vegetta@gmail.com","passowrd", "Muy buenas a todos guapisimos", "www.v777.com");
		} catch (NombreUsuarioExistente e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EmailRepetido e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//TEST ALTA DE EDICION
		try {
			ICE.altaEdicionDeEvento("Evento1", "Vegetta", "Edicion1", "ED1", LocalDate.of(2023, 05, 20), LocalDate.of(2023, 05, 25), LocalDate.of(2022, 11, 15), "Ciudad1", "Pais1","www.edicion1.com");
			ICE.altaEdicionDeEvento("Evento2", "Vegetta", "Edicion1", "ED1", LocalDate.of(2023, 05, 20), LocalDate.of(2023, 05, 25), LocalDate.of(2022, 11, 15), "Ciudad1", "Pais1","www.edicion1.com");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(true, mE.obtenerEvento("Evento1").getEdiciones().contains("Edicion1"));
		assertEquals(false, mE.obtenerEvento("Evento1").getEdiciones().contains("Edicion2"));

	try {
		CargaDatos.cargarDatos();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}}
	
	
	/*@Test
	public void testListarMetodosControllerEvento() {
		IControllerEvento ICE = Factory.getInstance().getControllerEvento();
		IControllerUsuario ICU = Factory.getInstance().getControllerUsuario();

		// Alta de categorías
		ICE.ingresarCategoria("CatA");
		ICE.ingresarCategoria("CatB");

		// Alta de evento y edición
		HashSet<String> categorias = new HashSet<>();
		categorias.add("CatA");
		
		try {
			ICU.ingresarOrganizador("Jorge","Jorge","a","b","c");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ICE.altaEvento("EventoTest", "ET", LocalDate.of(2025, 8, 31), "DescTest", categorias);
			ICE.altaEdicionDeEvento("EventoTest", "Jorge", "EdTest", "EDT", LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 2), LocalDate.of(2025, 8, 31), "CiudadTest", "PaisTest");
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		try {
			ICE.altaTipoDeRegistro( "EdTest", "pinguinesco", "DescTipoRegGratis", 0.0f, 100);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		// Alta de patrocinio
		ICE.altaPatrocinio("EdTest", "InstTest", logica.NivelPatrocinio.Oro, 1000.0,"pinguinesco", 0, "COD123");

		
		
		try {
			ICU.ingresarAsistente("willyrex", "willyrex", "z", "b", LocalDate.of(2000, 1, 1));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// Alta de tipo de registro
		
		
		try {
			ICE.altaTipoDeRegistro("EdTest", "TipoRegTest", "DescTipoReg", 50.0f, 100);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Alta de asistente y registro
		ICE.altaRegistro("willyrex", "TipoRegTest", "EdTest");

		// Test listarEventos
		Set<String> eventos = ICE.listarEventos();
		assertEquals(true, eventos.contains("EventoTest"));

		// Test listarCategorias
		Set<String> categoriasList = ICE.listarCategorias();
		assertEquals(true, categoriasList.contains("CatA"));
		assertEquals(true, categoriasList.contains("CatB"));

		// Test listarEdiciones
		Set<String> ediciones = ICE.listarEdiciones("EventoTest");
		assertEquals(true, ediciones.contains("EdTest"));

		// Test listarPatrocinios
		Set<String> patrocinios = ICE.listarPatrocinios("EdTest");
		assertEquals(true, patrocinios.contains("InstTest"));

		// Test listarTiposDeRegistro
		Set<String> tiposReg = ICE.listarTiposDeRegistro("EdTest");
		assertEquals(true, tiposReg.contains("TipoRegTest"));

		// Test listarAsistentesAEdicionDeEvento
		Set<DTAsistente> asistentes = ICE.listarAsistentesAEdicionDeEvento("EdTest");
		boolean found = false;
		for (DTAsistente a : asistentes) {
			if (a.getnickname().equals("willyrex")) {
				found = true;
				break;
			}
		}
		assertEquals(true, found);
	}}
	
	*/




// Cargar datos una sola vez antes de los tests
