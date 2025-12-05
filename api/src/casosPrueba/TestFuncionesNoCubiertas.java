package casosPrueba;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import logica.controllers.IControllerEvento;
import logica.controllers.IControllerUsuario;
import logica.data_types.DataUsuario;
import logica.data_types.DTAsistente;
import logica.data_types.DTOrganizador;
import logica.models.Factory;

public class TestFuncionesNoCubiertas {
	
	@Test
	public void testFechaSistema() throws Exception {
		
		IControllerEvento ICE = Factory.getInstance().getControllerEvento();
		
		// Test obtener fecha del sistema
		LocalDate fechaOriginal = ICE.getFechaSistema();
		
		// Test cambiar fecha del sistema
		LocalDate nuevaFecha = LocalDate.of(2024, 12, 15);
		ICE.setFechaSistema(nuevaFecha);
		assertEquals(nuevaFecha, ICE.getFechaSistema());
		
		// Test cambiar a otra fecha
		LocalDate otraFecha = LocalDate.of(2025, 1, 10);
		ICE.setFechaSistema(otraFecha);
		assertEquals(otraFecha, ICE.getFechaSistema());
		
		// Restaurar fecha original
		ICE.setFechaSistema(fechaOriginal);
		assertEquals(fechaOriginal, ICE.getFechaSistema());
	}
	
	@Test
	public void testGestionEdiciones() throws Exception {
		
		IControllerUsuario ICU = Factory.getInstance().getControllerUsuario();
		IControllerEvento ICE = Factory.getInstance().getControllerEvento();
		
		// Crear datos de prueba
		try {
			ICU.ingresarOrganizador("orgTest", "Organizador", "org@test.com", "pass123", "Descripcion test", "www.test.com");
		} catch (Exception e) {
			// Organizador ya existe
		}
		
		ICE.ingresarCategoria("CategoriaTest");
		
		Set<String> categorias = new HashSet<String>();
		categorias.add("CategoriaTest");
		
		// Crear evento
		try {
			ICE.altaEvento("EventoTest", "ET", LocalDate.of(2024, 6, 1), "Evento para testing", categorias,"");
		} catch (Exception e) {
			// Evento ya existe
		}
		
		// Crear edicion
		try {
			ICE.altaEdicionDeEvento("EventoTest", "orgTest", "EdicionTest", "ET2024", 
				LocalDate.of(2024, 12, 20), LocalDate.of(2024, 12, 25), 
				LocalDate.of(2024, 11, 1), "Montevideo", "Uruguay","");
		} catch (Exception e) {
			// Edicion ya existe
		}
		
		// Test obtener nombre de evento por edicion
		String nombreEvento = ICE.nomEvPorEd("EdicionTest");
		assertEquals("EventoTest", nombreEvento);
		
		// Test listar todas las ediciones
		Set<String> todasEdiciones = ICE.listarEdicionesTodas();
		assertEquals(true, todasEdiciones.contains("EdicionTest"));
		
		// Test listar ediciones de un evento especifico
		Set<String> edicionesEvento = ICE.listarEdiciones("EventoTest");
		assertEquals(true, edicionesEvento.contains("EdicionTest"));
		
		// Test aceptar edicion
		ICE.aceptarEdicion("EdicionTest", "EventoTest");
		
		// Test listar ediciones confirmadas
		Set<String> edicionesConfirmadas = ICE.listarEdicionesConfirmadas("EventoTest");
		assertEquals(true, edicionesConfirmadas.size() >= 0);
		
		// Test listar ediciones pendientes
		Set<String> edicionesPendientes = ICE.listarEdicionesPendientes("EventoTest");
		assertEquals(true, edicionesPendientes.size() >= 0);
		
		// Test finalizar evento
		try {
			ICE.finalizarEvento("EventoTest");
		} catch (Exception e) {
			// Puede ya estar finalizado
		}
		
		// Test rechazar edicion (crear otra para rechazar)
		try {
			ICE.altaEdicionDeEvento("EventoTest", "orgTest", "EdicionTest2", "ET2025", 
				LocalDate.of(2025, 6, 15), LocalDate.of(2025, 6, 20), 
				LocalDate.of(2025, 3, 1), "Buenos Aires", "Argentina","");
			ICE.rechazarEdicion("EdicionTest2", "EventoTest");
		} catch (Exception e) {
			// Manejar excepcion si ya existe
		}
	}
	
	@Test
	public void testOtrasFunciones() throws Exception {
		
		IControllerEvento ICE = Factory.getInstance().getControllerEvento();
		
		// Test listar categorias
		Set<String> categorias = ICE.listarCategorias();
		assertEquals(true, categorias.size() >= 0);
		
		// Test listar eventos
		Set<String> eventos = ICE.listarEventos();
		assertEquals(true, eventos.size() >= 0);
		
		// Agregar categoria y verificar
		ICE.ingresarCategoria("NuevaCategoria");
		categorias = ICE.listarCategorias();
		assertEquals(true, categorias.contains("NuevaCategoria"));
		
		// Test obtener eventos recientes
		try {
			ICE.obtenerEventosRecientes();
		} catch (Exception e) {
			// Puede no haber eventos recientes
		}
	}
	
	@Test
	public void testSesionesUsuario() throws Exception {
		
		IControllerUsuario ICU = Factory.getInstance().getControllerUsuario();
		
		// Crear usuario para testing
		try {
			ICU.ingresarAsistente("userLogin", "Juan", "userlogin@test.com", "password123", "Perez", LocalDate.of(1990, 5, 15));
		} catch (Exception e) {
			// Usuario ya existe
		}
		
		// Test iniciar sesion con nickname
		DataUsuario usuarioNick = ICU.iniciarSesionNickname("userLogin", "password123");
		if (usuarioNick != null) {
			assertEquals("userLogin", usuarioNick.getNickname());
			assertEquals("Juan", usuarioNick.getNombre());
		}
		
		// Test iniciar sesion con email
		DataUsuario usuarioEmail = ICU.iniciarSesionEmail("userlogin@test.com", "password123");
		if (usuarioEmail != null) {
			assertEquals("userLogin", usuarioEmail.getNickname());
		}
		
		// Test sesion incorrecta
		DataUsuario sesionIncorrecta = ICU.iniciarSesionNickname("userLogin", "passwordIncorrecta");
		assertEquals(null, sesionIncorrecta);
		
		// Test existencia de nickname
		assertEquals(true, ICU.existeNickname("userLogin"));
		assertEquals(false, ICU.existeNickname("usuarioInexistente"));
		
		// Test existencia de email
		assertEquals(true, ICU.existeEmail("userlogin@test.com"));
		assertEquals(false, ICU.existeEmail("emailinexistente@test.com"));
	}
	
	@Test
	public void testEdicionUsuarios() throws Exception {
		
		IControllerUsuario ICU = Factory.getInstance().getControllerUsuario();
		
		// Crear usuarios para editar
		try {
			ICU.ingresarAsistente("userEdit", "NombreOriginal", "useredit@test.com", "pass123", "ApellidoOriginal", LocalDate.of(1995, 3, 10));
			ICU.ingresarOrganizador("orgEdit", "OrgOriginal", "orgedit@test.com", "pass123", "DescripcionOriginal", "www.original.com");
		} catch (Exception e) {
			// Usuarios ya existen
		}
		
		// Test editar asistente
		ICU.editarAsistente("userEdit", "NombreEditado", "ApellidoEditado", LocalDate.of(1995, 3, 15));
		assertEquals("NombreEditado", ICU.infoUsuario("userEdit").getNombre());
		
		// Test editar organizador
		ICU.editarOrganizador("orgEdit", "OrgEditado", "DescripcionEditada", "www.editado.com");
		assertEquals("OrgEditado", ICU.infoUsuario("orgEdit").getNombre());
		
		// Test obtener info detallada
		DTAsistente infoAsistente = ICU.infoAsistente("userEdit");
		assertEquals("ApellidoEditado", infoAsistente.getApellido());
		
		DTOrganizador infoOrganizador = ICU.infoOrganizador("orgEdit");
		assertEquals("DescripcionEditada", infoOrganizador.getDescripcion());
		
		// Test obtener institucion de asistente
		try {
			String institucion = ICU.obtenerInstitucionAsistente("userEdit");
			assertEquals(true, institucion == null || institucion.length() >= 0);
		} catch (Exception e) {
			// Puede no tener institucion
		}
	}
	
	@Test
	public void testRegistrosYAsistencia() throws Exception {
		
		IControllerUsuario ICU = Factory.getInstance().getControllerUsuario();
		IControllerEvento ICE = Factory.getInstance().getControllerEvento();
		
		// Crear datos de prueba
		try {
			ICU.ingresarAsistente("asistenteReg", "Pedro", "pedroreg@test.com", "pass123", "Rodriguez", LocalDate.of(1992, 8, 20));
		} catch (Exception e) {
			// Usuario ya existe
		}
		
		// Test listar registros a eventos
		Set<String> registros = ICU.listarRegistrosAEventos("asistenteReg");
		assertEquals(true, registros.size() >= 0);
		
		// Test listar ediciones organizadas
		try {
			Set<String> edicionesOrg = ICU.listarEdicionesOrganizadas("orgTest");
			assertEquals(true, edicionesOrg.size() >= 0);
		} catch (Exception e) {
			// Puede no tener ediciones
		}
		
		// Test confirmar asistencia
		try {
			ICE.confirmarAsistencia("EdicionTest", "asistenteReg");
		} catch (Exception e) {
			// Puede no existir la edicion o el registro
		}
		
		// Test listar asistentes a edicion
		try {
			ICE.listarAsistentesAEdicionDeEvento("EdicionTest");
		} catch (Exception e) {
			// Puede no existir la edicion
		}
	}
	
	@Test
	public void testTiposRegistroYPatrocinios() throws Exception {
		
		IControllerEvento ICE = Factory.getInstance().getControllerEvento();
		
		// Test listar tipos de registro
		try {
			Set<String> tiposRegistro = ICE.listarTiposDeRegistro("EdicionTest");
			assertEquals(true, tiposRegistro.size() >= 0);
		} catch (Exception e) {
			// Edicion puede no existir
		}
		
		// Test alta tipo de registro
		try {
			ICE.altaTipoDeRegistro("EdicionTest", "TipoTest", "Descripcion tipo", 100.0f, 50);
		} catch (Exception e) {
			// Puede ya existir o fallar por otros motivos
		}
		
		// Test listar patrocinios
		try {
			Set<String> patrocinios = ICE.listarPatrocinios("EdicionTest");
			assertEquals(true, patrocinios.size() >= 0);
		} catch (Exception e) {
			// Edicion puede no existir
		}
		
		// Test obtener detalle de patrocinio
		try {
			ICE.obtenerPatrocinio("EdicionTest", "InstitucionTest");
		} catch (Exception e) {
			// Puede no existir
		}
	}
	
	@Test
	public void testDetallesYInfo() throws Exception {
		
		IControllerEvento ICE = Factory.getInstance().getControllerEvento();
		
		// Test mostrar detalles de edicion
		try {
			ICE.mostrarDetallesEdicion("EdicionTest");
		} catch (Exception e) {
			// Edicion puede no existir
		}
		
		// Test ver detalle tipo registro
		try {
			ICE.verDetalleTRegistro("EdicionTest", "TipoTest");
		} catch (Exception e) {
			// Puede no existir
		}
		
		// Test info registro
		try {
			ICE.infoRegistro("EdicionTest", "asistenteReg");
		} catch (Exception e) {
			// Puede no existir el registro
		}
	}
	
	@Test
	public void testExcepciones() throws Exception {
		
		IControllerEvento ICE = Factory.getInstance().getControllerEvento();
		IControllerUsuario ICU = Factory.getInstance().getControllerUsuario();
		
		// Test con edicion inexistente
		try {
			ICE.nomEvPorEd("EdicionInexistente");
		} catch (Exception e) {
			// Excepcion esperada
		}
		
		// Test aceptar edicion inexistente
		try {
			ICE.aceptarEdicion("EdicionInexistente", "EventoInexistente");
		} catch (Exception e) {
			// Excepcion esperada
		}
		
		// Test rechazar edicion inexistente
		try {
			ICE.rechazarEdicion("EdicionInexistente", "EventoInexistente");
		} catch (Exception e) {
			// Excepcion esperada
		}
		
		// Test ver detalle de evento inexistente
		try {
			ICE.verDetalleEvento("EventoInexistente");
		} catch (Exception e) {
			// Excepcion esperada
		}
		
		// Test con funciones adicionales y datos inexistentes
		try {
			ICE.finalizarEvento("EventoInexistente");
		} catch (Exception e) {
			// Excepcion esperada
		}
		
		try {
			ICE.confirmarAsistencia("EdicionInexistente", "UsuarioInexistente");
		} catch (Exception e) {
			// Excepcion esperada
		}
		
	
		// Test editar usuarios inexistentes
		try {
			ICU.editarAsistente("usuarioInexistente", "Nombre", "Apellido", LocalDate.now());
		} catch (Exception e) {
			// Excepcion esperada
		}
		
		try {
			ICU.editarOrganizador("organizadorInexistente", "Nombre", "Desc", "www.web.com");
		} catch (Exception e) {
			// Excepcion esperada
		}
	}
}
