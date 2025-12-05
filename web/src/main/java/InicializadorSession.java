
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import webservices.PublicadorEvento;
import webservices.PublicadorEventoService;

@WebListener
public class InicializadorSession implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent event) {
    	PublicadorEventoService serviceEvento = new PublicadorEventoService();
        PublicadorEvento portEvento = serviceEvento.getPublicadorEventoPort();
        List<Object>categorias = portEvento.listarCategorias().getItem();
        Set<String> categoriasSet = new java.util.HashSet<>();
        for (Object categoria : categorias) {
			categoriasSet.add((String) categoria);
		}
        
        event.getSession().setAttribute("categorias", categoriasSet);
        event.getSession().setAttribute("fecha", LocalDate.now());
        event.getSession().setAttribute("usuario", null);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
    }
}
