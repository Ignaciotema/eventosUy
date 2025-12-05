import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import webservices.PublicadorEvento;
import webservices.PublicadorEventoService;

@WebFilter("/*")
public class GeneralFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

    	PublicadorEventoService serviceEvento = new PublicadorEventoService();
        PublicadorEvento portEvento = serviceEvento.getPublicadorEventoPort();
        List<Object>categorias = portEvento.listarCategorias().getItem();
        Set<String> categoriasSet = new java.util.HashSet<>();
        for (Object categoria : categorias) {
			categoriasSet.add((String) categoria);
		}

        
        ((HttpServletRequest) request).getSession().setAttribute("categorias", categoriasSet);
        ((HttpServletRequest) request).getSession().setAttribute("fecha", LocalDate.now());

        chain.doFilter(request, response);
    }
}