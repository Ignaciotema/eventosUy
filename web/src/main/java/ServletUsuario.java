import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
//import logica.controllers.IControllerUsuario;
//import logica.controllers.IControllerEvento;
import webservices.DtAsistente;
import webservices.DtOrganizador;
import webservices.DataUsuario;
import webservices.TipoUsuario;
import webservices.UsuarioNoEncontrado;
//import webservices.Factory;
import webservices.PublicadorUsuario;
import webservices.PublicadorUsuarioService;
import webservices.UsuarioNoEncontrado_Exception;
import webservices.WrapperHashSet;

@MultipartConfig
@WebServlet({ "/usuarios", "/listarUsuarios", "/detalleUsuario", "/modificarDatos", "/perfil", "/seguirUsuario", "/dejarDeSeguir" })
public class ServletUsuario extends HttpServlet {
    private static final long serialVersionUID = 1L;
    //private IControllerUsuario portUsuario;
    private PublicadorUsuario portUsuario;

    public ServletUsuario() {
        super();
        PublicadorUsuarioService serviceUsuario = new PublicadorUsuarioService();
        portUsuario = serviceUsuario.getPublicadorUsuarioPort();

    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        DataUsuario usrSession = (DataUsuario) request.getSession().getAttribute("usuario");
        String usuario = usrSession != null ? usrSession.getNickname() : null;
        String usuarios = request.getParameter("usuarios");
        String path    = request.getServletPath();

        if ("/modificarDatos".equals(path)) {
            if (usuario == null || usuario.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta parámetro 'usuario'");
                return;
            }
            try {
                //DataUsuario du = portUsuario.infoUsuario(usuario);
                DataUsuario du = portUsuario.infoUsuario(usuario);
                request.setAttribute("usuario", du);

                if (du.getTipo() ==  TipoUsuario.ORGANIZADOR) {
                    //DtOrganizador org = portUsuario.infoOrganizador(usuario);
                    DtOrganizador org = portUsuario.infoOrganizador(usuario);
                    request.setAttribute("detalleUsuario", org);
                } else {
                    //DtAsistente asis = portUsuario.infoAsistente(usuario);
                    DtAsistente asis = portUsuario.infoAsistente(usuario);
                    request.setAttribute("detalleUsuario", asis);
                }

                // Imagen de perfil usando el nuevo sistema centralizado
                String imagenUsuario = ManejadorArchivos.buscarArchivo(usuario.toLowerCase(), "usuarios");
                request.setAttribute("imagenUsuario", imagenUsuario);

                if ("1".equals(request.getParameter("ok"))) {
                    request.setAttribute("mensaje", "Modificaciones realizadas exitosamente.");
                }

                request.getRequestDispatcher("/WEB-INF/pages/modificarDatos.jsp").forward(request, response);
                return;

            } catch (UsuarioNoEncontrado_Exception e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Usuario no encontrado");
                return;
            } catch (Exception e) {
                request.setAttribute("error", "No se pudieron cargar los datos: " + e.getMessage());
                request.getRequestDispatcher("/WEB-INF/pages/modificarDatos.jsp").forward(request, response);
                return;
            }
        }

  
        switch (path) {
            case "/usuarios":
            case "/listarUsuarios":
                listarUsuarios(request, response);
                break;

            case "/perfil":
                perfil(request, response);
                break;

            case "/detalleUsuario":
                try {
                    detalleUsuario(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Usuario no encontrado");
                }
                break;

            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
                break;
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String usuario = request.getParameter("usuario");
        String path = request.getServletPath();

        if ("/modificarDatos".equals(path) && usuario != null && !usuario.isBlank()) {
            modificarDatos(request, response);
            return;
        }
        
        // Manejar acciones de seguimiento
        if ("/seguirUsuario".equals(path) || "/dejarDeSeguir".equals(path)) {
            manejarSeguimiento(request, response);
            return;
        }
        
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
    }
    
    private void manejarSeguimiento(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        DataUsuario sessionUser = (DataUsuario) request.getSession().getAttribute("usuario");
        if (sessionUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String usuarioASeguir = request.getParameter("usuario");
        String path = request.getServletPath();
        String referer = request.getHeader("Referer");
        
        if (usuarioASeguir == null || usuarioASeguir.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parámetro usuario requerido");
            return;
        }

        try {
            if ("/seguirUsuario".equals(path)) {
                portUsuario.seguirUsuario(sessionUser.getNickname(), usuarioASeguir);
                request.getSession().setAttribute("mensajeSeguimiento", "Ahora sigues a " + usuarioASeguir);
            } else if ("/dejarDeSeguir".equals(path)) {
                portUsuario.dejarDeSeguirUsuario(sessionUser.getNickname(), usuarioASeguir);
                request.getSession().setAttribute("mensajeSeguimiento", "Ya no sigues a " + usuarioASeguir);
            }
            
            // Determinar a dónde redirigir basándose en el referer
            if (referer != null && referer.contains("/listarUsuarios")) {
                // Si viene de la lista de usuarios, regresar ahí
                response.sendRedirect(request.getContextPath() + "/listarUsuarios");
            } else {
                // Si viene del detalle de usuario, regresar ahí
                response.sendRedirect(request.getContextPath() + "/detalleUsuario?usuarios=" + 
                    java.net.URLEncoder.encode(usuarioASeguir, java.nio.charset.StandardCharsets.UTF_8));
            }
            
        } catch (UsuarioNoEncontrado_Exception e) {
            request.getSession().setAttribute("errorSeguimiento", "Usuario no encontrado");
            // Redirigir apropiadamente según el origen
            if (referer != null && referer.contains("/listarUsuarios")) {
                response.sendRedirect(request.getContextPath() + "/listarUsuarios");
            } else {
                response.sendRedirect(request.getContextPath() + "/detalleUsuario?usuarios=" + 
                    java.net.URLEncoder.encode(usuarioASeguir, java.nio.charset.StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            request.getSession().setAttribute("errorSeguimiento", "Error interno del servidor: " + e.getMessage());
            // Redirigir apropiadamente según el origen
            if (referer != null && referer.contains("/listarUsuarios")) {
                response.sendRedirect(request.getContextPath() + "/listarUsuarios");
            } else {
                response.sendRedirect(request.getContextPath() + "/detalleUsuario?usuarios=" + 
                    java.net.URLEncoder.encode(usuarioASeguir, java.nio.charset.StandardCharsets.UTF_8));
            }
        }
    }
    
    private void detalleUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        String usuario = request.getParameter("usuarios"); 
        if (usuario == null || usuario.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta parámetro 'usuarios'");
            return;
        }

        
        DataUsuario sessionUser = (DataUsuario) request.getSession().getAttribute("usuario");
        if (sessionUser != null && sessionUser.getNickname() != null
                && sessionUser.getNickname().equalsIgnoreCase(usuario)) {
            response.sendRedirect(request.getContextPath() + "/perfil");
            return;
        }

        DataUsuario usr;
        try {
            usr = this.portUsuario.infoUsuario(usuario);
        } catch (UsuarioNoEncontrado_Exception ex) {
            response.sendError(404);
            request.getRequestDispatcher("/WEB-INF/errorPages/404.jsp").include(request, response);
            return;
        }

        
        request.setAttribute("usuario", usr);

        // Obtener mensajes de seguimiento desde la sesión
        String mensajeSeguimiento = (String) request.getSession().getAttribute("mensajeSeguimiento");
        String errorSeguimiento = (String) request.getSession().getAttribute("errorSeguimiento");
        if (mensajeSeguimiento != null) {
            request.setAttribute("mensajeSeguimiento", mensajeSeguimiento);
            request.getSession().removeAttribute("mensajeSeguimiento");
        }
        if (errorSeguimiento != null) {
            request.setAttribute("errorSeguimiento", errorSeguimiento);
            request.getSession().removeAttribute("errorSeguimiento");
        }

        // Obtener información de seguidores y seguidos
        try {
            int cantidadSeguidores = portUsuario.cantidadSeguidores(usuario);
            int cantidadSeguidos = portUsuario.cantidadSeguidos(usuario);
            request.setAttribute("cantidadSeguidores", cantidadSeguidores);
            request.setAttribute("cantidadSeguidos", cantidadSeguidos);
            
            // Verificar si el usuario actual está siguiendo a este usuario
            boolean yaSigue = false;
            if (sessionUser != null && sessionUser.getNickname() != null) {
                try {
                    yaSigue = portUsuario.esSeguidor(sessionUser.getNickname(), usuario);
                } catch (UsuarioNoEncontrado_Exception e) {
                    yaSigue = false;
                }
                request.setAttribute("usuarioLogueado", sessionUser.getNickname());
            }
            request.setAttribute("yaSigue", yaSigue);
            
        } catch (UsuarioNoEncontrado_Exception e) {
            // Si hay error, ponemos valores por defecto
            request.setAttribute("cantidadSeguidores", 0);
            request.setAttribute("cantidadSeguidos", 0);
            request.setAttribute("yaSigue", false);
        }

        if (usr.getTipo() ==  TipoUsuario.ORGANIZADOR) {
            //DtOrganizador org = this.portUsuario.infoOrganizador(usuario);
            DtOrganizador org = portUsuario.infoOrganizador(usuario);
            request.setAttribute("detalleUsuario", org);

            
            try {
                //IControllerEvento ICE = Factory.getInstance().getControllerEvento();
                //Set<String> ediciones = this.portUsuario.listarEdicionesOrganizadas(usuario);
                Set<String> ediciones = new HashSet<>();
                List<Object> lista = portUsuario.listarEdicionesOrganizadas(usuario).getItem();
                for (Object obj : lista) {
					ediciones.add((String) obj);
				}
                
                request.setAttribute("ediciones", ediciones);

                Map<String, String> edicionesMap = new HashMap<>();
                for (String ed : ediciones) {
                    String imagenEdicion = ManejadorArchivos.buscarArchivo(ed.toLowerCase(), "ediciones");
                    edicionesMap.put(ed, imagenEdicion);
                }
                request.setAttribute("edicionesMap", edicionesMap);

            } catch (Exception ignore) {
                request.setAttribute("ediciones", java.util.Collections.emptySet());
                request.setAttribute("edicionesMap", java.util.Collections.emptyMap());
            }
        } else {
            //DtAsistente asis = this.portUsuario.infoAsistente(usuario);
            DtAsistente asis = portUsuario.infoAsistente(usuario);
            request.setAttribute("detalleUsuario", asis);
        }

        // Imagen de usuario usando el nuevo sistema centralizado
        String imagenUsuario = ManejadorArchivos.buscarArchivo(usuario.toLowerCase(), "usuarios");
        request.setAttribute("imagenUsuario", imagenUsuario);

        request.getRequestDispatcher("/WEB-INF/pages/detalleUsuario.jsp").forward(request, response);
    }

    
    private void perfil(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DataUsuario usrSession = (DataUsuario) request.getSession().getAttribute("usuario");
        String usuario = usrSession.getNickname();

        DataUsuario usr;
        try {
            //usr = this.portUsuario.infoUsuario(usuario);
            usr = portUsuario.infoUsuario(usuario);
        } catch (UsuarioNoEncontrado_Exception ex) {
            response.sendError(404);
            request.getRequestDispatcher("/WEB-INF/errorPages/404.jsp").include(request, response);
            return;
        }

        if (usr.getTipo() ==  TipoUsuario.ORGANIZADOR) {
            //DtOrganizador org = this.portUsuario.infoOrganizador(usuario);
            DtOrganizador org = portUsuario.infoOrganizador(usuario);
            request.setAttribute("usuario", org);
        } else {
            //DtAsistente asis = this.portUsuario.infoAsistente(usuario);
            DtAsistente asis = portUsuario.infoAsistente(usuario);
            request.setAttribute("usuario", asis);
        }

        // Obtener información de seguidores y seguidos para el perfil
        try {
            int cantidadSeguidores = portUsuario.cantidadSeguidores(usuario);
            int cantidadSeguidos = portUsuario.cantidadSeguidos(usuario);
            request.setAttribute("cantidadSeguidores", cantidadSeguidores);
            request.setAttribute("cantidadSeguidos", cantidadSeguidos);
        } catch (UsuarioNoEncontrado_Exception e) {
            // Si hay error, ponemos valores por defecto
            request.setAttribute("cantidadSeguidores", 0);
            request.setAttribute("cantidadSeguidos", 0);
        }

        // Imagen de usuario usando el nuevo sistema centralizado
        String imagenUsuario = ManejadorArchivos.buscarArchivo(usuario.toLowerCase(), "usuarios");
        request.setAttribute("imagenUsuario", imagenUsuario);

        request.getRequestDispatcher("/WEB-INF/pages/perfil.jsp").forward(request, response);
    }

    
    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String q = trimOrNull(request.getParameter("q"));
        DataUsuario sessionUser = (DataUsuario) request.getSession().getAttribute("usuario");

        Set<String> usrs = new HashSet<>();
        List<Object> lista = this.portUsuario.listarUsuarios().getItem();
        
        for (Object obj : lista) {
			usrs.add((String) obj);
        }
        
        try {
            Set<DataUsuario> usuarios = new java.util.HashSet<DataUsuario>();
            Map<String, String> imgsUsuarios = new java.util.HashMap<>();
            Map<String, Boolean> estadosSeguimiento = new java.util.HashMap<>();

            for (String u : usrs) {
                
                if (q != null && !q.isBlank()) {
                    if (!u.toLowerCase().contains(q.toLowerCase())) {
                        continue;
                    }
                }

                try {
                    DataUsuario usr = portUsuario.infoUsuario(u);
                    usuarios.add(usr);

                    // Imagen de usuario usando el nuevo sistema centralizado
                    String imagenUsuario = ManejadorArchivos.buscarArchivo(u.toLowerCase(), "usuarios");
                    imgsUsuarios.put(u, imagenUsuario);
                    
                    // Verificar si el usuario logueado ya sigue a este usuario
                    boolean yaSigue = false;
                    if (sessionUser != null && sessionUser.getNickname() != null 
                        && !sessionUser.getNickname().equals(u)) {
                        try {
                            yaSigue = portUsuario.esSeguidor(sessionUser.getNickname(), u);
                        } catch (UsuarioNoEncontrado_Exception e) {
                            yaSigue = false;
                        }
                    }
                    estadosSeguimiento.put(u, yaSigue);
                    
                } catch (UsuarioNoEncontrado_Exception e) {
                    e.printStackTrace();
                }
            }

            request.setAttribute("usuarios", usuarios);
            request.setAttribute("imgsUsuarios", imgsUsuarios);
            request.setAttribute("estadosSeguimiento", estadosSeguimiento);
            request.setAttribute("usuarioLogueado", sessionUser != null ? sessionUser.getNickname() : null);
            request.setAttribute("q", q == null ? "" : q);
            request.getRequestDispatcher("/WEB-INF/pages/listarUsuarios.jsp").forward(request, response);
            
        } catch (Exception e1) {
            e1.printStackTrace();
            request.setAttribute("usuarios", java.util.Collections.emptySet());
            request.setAttribute("imgsUsuarios", java.util.Collections.emptyMap());
            request.setAttribute("estadosSeguimiento", java.util.Collections.emptyMap());
            request.setAttribute("usuarioLogueado", sessionUser != null ? sessionUser.getNickname() : null);
            request.setAttribute("q", q == null ? "" : q);
            request.getRequestDispatcher("/WEB-INF/pages/listarUsuarios.jsp").forward(request, response);
        }
    }

    
    private void modificarDatos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nickParam = request.getParameter("usuario");
        if (nickParam == null || nickParam.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta parámetro 'usuario'");
            return;
        }

        DataUsuario usr;
        try {
            //usr = this.portUsuario.infoUsuario(nickParam);
            usr = portUsuario.infoUsuario(nickParam);
        } catch (UsuarioNoEncontrado_Exception ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Usuario no encontrado");
            return;
        }

        try {
            if (usr.getTipo() ==  TipoUsuario.ASISTENTE) {
                String nombre     = trimOrNull(request.getParameter("nombre"));
                String apellido   = trimOrNull(request.getParameter("apellido"));
                String fechaNacStr= trimOrNull(request.getParameter("fechaNac")); // YYYY-MM-DD

                if (isBlankAny(nombre, apellido, fechaNacStr)) {
                    request.setAttribute("error", "Completá nombre, apellido y fecha de nacimiento.");
                    request.setAttribute("usuario", usr);
                    forwardEditar(request, response);
                    return;
                }

                LocalDate fechaNac;
                try {
                    fechaNac = LocalDate.parse(fechaNacStr);
                } catch (Exception pe) {
                    request.setAttribute("error", "Fecha de nacimiento inválida (formato esperado: AAAA-MM-DD).");
                    request.setAttribute("usuario", usr);
                    request.setAttribute("nombre", nombre);
                    request.setAttribute("apellido", apellido);
                    request.setAttribute("fechaNac", fechaNacStr);
                    forwardEditar(request, response);
                    return;
                }

                portUsuario.editarAsistente(nickParam, nombre, apellido, fechaNacStr);

            } else if (usr.getTipo() ==  TipoUsuario.ORGANIZADOR) {
                String nombre      = trimOrNull(request.getParameter("nombre"));
                String descripcion = trimOrNull(request.getParameter("descripcion"));
                String web         = trimOrNull(request.getParameter("web"));

                if (isBlankAny(nombre, descripcion)) {
                    request.setAttribute("error", "Completá nombre y descripción.");
                    request.setAttribute("usuario", usr);
                    request.setAttribute("nombre", nombre);
                    request.setAttribute("descripcion", descripcion);
                    request.setAttribute("web", web);
                    forwardEditar(request, response);
                    return;
                }

                portUsuario.editarOrganizador(nickParam, nombre, descripcion, web);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tipo de usuario no soportado");
                return;
            }

            try {
                Part avatar = request.getPart("avatar");
                if (avatar != null && avatar.getSize() > 0) {
                	
                    ManejadorArchivos.guardarArchivo(avatar, nickParam, "usuarios", getServletContext());
    				String pfp = ManejadorArchivos.buscarArchivo(nickParam.toLowerCase(), "usuarios");

    				System.out.println("PFP seteada en sesión: " + pfp);
    				// Actualizar la sesión con la nueva imagen
    				request.getSession().setAttribute("pfp", pfp);
                }
            } catch (Exception ignore) {  }

            String url = request.getContextPath()
                    + "/modificarDatos?usuario="
                    + java.net.URLEncoder.encode(nickParam, java.nio.charset.StandardCharsets.UTF_8)
                    + "&ok=1";
            response.sendRedirect(url);
            return;

        } catch (Exception e) {
            request.setAttribute("error", "No se pudieron guardar los cambios: " + e.getMessage());
            request.setAttribute("usuario", usr);
            forwardEditar(request, response);
         }
     }

    
    private static String trimOrNull(String s) {
        return s == null ? null : s.trim();
    }

    private static boolean isBlankAny(String... arr) {
        if (arr == null) return true;
        for (String s : arr) {
            if (s == null || s.isBlank()) return true;
        }
        return false;
    }

    private void forwardEditar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Object uo = request.getAttribute("usuario");
        if (uo instanceof DataUsuario du) {
            // Imagen de usuario usando el nuevo sistema centralizado
            String imagenUsuario = ManejadorArchivos.buscarArchivo(du.getNickname().toLowerCase(), "usuarios");
            request.setAttribute("imagenUsuario", imagenUsuario);
        }
        request.getRequestDispatcher("/WEB-INF/pages/modificarDatos.jsp").forward(request, response);
    }
}
