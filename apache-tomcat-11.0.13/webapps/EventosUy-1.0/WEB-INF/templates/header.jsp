<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="webservices.*" %>
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css2?family=Inter&display=swap" />
<link rel="icon" type="image/x-icon" href="../assets/icons/Logo.png">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB"
	crossorigin="anonymous">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">

<link rel="stylesheet"
	href="https://fonts.googleapis.com/css2?family=Inter&display=swap" />
<link rel="icon" type="image/x-icon" href="assets/icons/Logo.png">
<!-- Icono de la pestaña -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB"
	crossorigin="anonymous">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
	<link rel="stylesheet" href="assets/css/styles.css">
	
	
<% 
	DataUsuario user = (DataUsuario) session.getAttribute("usuario");
	// Obtener valores para la searchbar
    String valorBusqueda = request.getParameter("nombre");
    if (valorBusqueda == null) {
        valorBusqueda = (String) request.getAttribute("nombreBusqueda");
    }
    if (valorBusqueda == null) {
        valorBusqueda = "";
    }
    
    String tipoFiltro = request.getParameter("tipo");
    if (tipoFiltro == null) {
        tipoFiltro = (String) request.getAttribute("tipoFiltro");
    }
    
    String ordenamiento = request.getParameter("orden");
    if (ordenamiento == null) {
        ordenamiento = (String) request.getAttribute("ordenamiento");
    }
	
	if (user == null) { %>
	<header>
		<nav class="navbar bg-white shadow-sm" style="height: 86px;">
			<div>
				<a class="fw-bold text-dark fs-2 m-4 text-decoration-none" href="HomeServlet"><b>Eventos.uy</b></a>
			</div>
			
			<!-- Searchbar simplificada en el centro -->
			<div class="text-center" style="flex: 1;">
				<form action="<%=request.getContextPath()%>/eventos" method="get" class="d-flex justify-content-center align-items-center gap-2" style="max-width: 500px; margin: 0 auto;">
					<input type="search" name="nombre" class="search flex-grow-1"
					       placeholder="Buscar eventos o ediciones..." 
					       value="<%=valorBusqueda%>">
					<div class="dropdown">
						<button class="btn btn-outline-secondary btn-sm dropdown-toggle" type="button" 
						        id="filtrosDropdown" data-bs-toggle="dropdown" aria-expanded="false">
							<i class="bi bi-funnel"></i>
						</button>
						<div class="dropdown-menu p-3" style="min-width: 220px;">
							<div class="mb-3">
								<strong>Tipo:</strong>
								<div class="form-check">
									<input class="form-check-input" type="radio" name="tipo" value="" id="tipoAmbos"
									       <%=(tipoFiltro == null || tipoFiltro.isEmpty()) ? "checked" : ""%>>
									<label class="form-check-label" for="tipoAmbos">Ambos</label>
								</div>
								<div class="form-check">
									<input class="form-check-input" type="radio" name="tipo" value="eventos" id="tipoEventos"
									       <%="eventos".equals(tipoFiltro) ? "checked" : ""%>>
									<label class="form-check-label" for="tipoEventos">Solo eventos</label>
								</div>
								<div class="form-check">
									<input class="form-check-input" type="radio" name="tipo" value="ediciones" id="tipoEdiciones"
									       <%="ediciones".equals(tipoFiltro) ? "checked" : ""%>>
									<label class="form-check-label" for="tipoEdiciones">Solo ediciones</label>
								</div>
							</div>
							<div class="mb-3">
								<strong>Ordenar:</strong>
								<div class="form-check">
									<input class="form-check-input" type="radio" name="orden" value="" id="ordenFecha"
									       <%=(ordenamiento == null || ordenamiento.isEmpty()) ? "checked" : ""%>>
									<label class="form-check-label" for="ordenFecha">Por fecha</label>
								</div>
								<div class="form-check">
									<input class="form-check-input" type="radio" name="orden" value="alfabetico" id="ordenAlfabetico"
									       <%="alfabetico".equals(ordenamiento) ? "checked" : ""%>>
									<label class="form-check-label" for="ordenAlfabetico">Alfabético</label>
								</div>
							</div>
							<button type="submit" class="btn btn-primary btn-sm w-100">Buscar</button>
						</div>
					</div>
				</form>
			</div>
			
			<div class="header-auth m-3 d-flex justify-content-end">
				<a href="iniciosesion" class="text-decoration-none">
					<button type="button" class="button1 rounded-3">
						<div class="header-button">Iniciar sesión</div>
					</button>
				</a> <a href="registro" class="text-decoration-none">
					<button type="button" class="button2 rounded-3">
						<div class="header-button">Regístrarse</div>
					</button>
				</a>
			</div>
		</nav>
	</header>
	<% } else { %> 
	
	<header>
		<nav class="navbar bg-white shadow-sm">
			<div>
				<a class="fw-bold text-dark fs-2 m-4 text-decoration-none" href="HomeServlet"><b>Eventos.uy</b></a>
			</div>
			
			<!-- Searchbar simplificada en el centro -->
			<div class="text-center" style="flex: 1;">
				<form action="<%=request.getContextPath()%>/eventos" method="get" class="d-flex justify-content-center align-items-center gap-2" style="max-width: 500px; margin: 0 auto;">
					<input type="search" name="nombre" class="search flex-grow-1"
					       placeholder="Buscar eventos o ediciones..." 
					       value="<%=valorBusqueda%>">
					<div class="dropdown">
						<button class="btn btn-outline-secondary btn-sm dropdown-toggle" type="button" 
						        id="filtrosDropdown" data-bs-toggle="dropdown" aria-expanded="false">
							<i class="bi bi-funnel"></i>
						</button>
						<div class="dropdown-menu p-3" style="min-width: 220px;">
							<div class="mb-3">
								<strong>Tipo:</strong>
								<div class="form-check">
									<input class="form-check-input" type="radio" name="tipo" value="" id="tipoAmbos"
									       <%=(tipoFiltro == null || tipoFiltro.isEmpty()) ? "checked" : ""%>>
									<label class="form-check-label" for="tipoAmbos">Ambos</label>
								</div>
								<div class="form-check">
									<input class="form-check-input" type="radio" name="tipo" value="eventos" id="tipoEventos"
									       <%="eventos".equals(tipoFiltro) ? "checked" : ""%>>
									<label class="form-check-label" for="tipoEventos">Solo eventos</label>
								</div>
								<div class="form-check">
									<input class="form-check-input" type="radio" name="tipo" value="ediciones" id="tipoEdiciones"
									       <%="ediciones".equals(tipoFiltro) ? "checked" : ""%>>
									<label class="form-check-label" for="tipoEdiciones">Solo ediciones</label>
								</div>
							</div>
							<div class="mb-3">
								<strong>Ordenar:</strong>
								<div class="form-check">
									<input class="form-check-input" type="radio" name="orden" value="" id="ordenFecha"
									       <%=(ordenamiento == null || ordenamiento.isEmpty()) ? "checked" : ""%>>
									<label class="form-check-label" for="ordenFecha">Por fecha</label>
								</div>
								<div class="form-check">
									<input class="form-check-input" type="radio" name="orden" value="alfabetico" id="ordenAlfabetico"
									       <%="alfabetico".equals(ordenamiento) ? "checked" : ""%>>
									<label class="form-check-label" for="ordenAlfabetico">Alfabético</label>
								</div>
							</div>
							<button type="submit" class="btn btn-primary btn-sm w-100">Buscar</button>
						</div>
					</div>
				</form>
			</div>
			
			<div class="d-flex justify-content-end align-items-center">
				<div class="dropdown">
					<a class="d-flex align-items-center text-decoration-none gap-2 m-3"
						href="#" id="userMenuDropdown" data-bs-toggle="dropdown"
						aria-expanded="false" aria-haspopup="true"> <img
						src="<%= session.getAttribute("pfp") %>" 
						class="rounded-circle"
						style="width: 38px; height: 38px; object-fit: cover; border: 1px solid rgba(0, 0, 0, .06);">
						<span><%= user.getNombre() %></span> <i class="bi bi-chevron-down"></i>
					</a>
					<ul class="dropdown-menu dropdown-menu-end shadow-sm"
						aria-labelledby="userMenuDropdown">
						<li><a class="dropdown-item" href="perfil">Mi
								perfil</a></li>
						<li>
							<hr class="dropdown-divider">
						</li>
						<li><a class="dropdown-item text-danger"
							href="cerrarsesion"
							style="color: #dc3545 !important;">Cerrar sesión</a></li>
					</ul>
				</div>
			</div>
		</nav>
	</header>
	<script src="assets/js/main.js"></script>
	
	<%}
%>