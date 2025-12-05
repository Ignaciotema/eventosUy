import java.io.IOException;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
// Agregar imports necesarios para conversión de fechas
import javax.xml.datatype.XMLGregorianCalendar;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import webservices.DataUsuario;
import webservices.DtDetalleEdicion;
import webservices.DtDetalleEvento;
import webservices.PublicadorEvento;
import webservices.PublicadorEventoService;
import webservices.PublicadorUsuario;
import webservices.PublicadorUsuarioService;
import webservices.TipoUsuario;
import webservices.WrapperHashSet;

/**
 * Servlet implementation class EventosServlet
 */
@WebServlet({ "/eventos", "/listarEventos", "/detalleEvento", "/altaEvento", "/categorias", "/finalizarEvento" })
@MultipartConfig
public class ServletEvento extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
   public ServletEvento() {
        super();
    }

    /**
     * Normaliza un texto removiendo acentos y convirtiéndolo a minúsculas
     * para hacer búsquedas insensibles a mayúsculas y acentos
     */
    private String normalizeText(String text) {
        if (text == null) return "";
        
        // Convertir a minúsculas
        String normalized = text.toLowerCase();
        
        // Remover acentos y diacríticos
        normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        
        return normalized;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        PublicadorEventoService serviceEvento = new PublicadorEventoService();
        PublicadorEvento portEvento = serviceEvento.getPublicadorEventoPort();
    	
        PublicadorUsuarioService serviceUsuario = new PublicadorUsuarioService();
        PublicadorUsuario portUsuario = serviceUsuario.getPublicadorUsuarioPort();
        
        switch (path) {
            case "/eventos":
            case "/listarEventos": {
                listarEventos(request, response, portEvento);
                return;
            }
            case "/detalleEvento": {
                mostrarDetalleEvento(request, response, portEvento);
                return;
            }
            case "/categorias": {
                listarCategorias(request, response, portEvento);
                return;
            }
            case "/altaEvento": {
                // Simplemente cargar el formulario - sin redirects
                request.setAttribute("destino", "altaEvento");
                request.setAttribute("error", null);
                // Clear any previous success message when opening the form
                request.setAttribute("mensaje", null);
                listarCategorias(request, response, portEvento);
                return;
            }
            case "/finalizarEvento": {
            	finalizarEvento(request, response, portEvento);
				return;
			}
            default:
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      
    	String path = request.getServletPath();
        
        if (path.equals("/altaEvento")) {
            PublicadorEventoService serviceEvento = new PublicadorEventoService();
            PublicadorEvento portEvento = serviceEvento.getPublicadorEventoPort();
            
            crearEvento(request, response, portEvento);
        } 
    }
    
    private void listarEventos(HttpServletRequest request, HttpServletResponse response, PublicadorEvento portEvento) throws ServletException, IOException {
        String categoria = request.getParameter("categoria");
        String nombreBusqueda = request.getParameter("nombre"); 
        String tipoFiltro = request.getParameter("tipo"); // "eventos", "ediciones", o null (ambos)
        String ordenamiento = request.getParameter("orden"); // "fecha", "alfabetico", o null (por defecto fecha descendente)
        
        try {
            Set<java.util.Map<String, Object>> todosLosItems = new java.util.LinkedHashSet<>();
            int totalEventos = 0;
            int totalEdiciones = 0;
            int eventosEncontrados = 0;
            int edicionesEncontradas = 0;
            
            // Obtener todos los eventos primero para contar totales
            WrapperHashSet todosLosEventosWrapper = portEvento.listarEventos();
            List<Object> todosLosEventosObj = todosLosEventosWrapper.getItem();
            Set<String> todosLosEventos = new java.util.HashSet<>();
            for (Object obj : todosLosEventosObj) {
                todosLosEventos.add((String) obj);
            }
            
            // Contar total de eventos
            totalEventos = todosLosEventos.size();
            
            // Contar total de ediciones confirmadas
            for (String nombreEvento : todosLosEventos) {
                try {
                    List<Object> edicionesConfirmadasObj = portEvento.listarEdicionesConfirmadas(nombreEvento).getItem();
                    totalEdiciones += edicionesConfirmadasObj.size();
                } catch (Exception e) {
                    // Continuar con el siguiente evento si hay error
                    continue;
                }
            }
            
            // Procesar eventos si no se filtra solo por ediciones
            if (tipoFiltro == null || tipoFiltro.isEmpty() || "eventos".equals(tipoFiltro)) {
                for (String nombreEvento : todosLosEventos) {
                    try {
                        DtDetalleEvento detalleEvento = portEvento.verDetalleEvento(nombreEvento);
                        boolean incluirEvento = true;
                        
                        // Filtro por categoría 
                        if (categoria != null && !categoria.trim().isEmpty() && !categoria.equals("todas")) {
                            List<String> categoriasObj = detalleEvento.getCategorias();
                            Set<String> categorias = new java.util.HashSet<>();
                            for (String cat : categoriasObj) {
                                categorias.add((String) cat);
                            }
                            incluirEvento = categorias.contains(categoria);
                        }
                        
                        // Filtro por nombre - búsqueda parcial insensible a mayúsculas y acentos
                        if (incluirEvento && nombreBusqueda != null && !nombreBusqueda.trim().isEmpty()) {
                            String nombreEventoNormalizado = normalizeText(nombreEvento);
                            String busquedaNormalizada = normalizeText(nombreBusqueda.trim());
                            incluirEvento = nombreEventoNormalizado.contains(busquedaNormalizada);
                        }
                        
                        // Solo agregar si pasa todos los filtros
                        if (incluirEvento) {
                            eventosEncontrados++;
                            
                            java.util.Map<String, Object> eventoInfo = new java.util.HashMap<>();
                            eventoInfo.put("nombre", detalleEvento.getNombre());
                            eventoInfo.put("descripcion", detalleEvento.getDescripcion());
                            eventoInfo.put("tipo", "evento");
                            
                            // Convertir fecha de alta para ordenamiento
                            XMLGregorianCalendar xmlFechaAlta = detalleEvento.getFechaAlta();
                            if (xmlFechaAlta != null) {
                                LocalDate fechaAlta = xmlFechaAlta.toGregorianCalendar().toZonedDateTime().toLocalDate();
                                eventoInfo.put("fechaAlta", fechaAlta);
                            }
                            
                            // Buscar imagen de evento usando el nuevo sistema centralizado
                            String eventoImg = ManejadorArchivos.buscarArchivo(nombreEvento.toLowerCase(), "eventos");
                            if (eventoImg != null && !eventoImg.equals("images/eventos/default.jpg")) {
                                eventoInfo.put("imagenEvento", eventoImg);
                            } else {
                                eventoInfo.put("imagenEvento", "images/eventos/default.jpg");
                            }
                            
                            todosLosItems.add(eventoInfo);
                        }
                    } catch (Exception e) {
                        // Continuar con el siguiente evento si hay error
                        continue;
                    }
                }
            }
            
            // Procesar ediciones confirmadas si no se filtra solo por eventos
            if (tipoFiltro == null || tipoFiltro.isEmpty() || "ediciones".equals(tipoFiltro)) {
                for (String nombreEvento : todosLosEventos) {
                    try {
                        // Verificar primero si el evento padre pasa el filtro de categoría
                        boolean eventoParentPasaFiltroCategoria = true;
                        if (categoria != null && !categoria.trim().isEmpty() && !categoria.equals("todas")) {
                            DtDetalleEvento eventoParent = portEvento.verDetalleEvento(nombreEvento);
                            List<String> categoriasObj = eventoParent.getCategorias();
                            Set<String> categorias = new java.util.HashSet<>();
                            for (String cat : categoriasObj) {
                                categorias.add((String) cat);
                            }
                            eventoParentPasaFiltroCategoria = categorias.contains(categoria);
                        }
                        
                        // Solo buscar ediciones si el evento padre pasa el filtro de categoría
                        if (eventoParentPasaFiltroCategoria) {
                            List<Object> edicionesConfirmadasObj = portEvento.listarEdicionesConfirmadas(nombreEvento).getItem();
                            for (Object obj : edicionesConfirmadasObj) {
                                String nombreEdicion = (String) obj;
                                
                                try {
                                    DtDetalleEdicion detalleEdicion = portEvento.mostrarDetallesEdicion(nombreEdicion);
                                    boolean incluirEdicion = true;
                                    
                                    // Filtro por nombre - buscar en nombre de edición y evento padre
                                    if (incluirEdicion && nombreBusqueda != null && !nombreBusqueda.trim().isEmpty()) {
                                        String nombreEdicionNormalizado = normalizeText(nombreEdicion);
                                        String nombreEventoNormalizado = normalizeText(nombreEvento);
                                        String busquedaNormalizada = normalizeText(nombreBusqueda.trim());
                                        incluirEdicion = nombreEdicionNormalizado.contains(busquedaNormalizada) || 
                                                       nombreEventoNormalizado.contains(busquedaNormalizada);
                                    }
                                    
                                    // Solo agregar si pasa todos los filtros
                                    if (incluirEdicion) {
                                        edicionesEncontradas++;
                                        
                                        java.util.Map<String, Object> edicionInfo = new java.util.HashMap<>();
                                        edicionInfo.put("nombre", detalleEdicion.getNombre());
                                        edicionInfo.put("descripcion", "Edición de " + nombreEvento + " - " + detalleEdicion.getCiudad() + ", " + detalleEdicion.getPais());
                                        edicionInfo.put("tipo", "edicion");
                                        edicionInfo.put("eventoParent", nombreEvento);
                                        
                                        // Convertir fecha de alta para ordenamiento
                                        XMLGregorianCalendar xmlFechaAlta = detalleEdicion.getFechaAlta();
                                        if (xmlFechaAlta != null) {
                                            LocalDate fechaAlta = xmlFechaAlta.toGregorianCalendar().toZonedDateTime().toLocalDate();
                                            edicionInfo.put("fechaAlta", fechaAlta);
                                        }
                                        
                                        // Buscar imagen de edicion usando el nuevo sistema centralizado
                                        String edicionImg = ManejadorArchivos.buscarArchivo(detalleEdicion.getNombre().toLowerCase(), "ediciones");
                                        if (edicionImg != null && !edicionImg.equals("images/ediciones/default.jpg")) {
                                            edicionInfo.put("imagenEvento", edicionImg);
                                        } else {
                                            edicionInfo.put("imagenEvento", "images/eventos/default.jpg");
                                        }
                                        
                                        todosLosItems.add(edicionInfo);
                                    }
                                } catch (Exception e) {
                                    // Continuar con la siguiente edición si hay error
                                    continue;
                                }
                            }
                        }
                    } catch (Exception e) {
                        // Continuar con el siguiente evento si hay error
                        continue;
                    }
                }
            }
            
            // Convertir a List para poder ordenar
            List<java.util.Map<String, Object>> listaItems = new ArrayList<>(todosLosItems);
            
            // Ordenamiento
            if ("alfabetico".equals(ordenamiento)) {
                // Ordenar alfabéticamente descendente
                listaItems.sort((a, b) -> {
                    String nombreA = (String) a.get("nombre");
                    String nombreB = (String) b.get("nombre");
                    return nombreB.compareToIgnoreCase(nombreA);
                });
            } else {
                // Por defecto: ordenar por fecha de alta descendente, luego alfabético descendente
                listaItems.sort((a, b) -> {
                    LocalDate fechaA = (LocalDate) a.get("fechaAlta");
                    LocalDate fechaB = (LocalDate) b.get("fechaAlta");
                    
                    if (fechaA != null && fechaB != null) {
                        int fechaComparison = fechaB.compareTo(fechaA); // Descendente
                        if (fechaComparison != 0) {
                            return fechaComparison;
                        }
                    } else if (fechaA != null) {
                        return -1;
                    } else if (fechaB != null) {
                        return 1;
                    }
                    
                    // Si las fechas son iguales o nulas, ordenar alfabéticamente descendente
                    String nombreA = (String) a.get("nombre");
                    String nombreB = (String) b.get("nombre");
                    return nombreB.compareToIgnoreCase(nombreA);
                });
            }
            
            // Convertir de vuelta a LinkedHashSet para mantener orden
            Set<java.util.Map<String, Object>> itemsOrdenados = new java.util.LinkedHashSet<>(listaItems);
            
            request.setAttribute("eventos", new java.util.HashSet<>()); // Mantener compatibilidad                   
            request.setAttribute("eventosInfo", itemsOrdenados);      
            request.setAttribute("categoriaSeleccionada", categoria);
            request.setAttribute("nombreBusqueda", nombreBusqueda); 
            request.setAttribute("tipoFiltro", tipoFiltro);
            request.setAttribute("ordenamiento", ordenamiento);
            request.setAttribute("totalEventos", totalEventos);
            request.setAttribute("totalEdiciones", totalEdiciones);
            request.setAttribute("eventosFiltrados", itemsOrdenados.size());
            
            request.getRequestDispatcher("/WEB-INF/pages/listarEventos.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().append("Error: " + e.getMessage());
        }
    }
    
    private void mostrarDetalleEvento(HttpServletRequest request, HttpServletResponse response, PublicadorEvento portEvento) throws ServletException, IOException {
        String nombreEvento = request.getParameter("nombre");

        
        // Registrar la visita al evento 
        if (nombreEvento != null && !nombreEvento.trim().isEmpty()) {
            try {
                portEvento.registrarVisitaEvento(nombreEvento);
            } catch (Exception e) {
                
                System.err.println("Error al registrar visita: " + e.getMessage());
            }
        }
        
        try {
        	DtDetalleEvento detalleEvento = null;
        	try {
        		detalleEvento = portEvento.verDetalleEvento(nombreEvento);
        	} catch (Exception e) {
				request.setAttribute("error", "El evento ha finalizado y no se pueden ver sus detalles.");
				request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
				return;
			}
            
            // Fetch imagen de evento usando el nuevo sistema centralizado
            String eventoImg = ManejadorArchivos.buscarArchivo(nombreEvento.toLowerCase(), "eventos");
            if (eventoImg != null && !eventoImg.equals("images/eventos/default.jpg")) {
                request.setAttribute("imagenEvento", eventoImg);
            } else {
                request.setAttribute("imagenEvento", "images/eventos/default.jpg");
            }
            
            // Verificar el tipo de usuario para listar ediciones
            DataUsuario usuario = (DataUsuario) request.getSession().getAttribute("usuario");
            
            // Obtener todas las ediciones (confirmadas y no confirmadas)
            List<Object> todasLasEdicionesObj = portEvento.listarEdiciones(nombreEvento).getItem();
            Set<String> todasLasEdiciones = new java.util.HashSet<>();
            for (Object obj : todasLasEdicionesObj) {
                todasLasEdiciones.add((String) obj);
            }
            
            List<Object> edicionesConfirmadasObj = portEvento.listarEdicionesConfirmadas(nombreEvento).getItem();
            Set<String> edicionesConfirmadas = new java.util.HashSet<>();
            for (Object obj : edicionesConfirmadasObj) {
                edicionesConfirmadas.add((String) obj);
            }
            
            Set<java.util.Map<String, Object>> edicionesMinimas = new java.util.LinkedHashSet<>();
            
            // Para cada nombre de edición, verificar si debe mostrarse
            for (String nombreEdicion : todasLasEdiciones) {
                DtDetalleEdicion detalleEdicion = portEvento.mostrarDetallesEdicion(nombreEdicion);
                
                // Verificar si el usuario es el organizador específico de esta edición
                boolean esOrganizadorDeEstaEdicion = usuario != null && 
                                                   usuario.getTipo() == TipoUsuario.ORGANIZADOR && 
                                                   usuario.getNickname().equals(detalleEdicion.getOrganizador());
                
                // Determinar si mostrar esta edición:
                // - Si es organizador de esta edición: mostrar siempre
                // - Si no es organizador de esta edición: mostrar solo si está confirmada
                boolean mostrarEdicion = esOrganizadorDeEstaEdicion || edicionesConfirmadas.contains(nombreEdicion);
                
                if (mostrarEdicion) {
                    java.util.Map<String, Object> edicionMinima = new java.util.HashMap<>();
                    edicionMinima.put("nombre", detalleEdicion.getNombre());
                    edicionMinima.put("ciudad", detalleEdicion.getCiudad());
                    edicionMinima.put("pais", detalleEdicion.getPais());
                    
                    // Convertir XMLGregorianCalendar a LocalDate
                    XMLGregorianCalendar xmlFechaInicio = detalleEdicion.getFechaInicio();
                    XMLGregorianCalendar xmlFechaFin = detalleEdicion.getFechaFin();
                    
                    LocalDate fechaInicio = null;
                    LocalDate fechaFin = null;
                    
                    if (xmlFechaInicio != null) {
                        fechaInicio = xmlFechaInicio.toGregorianCalendar().toZonedDateTime().toLocalDate();
                    }
                    if (xmlFechaFin != null) {
                        fechaFin = xmlFechaFin.toGregorianCalendar().toZonedDateTime().toLocalDate();
                    }
                    
                    edicionMinima.put("fechaInicio", fechaInicio);
                    edicionMinima.put("fechaFin", fechaFin);
                    
                    // Fetch imagen de edicion usando el nuevo sistema centralizado
                    String edicionImg = ManejadorArchivos.buscarArchivo(detalleEdicion.getNombre().toLowerCase(), "ediciones");
                    if (edicionImg != null && !edicionImg.equals("images/ediciones/default.jpg")) {
                        edicionMinima.put("imagenEdicion", edicionImg);
                    } else {
                        edicionMinima.put("imagenEdicion", "images/ediciones/default.jpg");
                    }
                    
                    // Incluir estado solo si el usuario es el organizador específico de esta edición
                    if (esOrganizadorDeEstaEdicion) {
                        edicionMinima.put("estado", detalleEdicion.getEstado());
                    }
                    edicionMinima.put("esOrganizadorDeEstaEdicion", esOrganizadorDeEstaEdicion);
                    
                    edicionesMinimas.add(edicionMinima);
                }
            }
            
            request.setAttribute("evento", detalleEvento);
            request.setAttribute("ediciones", edicionesMinimas);
         
            
            request.getRequestDispatcher("/WEB-INF/pages/detalleEvento.jsp").forward(request, response);
        } catch (Exception e) {
            response.getWriter().append(e.getMessage());
        }
    }
   
    private void crearEvento(HttpServletRequest request, HttpServletResponse response, PublicadorEvento portEvento) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String sigla = request.getParameter("sigla");
        String descripcion = request.getParameter("descripcion");
        String urlYoutube = request.getParameter("urlYoutube");
        
        try {
            // Obtener las categorías seleccionadas desde la request
            String[] categoriasArray = request.getParameterValues("categorias");
            WrapperHashSet categorias = new WrapperHashSet();
            if (categoriasArray != null) {
                for (String c : categoriasArray) {
                    if (c != null && !c.trim().isEmpty()) {
                        categorias.getItem().add(c.trim());;
                    }
                }
            }
          
            // Manejar la imagen usando ManejadorArchivos
            Part imagenPart = request.getPart("imagen");
            ManejadorArchivos.guardarArchivo(imagenPart, nombre.toLowerCase(), "eventos", getServletContext());
            
            LocalDate fechaEvento = (LocalDate) request.getSession().getAttribute("fecha");
            if (fechaEvento == null) {
                // Usar fecha hardcodeada en lugar de LocalDate.now()
                fechaEvento = LocalDate.of(2025, 1, 15);
            }
            
            // Si no se proporciona URL de YouTube, enviar cadena vacía
            String urlYoutubeParam = (urlYoutube != null && !urlYoutube.trim().isEmpty()) ? urlYoutube.trim() : "";
            
            portEvento.altaEvento(nombre != null ? nombre.trim() : "", 
                                sigla != null ? sigla.trim() : "", 
                                fechaEvento.toString(), 
                                descripcion != null ? descripcion.trim() : "", 
                                categorias,
                                urlYoutubeParam);
            
            // Mostrar mensaje de registro exitoso en la misma página de alta (como en altaEdicion)
            WrapperHashSet todasLasCategoriasWrapper = portEvento.listarCategorias();
            List<Object> todasLasCategoriasObj = todasLasCategoriasWrapper.getItem();
            Set<String> todasLasCategorias = new java.util.HashSet<>();
            for (Object obj : todasLasCategoriasObj) {
                todasLasCategorias.add((String) obj);
            }
            request.setAttribute("categorias", todasLasCategorias);
            // Clear any previous error and set success message
            request.setAttribute("error", null);
            request.setAttribute("mensaje", "Evento creado exitosamente.");
            // Preservar valores del formulario por si el usuario quiere crear otra cosa
            request.setAttribute("nombre", nombre);
            request.setAttribute("sigla", sigla);
            request.setAttribute("descripcion", descripcion);
            request.setAttribute("urlYoutube", urlYoutube);
            // Ensure no stale category selections remain
            request.setAttribute("categoriasSeleccionadas", null);
            request.getRequestDispatcher("/WEB-INF/pages/AltaEvento.jsp").forward(request, response);
             
        } catch(Exception e) {
            // Cargar las categorías para que el JSP pueda mostrar el dropdown
            try {
                WrapperHashSet todasLasCategoriasWrapper = portEvento.listarCategorias();
                List<Object> todasLasCategoriasObj = todasLasCategoriasWrapper.getItem();
                Set<String> todasLasCategorias = new java.util.HashSet<>();
                for (Object obj : todasLasCategoriasObj) {
                    todasLasCategorias.add((String) obj);
                }
                request.setAttribute("categorias", todasLasCategorias);
            } catch (Exception ex) {
                // Si no se pueden cargar las categorías, usar conjunto vacío
                request.setAttribute("categorias", new java.util.HashSet<String>());
            }
            
            String errorMessage = e.getMessage();
            if (errorMessage != null && errorMessage.contains("Ya existe un evento")) {
                request.setAttribute("error", "Ya existe un evento con el nombre ingresado.");
            } else {
                request.setAttribute("error", "Error al crear evento: " + errorMessage);
            }
            
            request.setAttribute("nombre", nombre);
            request.setAttribute("sigla", sigla);
            request.setAttribute("descripcion", descripcion);
            request.setAttribute("urlYoutube", urlYoutube);
            
            // Preservar categorías seleccionadas
            String[] categoriasSeleccionadas = request.getParameterValues("categorias");
            if (categoriasSeleccionadas != null) {
                request.setAttribute("categoriasSeleccionadas", categoriasSeleccionadas);
            }
            
            request.getRequestDispatcher("/WEB-INF/pages/AltaEvento.jsp").forward(request, response);
        }
    }
    
    private void listarCategorias(HttpServletRequest request, HttpServletResponse response, PublicadorEvento portEvento) throws ServletException, IOException {
        try {
            WrapperHashSet todasLasCategoriasWrapper = portEvento.listarCategorias();
            List<Object> todasLasCategoriasObj = todasLasCategoriasWrapper.getItem();
            Set<String> todasLasCategorias = new java.util.HashSet<>();
            for (Object obj : todasLasCategoriasObj) {
                todasLasCategorias.add((String) obj);
            }
            request.setAttribute("categorias", todasLasCategorias);
            
            // Verificar si viene un parámetro o atributo que indique dónde mostrar las categorías
            String destino = request.getParameter("destino");
            if (destino == null) {
                destino = (String) request.getAttribute("destino");
            }
            
            if ("altaEvento".equals(destino)) {
                // Si es para el formulario de alta de evento
                request.getRequestDispatcher("/WEB-INF/pages/AltaEvento.jsp").forward(request, response);
            } else {
                // Por defecto, mostrar el componente sidebar con las categorías cargadas
                request.getRequestDispatcher("/WEB-INF/pages/componente/categorias-sidebar.jsp").forward(request, response);
            }
        } catch (Exception e) {
            response.getWriter().append(e.getMessage());
        }
    }
    
    private void finalizarEvento(HttpServletRequest request, HttpServletResponse response, PublicadorEvento portEvento) throws ServletException, IOException {
		String nombreEvento = request.getParameter("nombreEvento");
		
		try {
			portEvento.finalizarEvento(nombreEvento);
			
			/* request.setAttribute("mensaje", "Evento finalizado exitosamente.");
    
			request.getRequestDispatcher("/WEB-INF/pages/home.jsp").forward(request, response); */
			
			response.sendRedirect(request.getContextPath() + "/HomeServlet?mensaje=Evento finalizado exitosamente.");
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Evento ya finalizado o no existe.");
		}
		}
}