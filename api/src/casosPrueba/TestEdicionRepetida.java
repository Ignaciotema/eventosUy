package casosPrueba;

import static org.junit.Assert.assertThrows;

import java.time.LocalDate;
import java.util.HashSet;

import org.junit.Test;

import excepciones.NombreEventoExcepcion;
import logica.controllers.IControllerEvento;
import logica.manejadores.ManejadorCategoria;
import logica.manejadores.ManejadorEdicion;
import logica.manejadores.ManejadorEvento;
import logica.manejadores.ManejadorUsuario;
import logica.models.Categoria;
import logica.models.Edicion;
import logica.models.Evento;
import logica.models.Factory;
import logica.models.Organizador;


public class TestEdicionRepetida {
	@Test
	public void TestEdicionRepetida() throws NombreEventoExcepcion, Exception {
		IControllerEvento ICE = Factory.getInstance().getControllerEvento();
		HashSet<String> categorias = new HashSet<String>();
		categorias.add("cat1");
		Categoria cat = new Categoria("cat1");
		ManejadorCategoria.getInstance().agregarCategoria(cat);
		//String nickname, String nombre, String email, String descripcion, String web
		Organizador org = new Organizador("Vegetta","Samuel","samu@gmail.com","a","desc1","www.vegetta.com");
		ManejadorUsuario.getInstance().agregarUsuario(org);
		ICE.altaEvento("ev1", "e1", ICE.getFechaSistema(), "desc1",categorias,"");
		Evento eventoSeleccionado = ManejadorEvento.getInstance().obtenerEvento("ev1");
		
		Edicion ed1 = new Edicion("edicion1", "sigla", LocalDate.of(2004, 1, 1), LocalDate.of(2004, 1, 9), ICE.getFechaSistema(),
				"Montevideo", "Uruguay", eventoSeleccionado,org);
		
		ManejadorEdicion.getInstance().agregarEdicionIngresada(ed1);
		
		assertThrows(Exception.class, () -> {
			ICE.altaEdicionDeEvento("ev1","Vegetta", "edicion1", "sigla2", LocalDate.of(2004, 1, 1), LocalDate.of(2004, 1, 9), ICE.getFechaSistema(), "Montevideo", "Uruguay","");
		});
	}
}
