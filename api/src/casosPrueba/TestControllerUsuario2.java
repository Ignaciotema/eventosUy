package casosPrueba;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

import excepciones.NombreInstiExistente;
import logica.controllers.IControllerUsuario;
import logica.data_types.DTAsistente;
import logica.data_types.DTOrganizador;
import logica.manejadores.ManejadorInstitucion;
import logica.manejadores.ManejadorUsuario;
import logica.models.Asistente;
import logica.models.Institucion;
import logica.models.Organizador;

public class TestControllerUsuario2{
	@Test
	public void Test1() {
		//no hace falta testear nada, solo correr la carga de datos
		try {
			CargaDatos.cargarDatos();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		IControllerUsuario ICU = logica.models.Factory.getInstance().getControllerUsuario();
		ManejadorInstitucion mi = ManejadorInstitucion.getInstance();
		
		try {
			ICU.altaInstitucion("AU", "Instituto de tecnologia de punta", "www.it.com");
		} catch (NombreInstiExistente e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Institucion ins= new Institucion("AU", "Instituto de tecnologia de punta", "www.it.com");
		Institucion ins2= mi.obtenerInstitucion("AU");
		assertEquals(true, ins.getNombre().equals(ins2.getNombre()));
		assertEquals(true, ins.getDescripcion().equals(ins2.getDescripcion()));
		assertEquals(true, ins.getWeb().equals(ins2.getWeb()));
		
		
		
		Set<String> orgs = ICU.listarOrganizadores();
		ManejadorUsuario mU = ManejadorUsuario.getInstance();
		Set<String> orgs2 = mU.obtenerOrganizadores();
		assertEquals(true, orgs.equals(orgs2));
		
		Asistente as= (Asistente) mU.obtenerUsuario("sofirod");
		DTAsistente dtas= ICU.infoAsistente("sofirod");
		DTAsistente dtas2= new DTAsistente(as.getNickname(), as.getNombre(), as.getEmail(), as.getApellido(), as.getFechaNacimiento(), as.getInstitucion().getNombre());
		assertEquals(true, dtas.getNombre().equals(dtas2.getNombre()));
		assertEquals(true, dtas.getApellido().equals(dtas2.getApellido()));
		assertEquals(true, dtas.getEmail().equals(dtas2.getEmail()));
		assertEquals(true, dtas.getFechaNacimiento().equals(dtas2.getFechaNacimiento()));
		assertEquals(true, dtas.getNickname().equals(dtas2.getNickname()));
		
		
		Organizador or= (Organizador) mU.obtenerUsuario("techcorp");
		DTOrganizador dtor= ICU.infoOrganizador("techcorp");
		DTOrganizador dtor2= new DTOrganizador(or.getNickname(), or.getNombre(), or.getEmail(), or.getDescripcion(), or.getWeb());
        
		assertEquals(true, dtor.getNombre().equals(dtor2.getNombre()));
		assertEquals(true, dtor.getEmail().equals(dtor2.getEmail()));
		assertEquals(true, dtor.getDescripcion().equals(dtor2.getDescripcion()));
		assertEquals(true, dtor.getWeb().equals(dtor2.getWeb()));
		assertEquals(true, dtor.getNickname().equals(dtor2.getNickname()));
		
		ICU.editarAsistente("sofirod", "Sofia", "Rodriguez", as.getFechaNacimiento());
		DTAsistente dtas3= ICU.infoAsistente("sofirod");
		assertEquals(true, dtas3.getNombre().equals("Sofia"));
		assertEquals(true, dtas3.getApellido().equals("Rodriguez"));
		assertEquals(true, dtas3.getFechaNacimiento().equals(dtas2.getFechaNacimiento()));
		
		
		ICU.editarOrganizador("techcorp", "TechCorp Ltda", or.getDescripcion(), "www.techcorp.com");
		DTOrganizador dtor3= ICU.infoOrganizador("techcorp");
		assertEquals(true, dtor3.getNombre().equals("TechCorp Ltda"));
		assertEquals(true, dtor3.getDescripcion().equals(or.getDescripcion()));
		assertEquals(true, dtor3.getWeb().equals("www.techcorp.com"));
		
		
		
		
		
	}
	

}