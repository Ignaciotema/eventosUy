<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="webservices.DataUsuario" %>
<%@ page import="webservices.DtOrganizador" %>
<%@ page import="webservices.DtAsistente" %>

<!doctype html>

<html lang="es">
<head>
<meta charset="utf-8" />
<meta http-equiv="x-ua-compatible" content="ie=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Detalle de Usuario - Eventos.uy</title>



<link
	href="https://cdn.jsdelivr.net/npm/boxicons@latest/css/boxicons.min.css"
	rel="stylesheet">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Titillium+Web:wght@300;400;600&display=swap"
	rel="stylesheet">

<link rel="stylesheet" href="assets/css/ConsultaEvento.css">
<link rel="stylesheet" href="assets/css/styles.css">


<link rel="stylesheet"
	href="assets/css/RolVisitante_listarUsuarios.css">
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css2?family=Inter&display=swap" />
<link rel="icon" type="image/x-icon" href="assets/icons/Logo.png">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">

<!-- Bootstrap Icons CDN -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css"
	rel="stylesheet">

<style>
:root {
	--bg: #f4f6f8;
	--surface: #ffffff;
	--text: #1f2937;
	--muted: #6b7280;
	--border: #e6e8eb;
	--accent: #6d5dfc;
	--accent-weak: #edeaff;
	--radius: 8px;
	--shadow: 0 6px 18px rgba(15, 23, 42, .06);
}

body {
	background: var(--bg);
	color: var(--text);
	margin: 0;
	padding-left: var(--sidebar-w);
	transition: padding-left .2s ease;
}

/* main container spacing */
main.contUser {
	padding: 18px;
}

.container-xl {
	max-width: 1200px;
}

/* card tweaks */
.card {
	border: 1px solid var(--border);
	border-radius: var(--radius);
	box-shadow: var(--shadow);
}

/* avatar */
.avatar {
	width: 64px;
	height: 64px;
	border-radius: 50%;
	border: 2px solid var(--accent-weak);
	object-fit: cover;
	background: #fff;
}

/* actions grid */
.action-card {
	display: flex;
	gap: 12px;
	align-items: center;
	background: #fcfcff;
	border: 1px solid var(--border);
	border-radius: 10px;
	padding: 12px;
	text-decoration: none;
	color: inherit;
	transition: background .12s ease, transform .06s ease;
}

.action-card:hover {
	background: #f6f7ff;
	border-color: #dfe3ff;
	transform: translateY(-1px);
}

/* collapsed styles */
body.with-collapsed {
	padding-left: var(--sidebar-w-collapsed);
}

.sidebar.collapsed .nav-text {
	display: none;
}

.sidebar.collapsed .nav-link {
	justify-content: center;
}

.sidebar.collapsed .nav-icon {
	margin-right: 0 !important;
}

/* responsive */
@media ( max-width : 767.98px) {
	body {
		padding-left: var(--sidebar-w-collapsed);
	}
	.sidebar {
		width: var(--sidebar-w-collapsed);
	}
}

.action-card {
	min-width: 0;
}
/*.actions-grid .col-12 { display:flex; }*/
.action-card {
	width: 100%;
}

.row {
	display: flex !important;
	flex-wrap: nowrap !important; /* evita que salten de línea */
}

.row>[class*="col-"] {
	flex: 0 0 auto; /* que respeten su ancho */
}

.contenedor-edicion { gap: 6px; align-items: center; }
.nombreEdicion { margin-left: 4px; }
.contenedor-fotoPerfil { margin-bottom: 8px; }
.contenedor-NickRolUser { margin-top: 4px; }
</style>
</head>
	<jsp:include page="../templates/header.jsp"/>

	<%
	DataUsuario usuario = (DataUsuario) request.getAttribute("usuario"); 
	if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/usuarios?accion=listar");
        return;
    }
	
	Object detalleObj = request.getAttribute("detalleUsuario");
	DtAsistente asis = null;
	DtOrganizador org = null;
	if (detalleObj instanceof DtOrganizador) {
		org = (DtOrganizador) detalleObj;
	} else if (detalleObj instanceof DtAsistente) {
		asis = (DtAsistente) detalleObj;
	}
	
	@SuppressWarnings("unchecked")
	Set<String> ediciones = (Set<String>) request.getAttribute("ediciones");
	@SuppressWarnings("unchecked")
	Map<String, String> edicionesMap = (Map<String, String>) request.getAttribute("edicionesMap");
	String imagenUsuario = (String) request.getAttribute("imagenUsuario");
	%>

<body id="body-pd">
	
	<!-- Contenido -->
	<main class="contUser" id="cuenta-user">
		<div class="container-xl ">
			<div class="row">
				<!--pa dejar dos cartas en una sola columna y la otra a su izq-->
				<div class="col-10 mb-3 mb-lg-0">

					<!-- Header perfil -->
					<section class="card mb-3 bg-light ">
						<!-- make card-body a centered flex container so inner row is centered horizontally and vertically -->
                        <div class="card-body align-items-center d-flex justify-content-between gap-4 " style="min-height:180px;">
							<div class="d-flex align-items-center justify-content-between gap-4 w-100">
                                <div class="d-flex align-items-center gap-3 flex-wrap col-4" id="fotoCarnet">
                                    <div class="contenedor-fotoPerfil mb-0">
                                        <img class="foto-usuario avatar" src="<%= imagenUsuario %>"
                                            alt="<%= usuario.getNickname() %>" style="height: 127px; width:127px;">
                                    </div>
                                    <div class="contenedor-NickRolUser text-start">
                                        <div class="nickname">
                                            <b><%= usuario.getNickname() %></b>
                                        </div>
                                        <div class="rol"><%= usuario.getTipo() %></div>
                                    </div>
                                </div>
                                <div class="d-flex flex-column contenedor-datosUsuario text-start col-4" id="datos">
                                     <div class="datosUsuario">
                                        <div class="nombre">
                                            <u>Nombre:</u> <%= usuario.getNombre() %>
                                        </div>
                                        <div class="email">
                                            <u>Email:</u> <%= usuario.getEmail() %>
                                        </div>
                                        
                                        <!-- Contador de seguidores y seguidos -->
                                        <div class="seguidores-info mt-2">
                                            <div class="seguidores">
                                                <u>Seguidores:</u> <span id="cantidadSeguidores"><%= request.getAttribute("cantidadSeguidores") != null ? request.getAttribute("cantidadSeguidores") : 0 %></span>
                                            </div>
                                            <div class="seguidos">
                                                <u>Siguiendo:</u> <span id="cantidadSeguidos"><%= request.getAttribute("cantidadSeguidos") != null ? request.getAttribute("cantidadSeguidos") : 0 %></span>
                                            </div>
                                        </div>
                                        
                                        <% if (org != null) { %>
                                            <div class="fechaNacimiento">
                                                <u>Descripción:</u> <%= org.getDescripcion() %>
                                            </div>
                                            <div class="institucion">
                                                <u>Web:</u> <a href="<%= org.getWeb() %>" target="_blank"><%= org.getWeb() %></a>
                                            </div>
                                        <% } else if (asis != null) {  %>
                                            <div class="fechaNacimiento">
                                                <u>Fecha de Nacimiento:</u> <%= asis.getFechaNacimiento() %>
                                            </div>
                                            <div class="apellido">
                                                <u>Apellido:</u> <%= asis.getApellido() %>
                                            </div>
                                        
                                            <% if (asis.getInstitucion() != null && !asis.getInstitucion().isBlank()) { %>
                                                <div class="institucion">
                                                    <u>Institución:</u> <%= asis.getInstitucion() %>
                                                </div>
                                            <% } %>
                                        <% } %>
                                    </div>
                                </div>
                                
                                <!-- Botón de seguir/dejar de seguir -->
                                <% 
                                String usuarioLogueado = (String) request.getAttribute("usuarioLogueado");
                                Boolean yaSigue = (Boolean) request.getAttribute("yaSigue");
                                if (usuarioLogueado != null && !usuarioLogueado.equals(usuario.getNickname())) { 
                                %>
                                <div class="d-flex flex-column justify-content-center col-3" id="accionesSeguimiento">
                                    <% if (yaSigue != null && yaSigue) { %>
                                        <!-- Botón para dejar de seguir (cuando ya sigue) -->
                                        <form method="post" action="<%= request.getContextPath() %>/dejarDeSeguir">
                                            <input type="hidden" name="usuario" value="<%= usuario.getNickname() %>">
                                            <button type="submit" class="btn btn-outline-secondary w-100">
                                                <i class="bi bi-person-dash"></i> Dejar de seguir
                                            </button>
                                        </form>
                                    <% } else { %>
                                        <!-- Botón para seguir (cuando no sigue) -->
                                        <form method="post" action="<%= request.getContextPath() %>/seguirUsuario">
                                            <input type="hidden" name="usuario" value="<%= usuario.getNickname() %>">
                                            <button type="submit" class="btn btn-primary w-100">
                                                <i class="bi bi-person-plus"></i> Seguir
                                            </button>
                                        </form>
                                    <% } %>
                                    
                                    <!-- Mostrar mensajes de seguimiento -->
                                    <% 
                                    String mensajeSeguimiento = (String) request.getAttribute("mensajeSeguimiento");
                                    String errorSeguimiento = (String) request.getAttribute("errorSeguimiento");
                                    if (mensajeSeguimiento != null) { 
                                    %>
                                        <div class="alert alert-success mt-2" role="alert">
                                            <%= mensajeSeguimiento %>
                                        </div>
                                    <% } %>
                                    <% if (errorSeguimiento != null) { %>
                                        <div class="alert alert-danger mt-2" role="alert">
                                            <%= errorSeguimiento %>
                                        </div>
                                    <% } %>
                                </div>
                                <% } %>
                            </div>
                        </div>
                    </section>

				</div>
				
			</div>
			
			
			<% if (org != null) { %>
			<div class="container-xl mt-3">
				<section class="card p-3">
					<h5>Ediciones organizadas:</h5>
					<div class="d-flex flex-column mt-2">
						<% if (ediciones != null && !ediciones.isEmpty()) {
                            for (String ed : ediciones) {
                                String imgEd = (edicionesMap != null) ? edicionesMap.get(ed) : null;
                        %>
                                <div class="contenedor-edicion d-flex align-items-center gap-2 mb-2">
                                    <a href="<%= request.getContextPath() %>/detalleEdicion?nombre=<%= URLEncoder.encode(ed, "UTF-8") %>" style="display:inline-block;width:64px;height:64px;">
                                        <img class="foto-edicion" src="<%=imgEd%>" alt="edicion" style="width:64px;height:64px;object-fit:cover;display:inline-block;vertical-align:middle;">
                                    </a>
                                    <div class="nombreEdicion" style="margin-left:6px;">
                                        <a href="<%= request.getContextPath() %>/detalleEdicion?nombre=<%= URLEncoder.encode(ed, "UTF-8") %>" class="text-decoration-none"><b><%= ed %></b></a>
                                    </div>
                                  
                                </div>
                            <% }
                        } else { %>
							<div class="text-muted">No tiene ediciones organizadas.</div>
						<% } %>
					</div>
				</section>
			</div>
			<% } %>
			
		</main>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

	
</body>
</html>