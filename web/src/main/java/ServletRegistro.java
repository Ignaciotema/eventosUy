import java.io.IOException;


import java.lang.reflect.Method;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import webservices.DtAsistente;
import webservices.DtRegistro;
import webservices.PublicadorEvento;
import webservices.PublicadorEventoService;
import webservices.PublicadorUsuario;
import webservices.PublicadorUsuarioService;


/**
 * Servlet registrossss
 */
@WebServlet({ "/ver-registro", "/listar-registros", "/alta-tipo-registro", "/confirmar-asistencia" })
public class ServletRegistro extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ServletRegistro() { super(); }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();
        // publicadres y soap
        PublicadorEventoService serviceEvento = new PublicadorEventoService();
        PublicadorEvento portEvento = serviceEvento.getPublicadorEventoPort();

        PublicadorUsuarioService serviceUsuario = new PublicadorUsuarioService();
        PublicadorUsuario portUsuario = serviceUsuario.getPublicadorUsuarioPort();

        switch (path) {
            case "/ver-registro": {
                String edicion = request.getParameter("edicion");
                String usuario = request.getParameter("usuario");

                if (usuario == null || usuario.isBlank()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta parámetro 'usuario'");
                    return;
                }

                try {
                    DtRegistro registro = portEvento.infoRegistro(edicion, usuario);
                    if (registro == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Registro no encontrado");
                        return;
                    }

                    request.setAttribute("registro", registro);
                    request.setAttribute("usuario", usuario);

                    // img usr
                    String imgUsuario = ManejadorArchivos.buscarArchivo(usuario.toLowerCase(), "usuarios");
                    request.setAttribute("imagenUsuario", imgUsuario);

                    // nombre de ediciòn 
                    String nombreEdicion = (edicion != null && !edicion.isBlank()) ? edicion : registro.getNombreEdicion();

                    // imagen de la ediciòn
                    String imgEdicion = ManejadorArchivos.buscarArchivo(nombreEdicion.toLowerCase(), "ediciones");
                    request.setAttribute("imagenEdicion", imgEdicion);

                    request.getRequestDispatcher("/WEB-INF/pages/detalleRegistro.jsp").forward(request, response);
                    return;
                } catch (Exception ex) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "No se pudo obtener el registro: " + ex.getMessage());
                    return;
                }
            }

            case "/listar-registros": {
                String edicion = request.getParameter("edicion");
                if (edicion == null || edicion.isBlank()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta parámetro 'edicion'");
                    return;
                }

                String q = request.getParameter("q");
                String qNorm = q == null ? "" : q.trim().toLowerCase();

                try {
                    // asistentes por la ediciòn 
                    List<Object> asistObj = portEvento.listarAsistentesAEdicionDeEvento(edicion).getItem();

                    List<Map.Entry<String, DtRegistro>> regs = new ArrayList<>();
                    if (asistObj != null) {
                        for (Object nick : asistObj) {
                        	
                            if (nick == null || ((String)nick).isBlank()) continue;

                            // filtro de bùsqueda
                            if (!qNorm.isEmpty() && (nick == null || !((String)nick).toLowerCase().contains(qNorm))) {
                                continue;
                            }

                            DtRegistro r = portEvento.infoRegistro(edicion, ( (String) nick) );
                            if (r != null) {
                                regs.add(new AbstractMap.SimpleEntry<>( ( (String) nick) , r));
                            }
                        }
                    }

                    if (regs.isEmpty()) {
                        request.setAttribute("mensaje",
                                (qNorm.isEmpty() ? "No hay registros para la edición" :
                                                   "No hubo coincidencias para la búsqueda"));
                    }

                    // imàgenes por usuario
                    Map<String, String> imgsUsuarios = new HashMap<>();
                    for (var e : regs) {
                        String nick = e.getKey();
                        String img = ManejadorArchivos.buscarArchivo(nick.toLowerCase(), "usuarios");
                        imgsUsuarios.put(nick, img);
                    }

                    request.setAttribute("edicion", edicion);
                    request.setAttribute("registros", regs);
                    request.setAttribute("q", q == null ? "" : q);
                    request.setAttribute("imgsUsuarios", imgsUsuarios);

                    request.getRequestDispatcher("/WEB-INF/pages/listarRegistros.jsp").forward(request, response);
                    return;
                } catch (Exception ex) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "No se pudo listar registros: " + ex.getMessage());
                    return;
                }
            }

            case "/alta-tipo-registro": {
                String edicion = request.getParameter("edicion");
                request.setAttribute("edicion", edicion);
                request.getRequestDispatcher("/WEB-INF/pages/altaTipoRegistro.jsp").forward(request, response);
                return;
            }

            default:
                break;
        }

        response.getWriter().append("Served at: ").append(getServletContext().getContextPath());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/alta-tipo-registro".equals(path)) {
            PublicadorEventoService serviceEvento = new PublicadorEventoService();
            PublicadorEvento portEvento = serviceEvento.getPublicadorEventoPort();

            String edicion = request.getParameter("edicion");
            String nombre  = request.getParameter("nombre");
            String desc    = request.getParameter("descripcion");
            String costoS  = request.getParameter("costo");
            String cupoS   = request.getParameter("cupo");

            try {
                Float costo = Float.parseFloat(costoS.replace(",", "."));
                int cupo = Integer.parseInt(cupoS);

                if (costo < 0 || cupo <= 0) {
                    throw new IllegalArgumentException("Costo y cupo deben ser positivos");
                }

                portEvento.altaTipoDeRegistro(edicion, nombre, desc, costo, cupo);

                String url = request.getContextPath() + "/detalleEdicion?nombre=" + edicion;
                response.sendRedirect(url);
                return;

            } catch (NumberFormatException nfe) {
                request.setAttribute("error", "Formato numérico inválido en costo o cupo.");
                request.setAttribute("edicion", edicion);
                request.setAttribute("nombre", nombre);
                request.setAttribute("descripcion", desc);
                request.setAttribute("costo", costoS);
                request.setAttribute("cupo", cupoS);
                request.getRequestDispatcher("/WEB-INF/pages/altaTipoRegistro.jsp").forward(request, response);
                return;

            } catch (Exception e) {
                request.setAttribute("error", "Error al dar de alta el tipo de registro: " + e.getMessage());
                request.setAttribute("edicion", edicion);
                request.setAttribute("nombre", nombre);
                request.setAttribute("descripcion", desc);
                request.setAttribute("costo", costoS);
                request.setAttribute("cupo", cupoS);
                request.getRequestDispatcher("/WEB-INF/pages/altaTipoRegistro.jsp").forward(request, response);
                return;
            }
        }

    }
}