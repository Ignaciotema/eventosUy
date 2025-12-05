<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="webservices.DataUsuario" %>
    <%@ page import="webservices.TipoUsuario" %>
    <%@ page import="webservices.DtDetalleEdicion" %>
    <%@ page import="webservices.EstadoEdicion" %>
    <%@ page import="java.util.Set" %>
<!DOCTYPE html>
<html lang="es">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Lista ediciones</title>
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css2?family=Inter&display=swap" />
<link rel="stylesheet" href="assets/css/styles.css">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB"
	crossorigin="anonymous">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI"
	crossorigin="anonymous"></script>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
</head>

<body>
</head>

<body>
	<jsp:include page="../templates/header.jsp"></jsp:include>

	<div class="container">
		<div class="m-4">
			<%
			DataUsuario user = (DataUsuario) session.getAttribute("usuario");
			if(user != null && user.getTipo() == TipoUsuario.ORGANIZADOR){
			%>
			<h2 class="fw-bold my-3">Mis ediciones</h2>
			<%} else if(user != null){ %>
				<h2 class="fw-bold my-3">Mis registros</h2>
			<% } %>
		
			<jsp:include page="../templates/searchbarevento.jsp"></jsp:include>
			<div class="row row-cols-2 w-100 justify-content-center my-4 gap-5">
				<div class="align-items-center col-12 col-xl-3 my-3">


					<h3 class="categorias fw-bold text-center">Categorías</h3>
					<jsp:include page="../templates/categorias-sidebar.jsp"></jsp:include>
				</div>

				<div class="row container m-2 col-12 col-xl-8">
					<div class="row row-cols-1 gx-0 gy-3 col-12 mt-0">
					<% 
						Set<DtDetalleEdicion> ediciones = (Set<DtDetalleEdicion>) request.getAttribute("ediciones");
						String mensaje;
						if (user != null && user.getTipo() == TipoUsuario.ORGANIZADOR) {
							mensaje = "Aún no organizas ninguna edición";
						} else {
							mensaje = "Aún no estas registrado a ninguna edicion";
						}
						if (ediciones.isEmpty()) {
						%> <div class="alert alert-secondary text-center mb-0 align-items-center" role="alert">
						<%= mensaje %></div> <% } %>
						<% for (DtDetalleEdicion edicion : ediciones) { 
							if (user == null || user.getTipo() == TipoUsuario.ASISTENTE) {
						%>
							<div class="col-12">
								<a href="detalleEdicion?nombre=<%= edicion.getNombre() %>"
									class="text-decoration-none text-reset">
									<div class="carta p-4">
										<div class="text-heading">
											<div class="search-text-heading"><%= edicion.getNombre() %></div>
										</div>
										<div
											class="d-flex flex-row justify-content-between align-items-start w-100">
											<div class="avatar-block">
												<div class="avatar">
													<img class="shape-icon" alt=""
														src="<%= request.getAttribute(edicion.getNombre()) %>">
												</div>
												<div class="info gap-1">
													<div class="d-flex align-items-center">
														<i class="bi bi-geo-alt-fill mx-2"></i>
														<div class="description"><%= edicion.getCiudad() %>, <%= edicion.getPais() %></div>
													</div>
													<div class="d-flex align-items-center">
														<i class="bi bi-calendar-fill mx-2"></i>
														<div class="description"><%= edicion.getFechaInicio() %> / <%= edicion.getFechaFin() %></div>
													</div>
												</div>
											</div>
										</div>
	
									</div>
							</div> <% } else {
								switch (edicion.getEstado()) {
									case INGRESADA : {
										%>
										<div class="col-12">
								<a href="detalleEdicion?nombre=<%= edicion.getNombre() %>"
									class="text-decoration-none text-reset">
									<div class="carta p-4 bg-warning bg-opacity-10 bg-gradient border-warning">
										<div class="text-heading">
											<div class="search-text-heading"><%= edicion.getNombre() %></div>
										</div>
										<div
											class="d-flex flex-row justify-content-between align-items-start w-100">
											<div class="avatar-block">
												<div class="avatar">
													<img class="shape-icon" alt=""
														src="<%= request.getAttribute(edicion.getNombre()) %>">
												</div>
												<div class="info gap-1">
													<div class="d-flex align-items-center">
														<i class="bi bi-geo-alt-fill mx-2"></i>
														<div class="description"><%= edicion.getCiudad() %>, <%= edicion.getPais() %></div>
													</div>
													<div class="d-flex align-items-center">
														<i class="bi bi-calendar-fill mx-2"></i>
														<div class="description"><%= edicion.getFechaInicio() %> / <%= edicion.getFechaFin() %></div>
													</div>
												</div>
											</div>
											<div class="button1 rounded-5 px-4 py-3 bg-warning bg-gradient bg-opacity-75 text-white">
											<div class="header-button">
												<i class="bi bi-hourglass-split me-2"></i>Pendiente
											</div>
										</div>
										</div>
	
									</div>
							</div>
										<% break;
									}
									case CONFIRMADA: {
										%>
										<div class="col-12">
								<a href="detalleEdicion?nombre=<%= edicion.getNombre() %>"
									class="text-decoration-none text-reset">
									<div class="carta p-4 bg-success bg-opacity-10 bg-gradient border-success">
										<div class="text-heading">
											<div class="search-text-heading"><%= edicion.getNombre() %></div>
										</div>
										<div
											class="d-flex flex-row justify-content-between align-items-start w-100">
											<div class="avatar-block">
												<div class="avatar">
													<img class="shape-icon" alt=""
														src="<%= request.getAttribute(edicion.getNombre()) %>">
												</div>
												<div class="info gap-1">
													<div class="d-flex align-items-center">
														<i class="bi bi-geo-alt-fill mx-2"></i>
														<div class="description"><%= edicion.getCiudad() %>, <%= edicion.getPais() %></div>
													</div>
													<div class="d-flex align-items-center">
														<i class="bi bi-calendar-fill mx-2"></i>
														<div class="description"><%= edicion.getFechaInicio() %> / <%= edicion.getFechaFin() %></div>
													</div>
												</div>
											</div>
											<div class="button1 rounded-5 p-3 bg-success bg-gradient bg-opacity-75 text-white">
											<div class="header-button">
												<i class="bi bi-check-circle-fill me-2"></i>Aceptada
											</div>
										</div>
										</div>
	
									</div>
							</div>
										<% break;
									}
									case RECHAZADA: {
										%>
										<div class="col-12">
								<a href="detalleEdicion?nombre=<%= edicion.getNombre() %>"
									class="text-decoration-none text-reset">
									<div class="carta p-4 bg-danger bg-opacity-10 bg-gradient border-danger">
										<div class="text-heading">
											<div class="search-text-heading"><%= edicion.getNombre() %></div>
										</div>
										<div
											class="d-flex flex-row justify-content-between align-items-start w-100">
											<div class="avatar-block">
												<div class="avatar">
													<img class="shape-icon" alt=""
														src="<%= request.getAttribute(edicion.getNombre()) %>">
												</div>
												<div class="info gap-1">
													<div class="d-flex align-items-center">
														<i class="bi bi-geo-alt-fill mx-2"></i>
														<div class="description"><%= edicion.getCiudad() %>, <%= edicion.getPais() %></div>
													</div>
													<div class="d-flex align-items-center">
														<i class="bi bi-calendar-fill mx-2"></i>
														<div class="description"><%= edicion.getFechaInicio() %> / <%= edicion.getFechaFin() %></div>
													</div>
												</div>
											</div>
											<div class="button1 rounded-5 p-3 bg-danger bg-gradient bg-opacity-75 text-white">
											<div class="header-button">
												<i class="bi bi-x-circle-fill me-2"></i>Rechazada
											</div>
										</div>
										</div>
	
									</div>
							</div>
										<% break;
									}
								}
							}} %>

					</div>



				</div>


			</div>


		</div>


	</div>

	<script src="assets/js/main.js"></script>
</body>

</html>
