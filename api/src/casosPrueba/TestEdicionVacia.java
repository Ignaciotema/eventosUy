package casosPrueba;

import static org.junit.Assert.assertThrows;

import java.util.HashSet;

import org.junit.Test;

import excepciones.NombreEventoExcepcion;
import logica.controllers.IControllerEvento;
import logica.manejadores.ManejadorCategoria;
import logica.manejadores.ManejadorEvento;
import logica.models.Categoria;
import logica.models.Evento;
import logica.models.Factory;

public class TestEdicionVacia {

	@Test
	public void testEdicionVacia() throws NombreEventoExcepcion, Exception {
		IControllerEvento ICE = Factory.getInstance().getControllerEvento();
		HashSet<String> categorias = new HashSet<String>();
		categorias.add("cat1");
		Categoria cat = new Categoria("cat1");
		ManejadorCategoria.getInstance().agregarCategoria(cat);
		ICE.altaEvento("ev1", "e1", ICE.getFechaSistema(), "desc1",categorias,"");
		Evento eventoSeleccionado = ManejadorEvento.getInstance().obtenerEvento("ev1");
		
		//nombre de edicion vacio entonces deberia tirar excepcion
		assertThrows(Exception.class, () -> {
		    eventoSeleccionado.agregarEdicion(null);
		});
		
	}
}
