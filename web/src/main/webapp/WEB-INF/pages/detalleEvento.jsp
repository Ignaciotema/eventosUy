<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="webservices.*" %>


<%
    DtDetalleEvento evento = (DtDetalleEvento) request.getAttribute("evento");
    String imagenEvento = (String) request.getAttribute("imagenEvento");
    @SuppressWarnings("unchecked")
    Set<Map<String, Object>> ediciones = (Set<Map<String, Object>>) request.getAttribute("ediciones");
    
    // Obtener información del usuario para determinar si es organizador
    DataUsuario usuario = (DataUsuario) request.getSession().getAttribute("usuario");
    boolean esOrganizador = usuario != null && usuario.getTipo() == TipoUsuario.ORGANIZADOR;
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
%>

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
<title>Consulta Evento - Eventos.uy</title>
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css2?family=Inter&display=swap" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/styles.css">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css"
	rel="stylesheet">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB"
	crossorigin="anonymous">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI"
	crossorigin="anonymous"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/ConsultaEvento.css">
<link rel="icon" type="image/x-icon" href="<%=request.getContextPath()%>/assets/icons/Logo.png">
</head>

<body>
	<jsp:include page="../templates/header.jsp"></jsp:include>

	<div class="container-fluid px-4 mt-4">
		
	</div>

	<div class="container-fluid px-4 mt-3">
		<div class="row">

			<!-- Columna derecha: categorías usando componente separado -->
			<div class="col-md-3 col-lg-2 col-xl-2 mt-3 mt-md-0">
				<jsp:include page="../templates/categorias-sidebar.jsp" />
			</div>

			<!-- Columna izquierda: imagen -->
			<div class="col-md-4 col-lg-3 col-xl-2 px-5">
				<img src="<%=imagenEvento%>" 
				     alt="Imagen del Evento <%=evento != null ? evento.getNombre() : ""%>"
				     class="img-fluid rounded imagen-evento"
				     onerror="this.onerror=null;this.src='<%=request.getContextPath()%>/assets/images/no-photo-or-blank-image-icon-loading-images-or-missing-image-mark-image-not-available-or-image-coming-soon-sign-simple-nature-silhouette-in-frame-isolated-illustration-vector.jpg'">
			</div>

			<!-- Columna central: info del evento -->
			<div class="col-md-5 col-lg-7 col-xl-8 mt-3 mt-sm-3 mt-md-0 info-evento">
				<%
				if (evento != null) {
				%>
					<h1 class="mb-3"><%=evento.getNombre()%> 
					
					
					<% if (esOrganizador && evento.isFinalizado() == false ) { %> <!--  NO SE PQ PASA ESTO WTFF JAJJAJAJA AYUDAAAAA -->
					
						<%
							String urlFinalizar = "";
							if (evento != null) {
							    urlFinalizar = request.getContextPath() + "/finalizarEvento?nombreEvento=" 
							                   + java.net.URLEncoder.encode(evento.getNombre(), "UTF-8");
							}
							%>
						<button type="button" class="btn btn-danger btn-sm d-flex align-items-center ms-2"
						        data-url-finalizar="<%=urlFinalizar%>"
						        data-bs-toggle="modal" data-bs-target="#modalConfirmarFinalizacion"
						        title="Finalizar evento">
						    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-calendar-x-fill" viewBox="0 0 16 16">
						        <path d="M4 .5a.5.5 0 0 0-1 0V1H2a2 2 0 0 0-2 2v1h16V3a2 2 0 0 0-2-2h-1V.5a.5.5 0 0 0-1 0V1H4zM16 14V5H0v9a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2M6.854 8.146 8 9.293l1.146-1.147a.5.5 0 1 1 .708.708L8.707 10l1.147 1.146a.5.5 0 0 1-.708.708L8 10.707l-1.146 1.147a.5.5 0 0 1-.708-.708L7.293 10 6.146 8.854a.5.5 0 1 1 .708-.708"></path>
						    </svg>
						    Finalizar evento
						</button>
						
					<% } else if (evento.isFinalizado()) {%>
						
						<span class="badge bg-danger ms-2">Finalizado</span>
					
					<% } %></h1>
					<p class="mb-2">
						<strong>Sigla:</strong> <%=evento.getSigla()%>
					</p>
					<p class="mb-2">
						<strong>Fecha de creación:</strong> <%= evento.getFechaAlta().toGregorianCalendar().toZonedDateTime().toLocalDate() != null ? evento.getFechaAlta().toGregorianCalendar().toZonedDateTime().toLocalDate().format(formatter) : ""%>
					</p>
					<p class="mb-4">
						<strong>Descripción:</strong> <%=evento.getDescripcion()%>
					</p>
					<p class="mb-2">
						<strong>Categorías:</strong> 
						<%
						if (evento.getCategorias() != null && !evento.getCategorias().isEmpty()) {
							String[] categoriasArray = evento.getCategorias().toArray(new String[0]);
							for (int i = 0; i < categoriasArray.length; i++) {
								out.print(categoriasArray[i]);
								if (i < categoriasArray.length - 1) {
									out.print(", ");
								}
							}
						}
						%>
					</p>
					
					<!-- Sección de video de YouTube -->
					<%
					String videoUrl = evento.getVideourl();
					if (videoUrl != null && !videoUrl.trim().isEmpty()) {
						// Convertir URL de YouTube a formato embed
						String embedUrl = convertToEmbedUrl(videoUrl.trim());
						if (embedUrl != null) {
					%>
						<div class="mb-4">
							<h5 class="mb-3"><i class="bi bi-play-circle"></i> Video del Evento</h5>
							<div class="ratio ratio-16x9" style="max-width: 600px;">
								<iframe src="<%=embedUrl%>" 
								        title="Video de YouTube para <%=evento.getNombre()%>"
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
				<%
				}
				%>
			</div>
		</div>

		<!-- Sección de ediciones del evento -->
		<div class="container-fluid px-4 mt-5">
			<div class="d-flex align-items-center mb-2">
				<h4 class="titulo-ediciones mb-0">Ediciones del Evento</h4>
				<% if (esOrganizador && !evento.isFinalizado()) { %>
					<a href="<%=request.getContextPath()%>/detalleEdicion/altaEdicion?nombreEvento=<%=evento != null ? java.net.URLEncoder.encode(evento.getNombre(), "UTF-8") : ""%>"
					   class="btn btn-success btn-sm ms-2" title="Agregar edición">
						<i class="bi bi-plus-lg"></i>
					</a>
				<% } %>
			</div>
			<div class="row g-4">
				<%
				if (ediciones == null || ediciones.isEmpty()) {
				%>
					<div class="col-12">
						<div class="alert alert-info text-center">
							<i class="bi bi-info-circle"></i>
							No hay ediciones disponibles para este evento.
						</div>
					</div>
				<%
				} else {
					for (Map<String, Object> edicion : ediciones) {
						String nombreEdicion = (String) edicion.get("nombre");
						String ciudad = (String) edicion.get("ciudad");
						String pais = (String) edicion.get("pais");
						java.time.LocalDate fechaInicio = (java.time.LocalDate) edicion.get("fechaInicio");
						java.time.LocalDate fechaFin = (java.time.LocalDate) edicion.get("fechaFin");
						String imagenEdicion = (String) edicion.get("imagenEdicion");
						EstadoEdicion estado = (EstadoEdicion) edicion.get("estado");
						Boolean esOrganizadorDeEstaEdicion = (Boolean) edicion.get("esOrganizadorDeEstaEdicion");
				%>
					<div class="col-md-6">
						<a class="text-decoration-none text-reset"
							href="<%=request.getContextPath()%>/detalleEdicion?nombre=<%=java.net.URLEncoder.encode(nombreEdicion, "UTF-8")%>">
							<div class="carta p-4 
								<% if (esOrganizadorDeEstaEdicion != null && esOrganizadorDeEstaEdicion && estado != null) { %>
									<% if (estado == EstadoEdicion.CONFIRMADA) { %>
										bg-success bg-opacity-10 bg-gradient border-success
									<% } else if (estado == EstadoEdicion.RECHAZADA) { %>
										bg-danger bg-opacity-10 bg-gradient border-danger
									<% } else if (estado == EstadoEdicion.INGRESADA) { %>
										bg-warning bg-opacity-10 bg-gradient border-warning
									<% } %>
								<% } else { %>
									carta-edicion
								<% } %>
							">
								<div class="text-heading">
									<div class="search-text-heading"><%=nombreEdicion%></div>
								</div>
								<div class="d-flex flex-row justify-content-between align-items-start w-100">
									<div class="avatar-block d-flex">
										<div class="avatar me-3">
											<img class="shape-icon rounded" 
											     alt="Imagen de <%=nombreEdicion%>"
											     src="<%=imagenEdicion%>" 
											     width="120"
											     onerror="this.onerror=null;this.src='<%=request.getContextPath()%>/assets/images/no-photo-or-blank-image-icon-loading-images-or-missing-image-mark-image-not-available-or-image-coming-soon-sign-simple-nature-silhouette-in-frame-isolated-illustration-vector.jpg'">
										</div>
										<div class="info gap-1">
											<div class="d-flex align-items-center mb-1">
												<i class="bi bi-geo-alt-fill mx-2"></i>
												<div class="description"><%=ciudad%>, <%=pais%></div>
											</div>
											<div class="d-flex align-items-center">
												<i class="bi bi-calendar-fill mx-2"></i>
												<div class="description">
													<%=fechaInicio != null ? fechaInicio.format(formatter) : ""%> - 
													<%=fechaFin != null ? fechaFin.format(formatter) : ""%>
												</div>
											</div>
										</div>
									</div>
									<% if (esOrganizadorDeEstaEdicion != null && esOrganizadorDeEstaEdicion && estado != null) { %>
										<div class="button1 rounded-5 p-3 
											<% if (estado == EstadoEdicion.CONFIRMADA) { %>
												bg-success bg-gradient bg-opacity-75 text-white
											<% } else if (estado == EstadoEdicion.RECHAZADA) { %>
												bg-danger bg-gradient bg-opacity-75 text-white
											<% } else if (estado == EstadoEdicion.INGRESADA) { %>
												bg-warning bg-gradient bg-opacity-75 text-white
											<% } %>
										">
											<div class="header-button">
												<% if (estado == EstadoEdicion.CONFIRMADA) { %>
													<i class="bi bi-check-circle-fill me-2"></i>Aceptada
												<% } else if (estado == EstadoEdicion.RECHAZADA) { %>
													<i class="bi bi-x-circle-fill me-2"></i>Rechazada
												<% } else if (estado == EstadoEdicion.INGRESADA) { %>
													<i class="bi bi-hourglass-split me-2"></i>Pendiente
												<% } %>
											</div>
										</div>
									<% } %>
								</div>
							</div>
						</a>
					</div>
				<%
					}
				}
				%>
			</div>
		</div>
	</div>
	
	<!-- Modal de confirmación para finalizar evento wAAAA-->
		<div class="modal fade" id="modalConfirmarFinalizacion" tabindex="-1" aria-labelledby="modalConfirmarFinalizacionLabel" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header bg-danger text-white">
		        <h5 class="modal-title" id="modalConfirmarFinalizacionLabel">
		          <i class="bi bi-exclamation-triangle-fill me-2"></i>Confirmar finalización
		        </h5>
		        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Cerrar"></button>
		      </div>
		      <div class="modal-body">
		        ¿Desea finalizar el evento <strong id="nombreEventoAMostrar"></strong>?<br>
		        <small class="text-muted">Esta acción no se puede deshacer.</small>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
		        <button type="button" class="btn btn-danger" id="btnConfirmarFinalizacion">Finalizar evento</button>
		      </div>
		    </div>
		  </div>
		</div>

		<script>
			document.addEventListener('DOMContentLoaded', function () {
			    const modal = document.getElementById('modalConfirmarFinalizacion');
			    const btnConfirmar = document.getElementById('btnConfirmarFinalizacion');
			    let urlFinalizar = '';
			
			    modal.addEventListener('show.bs.modal', function (event) {
			        const button = event.relatedTarget;
			        urlFinalizar = button.getAttribute('data-url-finalizar');
			        
			        // Opcional: mostrar el nombre del evento en el modal
			        const nombreEvento = '<%= evento != null ? evento.getNombre().replace("'", "\\'") : "" %>';
			        document.getElementById('nombreEventoAMostrar').textContent = '"' + nombreEvento + '"';
			    });
			
			    btnConfirmar.addEventListener('click', function () {
			        if (urlFinalizar) {
			            window.location.href = urlFinalizar;
			        }
			    });
			});
			</script>

</body>
</html>