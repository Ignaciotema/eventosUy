<%@page import="java.util.HashSet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Set" %>
<%@ page import="webservices.*" %>
<%@ page import="java.util.GregorianCalendar" %>
<%@ page import="javax.xml.datatype.XMLGregorianCalendar" %>
<%@ page import="java.time.LocalDate" %>

<%!
	// Función Java para convertir URL de YouTube a formato embed
	private String convertToEmbedUrl(String youtubeUrl) {
		if (youtubeUrl == null || youtubeUrl.trim().isEmpty()) {
			return null;
		}
		
		try {
			// Patrones comunes de URL de YouTube
			String videoId = null;
			
			// Para URLs como: https://www.youtube.com/watch?v=VIDEO_ID
			if (youtubeUrl.contains("youtube.com/watch?v=")) {
				int startIndex = youtubeUrl.indexOf("v=") + 2;
				int endIndex = youtubeUrl.indexOf("&", startIndex);
				if (endIndex == -1) {
					endIndex = youtubeUrl.length();
				}
				videoId = youtubeUrl.substring(startIndex, endIndex);
			}
			// Para URLs como: https://youtu.be/VIDEO_ID
			else if (youtubeUrl.contains("youtu.be/")) {
				int startIndex = youtubeUrl.lastIndexOf("/") + 1;
				int endIndex = youtubeUrl.indexOf("?", startIndex);
				if (endIndex == -1) {
					endIndex = youtubeUrl.length();
				}
				videoId = youtubeUrl.substring(startIndex, endIndex);
			}
			// Para URLs que ya están en formato embed
			else if (youtubeUrl.contains("youtube.com/embed/")) {
				return youtubeUrl;
			}
			
			// Si encontramos el video ID, construir URL embed
			if (videoId != null && !videoId.isEmpty()) {
				return "https://www.youtube.com/embed/" + videoId;
			}
		} catch (java.lang.Exception e) {
			// En caso de error, no mostrar video
			return null;
		}
		
		return null;
	}
%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Consulta Edición</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css"
	rel="stylesheet">
<link rel="stylesheet" href="assets/css/ConsultaEvento.css">
<link rel="stylesheet" href="assets/css/styles.css">
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css2?family=Inter&display=swap" />
<link rel="icon" type="image/x-icon" href="assets/icons/Logo.png">
<link
	href="https://cdn.jsdelivr.net/npm/boxicons@latest/css/boxicons.min.css"
	rel="stylesheet">

</head>

<body>
	<% DataUsuario user = (DataUsuario) session.getAttribute("usuario");  %>
	<jsp:include page="../templates/header.jsp"></jsp:include>

	<div class="container px-4 ">
	
	</div>
	<div class="container px-4 mt-3">
		<div class="row pb-4">
			<!-- Imagen de la edición y categorias-->
			<% DtDetalleEdicion edi = (DtDetalleEdicion) request.getAttribute("edicion");%>
			<div class="col-md-3 col-lg-3 col-xl-3 ">
				<img src="<%= request.getAttribute("imagenEdicion") %>"
					alt="<%= request.getAttribute("nombre") %>" class="img-fluid rounded"
					style="width: 100%; height: 180px; object-fit: cover; aspect-ratio: 1/1;">
					<jsp:include page="../templates/categorias-sidebar.jsp"></jsp:include>
			</div>
			<!-- Información principal de la edición -->
			<div class="col-md-9 col-lg-9 col-xl-9 mt-3 mt-md-0 info-evento px-5">
				<div class="d-flex align-items-center gap-3 mb-2">
					<h2 class="fw-bold mb-0"> <%= edi.getNombre() %></h2>
					<%
					// Mostrar tag de estado solo si es organizador de la edición
					if (user != null && user.getTipo() == TipoUsuario.ORGANIZADOR) { 
						boolean esOrg = (boolean) request.getAttribute("esOrganizador");
						if (esOrg) {
							String estadoClass = "";
							String estadoTexto = "";
							EstadoEdicion estado = edi.getEstado();
							
							switch(estado) {
								case CONFIRMADA:
									estadoClass = "badge bg-success";
									estadoTexto = "Confirmada";
									break;
								case INGRESADA:
									estadoClass = "badge bg-warning text-dark";
									estadoTexto = "Pendiente";
									break;
								case RECHAZADA:
									estadoClass = "badge bg-danger";
									estadoTexto = "Rechazada";
									break;
							}
					%>
					<span class="<%= estadoClass %>"><%= estadoTexto %></span>
					<% }} %>
				</div>
				<div class="mb-2 d-flex align-items-center">
					<strong class="me-2">Organizador:</strong> <a
						href="detalleUsuario?usuarios=<%= edi.getOrganizador() %>"
						class="d-flex align-items-center text-decoration-none"> <img
					<% 
					String imagenOrganizador = (String) request.getAttribute("imagenOrganizador");
					String nombre = (String) request.getAttribute("nombre");
					%>
					src="<%= imagenOrganizador %>" alt="<%= edi.getOrganizador() %>"
					class="rounded-circle me-2"
					style="width: 32px; height: 32px; object-fit: cover; border: 1px solid rgba(0, 0, 0, .06);">
					<span class="fw-bold text-dark"><%= edi.getOrganizador() %></span>
				</a>
				</div>
				<div class="mb-2 d-flex align-items-center">
					<% 
					String imagenEvento = (String) request.getAttribute("imagenEvento");
					String nombreEvento = (String) request.getAttribute("nombreEvento");
					%>
					<strong class="me-2">Evento:</strong> <a
						href="detalleEvento?nombre=<%= nombreEvento %>"
						class="d-flex align-items-center text-decoration-none"> <img
					src="<%= imagenEvento %>" alt="<%= nombreEvento %>"
					class="rounded-circle me-2"
					style="width: 32px; height: 32px; object-fit: cover; border: 1px solid rgba(0, 0, 0, .06);">
					<span class="fw-bold text-dark"><%= nombreEvento %></span>
				</a>
				</div>
				<div class="mb-2 d-flex align-items-center">
					<strong class="me-2">Sigla:</strong> <span
						class="fw-bold text-dark"><%= edi.getSigla() %></span>
				</div>
				<div class="mb-2">
					<strong>Fecha inicio:</strong> <%= edi.getFechaInicio() %>
				</div>
				<div class="mb-2">
					<strong>Fecha fin:</strong> <%= edi.getFechaFin() %>
				</div>
				<div class="mb-2">
					<strong>Fecha alta:</strong> <%= edi.getFechaAlta() %>
				</div>
				<div class="mb-2">
					<strong>País:</strong> <%= edi.getPais() %>
				</div>
				<div class="mb-2">
					<strong>Ciudad:</strong> <%= edi.getCiudad() %>
				</div>
				<%
				boolean esOrganizador = false;
				if (user != null && user.getTipo() == TipoUsuario.ORGANIZADOR) { 
					esOrganizador = (boolean) request.getAttribute("esOrganizador");
					if ((boolean) request.getAttribute("esOrganizador") == true 
						&& edi.getEstado() != EstadoEdicion.INGRESADA 
						&& edi.getEstado() != EstadoEdicion.RECHAZADA) {
				%>
				<div class="mt-4">
					<a href="listar-registros?edicion=<%= edi.getNombre() %>" style="text-decoration: none;">
						<button class="button2 rounded-3 p-3">
							<div class="header-button">Ver Registros</div>
						</button>
					</a>
				</div> <% }} else if (user != null) {
					boolean usuarioRegistrado = (boolean) request.getAttribute("usuarioRegistrado");
					if (!usuarioRegistrado) {
						LocalDate fechaActual = (LocalDate) session.getAttribute("fecha");
						XMLGregorianCalendar fechaFin = edi.getFechaFin();
						if (fechaActual.isAfter(fechaFin.toGregorianCalendar().toZonedDateTime().toLocalDate()) ) {
					%> 
					<div class="alert alert-secondary text-center mb-0" role="alert">
						Esta edición ya finalizó.</div>
					<% } else { %>
				<div class="mt-4">
					<a href="altaRegistro?edicion=<%= edi.getNombre() %>" style="text-decoration: none;">
						<button class="btn btn-success rounded-3 p-3">
							<div class="header-button">Registrarse a la Edicion</div>
						</button>
					</a>
				</div> <% }} else { %>
				<div class="mt-4">
					<a href="ver-registro?edicion=<%= edi.getNombre()  %>&usuario=<%= user.getNickname() %>" style="text-decoration: none;">
						<button class="button2 rounded-3 p-3">
							<div class="header-button">Ver detalle del Registro</div>
						</button>
					</a>
				</div> <% }} %>

				<!-- Tipos de registros -->
				<div class="mt-4">
				
					<div class="d-flex align-items-baseline gap-2">
						<h5 class="py-2">Tipos de Registro</h5>
						<% 
						if (user != null && user.getTipo() == TipoUsuario.ORGANIZADOR && esOrganizador) {
							LocalDate fechaActual2 = (LocalDate) session.getAttribute("fecha");
							XMLGregorianCalendar fechaFin2 = edi.getFechaFin();
							if (fechaActual2.isBefore(fechaFin2.toGregorianCalendar().toZonedDateTime().toLocalDate()) 
								&& edi.getEstado() != EstadoEdicion.RECHAZADA) {
						%>
						<a href="/tarea2/alta-tipo-registro?edicion=<%= edi.getNombre() %>" class="btn btn-success btn-sm"
							title="Agregar tipo de registro"> <i class="bi bi-plus-lg"></i>
						</a> <% }} %>
					</div>
					<div class="accordion" id="accordionTiposRegistro">
					<% 
					@SuppressWarnings("unchecked")
					Set<DtTipoRegistro> trSet = (Set<DtTipoRegistro>) request.getAttribute("tiposRegistro");
					if (trSet.isEmpty()) {
						%> 	<div class="alert alert-secondary text-center mb-0" role="alert">
						Aún no existen tipos de registro para esta edición.</div>
					<%} else { 
						int i = 0;
						for (DtTipoRegistro tipoReg : trSet) {
					%>
						<div class="accordion-item">
							<h2 class="accordion-header" id="heading<%= i %>">
								<button class="accordion-button collapsed" type="button"
									data-bs-toggle="collapse" data-bs-target="#collapse<%= i %>"
									aria-expanded="false" aria-controls="collapse<%= i %>">
									<%= tipoReg.getNombre() %></button>
							</h2>
							<div id="collapse<%= i %>" class="accordion-collapse collapse"
								aria-labelledby="heading<%= i %>"
								data-bs-parent="#accordionTiposRegistro">
								<div class="accordion-body registro-info ">
									<p>
										<strong>Costo:</strong> $<%= tipoReg.getCosto()%>
									</p>
									<p>
										<strong>Cupo:</strong> <%= tipoReg.getCupo() %>
									</p>
									<p>
										<strong>Descripción:</strong> <%= tipoReg.getDescripcion() %>
									</p>
								</div>
							</div>
						</div> <% i++;}} %>
					</div>
				</div>
				<!-- Patrocinios -->
				<div class="mt-4">
					<div class="d-flex align-items-baseline gap-2">
						<h5 class="py-2">Patrocinadores</h5>
						<% 
						if (user != null && user.getTipo() == TipoUsuario.ORGANIZADOR && esOrganizador) {
							LocalDate fechaActual3 = (LocalDate) session.getAttribute("fecha");
							XMLGregorianCalendar fechaFin3 = edi.getFechaFin();
							if (fechaActual3.isBefore(fechaFin3.toGregorianCalendar().toZonedDateTime().toLocalDate())
								&& edi.getEstado() != EstadoEdicion.RECHAZADA) {
						%>
						<a href="altaPatrocinio?nombreEdicion=<%= edi.getNombre() %>" class="btn btn-success btn-sm"
							title="Agregar patrocinio"> <i class="bi bi-plus-lg"></i>
						</a> <% }} %>
						
					</div>
					<div class="accordion" id="accordionPatrocinadores">
					<%
					@SuppressWarnings("unchecked")
					Set<DtPatrocinio> patSet = (Set<DtPatrocinio>) request.getAttribute("patrocinios");
					if (patSet.isEmpty()) {
					%> <div class="alert alert-secondary text-center mb-0" role="alert">
						Aún no existen patrocinios para esta edición.</div>
					<%} else { 
						for (DtPatrocinio patr : patSet) {
					%>
						<div class="accordion-item">
							<h2 class="accordion-header">
								<button class="accordion-button collapsed" type="button"
									data-bs-toggle="collapse" data-bs-target="#collapse<%= patr.getCodigo() %>"
									aria-expanded="false" aria-controls="collapse<%= patr.getCodigo() %>">
									<%= patr.getInstitucion() %></button>
							</h2>
							<div id="collapse<%= patr.getCodigo() %>" class="accordion-collapse collapse"
								aria-labelledby="headingPatro"
								data-bs-parent="#accordionPatrocinadores">
								<div class="accordion-body registro-info">
									<div class="d-flex align-items-center mb-2">
										<span class="mb-0"
											style="font-weight: 600; font-size: 1.2rem;"><%= patr.getInstitucion() %></span>
									</div>
									<p>
										<strong>Nivel:</strong> <%= patr.getNivelPatrocinio() %>
									</p>
									<p>
										<strong>Tipo de Registro :</strong> <%= patr.getTipoRegistroGratis() %>
									</p>
									<p>
										<strong>Aporte:</strong> $<%= patr.getMonto() %>
									</p>
									<p>
										<strong>Fecha:</strong> <%= patr.getFecha() %>
									</p>
									<p>
										<strong>Registros gratis:</strong> <%= patr.getCantRegsGratis() %>
									</p>
									<p>
										<strong>Código:</strong><span class="text-success">
											<%= patr.getCodigo() %></span>
									</p>
								</div>
							</div>
						</div> <% }} %>
					</div>
				</div>
				<!-- Sección de video de YouTube -->
				<%
				String videoUrl = edi.getVideourl();
				if (videoUrl != null && !videoUrl.trim().isEmpty()) {
					// Convertir URL de YouTube a formato embed
					String embedUrl = convertToEmbedUrl(videoUrl.trim());
					if (embedUrl != null) {
				%>
					<div class="mt-4">
						<h5 class="mb-3"><i class="bi bi-play-circle"></i> Video de la Edición</h5>
						<div class="ratio ratio-16x9" style="max-width: 600px;">
							<iframe src="<%=embedUrl%>" 
							        title="Video de YouTube para <%=edi.getNombre()%>"
							        frameborder="0" 
							        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" 
							        referrerpolicy="strict-origin-when-cross-origin"
							        allowfullscreen>
							</iframe>
						</div>
					</div>
				<%
					}
				}
				%>

			</div>
		</div>
	</div>
	<script
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>