<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="webservices.DataUsuario" %>

<!doctype html>

<%  @SuppressWarnings("unchecked")
    DataUsuario usuariO = (DataUsuario) request.getAttribute("usuario");
    
    @SuppressWarnings("unchecked")
    Set<DataUsuario> usuarios = (Set<DataUsuario>) request.getAttribute("usuarios");
    if (usuarios == null) usuarios = java.util.Collections.emptySet();

    @SuppressWarnings("unchecked")
    Map<String,String> imgsUsuarios = (Map<String,String>) request.getAttribute("imgsUsuarios");
    if (imgsUsuarios == null) imgsUsuarios = java.util.Collections.emptyMap();

    @SuppressWarnings("unchecked")
    Map<String,Boolean> estadosSeguimiento = (Map<String,Boolean>) request.getAttribute("estadosSeguimiento");
    if (estadosSeguimiento == null) estadosSeguimiento = java.util.Collections.emptyMap();

    String usuarioLogueado = (String) request.getAttribute("usuarioLogueado");
    String q = (String) request.getAttribute("q");
    if (q == null) q = "";

    String ctx = request.getContextPath();
%>

<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Listar usuarios</title>
		<link rel="stylesheet" href="<%= ctx %>/assets/css/styles.css">
		<link rel="stylesheet"
			href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
		<link rel="stylesheet"
			href="<%= ctx %>/assets/css/RolVisitante_listarUsuarios.css">
		<link rel="stylesheet"
			href="https://fonts.googleapis.com/css2?family=Inter&display=swap" />
		<!-- Bootstrap Icons CDN -->
		<link
			href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css"
			rel="stylesheet">
		<link rel="icon" type="image/x-icon" href="<%= ctx %>/assets/icons/Logo.png">
		
		<style>
			.card {
				overflow: hidden;
				box-shadow: 0 2px 4px rgba(0,0,0,0.1);
			}
			
			.user.row {
				margin: 0;
				width: 100%;
			}
			
			.user .col-auto:first-child {
				padding-right: 0;
			}
			
			.user .col {
				padding-left: 0;
				padding-right: 0;
			}
			
			.user .col-auto:last-child {
				padding-left: 0;
				padding-right: 20px;
			}
			
			.user-actions {
				display: flex;
				align-items: center;
				justify-content: flex-end;
			}
			
			.btn-seguir {
				white-space: nowrap;
				font-size: 0.875rem;
				padding: 0.375rem 0.75rem;
			}
			
			/* Para pantallas más pequeñas */
			@media (max-width: 768px) {
				.btn-seguir {
					font-size: 0.75rem;
					padding: 0.25rem 0.5rem;
				}
				
				.user .col-auto:last-child {
					padding-right: 15px;
				}
			}
			
			@media (max-width: 576px) {
				.btn-seguir {
					font-size: 0.7rem;
					padding: 0.2rem 0.4rem;
				}
				
				.user .col-auto:last-child {
					padding-right: 10px;
				}
			}
		</style>
	</head>
	
	<body>

		<jsp:include page="../templates/header.jsp"></jsp:include>
	
		<main class="container my-5">
			<div class="row justify-content-center mb-4 align-items-center">
				<div class="col-md-6">
					<form method="get" action="<%= ctx %>/listarUsuarios" role="search">
						<div class="input-group rounded-pill overflow-hidden shadow-sm">
							<!-- Only show the search input; submit on Enter. -->
							<input type="search" name="q" class="form-control form-control-lg border-0"
								placeholder="Buscar usuarios registrados"
								value="<%= q %>" aria-label="Buscar usuarios"
								onkeydown="if(event.key === 'Enter'){ this.form.submit(); }">
						</div>
					</form>
				</div>
			</div>

			<div id="listar" class="row justify-content-center">
				<div class="col-md-8">
					<% if (usuarios.isEmpty()) { %>
						<div class="alert alert-info text-center">
							<i class="bi bi-info-circle"></i>
							No se encontraron usuarios.
						</div>
					<% } else {
						for (DataUsuario usuario : usuarios) {
							String nick = usuario.getNickname();
							String imgRel = imgsUsuarios.get(nick);
							if (imgRel == null || imgRel.isBlank()) imgRel = "uploads/usuarios/default.jpg";
							String detalleUrl = ctx + "/detalleUsuario?usuarios=" + URLEncoder.encode(nick, "UTF-8");
							Boolean seguido = estadosSeguimiento.get(nick);
							Boolean esUsuarioLogueado = usuarioLogueado != null && nick.equals(usuarioLogueado);
						%>
							<div class="card rounded-5 mb-3">
								<div class="user row g-0 align-items-center">
									<div class="col-auto">
										<a href="<%= detalleUrl %>" class="text-decoration-none">
											<img src="<%= imgRel %>" alt="<%= nick %>"
											class="img-fluid rounded-circle m-3"
											style="width: 90px; height: 90px; object-fit: cover;">
										</a>
									</div>
									<div class="col user-card-content">
										<a href="<%= detalleUrl %>" class="text-decoration-none">
											<div class="card-body">
												<h5 class="card-title mb-1 text-dark"><%= nick %></h5>
												<p class="card-text text-secondary mb-0"><%= usuario.getTipo() %></p>
											</div>
										</a>
									</div>
									<% if (usuarioLogueado != null && !esUsuarioLogueado) { %>
									<div class="col-auto user-actions">
										<% if (seguido != null && seguido) { %>
											<!-- Botón para dejar de seguir -->
											<form action="<%= ctx %>/dejarDeSeguir" method="post" class="d-inline" 
												  onclick="event.stopPropagation();">
												<input type="hidden" name="usuario" value="<%= nick %>">
												<button type="submit" class="btn btn-outline-secondary btn-seguir">
													<i class="bi bi-person-dash"></i> Dejar de seguir
												</button>
											</form>
										<% } else { %>
											<!-- Botón para seguir -->
											<form action="<%= ctx %>/seguirUsuario" method="post" class="d-inline" 
												  onclick="event.stopPropagation();">
												<input type="hidden" name="usuario" value="<%= nick %>">
												<button type="submit" class="btn btn-primary btn-seguir">
													<i class="bi bi-person-plus"></i> Seguir
												</button>
											</form>
										<% } %>
									</div>
									<% } %>
								</div>
							</div>
						<% }
						} %>
				</div>
			</div>
		</main>
		
		<script
			src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
	</body>

</html>