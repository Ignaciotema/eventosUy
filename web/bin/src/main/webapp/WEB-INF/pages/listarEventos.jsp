<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Map" %>
<%@ page import="webservices.*" %>

<%
    @SuppressWarnings("unchecked")
    Set<String> eventos = (Set<String>) request.getAttribute("eventos");
    @SuppressWarnings("unchecked")
    Set<Map<String, Object>> eventosInfo = (Set<Map<String, Object>>) request.getAttribute("eventosInfo");
    @SuppressWarnings("unchecked")
    Set<String> categorias = (Set<String>) request.getAttribute("categorias");
    String categoriaSeleccionada = (String) request.getAttribute("categoriaSeleccionada");
    String nombreBusqueda = (String) request.getAttribute("nombreBusqueda");
    String tipoFiltro = (String) request.getAttribute("tipoFiltro");
    String ordenamiento = (String) request.getAttribute("ordenamiento");
    Integer totalEventos = (Integer) request.getAttribute("totalEventos");
    Integer totalEdiciones = (Integer) request.getAttribute("totalEdiciones");
    Integer eventosFiltrados = (Integer) request.getAttribute("eventosFiltrados");
    
    // Obtener información del usuario de la sesión
    DataUsuario usuario = (DataUsuario) request.getSession().getAttribute("usuario");
%>

<!DOCTYPE html>
<html lang="es">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Buscar eventos/ediciones - Eventos.uy</title>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css2?family=Inter&display=swap" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/styles.css">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB"
	crossorigin="anonymous">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI"
	crossorigin="anonymous"></script>
<link rel="icon" type="image/x-icon" href="<%=request.getContextPath()%>/assets/icons/Logo.png">
</head>

<jsp:include page="../templates/header.jsp"></jsp:include>

<body>

	<div class="container">
		<div class="m-4">
			<h2 class="fw-bold my-3">Buscar eventos/ediciones</h2>
			
			<div class="row row-cols-2 w-100 justify-content-center my-4 gap-5">
				<div class="align-items-center col-12 col-xl-3 my-3">
					<h3 class="categorías fw-bold text-center">Categorías</h3>
					<jsp:include page="../templates/categorias-sidebar.jsp" />
				</div>

				<div class="row container m-2 col-12 col-xl-8 h-100">

					<!-- Mostrar información de filtros -->
					<%if (eventosFiltrados != null) { %>
						<div class="col-12 mb-3">
							<div class="d-flex flex-wrap align-items-center gap-2 mb-2">
								<span class="text-muted">
									<%=eventosFiltrados%> resultado(s)
									<%if (totalEventos != null && totalEdiciones != null) { %>
										(<%=totalEventos + totalEdiciones%> total)
									<%} %>
								</span>
								<%if (tipoFiltro != null && !tipoFiltro.isEmpty()) { %>
									<span class="badge bg-primary"><%="eventos".equals(tipoFiltro) ? "Eventos" : "Ediciones"%></span>
								<%} %>
								<%if (categoriaSeleccionada != null && !categoriaSeleccionada.isEmpty() && !"todas".equals(categoriaSeleccionada)) { %>
									<span class="badge bg-secondary"><%=categoriaSeleccionada%></span>
								<%} %>
								<%if (nombreBusqueda != null && !nombreBusqueda.isEmpty()) { %>
									<span class="badge bg-info">"<%=nombreBusqueda%>"</span>
								<%} %>
							</div>
						</div>
					<%} %>

					<div class="row row-cols-1 gx-0 gy-3 col-12 mt-0 gap-3">
						<%
						if (eventosInfo == null || eventosInfo.isEmpty()) {
						%>
							<div class="col-12">
								<div class="alert alert-info text-center">
									<i class="bi bi-info-circle"></i>
									No se encontraron eventos o ediciones que coincidan con los criterios de búsqueda.
								</div>
							</div>
						<%
						} else {
							for (Map<String, Object> itemInfo : eventosInfo) {
								String nombreItem = (String) itemInfo.get("nombre");
								String descripcionItem = (String) itemInfo.get("descripcion");
								String imagenItem = (String) itemInfo.get("imagenEvento");
								String tipoItem = (String) itemInfo.get("tipo");
								String eventoParent = (String) itemInfo.get("eventoParent");
								
								// Determinar el enlace de destino
								String enlaceDestino;
								if ("evento".equals(tipoItem)) {
									enlaceDestino = request.getContextPath() + "/detalleEvento?nombre=" + java.net.URLEncoder.encode(nombreItem, "UTF-8");
								} else {
									enlaceDestino = request.getContextPath() + "/detalleEdicion?nombre=" + java.net.URLEncoder.encode(nombreItem, "UTF-8");
								}
						%>
							<!-- Card dinámico para evento o edición -->
							<div class="col m-0">
								<a href="<%=enlaceDestino%>" class="text-decoration-none text-dark">
									<div class="carta p-4">
										<div class="d-flex justify-content-between align-items-start mb-2">
											<div class="text-heading flex-grow-1">
												<div class="search-text-heading"><%=nombreItem%></div>
											</div>
											<div class="ms-2">
												<%if ("evento".equals(tipoItem)) { %>
													<span class="badge bg-success">
														<i class="bi bi-calendar-event"></i> Evento
													</span>
												<%} else { %>
													<span class="badge bg-primary">
														<i class="bi bi-geo-alt"></i> Edición
													</span>
												<%} %>
											</div>
										</div>
										<div class="d-flex flex-row justify-content-between align-items-start w-100">
											<div class="avatar-block">
												<div class="avatar">
													<img class="shape-icon" alt="Imagen de <%=nombreItem%>"
														src="<%=imagenItem%>"
														onerror="this.onerror=null;this.src='<%=request.getContextPath()%>/assets/images/no-photo-or-blank-image-icon-loading-images-or-missing-image-mark-image-not-available-or-image-coming-soon-sign-simple-nature-silhouette-in-frame-isolated-illustration-vector.jpg'">
												</div>
												<div class="info gap-1">
													<%if (descripcionItem != null && !descripcionItem.trim().isEmpty()) { %>
														<div class="d-flex align-items-center">
															<i class="bi bi-info-circle mx-2"></i>
															<div class="description"><%=descripcionItem.length() > 150 ? descripcionItem.substring(0, 150) + "..." : descripcionItem%></div>
														</div>
													<%} %>
													<%if ("edicion".equals(tipoItem) && eventoParent != null) { %>
														<div class="d-flex align-items-center mt-1">
															<i class="bi bi-arrow-up-right mx-2 text-muted"></i>
															<small class="text-muted">Evento: <%=eventoParent%></small>
														</div>
													<%} %>
												</div>
											</div>
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

		</div>

	</div>

	<script src="<%=request.getContextPath()%>/assets/js/main.js"></script>
</body>

</html>