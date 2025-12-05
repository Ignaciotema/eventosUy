import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import webservices.DataUsuario;
import webservices.DtPatrocinio;
import webservices.DtTipoRegistro;
import webservices.PublicadorEvento;
import webservices.PublicadorEventoService;
import webservices.PublicadorUsuario;
import webservices.PublicadorUsuarioService;
import webservices.TipoUsuario;
import webservices.WrapperHashSet;


@WebServlet({"/altaInstitucion","/altaPatrocinio"})
@MultipartConfig
public class ServletPatrocinio extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletPatrocinio() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String path = request.getServletPath();
    	String nombreEdicion = request.getParameter("nombreEdicion");
    	request.getSession().setAttribute("nombreEdicion", nombreEdicion);
    	
    	PublicadorEventoService serviceEvento = new PublicadorEventoService();
        PublicadorEvento portEvento = serviceEvento.getPublicadorEventoPort();
     	
        PublicadorUsuarioService serviceUsuario = new PublicadorUsuarioService();
        PublicadorUsuario portUsuario = serviceUsuario.getPublicadorUsuarioPort();
         
    	switch(path) {
        case "/detallePatrocinio":
            try {
                String nombreInstitucion = request.getParameter("nombreInstitucion");
                DtPatrocinio patrocinio = portEvento.obtenerPatrocinio(nombreEdicion, nombreInstitucion);
                request.setAttribute("patrocinio", patrocinio);
                request.getRequestDispatcher("/WEB-INF/pages//detallePatrocinio.jsp").forward(request, response);
            } catch (Exception e) {
                
                request.setAttribute("error", "Error al obtener detalles del patrocinio: " + e.getMessage());
                request.getRequestDispatcher("/WEB-INF/pages//error.jsp").forward(request, response);
            }
            break;
            
        case "/altaInstitucion":
        	
        	request.setAttribute("mensaje", null);
        	request.setAttribute("error", null);
        	request.getRequestDispatcher("/WEB-INF/pages/altaInstitucion.jsp").forward(request, response);
             break;
            
        case "/altaPatrocinio":
            request.getSession().setAttribute("nombreEdicion", nombreEdicion);
            try {
            	WrapperHashSet instituciones = portUsuario.listarInstituciones();
                request.setAttribute("instituciones", instituciones);
               
                try {
                    if (nombreEdicion != null && !nombreEdicion.isEmpty()) {
                    	WrapperHashSet tipos = portEvento.listarTiposDeRegistro(nombreEdicion);
                        request.setAttribute("tiposRegistro", tipos);
                    }
                } catch (Exception e) {
                   
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
          
            Object sessFecha = request.getSession().getAttribute("fecha");
            if (sessFecha != null) {
                request.setAttribute("fecha", sessFecha.toString());
            } else {
                request.setAttribute("fecha", null);
            }
            request.getRequestDispatcher("/WEB-INF/pages//altaPatrocinio.jsp").forward(request, response);
            break;
    }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        PublicadorEventoService serviceEvento = new PublicadorEventoService();
        PublicadorEvento portEvento = serviceEvento.getPublicadorEventoPort();
     	
        PublicadorUsuarioService serviceUsuario = new PublicadorUsuarioService();
        PublicadorUsuario portUsuario = serviceUsuario.getPublicadorUsuarioPort();
    

        switch(path) {

            case "/altaPatrocinio": {
                String nombreEdi = request.getParameter("edicion");
           
                if (nombreEdi == null || nombreEdi.isEmpty()) {
                    Object s = request.getSession().getAttribute("nombreEdicion");
                    if (s != null) nombreEdi = s.toString();
                }
              
                String institucion = request.getParameter("institucion");
                webservices.NivelPatrocinio nivel = null;
                try { nivel = webservices.NivelPatrocinio.valueOf(request.getParameter("nivelPatrocinio")); } catch (Exception ex) {  }
                double aporteEconomico = 0.0;
                try { aporteEconomico = Double.parseDouble(request.getParameter("aporte")); } catch (Exception ex) { aporteEconomico = 0.0; }
                String tipoRegistroGratis = request.getParameter("tipoRegGratis");
                int cantidadGratis = 0;
                try { cantidadGratis = Integer.parseInt(request.getParameter("cantGratis")); } catch (Exception ex) { cantidadGratis = 0; }
                String codigo = request.getParameter("codigo");
             
                portEvento.setFechaSistema((String)request.getSession().getAttribute("fecha"));

                
                request.setAttribute("edicion", nombreEdi);
                request.setAttribute("institucion", institucion);
                request.setAttribute("nivelPatrocinio", request.getParameter("nivelPatrocinio"));
                request.setAttribute("aporte", request.getParameter("aporte"));
                request.setAttribute("tipoRegGratis", tipoRegistroGratis);
                request.setAttribute("cantGratis", request.getParameter("cantGratis"));
                request.setAttribute("codigo", codigo);

                HttpSession session = request.getSession();
                DataUsuario user = (DataUsuario) session.getAttribute("usuario");
                if (user == null || user.getTipo() != TipoUsuario.ORGANIZADOR) {
                    request.setAttribute("error", "Debe iniciar sesión como organizador para registrar un patrocinio.");
                    request.getRequestDispatcher("/WEB-INF/pages/altaPatrocinio.jsp").forward(request, response);
                    break;
                }

                try {
                    WrapperHashSet tiposDisponibles = null;
                    try {
                    	if (nombreEdi != null && !nombreEdi.isEmpty()) tiposDisponibles = portEvento.listarTiposDeRegistro(nombreEdi);
                    } catch (Exception ignore) { }
                    if (tiposDisponibles == null || tiposDisponibles == null) {
                        request.setAttribute("error", "No existen tipos de registro para la edición seleccionada. No es posible registrar un patrocinio hasta que exista al menos un tipo de registro para la edición.");
                        try { request.setAttribute("instituciones", portUsuario.listarInstituciones()); } catch (Exception e) { }
                        request.setAttribute("tiposRegistro", tiposDisponibles);
                        request.getRequestDispatcher("/WEB-INF/pages/altaPatrocinio.jsp").forward(request, response);
                        break;
                    }

                    String aporteParam = request.getParameter("aporte");
                    String tipoRegGratisParam = request.getParameter("tipoRegGratis");
                    String cantGratisParam = request.getParameter("cantGratis");
                    String codigoParam = request.getParameter("codigo");
                    if (institucion == null || institucion.trim().isEmpty() || nivel == null || aporteParam == null || aporteParam.trim().isEmpty() || tipoRegGratisParam == null || tipoRegGratisParam.trim().isEmpty() || cantGratisParam == null || cantGratisParam.trim().isEmpty() || codigoParam == null || codigoParam.trim().isEmpty()) {
                        request.setAttribute("error", "Todos los campos del formulario son obligatorios.");
                        try { request.setAttribute("instituciones", portUsuario.listarInstituciones()); } catch (Exception e) { }
                        try { request.setAttribute("tiposRegistro", portEvento.listarTiposDeRegistro(nombreEdi)); } catch (Exception e) { }
                        request.getRequestDispatcher("/WEB-INF/pages/altaPatrocinio.jsp").forward(request, response);
                        break;
                    }

                    try {
                        if (tipoRegGratisParam != null && !tipoRegGratisParam.isEmpty()) {
                            DtTipoRegistro detalleTipo = portEvento.verDetalleTRegistro(nombreEdi, tipoRegGratisParam);
                            if (detalleTipo != null) {
                                int cupo = detalleTipo.getCupo();
                                if (cantidadGratis > cupo) {
                                    request.setAttribute("error", "La cantidad de registros gratuitos (" + cantidadGratis + ") no puede ser mayor al cupo del tipo de registro escogido (" + cupo + ").");
                                    try { request.setAttribute("instituciones", portUsuario.listarInstituciones()); } catch (Exception e) { }
                                    try { request.setAttribute("tiposRegistro", portEvento.listarTiposDeRegistro(nombreEdi)); } catch (Exception e) { }
                                    request.getRequestDispatcher("/WEB-INF/pages/altaPatrocinio.jsp").forward(request, response);
                                    break;
                                }
                            }
                        }
                    } catch (Exception ignore) { }

                    boolean existe = false;
                    try {
                       
                        System.out.println("DEBUG: comprobando existencia de patrocinio por obtenerPatrocinio(edicion, institucion)");
                        System.out.println("DEBUG institucion recibida='" + institucion + "', nombreEdi='" + nombreEdi + "'");
                        if (institucion != null && !institucion.trim().isEmpty()) {
                            DtPatrocinio dtp = null;
                            try {
                                dtp = portEvento.obtenerPatrocinio(nombreEdi, institucion);
                            } catch (Exception e) {
                                System.out.println("DEBUG obtenerPatrocinio lanzó excepción: " + e.getMessage());
                            }
                            if (dtp != null) {
                                System.out.println("DEBUG: obtenerPatrocinio devolvió un patrocinio (no null)");
                                existe = true;
                            } else {
                                System.out.println("DEBUG: obtenerPatrocinio devolvió null => no existe patrocinio");
                            }
                        } else {
                            System.out.println("DEBUG: institucion vacia, no chequeamos existencia");
                        }
                    } catch (Exception ex) {
                        System.out.println("DEBUG listarPatrocinios: excepción al listar => " + ex.getMessage());
                    }

                    
                    boolean excedePorcentaje = false;
                    float costoTipo = 0.0f;
                    try {
                        if (tipoRegistroGratis != null && !tipoRegistroGratis.isEmpty()) {
                            DtTipoRegistro dttr = portEvento.verDetalleTRegistro(nombreEdi, tipoRegistroGratis);
                            if (dttr != null) costoTipo = dttr.getCosto();
                        }
                    } catch (Exception ignore) { }

                    double costoTotalGratis = costoTipo * (double) cantidadGratis;
                    if (aporteEconomico <= 0.0 && cantidadGratis > 0) {
                        excedePorcentaje = true;
                    } else if (aporteEconomico > 0.0) {
                        if (costoTotalGratis > 0.2 * aporteEconomico) excedePorcentaje = true;
                    }

                    if (existe) {
                        request.setAttribute("error", "Ya existe un patrocinio de la institución '" + institucion + "' para la edición '" + nombreEdi + "'. Puede editarlo o cancelar.");
                        request.setAttribute("permitirEditar", true);

                        try { request.setAttribute("instituciones", portUsuario.listarInstituciones()); } catch (Exception e) { }
                        try { request.setAttribute("tiposRegistro", portEvento.listarTiposDeRegistro(nombreEdi)); } catch (Exception e) { }
                        request.getRequestDispatcher("/WEB-INF/pages/altaPatrocinio.jsp").forward(request, response);
                        break;
                    } else if (excedePorcentaje) {
                        request.setAttribute("error", "El costo de los registros gratuitos ("+costoTotalGratis+") supera el 20% del aporte económico.");
                        request.setAttribute("permitirEditar", true);
                        try { request.setAttribute("instituciones", portUsuario.listarInstituciones()); } catch (Exception e) { }
                        try { request.setAttribute("tiposRegistro", portEvento.listarTiposDeRegistro(nombreEdi)); } catch (Exception e) { }
                        request.getRequestDispatcher("/WEB-INF/pages/altaPatrocinio.jsp").forward(request, response);
                        break;
                    } else {
                     
                        try {
                        	portEvento.altaPatrocinio(nombreEdi, institucion, nivel, aporteEconomico, tipoRegistroGratis, cantidadGratis, codigo);
                           
                            try {
                                String target = request.getContextPath() + "/altaPatrocinio";
                                boolean hasParam = false;
                                if (nombreEdi != null && !nombreEdi.isEmpty()) {
                                    target += "?nombreEdicion=" + URLEncoder.encode(nombreEdi, "UTF-8");
                                    hasParam = true;
                                }
                                String mensajeExito = "Patrocinio registrado con éxito.";
                                if (hasParam) {
                                    target += "&mensaje=" + URLEncoder.encode(mensajeExito, "UTF-8");
                                } else {
                                    target += "?mensaje=" + URLEncoder.encode(mensajeExito, "UTF-8");
                                }
                                response.sendRedirect(target);
                                return;
                            } catch (UnsupportedEncodingException uee) {
                                String target = request.getContextPath() + "/altaPatrocinio";
                                if (nombreEdi != null && !nombreEdi.isEmpty()) {
                                    target += "?nombreEdicion=" + nombreEdi;
                                }
                                if (target.contains("?")) target += "&mensaje=Patrocinio registrado con éxito.";
                                else target += "?mensaje=Patrocinio registrado con éxito.";
                                response.sendRedirect(target);
                                return;
                            }
                        } catch (Exception e) {
                            request.setAttribute("error", "Error al registrar el patrocinio: " + e.getMessage());
                            try { request.setAttribute("instituciones", portUsuario.listarInstituciones()); } catch (Exception ex) { }
                            request.getRequestDispatcher("/WEB-INF/pages/altaPatrocinio.jsp").forward(request, response);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("error", "Error procesando el alta de patrocinio: " + e.getMessage());
                    request.getRequestDispatcher("/WEB-INF/pages/altaPatrocinio.jsp").forward(request, response);
                }
                break;
            }

            case "/altaInstitucion": {
               
                String nombreInsti = request.getParameter("nombre");
                String desc = request.getParameter("descripcion");
                String web = request.getParameter("url");
                Part imagenInsti = null;
                try { imagenInsti = request.getPart("imagen"); } catch (Exception e) {  }

                try {
                    portUsuario.altaInstitucion(nombreInsti, desc, web);
                    
                    ManejadorArchivos.guardarArchivo(imagenInsti, nombreInsti, "instituciones", getServletContext());
                    request.setAttribute("mensaje", "Institución creada exitosamente.");
                    request.setAttribute("error", null);
                    request.setAttribute("nombre", nombreInsti);
                    request.setAttribute("descripcion", desc);
                    request.setAttribute("url", web);
                    request.getRequestDispatcher("/WEB-INF/pages/altaInstitucion.jsp").forward(request, response);
                } catch (webservices.NombreInstiExistente_Exception e) {
                    request.setAttribute("error", "El nombre de la institucion ya existe");
                 
                    request.setAttribute("nombre", nombreInsti);
                    request.setAttribute("descripcion", desc);
                    request.setAttribute("url", web);
                    request.getRequestDispatcher("/WEB-INF/pages/altaInstitucion.jsp").forward(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("error", "Error al crear la institución: " + e.getMessage());
                    request.setAttribute("nombre", nombreInsti);
                    request.setAttribute("descripcion", desc);
                    request.setAttribute("url", web);
                    request.getRequestDispatcher("/WEB-INF/pages/altaInstitucion.jsp").forward(request, response);
                }
                break;
            }

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }
}
