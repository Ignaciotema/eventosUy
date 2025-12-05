import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import jakarta.servlet.annotation.MultipartConfig;
import java.io.IOException;
import java.util.List;
import java.util.Set;
// Imports de webservices
import webservices.DataUsuario;
import webservices.PublicadorUsuario;
import webservices.PublicadorUsuarioService;
import webservices.WrapperHashSet;
import webservices.NombreUsuarioExistente_Exception;
import webservices.EmailRepetido_Exception;
import webservices.UsuarioNoEncontrado_Exception;

/**
 * Servlet implementation class ServletAutenticator
 */
@WebServlet({"/registro", "/iniciosesion", "/cerrarsesion", "/verificar-disponibilidad"})
@MultipartConfig
public class ServletAutenticator extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public ServletAutenticator() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getServletPath();
		
		switch (path) {
			case "/registro": {
				// Obtener instituciones usando webservices
				PublicadorUsuarioService serviceUsuario = new PublicadorUsuarioService();
				PublicadorUsuario portUsuario = serviceUsuario.getPublicadorUsuarioPort();
				
				try {
					WrapperHashSet institucionesWrapper = portUsuario.listarInstituciones();
					List<Object> institucionesObj = institucionesWrapper.getItem();
					Set<String> instituciones = new java.util.HashSet<>();
					for (Object obj : institucionesObj) {
						instituciones.add((String) obj);
					}
					request.setAttribute("instituciones", instituciones);
				} catch (Exception e) {
					System.err.println("Error obteniendo instituciones: " + e.getMessage());
					request.setAttribute("instituciones", new java.util.HashSet<String>());
				}
				
				request.getRequestDispatcher("/WEB-INF/pages/registro.jsp").forward(request, response);
				break;
			}
			case "/iniciosesion": {
				request.getRequestDispatcher("/WEB-INF/pages/iniciosesion.jsp").forward(request, response);
				break;
			}
			case "/cerrarsesion": {
				HttpSession session = request.getSession(false);
				if (session != null) {
					session.invalidate();
				}
				response.sendRedirect(request.getContextPath() + "/HomeServlet");
				break;
			}
			case "/verificar-disponibilidad": {
				verificarDisponibilidad(request, response);
				break;
			}
			default:
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				break;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getServletPath();
		
		switch (path) {
			case "/registro": {
				procesarRegistro(request, response);
				break;
			}
			case "/iniciosesion": {
				procesarInicioSesion(request, response);
				break;
			}
			default:
				doGet(request, response);
				break;
		}
	}
	
	private void procesarRegistro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nickname = request.getParameter("nickname");
		String nombre = request.getParameter("nombre");
		String apellido = request.getParameter("apellido");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String fechaNacimiento = request.getParameter("fechaNacimiento");
		
		PublicadorUsuarioService serviceUsuario = new PublicadorUsuarioService();
		PublicadorUsuario portUsuario = serviceUsuario.getPublicadorUsuarioPort();
		
		// Determinar tipo de usuario
		String[] tiposUsuario = request.getParameterValues("tipoUsuario");
		String tipoUsuario = "asistente"; 
		
		if (tiposUsuario != null) {
			for (String tipo : tiposUsuario) {
				if ("organizador".equals(tipo)) {
					tipoUsuario = "organizador";
					break;
				} else if ("asistente".equals(tipo)) {
					tipoUsuario = "asistente";
				}
			}
		}
		
		try {
			// Procesar fecha de nacimiento
			java.time.LocalDate fechaNac = null;
			if (fechaNacimiento != null && !fechaNacimiento.trim().isEmpty()) {
				try {
					fechaNac = java.time.LocalDate.parse(fechaNacimiento);
				} catch (Exception e) {
					request.setAttribute("error", "Formato de fecha inválido.");
					preservarDatosFormulario(request, nickname, nombre, apellido, email, fechaNacimiento, tipoUsuario);
					cargarInstituciones(request, portUsuario);
					request.getRequestDispatcher("/WEB-INF/pages/registro.jsp").forward(request, response);
					return;
				}
			}
			
			// Registrar usuario usando webservices
			if ("organizador".equals(tipoUsuario)) {
				String descripcion = request.getParameter("descripcion");
				String web = request.getParameter("sitioWeb");
				if (descripcion == null) descripcion = "";
				if (web == null) web = "";
				
				portUsuario.ingresarOrganizador(nickname.trim(), nombre.trim(), 
											   email.trim(), password.trim(), descripcion, web);
			} else {
				String fechaString = fechaNac != null ? fechaNac.toString() : "";
				portUsuario.ingresarAsistente(nickname.trim(), nombre.trim(), 
											 email.trim(), password.trim(),
											 apellido != null ? apellido.trim() : "", fechaString);
				
				String institucion = request.getParameter("institucion");
				if (institucion != null && !institucion.trim().isEmpty()) {
					portUsuario.agregarAsistente(nickname.trim(), institucion.trim());
				}
			}
			
			// Guardar imagen de perfil
			Part imagen = request.getPart("imagen");
			ManejadorArchivos.guardarArchivo(imagen, nickname.toLowerCase(), "usuarios", getServletContext());
			
			// Registro exitoso
			response.sendRedirect(request.getContextPath() + "/iniciosesion?registered=true");
			
		} catch (NombreUsuarioExistente_Exception e) {
			request.setAttribute("error", "Ya existe un usuario con ese nickname.");
			preservarDatosFormulario(request, nickname, nombre, apellido, email, fechaNacimiento, tipoUsuario);
			cargarInstituciones(request, portUsuario);
			request.getRequestDispatcher("/WEB-INF/pages/registro.jsp").forward(request, response);
		} catch (EmailRepetido_Exception e) {
			request.setAttribute("error", "Ya existe un usuario con ese email.");
			preservarDatosFormulario(request, nickname, nombre, apellido, email, fechaNacimiento, tipoUsuario);
			cargarInstituciones(request, portUsuario);
			request.getRequestDispatcher("/WEB-INF/pages/registro.jsp").forward(request, response);
		} catch (Exception e) {
			request.setAttribute("error", "Error al registrar usuario: " + e.getMessage());
			preservarDatosFormulario(request, nickname, nombre, apellido, email, fechaNacimiento, tipoUsuario);
			cargarInstituciones(request, portUsuario);
			request.getRequestDispatcher("/WEB-INF/pages/registro.jsp").forward(request, response);
		}
	}
	
	private void procesarInicioSesion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nicknameomail = request.getParameter("nickname");
		String password = request.getParameter("password");

		PublicadorUsuarioService serviceUsuario = new PublicadorUsuarioService();
		PublicadorUsuario portUsuario = serviceUsuario.getPublicadorUsuarioPort();
		
		DataUsuario usuario = new DataUsuario();
		boolean loginExitoso = false;
		
		// Intentar login por nickname primero
		try {
			usuario = portUsuario.iniciarSesionNickname(nicknameomail.trim(), password.trim());
			if (usuario != null) {
				loginExitoso = true;
			}
		} catch (Exception e) {
			// Otros errores (contraseña incorrecta, etc.)
		}
		
		// Si no funcionó por nickname, intentar por email
		if (!loginExitoso) {
			try {
				usuario = portUsuario.iniciarSesionEmail(nicknameomail.trim(), password.trim());
				if (usuario != null) {
					loginExitoso = true;
				}
			} catch (Exception e) {
				// Login falló completamente
			}
		}
		
		if (loginExitoso && usuario != null) {
			// Crear sesión y configurar atributos
			HttpSession session = request.getSession();
			session.setAttribute("usuario", usuario);
			
			// Configurar imagen de perfil usando el sistema centralizado
			String pfp = ManejadorArchivos.buscarArchivo(usuario.getNickname().toLowerCase(), "usuarios");
			session.setAttribute("pfp", pfp);
			
			response.sendRedirect(request.getContextPath() + "/HomeServlet");
		} else {
			request.setAttribute("error", "Credenciales incorrectas.");
			request.setAttribute("nickname", nicknameomail);
			request.getRequestDispatcher("/WEB-INF/pages/iniciosesion.jsp").forward(request, response);
		}
	}
	
	private void verificarDisponibilidad(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		String tipo = request.getParameter("tipo");
		String valor = request.getParameter("valor");
		
		if (tipo == null || valor == null || valor.trim().isEmpty()) {
			response.getWriter().write("{\"disponible\": false}");
			return;
		}
		
		PublicadorUsuarioService serviceUsuario = new PublicadorUsuarioService();
		PublicadorUsuario portUsuario = serviceUsuario.getPublicadorUsuarioPort();
		
		boolean disponible = false;
		
		try {
			if ("nickname".equals(tipo)) {
				// Usar la función específica existeNickname
				disponible = !portUsuario.existeNickname(valor.trim());
			} else if ("email".equals(tipo)) {
				// Usar la función específica existeEmail
				disponible = !portUsuario.existeEmail(valor.trim());
			}
		} catch (Exception e) {
			// Para cualquier error, asumimos que no está disponible por seguridad
			disponible = false;
		}
		
		response.getWriter().write("{\"disponible\": " + disponible + "}");
	}
	
	private void preservarDatosFormulario(HttpServletRequest request, String nickname, String nombre, 
										 String apellido, String email, String fechaNacimiento, String tipoUsuario) {
		request.setAttribute("nickname", nickname);
		request.setAttribute("nombre", nombre);
		request.setAttribute("apellido", apellido);
		request.setAttribute("email", email);
		request.setAttribute("fechaNacimiento", fechaNacimiento);
		request.setAttribute("tipoUsuario", tipoUsuario);
	}
	
	private void cargarInstituciones(HttpServletRequest request, PublicadorUsuario portUsuario) {
		try {
			WrapperHashSet institucionesWrapper = portUsuario.listarInstituciones();
			List<Object> institucionesObj = institucionesWrapper.getItem();
			Set<String> instituciones = new java.util.HashSet<>();
			for (Object obj : institucionesObj) {
				instituciones.add((String) obj);
			}
			request.setAttribute("instituciones", instituciones);
		} catch (Exception ex) {
			System.err.println("Error obteniendo instituciones: " + ex.getMessage());
			request.setAttribute("instituciones", new java.util.HashSet<String>());
		}
	}
}