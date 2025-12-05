package casosPrueba;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.HashSet;

import org.junit.Test;

import logica.models.Factory;
import logica.models.Organizador;
import logica.controllers.IControllerUsuario;
import logica.data_types.DataUsuario;
import logica.data_types.DataUsuario.TipoUsuario;
import logica.manejadores.ManejadorUsuario;

public class TestControllerUsuario {
		
	@Test
	public void testAltaUsuarioInstitucion() throws Exception {

		IControllerUsuario ICU = Factory.getInstance().getControllerUsuario();
		ManejadorUsuario mU = ManejadorUsuario.getInstance();
		
		//TEST ALTA DE ASISTENTE
		try {
			ICU.altaInstitucion("Instituto Tecnologico", "Instituto de tecnologia de punta", "www.it.com");
			ICU.ingresarAsistente("ignaciotema", "Ignacio", "ignaciotema@gmail.com","a", "Tejera", LocalDate.of(2006, 02, 18));
			ICU.agregarAsistente("ignaciotema", "Instituto Tecnologico");
			ICU.ingresarAsistente("ignaciotema", "Nombre", "correo@gmail.com","a", "Apellido", LocalDate.of(2000, 01, 01));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ICU.ingresarAsistente("otroNick", "Nombre", "ignaciotema@gmail.com","a", "Apellido", LocalDate.of(2000, 01, 01));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		assertEquals(ICU.infoUsuario("ignaciotema").getNickname(), "ignaciotema");
		assertEquals(ICU.infoUsuario("ignaciotema").getNombre(), "Ignacio");
		assertEquals(ICU.infoUsuario("ignaciotema").getEmail(), "ignaciotema@gmail.com");
		assertEquals(ICU.infoUsuario("ignaciotema").getTipo(), TipoUsuario.ASISTENTE);
		assertEquals(mU.obtenerAsistente("ignaciotema").getInstitucion().getNombre(), "Instituto Tecnologico");
		assertEquals(ICU.infoUsuario("ignaciotema").getEmail(), ICU.infoUsuario(ManejadorUsuario.getInstance().obtenerUsuarioPorEmail("ignaciotema@gmail.com").getNickname()).getEmail());
		
		
		//TEST ALTA DE INSTITUCION
		try {
			ICU.altaInstitucion("Instituto de Artes", "Descripcion diferente", "www.it.com");
			ICU.altaInstitucion("Instituto de Artes", "Instituto de tecnologia de punta", "www.it.com");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HashSet<String> institucionesSet = new HashSet<String>();
		institucionesSet.add("Instituto Tecnologico");
		institucionesSet.add("Instituto de Artes");
		assertEquals(true, ICU.listarInstituciones().equals(institucionesSet));
		
		
		
		//TEST ALTA DE ORGANIZADOR
		try {
			ICU.ingresarOrganizador("nachito", "Ignacio", "nachito@gmail.com","a", "descripcion generica 123", "www.nachito.com");
			ICU.ingresarOrganizador("nachito", "Nombre", "correo@gmail.com","a", "descripcion generica 123", "www.nachito.com");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ICU.ingresarOrganizador("otroNick", "Nombre", "nachito@gmail.com","a", "Apellido", "www.nachito.com");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(ICU.infoUsuario("nachito").getNickname(), "nachito");
		assertEquals(ICU.infoUsuario("nachito").getNombre(), "Ignacio");
		assertEquals(ICU.infoUsuario("nachito").getEmail(), "nachito@gmail.com");
		assertEquals(ICU.infoUsuario("nachito").getTipo(), TipoUsuario.ORGANIZADOR);
		assertEquals(ICU.infoOrganizador("nachito").getDescripcion(), "descripcion generica 123");
	
		
		try {
			ICU.ingresarOrganizador("ignaciotema", "Nombre", "correo232@gmail.com","a","desc", "www.web.com");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			ICU.ingresarAsistente("jorge", "Jorge", "jorge@gmail.com","a", "Gonzalez", LocalDate.of(2005, 05, 05));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		HashSet<String> usuariosSet = new HashSet<String>();
		usuariosSet.add("ignaciotema");
		usuariosSet.add("nachito");
		usuariosSet.add("jorge");
		assertEquals(true, ICU.listarUsuarios().equals(usuariosSet));
		
		HashSet<String> asistentesSet = new HashSet<String>();
		asistentesSet.add("ignaciotema");
		asistentesSet.add("jorge");
		assertEquals(true, ICU.listarAsistentes().equals(asistentesSet));
		
		
		CargaDatos.cargarDatos();
		
		

		// TEST LISTAR REGISTROS A EDICIONES
		HashSet<String> registros = new HashSet<String>();
		registros.add("Maratón de Montevideo 2025");
		registros.add("Maratón de Montevideo 2024");
		registros.add("Montevideo Rock 2025");
		
		assertEquals(true, ICU.listarRegistrosAEventos("sofirod").equals(registros));
		
		
		
		
		//TEST LISTAR EDICIONES ORGANIZADAS
		HashSet<String> ediciones = new HashSet<String>();
		ediciones.add("Maratón de Montevideo 2025");
		ediciones.add("Maratón de Montevideo 2024");
		ediciones.add("Montevideo Rock 2025");
		ediciones.add("Maratón de Montevideo 2022");
		
		assertEquals(true, ICU.listarEdicionesOrganizadas("imm").equals(ediciones));
		
	
	
	}
	
}
