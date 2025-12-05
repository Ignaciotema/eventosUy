package casosPrueba;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import logica.models.Edicion;
import logica.models.Factory;
import logica.models.Organizador;
import logica.controllers.IControllerEvento;
import logica.controllers.IControllerUsuario;
import logica.data_types.DTDetalleEdicion;
import logica.data_types.DTDetalleEvento;
import logica.data_types.DTEdicion;
import logica.data_types.DTTipoRegistro;
import logica.data_types.DataUsuario;
import logica.data_types.DataUsuario.TipoUsuario;
import logica.manejadores.ManejadorEdicion;

public class TestVerDetalles {
	
	@Test
	public void testVerDetalles() throws Exception {
		// Checkeo si funciona el alta de usuario y ver detalles
		IControllerUsuario ICU = Factory.getInstance().getControllerUsuario();
		IControllerEvento ICE = Factory.getInstance().getControllerEvento();
		
		ICU.ingresarAsistente("Willyrex", "Guillermo", "willy@gmail.com","a", "Diaz", LocalDate.of(2004, 1,1));
		ICU.ingresarOrganizador("Vegetta", "Samuel", "vegetta@gmail.com","a", "Muy buenas a todos guapisimos", "www.v777.com");
		DataUsuario dtaAsistente = (DataUsuario) ICU.infoUsuario("Willyrex");
		DataUsuario dtaOrganizador = (DataUsuario) ICU.infoUsuario("Vegetta");
		
		DataUsuario dt1 = new DataUsuario("Willyrex", "Guillermo", "willy@gmail.com",TipoUsuario.ASISTENTE);
		DataUsuario dt2 = new DataUsuario("Vegetta", "Samuel", "vegetta@gmail.com",TipoUsuario.ORGANIZADOR);
		
		// Checkeo si coinciden datos del asistente
		assertEquals(dtaAsistente.getNickname(), dt1.getNickname());
		assertEquals(dtaAsistente.getNombre(), dt1.getNombre());
		assertEquals(dtaAsistente.getEmail(), dt1.getEmail());
		assertEquals(dtaAsistente.getTipo(), dt1.getTipo());
		
		//Checkeo si coinnciden datos del organizdor
		assertEquals(dtaOrganizador.getNickname(), dt2.getNickname());
		assertEquals(dtaOrganizador.getNombre(), dt2.getNombre());
		assertEquals(dtaOrganizador.getEmail(), dt2.getEmail());
		assertEquals(dtaOrganizador.getTipo(), dt2.getTipo());
		
		// Checkeo si funciona mostrarDetallesEdicion
		Set<String> categoriasSet = new java.util.HashSet<String>();
		categoriasSet.add("Categoria1");
		ICE.ingresarCategoria("Categoria1");
		ICE.altaEvento("Evento1", "ev1", ICE.getFechaSistema(), "descripcion 1", categoriasSet,""); 
		ICE.altaEdicionDeEvento("Evento1", "Vegetta", "Edicion1", "ed1", LocalDate.of(2027, 1,2), LocalDate.of(2027, 1,8), LocalDate.of(2027, 1,1), "Montevideo", "Uruguay","");
		DTEdicion dtEdicion = new DTEdicion("Edicion1", "ed1",LocalDate.of(2027, 1,2), LocalDate.of(2027, 1,8), "Montevideo", "Uruguay");
		DTDetalleEdicion detalleEdicion = ICE.mostrarDetallesEdicion("Edicion1");

		
		// Checkeo si coinciden los datos de la edicion
		assertEquals(detalleEdicion.getNombre(), dtEdicion.getNombre());
		assertEquals(detalleEdicion.getSigla(), dtEdicion.getSigla());
		assertEquals(detalleEdicion.getFechaInicio(), dtEdicion.getFechaInicio());
		assertEquals(detalleEdicion.getFechaFin(), dtEdicion.getFechaFin());
		assertEquals(detalleEdicion.getCiudad(), dtEdicion.getCiudad());
		assertEquals(detalleEdicion.getPais(), dtEdicion.getPais());
		assertEquals(detalleEdicion.getOrganizador()
				, "Vegetta");
		// Checkeo que funcione DTDetalleEdicion creandola a mano
		HashSet<String> categorias = new HashSet<String>();
		categorias.add("Categoria1");
		HashSet<String> edis = new HashSet<String>();
		edis.add("Edicion1");
		edis.add("Edicion2");
		
		DTDetalleEvento dtde = new DTDetalleEvento("Edicion1","ev1",ICE.getFechaSistema(),"descripcion 1",categorias,edis, false);
		assertEquals("Edicion1",dtde.getNombre());
		assertEquals("ev1",dtde.getSigla());
		assertEquals("descripcion 1",dtde.getDescripcion());
		assertEquals(categorias,dtde.getCategorias());
		assertEquals(edis,dtde.getEdiciones());
		
		// Checkeo que funcione DTTipoRegistro
		
		DTTipoRegistro tRegis = new DTTipoRegistro("Tipo1", "Descripcion de Tipo1",100, 5);
		Edicion edicionTest  = ManejadorEdicion.getInstance().encontrarEdicion("Edicion1");
		ICE.altaTipoDeRegistro("Edicion1", "Tipo1", "Descripcion de Tipo1",(float) 100, 5);
		DTTipoRegistro detalleTRegis = ICE.verDetalleTRegistro("Edicion1", "Tipo1");
		assertEquals(tRegis.getNombre(), detalleTRegis.getNombre());
		assertEquals(tRegis.getDescripcion(), detalleTRegis.getDescripcion());
		assertEquals(tRegis.getCupo(), detalleTRegis.getCupo());
		
		ICE.altaTipoDeRegistro("Edicion1", "Tipo2", "Descripcion de Tipo2",(float) 100, 1);
		ICE.altaRegistro("Willyrex", "Tipo2", "Edicion1",false);
		//ahora se supone que tiene que tener 0 cupos
		assertEquals(0, edicionTest.getTipoRegistro("Tipo2").getCupo());
		
		//ahora deberia no funcionar porque no hay cupos en Tipo2
		ICU.ingresarAsistente("Willyrex2", "Guillermo2", "willy2@gmail.com","a", "Diaz", LocalDate.of(2004, 1,1));
		assertThrows(Exception.class, () -> {
		    ICE.elegirAsistenteYTipoRegistro("Willyrex2", "Tipo2", "Edicion1",false);
		});
		
		
		
	}
	
}
