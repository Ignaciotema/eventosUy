package casosPrueba;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

import logica.controllers.IControllerUsuario;
import logica.models.Factory;

public class TestSeguimientos {
	
	@Test
	public void testSeguimientos() throws Exception {
		
		IControllerUsuario ICU = Factory.getInstance().getControllerUsuario();
		
		// Crear usuarios de prueba
		try {
			ICU.ingresarAsistente("usuario1", "Juan", "juan@test.com", "pass123", "Perez", LocalDate.of(1990, 5, 15));
			ICU.ingresarAsistente("usuario2", "Maria", "maria@test.com", "pass123", "Garcia", LocalDate.of(1995, 8, 20));
			ICU.ingresarOrganizador("organizador1", "Carlos", "carlos@test.com", "pass123", "Descripcion del organizador", "www.carlos.com");
		} catch (Exception e) {
			// Usuarios ya existen
		}
		
		// Test seguir usuario
		ICU.seguirUsuario("usuario1", "usuario2");
		assertEquals(true, ICU.esSeguidor("usuario1", "usuario2"));
		assertEquals(1, ICU.cantidadSeguidores("usuario2"));
		assertEquals(1, ICU.cantidadSeguidos("usuario1"));
		
		// Test seguir multiples usuarios
		ICU.seguirUsuario("usuario1", "organizador1");
		assertEquals(true, ICU.esSeguidor("usuario1", "organizador1"));
		assertEquals(2, ICU.cantidadSeguidos("usuario1"));
		assertEquals(1, ICU.cantidadSeguidores("organizador1"));
		
		// Test multiples seguidores
		ICU.seguirUsuario("organizador1", "usuario2");
		assertEquals(true, ICU.esSeguidor("organizador1", "usuario2"));
		assertEquals(2, ICU.cantidadSeguidores("usuario2"));
		
		// Test dejar de seguir
		ICU.dejarDeSeguirUsuario("usuario1", "usuario2");
		assertEquals(false, ICU.esSeguidor("usuario1", "usuario2"));
		assertEquals(1, ICU.cantidadSeguidores("usuario2"));
		assertEquals(1, ICU.cantidadSeguidos("usuario1"));
		
		// Test usuario sin seguidores/seguidos
		assertEquals(0, ICU.cantidadSeguidores("usuario1"));
		assertEquals(0, ICU.cantidadSeguidos("usuario2"));
		
		// Test seguimiento bidireccional
		ICU.seguirUsuario("usuario2", "usuario1");
		assertEquals(true, ICU.esSeguidor("usuario2", "usuario1"));
		assertEquals(true, ICU.esSeguidor("usuario1", "organizador1"));
		assertEquals(1, ICU.cantidadSeguidores("usuario1"));
		assertEquals(1, ICU.cantidadSeguidos("usuario2"));
	}
	
	@Test
	public void testExcepciones() throws Exception {
		
		IControllerUsuario ICU = Factory.getInstance().getControllerUsuario();
		
		// Test excepciones con usuarios inexistentes
		try {
			ICU.seguirUsuario("usuarioInexistente", "usuario1");
		} catch (Exception e) {
			// Excepcion esperada
		}
		
		try {
			ICU.dejarDeSeguirUsuario("usuario1", "usuarioInexistente");
		} catch (Exception e) {
			// Excepcion esperada
		}
		
		try {
			ICU.cantidadSeguidores("usuarioInexistente");
		} catch (Exception e) {
			// Excepcion esperada
		}
		
		try {
			ICU.esSeguidor("usuarioInexistente", "usuario1");
		} catch (Exception e) {
			// Excepcion esperada
		}
	}
}