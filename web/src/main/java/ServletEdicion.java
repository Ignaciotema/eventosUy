import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import webservices.DataUsuario;
import webservices.DtDetalleEdicion;
import webservices.DtDetalleEvento;
import webservices.DtPatrocinio;
import webservices.DtTipoRegistro;
import webservices.PublicadorEvento;
import webservices.PublicadorEventoService;
import webservices.PublicadorUsuario;
import webservices.PublicadorUsuarioService;
import webservices.TipoUsuario;
import webservices.WrapperHashSet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

@MultipartConfig
@WebServlet({ "/detalleEdicion", "/altaEdicion", "/altaRegistro", "/listarEdiciones", "/detalleEdicion/altaEdicion" })
public class ServletEdicion extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletEdicion() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = request.getServletPath();
        
        PublicadorEventoService serviceEvento = new PublicadorEventoService();
        PublicadorEvento portEvento = serviceEvento.getPublicadorEventoPort();
    	
        PublicadorUsuarioService serviceUsuario = new PublicadorUsuarioService();
        PublicadorUsuario portUsuario = serviceUsuario.getPublicadorUsuarioPort();
        
        switch (path) {
            case "/detalleEdicion": {  // EJEMPLO: /detalleEdicion?nombre=edicion1

                String nombre = request.getParameter("nombre");
                
                try {
                	// Fetch detalle de edicion
					DtDetalleEdicion ed = portEvento.mostrarDetallesEdicion(nombre);
	                request.setAttribute("edicion", ed);
	                
	                // Fetch tipos de Registro
	                Set<DtTipoRegistro> tiposReg = new HashSet<>();
	                List<Object> lista= portEvento.listarTiposDeRegistro(nombre).getItem();
	                for (Object tr : lista) {	
	                	tiposReg.add(portEvento.verDetalleTRegistro(nombre, (String) tr));
	                }
	                request.setAttribute("tiposRegistro", tiposReg);

	                // Fetch Patrocinios
	                Set<DtPatrocinio> setPatrocinios = new HashSet<>();
	                List<Object> listaPatrocinios = portEvento.listarPatrocinios(nombre).getItem();
	                for (Object patrocinio : listaPatrocinios) {
	                	setPatrocinios.add(portEvento.obtenerPatrocinio(nombre, (String) patrocinio));
	                }
	                request.setAttribute("patrocinios", setPatrocinios);
	                
	                // Fetch imagen de edicion usando el nuevo sistema centralizado
	                String edicionImg = ManejadorArchivos.buscarArchivo(nombre.toLowerCase(), "ediciones");
	                if (edicionImg != null && !edicionImg.equals("images/edicion/default.jpg")) {
	                	request.setAttribute("imagenEdicion", edicionImg);
	                } else {
	                	request.setAttribute("imagenEdicion", "images/edicion/default.jpg");
	                }
	                
	                // Fetch imagen organizador usando el nuevo sistema centralizado
	                String organizadorImg = ManejadorArchivos.buscarArchivo(ed.getOrganizador().toLowerCase(), "usuarios");
	                if (organizadorImg != null && !organizadorImg.equals("images/usuarios/default.png")) {
	                	request.setAttribute("imagenOrganizador", organizadorImg);
	                } else {
	                	request.setAttribute("imagenOrganizador", "images/usuarios/default.jpg");
	                }
	                
	                // Fetch nombre evento
	                String nombreEvento = portEvento.nomEvPorEd(nombre);
	                request.setAttribute("nombreEvento", nombreEvento);
	                
	                // Fetch imagen evento usando el nuevo sistema centralizado
	                String imagenEvento = ManejadorArchivos.buscarArchivo(nombreEvento.toLowerCase(), "eventos");
	                if (imagenEvento != null && !imagenEvento.equals("images/eventos/default.jpg")) {
	                    request.setAttribute("imagenEvento", imagenEvento);
	                } else {
	                    request.setAttribute("imagenEvento", "images/eventos/default.jpg");
	                }

	                
	                // Fetch si el usuario registrado en la edicion
	                HttpSession session = request.getSession();
	                
	                DataUsuario user = (DataUsuario) session.getAttribute("usuario");
	                if (user != null && user.getTipo() == TipoUsuario.ASISTENTE) {
	                	List<Object> edicionesRegistradasObj = portUsuario.listarRegistrosAEventos(user.getNickname()).getItem();
	                	List<String> edicionesRegistradas = new ArrayList<>();
	                	for (Object obj : edicionesRegistradasObj) {
	                		edicionesRegistradas.add((String) obj);
	                	}
	                	if (edicionesRegistradas.contains(nombre)) {
	                		request.setAttribute("usuarioRegistrado", true);
	                	} else {
	                		request.setAttribute("usuarioRegistrado", false);
	                	}
	                }
	                
	                // Fetch si el usuario es organizador de la edicion
	                if (user != null && user.getTipo() == TipoUsuario.ORGANIZADOR && user.getNickname().equals(ed.getOrganizador())) {
	                	request.setAttribute("esOrganizador", true);
	                } else {
	                	request.setAttribute("esOrganizador", false);
	                }

	                
	                // Despachar a JSP
	                request.getRequestDispatcher("/WEB-INF/pages/detalleEdicion.jsp").forward(request, response);
				} catch (Exception e) {
					response.getWriter().append(e.getMessage());
				}
                return;
            }
            case "/listarEdiciones": {
            	
            	DataUsuario user = (DataUsuario) request.getSession().getAttribute("usuario"); 
            	
            	// Fetch ediciones
        		Set<DtDetalleEdicion> ediciones = new HashSet<>();
            	if (user != null && user.getTipo() == TipoUsuario.ORGANIZADOR) {
            		List<Object> edicionesOrganizadasObj = portUsuario.listarEdicionesOrganizadas(user.getNickname()).getItem();
            		for (Object obj : edicionesOrganizadasObj) {
            			String edicion = (String) obj;
            			DtDetalleEdicion ed = portEvento.mostrarDetallesEdicion(edicion);
            			ediciones.add(ed);
            			
                        // Fetch imagen de ediciones
                        String edicionImg = ManejadorArchivos.buscarArchivo(ed.getNombre().toLowerCase(), "ediciones");
                        if (edicionImg != null && !edicionImg.equals("images/ediciones/default.jpg")) {
                        	request.setAttribute(ed.getNombre(), edicionImg);
                        } else {
                        	request.setAttribute(ed.getNombre(), "images/ediciones/default.jpg");
                        }
            		} 
            	
            	} else if (user != null ) {
            		List<Object> registrosObj = portUsuario.listarRegistrosAEventos(user.getNickname()).getItem();
            		for (Object obj : registrosObj) {
            			String edicion = (String) obj;
            			DtDetalleEdicion ed = portEvento.mostrarDetallesEdicion(edicion);
            			ediciones.add(ed);
            			
                        // Fetch imagen de ediciones
                        String edicionImg = ManejadorArchivos.buscarArchivo(ed.getNombre().toLowerCase(), "ediciones");
                        if (edicionImg != null && !edicionImg.equals("images/ediciones/default.jpg")) {
                        	request.setAttribute(ed.getNombre(), edicionImg);
                        } else {
                        	request.setAttribute(ed.getNombre(), "images/ediciones/default.jpg");
                        }
					}
            	}
            
            	
                request.setAttribute("ediciones", ediciones);
                request.getRequestDispatcher("/WEB-INF/pages/listarEdiciones.jsp").forward(request, response);
                return;
            }
    		
            
            case "/detalleEdicion/altaEdicion" : {       // EJEMPLO: /detalleEdicion/altaEdicion?nombreEvento=evento1
                
                HttpSession session = request.getSession();
                DataUsuario user = (DataUsuario) session.getAttribute("usuario");
                String nombreEvento = request.getParameter("nombreEvento");

                // Verifica que el usuario haya iniciado sesion como organizador
                if (user == null || user.getTipo() != TipoUsuario.ORGANIZADOR) {
					// mostrar mensaje de error, el usuario no es organizador
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Solo los organizadores pueden dar de alta ediciones.");
				} else {
					try {
						// Verificar si el evento existe y si está finalizado
						DtDetalleEvento evento = portEvento.verDetalleEvento(nombreEvento);
						
						if (evento.isFinalizado()) {
							// Si el evento está finalizado, mostrar error
							response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No se pueden crear ediciones para un evento finalizado.");
							return;
						}
						
						// Si el evento no está finalizado, continuar normalmente
						request.setAttribute("nombreEvento", nombreEvento);
						request.getRequestDispatcher("/WEB-INF/pages/altaEdicion.jsp").forward(request, response);
					} catch (Exception e) {
						// Si hay error al obtener el evento (no existe), mostrar error
						response.sendError(HttpServletResponse.SC_NOT_FOUND, "El evento especificado no existe.");
					}
				}
    			return;
        	}
            case "/altaRegistro": {

                HttpSession session = request.getSession(false);
                DataUsuario user = (session == null) ? null : (DataUsuario) session.getAttribute("usuario");
                if (user == null || user.getTipo() != TipoUsuario.ASISTENTE) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Solo los asistentes pueden registrarse a ediciones.");
                    return;
                }

                // 1) Param obligatorio
                String edicionParam = request.getParameter("edicion");
                if (edicionParam == null || edicionParam.isBlank()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta parámetro 'edicion'");
                    return;
                }

                try {
                    // 2) Verificar que la edición exista y obtener su DtO
                    DtDetalleEdicion ed = portEvento.mostrarDetallesEdicion(edicionParam);
                    if (ed == null) { // por si tu implementación devuelve null en vez de tirar excepción
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "La edición indicada no existe.");
                        return;
                    }
                    request.setAttribute("edicion", ed); // <-- el JSP debe mostrar ed.getNombre(), no el parámetro

                    // 3) Tipos de registro (desde backend)
                    Set<DtTipoRegistro> tiposReg = new java.util.HashSet<>();
                    List<Object> listaTipos = portEvento.listarTiposDeRegistro(ed.getNombre()).getItem();
                    for (Object tr : listaTipos) {
                        tiposReg.add(portEvento.verDetalleTRegistro(ed.getNombre(), (String) tr));
                    }
                    request.setAttribute("tiposRegistro", tiposReg);

                    // 4) Imagen de edición (buscada por nombre canónico del DtO)
                    String edicionImg = ManejadorArchivos.buscarArchivo(
                            ed.getNombre().toLowerCase(),
                            "ediciones");
                    request.setAttribute("imagenEdicion",
                            edicionImg != null ? edicionImg : "images/ediciones/default.jpg");

                    // 5) Evento + imagen del evento
                    String nombreEvento = portEvento.nomEvPorEd(ed.getNombre());
                    request.setAttribute("nombreEvento", nombreEvento);
                    String imagenEvento = ManejadorArchivos.buscarArchivo(
                            (nombreEvento == null ? "" : nombreEvento.toLowerCase()),
                            "eventos");
                    request.setAttribute("imagenEvento",
                            imagenEvento != null ? imagenEvento : "images/eventos/default.jpg");

                    // 6) Ya registrado
                    List<Object> registrosObj = portUsuario.listarRegistrosAEventos(user.getNickname()).getItem();
                    boolean yaRegistrado = false;
                    for (Object obj : registrosObj) {
                        if (ed.getNombre().equals((String) obj)) {
                            yaRegistrado = true;
                            break;
                        }
                    }
                    request.setAttribute("yaRegistrado", yaRegistrado);

                    // 7) Mostrar form
                    request.getRequestDispatcher("/WEB-INF/pages/altaRegistro.jsp").forward(request, response);
                    return;

                } catch (Exception ex) {
                    // Si tu mostrarDetallesEdicion lanza excepción cuando no existe
                    request.setAttribute("error", "No se pudo cargar la edición: " + ex.getMessage());
                    request.getRequestDispatcher("/WEB-INF/pages/altaRegistro.jsp").forward(request, response);
                    return;
                }
            }

            default:
                break;
        }

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
        PublicadorEventoService serviceEvento = new PublicadorEventoService();
        PublicadorEvento portEvento = serviceEvento.getPublicadorEventoPort();
    	
        PublicadorUsuarioService serviceUsuario = new PublicadorUsuarioService();
        PublicadorUsuario portUsuario = serviceUsuario.getPublicadorUsuarioPort();
    	
    	String path = request.getServletPath();
    	
    	switch (path) {
        case "/altaEdicion": {
        	
            HttpSession session = request.getSession();
            DataUsuario user = (DataUsuario) session.getAttribute("usuario");
            
			String nombre = request.getParameter("nombre");
			String sigla = request.getParameter("sigla");
			String ciudad = request.getParameter("ciudad");
			String pais = request.getParameter("pais");
			String fechaInicio = request.getParameter("fechaInicio");
			String fechaFin = request.getParameter("fechaFin");
			String urlYoutube = request.getParameter("urlYoutube");
			Part imagen = request.getPart("imagen");
			String nombreEvento = request.getParameter("nombreEvento");
			String organizador = user.getNickname();

			
			try {
				if (user.getTipo() != TipoUsuario.ORGANIZADOR) {
					// mostrar mensaje de error, el usuario no es organizador
					throw new Exception("Necesita estar autenticado como organizador para dar de alta una edición.");
				}
				
				// Verificar si el evento existe y si está finalizado
				DtDetalleEvento evento = portEvento.verDetalleEvento(nombreEvento);
				if (evento.isFinalizado()) {
					// Si el evento está finalizado, mostrar error
					throw new Exception("No se pueden crear ediciones para un evento finalizado.");
				}
				
				// Si no se proporciona URL de YouTube, enviar cadena vacía
				String urlYoutubeParam = (urlYoutube != null && !urlYoutube.trim().isEmpty()) ? urlYoutube.trim() : "";
				
				portEvento.altaEdicionDeEvento(nombreEvento, organizador, nombre, sigla, fechaInicio, fechaFin, session.getAttribute("fecha").toString(), ciudad, pais, urlYoutubeParam);
				ManejadorArchivos.guardarArchivo(imagen, nombre, "ediciones", getServletContext());
				
				
				request.setAttribute("mensaje", "Edicion dada de alta exitosamente");
				request.setAttribute("error", null);
				request.setAttribute("nombreEvento", nombreEvento);
				request.getRequestDispatcher("/WEB-INF/pages/altaEdicion.jsp").forward(request, response);
			}	
			catch (Exception e) {
				// Preserve form field values when there's an error
				request.setAttribute("nombre", nombre);
				request.setAttribute("sigla", sigla);
				request.setAttribute("ciudad", ciudad);
				request.setAttribute("pais", pais);
				request.setAttribute("fechaInicio", fechaInicio);
				request.setAttribute("fechaFin", fechaFin);
				request.setAttribute("urlYoutube", urlYoutube);
				request.setAttribute("nombreEvento", nombreEvento);
				
				request.setAttribute("error", e.getMessage());
				request.setAttribute("mensaje", null);
				request.getRequestDispatcher("/WEB-INF/pages/altaEdicion.jsp").forward(request, response);
				e.printStackTrace();
			}
			
            return;
        }
        case "/altaRegistro": {
        	
            var session = request.getSession(false);
            DataUsuario user = (session == null) ? null : (DataUsuario) session.getAttribute("usuario");
            if (user == null || user.getTipo() != TipoUsuario.ASISTENTE) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                        "Solo los asistentes pueden registrarse a ediciones.");
                return;
            }

            String edicion = request.getParameter("edicion");
            String tipoReg = request.getParameter("tipoRegistro");
            String forma   = request.getParameter("formaRegistro"); // "general" | "patrocinio"
            String codigo  = request.getParameter("codigoPatrocinio"); // puede ser null

            // Reinyectar por si hay error
            request.setAttribute("edicionStr", edicion);
            request.setAttribute("tipoRegistroSel", tipoReg);
            request.setAttribute("formaRegistroSel", forma);
            request.setAttribute("codigoPatrocinioVal", codigo == null ? "" : codigo);

            if (edicion == null || edicion.isBlank() || tipoReg == null || tipoReg.isBlank() ||
                forma == null || forma.isBlank()) {
                request.setAttribute("error", "Completá la edición, el tipo de registro y la forma de registro.");
                // Instead of redirecting (which loses attributes), populate the attributes and forward so the combobox stays filled
                populateAltaRegistroAttributes(request, edicion, user, portEvento, portUsuario);
                request.getRequestDispatcher("/WEB-INF/pages/altaRegistro.jsp").forward(request, response);
                return;
            }

            try {
                // Check if user is already registered
                List<Object> registrosUsuario = portUsuario.listarRegistrosAEventos(user.getNickname()).getItem();
                boolean yaRegistrado = false;
                for (Object obj : registrosUsuario) {
                    if (edicion.equals((String) obj)) {
                        yaRegistrado = true;
                        break;
                    }
                }
                if (yaRegistrado) {
                    request.setAttribute("error", "Ya estás registrado en esta edición.");
                    populateAltaRegistroAttributes(request, edicion, user, portEvento, portUsuario);
                    request.getRequestDispatcher("/WEB-INF/pages/altaRegistro.jsp").forward(request, response);
                    return;
                }

                // Obtenemos el DtO del tipo para consultar costo/cupo/lo que haya
                DtTipoRegistro DtoTipo = portEvento.verDetalleTRegistro(edicion, tipoReg);

                boolean hayCupo = true; // fallback si no hay API. De ser posible, usar DtoTipo.getCupoRestante() > 0
                try {
                    // si tu DtO expone cupo disponible:
                    var m = DtoTipo.getClass().getMethod("getCupoRestante");
                    Object v = m.invoke(DtoTipo);
                    if (v instanceof Integer rest) hayCupo = rest > 0;
                } catch (Exception ignore) {}

                if (!hayCupo) {
                    request.setAttribute("error", "No hay cupos disponibles para el tipo seleccionado.");
                    populateAltaRegistroAttributes(request, edicion, user, portEvento, portUsuario);
                    request.getRequestDispatcher("/WEB-INF/pages/altaRegistro.jsp").forward(request, response);
                    return;
                }

                boolean usarPatrocinio = "patrocinio".equalsIgnoreCase(forma);
                if (usarPatrocinio) {
                    if (codigo == null || codigo.isBlank()) {
                        request.setAttribute("error", "Ingresá el código de patrocinio.");
                        populateAltaRegistroAttributes(request, edicion, user, portEvento, portUsuario);
                        request.getRequestDispatcher("/WEB-INF/pages/altaRegistro.jsp").forward(request, response);
                        return;
                    }

                   
                    boolean valido = true;
                    try {
                        // 'obtenerPatrocinio' expects (edicion, nombreInstitucion).
                        // The form provides a codigo de patrocinio, so search the patrocinios of the edicion
                        // and find the DtPatrocinio whose codigo matches the provided codigo.
                        DtPatrocinio p = null;
                        List<Object> listaPatrociniosValidacion = portEvento.listarPatrocinios(edicion).getItem();
                        for (Object inst : listaPatrociniosValidacion) {
                            DtPatrocinio cand = portEvento.obtenerPatrocinio(edicion, (String) inst);
                            if (cand != null && codigo.equals(cand.getCodigo())) {
                                p = cand;
                                break;
                            }
                        }

                        if (p == null) {
                            valido = false;
                        } else {
                            // Validate tipo de registro
                            boolean okTipo = true;
                            try {
                                okTipo = tipoReg.equals(p.getTipoRegistroGratis());
                            } catch (Exception ignore) { okTipo = false; }

                            // Obtener institución del patrocinio mediante el DtO
                            String instName = "";
                            try {
                                instName = (p.getInstitucion() == null) ? "" : String.valueOf(p.getInstitucion());
                            } catch (Exception ignore) { instName = ""; }

                            // Obtener institución del asistente mediante el controller
                            String userInstName = "";
                            try {
                                String inst = portUsuario.obtenerInstitucionAsistente(user.getNickname());
                                userInstName = (inst == null) ? "" : inst;
                            } catch (Exception e) {
                                userInstName = "";
                            }

                            // Si la institución no coincide, mensaje específico
                            if (!instName.equals(userInstName)) {
                                request.setAttribute("error", "El código de patrocinio no corresponde a tu institución.");
                                populateAltaRegistroAttributes(request, edicion, user, portEvento, portUsuario);
                                request.getRequestDispatcher("/WEB-INF/pages/altaRegistro.jsp").forward(request, response);
                                return;
                            }

                            // Si el tipo o los usos no son válidos, mensaje genérico
                            if (!okTipo ) {
                                valido = false;
                            }
                        }
                    } catch (Exception ex) {
                        valido = false;
                    }

                    if (!valido) {
                        request.setAttribute("error", "El código de patrocinio es inválido o no aplica.");
                        populateAltaRegistroAttributes(request, edicion, user, portEvento, portUsuario);
                        request.getRequestDispatcher("/WEB-INF/pages/altaRegistro.jsp").forward(request, response);
                        return;
                    }

                    portEvento.elegirAsistenteYTipoRegistro(user.getNickname(), tipoReg, edicion,valido);

                    request.setAttribute("mensaje", "Registro realizado exitosamente con patrocinio (costo $0).");
                } else {
                    portEvento.elegirAsistenteYTipoRegistro(user.getNickname(), tipoReg, edicion,false);
                    request.setAttribute("mensaje", "Registro realizado exitosamente.");
                }

                DtDetalleEdicion ed = portEvento.mostrarDetallesEdicion(edicion);
                request.setAttribute("edicion", ed);
                Set<DtTipoRegistro> tiposReg = new java.util.HashSet<>();
                List<Object> listaTipos2 = portEvento.listarTiposDeRegistro(edicion).getItem();
                for (Object tr : listaTipos2) {
                    tiposReg.add(portEvento.verDetalleTRegistro(edicion, (String) tr));
                }
                request.setAttribute("tiposRegistro", tiposReg);
                String edImg = ManejadorArchivos.buscarArchivo(edicion.toLowerCase(),
                        "ediciones");
                request.setAttribute("imagenEdicion",
                        edImg != null ? edImg : "images/ediciones/default.jpg");
                String nomEv = portEvento.nomEvPorEd(edicion);
                request.setAttribute("nombreEvento", nomEv);
                String imgEv = ManejadorArchivos.buscarArchivo(nomEv.toLowerCase(),
                        "eventos");
                request.setAttribute("imagenEvento",
                        imgEv != null ? imgEv : "images/eventos/default.jpg");

                request.getRequestDispatcher("/WEB-INF/pages/altaRegistro.jsp").forward(request, response);
            } catch (Exception e) {
                request.setAttribute("error", e.getMessage());
                // Ensure tiposRegistro and images are present when forwarding on exception
                populateAltaRegistroAttributes(request, edicion, user, portEvento, portUsuario);
                request.getRequestDispatcher("/WEB-INF/pages/altaRegistro.jsp").forward(request, response);
            }
            return;
        }
        default:
            break;}
    }

    // Helper: populate attributes required by altaRegistro.jsp so the combobox and images keep their values on errors
    private void populateAltaRegistroAttributes(HttpServletRequest request, String edicion, DataUsuario user, PublicadorEvento portEvento, PublicadorUsuario portUsuario) {
        try {
            if (edicion == null) {
                request.setAttribute("edicion", null);
                request.setAttribute("tiposRegistro", Collections.emptySet());
                request.setAttribute("imagenEdicion", "images/ediciones/default.jpg");
                request.setAttribute("nombreEvento", "");
                request.setAttribute("imagenEvento", "images/ediciones/default.jpg");
                request.setAttribute("yaRegistrado", false);
                return;
            }

            DtDetalleEdicion ed = portEvento.mostrarDetallesEdicion(edicion);
            request.setAttribute("edicion", ed);

            Set<DtTipoRegistro> tiposReg = new HashSet<>();
            List<Object> listaTipos = portEvento.listarTiposDeRegistro(ed.getNombre()).getItem();
            for (Object tr : listaTipos) {
                tiposReg.add(portEvento.verDetalleTRegistro(ed.getNombre(), (String) tr));
            }
            request.setAttribute("tiposRegistro", tiposReg);

            String edImg = ManejadorArchivos.buscarArchivo(edicion.toLowerCase(), "ediciones");
            request.setAttribute("imagenEdicion", edImg != null ? edImg : "images/ediciones/default.jpg");

            String nomEv = portEvento.nomEvPorEd(edicion);
            request.setAttribute("nombreEvento", nomEv == null ? "" : nomEv);
            String imgEv = ManejadorArchivos.buscarArchivo((nomEv == null ? "" : nomEv).toLowerCase(), "eventos");
            request.setAttribute("imagenEvento", imgEv != null ? imgEv : "images/eventos/default.jpg");

            boolean yaRegistrado = false;
            try {
                List<Object> registrosObj = portUsuario.listarRegistrosAEventos(user.getNickname()).getItem();
                for (Object obj : registrosObj) {
                    if (edicion.equals((String) obj)) {
                        yaRegistrado = true;
                        break;
                    }
                }
            } catch (Exception ignore) {}
            request.setAttribute("yaRegistrado", yaRegistrado);
        } catch (Exception e) {
            request.setAttribute("edicion", null);
            request.setAttribute("tiposRegistro", Collections.emptySet());
            request.setAttribute("imagenEdicion", "images/ediciones/default.jpg");
            request.setAttribute("nombreEvento", "");
            request.setAttribute("imagenEvento", "images/eventos/default.jpg");
            request.setAttribute("yaRegistrado", false);
        }
    }
}
