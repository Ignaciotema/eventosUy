package casosPrueba;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import logica.controllers.IControllerEvento;
import logica.controllers.IControllerUsuario;
import logica.data_types.DTAsistente;
import logica.models.Factory;


public class TestControllerEvento2 {
@Test
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
			ICU.ingresarOrganizador("Jorge","Jorge","d","a","b","c");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ICE.altaEvento("EventoTest", "ET", LocalDate.of(2025, 8, 31), "DescTest", categorias,"");
			ICE.altaEdicionDeEvento("EventoTest", "Jorge", "EdTest", "EDT", LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 2), LocalDate.of(2025, 8, 31), "CiudadTest", "PaisTest","");
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
		ICE.altaPatrocinio("EdTest", "InstTest", logica.enumerators.NivelPatrocinio.Oro, 1000.0,"pinguinesco", 0, "COD123");

		
		
		try {
			ICU.ingresarAsistente("willyrex", "willyrex","l", "z", "b", LocalDate.of(2000, 1, 1));
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
		ICE.altaRegistro("willyrex", "TipoRegTest", "EdTest",false);

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
		List<DTAsistente> asistentes = ICE.listarAsistentesAEdicionDeEvento("EdTest");
		boolean found = false;
		for (DTAsistente a : asistentes) {
			if (a.getNickname().equals("willyrex")) {
				found = true;
				break;
			}
		}
		assertEquals(true, found);
		
		try {
			CargaDatos.cargarDatos();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}}