import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webservices.DtDetalleEvento;
import webservices.PublicadorEvento;
import webservices.PublicadorEventoService;
import webservices.PublicadorUsuario;
import webservices.PublicadorUsuarioService;


@WebServlet("/HomeServlet")
public class ServletHome extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public ServletHome() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        PublicadorEventoService serviceEvento = new PublicadorEventoService();
        PublicadorEvento portEvento = serviceEvento.getPublicadorEventoPort();
        
        List<DtDetalleEvento> eventosRecientes = new ArrayList<DtDetalleEvento>();
		List<Object> lista = portEvento.obtenerEventosRecientes().getItem();
		for (Object obj : lista) {
			eventosRecientes.add((DtDetalleEvento) obj);
			System.out.println(((DtDetalleEvento) obj).getNombre());
		}
		
		request.setAttribute("eventos_recientes", eventosRecientes);
		
		String mensaje = request.getParameter("mensaje");
		if (mensaje != null) {
		    request.setAttribute("mensaje", mensaje);
		}
		
        // Fetch imagen de eventos usando el nuevo sistema centralizado
		for (DtDetalleEvento e : eventosRecientes) {
	        String eventoImg = ManejadorArchivos.buscarArchivo(e.getNombre().toLowerCase(), "eventos");
	        if (eventoImg != null ) {
	        	request.setAttribute(e.getNombre(), eventoImg);
	        } else {
	        	request.setAttribute(e.getNombre(), "images/eventos/default.jpg");
	        }
        }
		
		request.getRequestDispatcher("/WEB-INF/pages/home.jsp").forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}