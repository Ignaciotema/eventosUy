<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8" />
<meta http-equiv="x-ua-compatible" content="ie=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Alta de Edición — Eventos.uy</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="https://cdn.jsdelivr.net/npm/boxicons@latest/css/boxicons.min.css"
	rel="stylesheet">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css2?family=Inter&display=swap" />
<link
	href="https://fonts.googleapis.com/css2?family=Titillium+Web:wght@300;400;600&display=swap"
	rel="stylesheet">
<link rel="stylesheet" href="../assets/css/ConsultaEvento.css">
<link rel="stylesheet" href="../assets/css/styles.css">
<link
	href="https://cdn.jsdelivr.net/npm/boxicons@latest/css/boxicons.min.css"
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
	font-family: system-ui, -apple-system, "Segoe UI", Roboto,
		"Titillium Web", Arial, sans-serif;
	background: var(--bg);
	color: var(--text);
	margin: 0;
	padding-left: var(--sidebar-w);
	transition: padding-left .2s ease;
}

main.contUser {
	padding: 18px;
}

.container-xl {
	max-width: 980px;
}

.card {
	border: 1px solid var(--border);
	border-radius: var(--radius);
	box-shadow: var(--shadow);
}

.form-label {
	font-weight: 600;
}
</style>
</head>

<jsp:include page="../templates/header.jsp"></jsp:include>

<body id="body-pd">
	<main class="contUser">
		<div class="container-xl">

			<!-- Header del formulario con botón volver -->
			<section class="card mb-3">
				<div class="card-body d-flex align-items-center gap-3">
				<% String back = request.getParameter("nombreEvento") != null ? "/tarea2/detalleEvento?nombre=" + request.getParameter("nombreEvento") : "HomeServlet"; %>
					<a href="<%= back %>"
						class="btn btn-light d-flex align-items-center justify-content-center p-2"
						style="width: 40px; height: 40px; border-radius: 50%;"> <i
						class='bx bx-chevron-left fs-4'></i>
					</a> <i class="bx bx-calendar fs-3"></i>
					<h2 class="mb-0 fw-bold">Alta de Edición</h2>
				</div>
			</section>

			<!-- Formulario -->
			<section class="card mb-3">
				<div class="card-body">
					<% if (request.getAttribute("mensaje") != null) { %>
						<div class="alert alert-primary" role="alert">
							<i class="bx bx-check-circle me-2"></i>
							<%= request.getAttribute("mensaje") %>
						</div>
					<% } else if (request.getAttribute("error") != null ) { %>
						<div class="alert alert-danger" role="alert">
							<i class="bx bx-error-circle me-2"></i>
							<%= request.getAttribute("error") %>
						</div>
					<% } %>

					<form id="altaEdicionForm" enctype="multipart/form-data" action="${pageContext.request.contextPath}/altaEdicion?nombreEvento=<%= request.getParameter("nombreEvento") != null ? request.getParameter("nombreEvento") : "" %>" method="post" class="needs-validation" autocomplete="off">
						<div class="mb-3">
							<label for="nombreEdicion" class="form-label">Nombre de
								la edición <span class="text-danger">*</span></label> 
							<input type="text" class="form-control"
								id="nombreEdicion" name="nombre" placeholder="Ingrese el nombre" 
								value="<%= request.getAttribute("nombre") != null ? (String)request.getAttribute("nombre") : "" %>"
								required autocomplete="off"
								oninvalid="this.setCustomValidity('Por favor ingrese el nombre de la edición.')"
								oninput="this.setCustomValidity('')">
							<div class="invalid-feedback">
								Por favor ingrese el nombre de la edición.
							</div>
						</div>

						<div class="mb-3">
							<label for="siglaEdicion" class="form-label">Sigla <span class="text-danger">*</span></label> 
							<input type="text" class="form-control" id="siglaEdicion" name="sigla"
								placeholder="Ej: EVT2025-1" 
								value="<%= request.getAttribute("sigla") != null ? (String)request.getAttribute("sigla") : "" %>"
								required autocomplete="off"
								oninvalid="this.setCustomValidity('Por favor ingrese la sigla de la edición.')"
								oninput="this.setCustomValidity('')">
							<div class="invalid-feedback">
								Por favor ingrese la sigla de la edición.
							</div>
						</div>

						<div class="mb-3">
							<label for="ciudadEdicion" class="form-label">Ciudad <span class="text-danger">*</span></label> 
							<input type="text" class="form-control" id="ciudadEdicion" name="ciudad"
								placeholder="Ingrese la ciudad" 
								value="<%= request.getAttribute("ciudad") != null ? (String)request.getAttribute("ciudad") : "" %>"
								required autocomplete="off"
								oninvalid="this.setCustomValidity('Por favor ingrese la ciudad.')"
								oninput="this.setCustomValidity('')">
							<div class="invalid-feedback">
								Por favor ingrese la ciudad.
							</div>
						</div>

						<div class="mb-3">
							<label for="paisEdicion" class="form-label">País <span class="text-danger">*</span></label> 
							<input type="text" class="form-control" id="paisEdicion" name="pais"
								placeholder="Ingrese el país" 
								value="<%= request.getAttribute("pais") != null ? (String)request.getAttribute("pais") : "" %>"
								required autocomplete="off"
								oninvalid="this.setCustomValidity('Por favor ingrese el país.')"
								oninput="this.setCustomValidity('')">
							<div class="invalid-feedback">
								Por favor ingrese el país.
							</div>
						</div>

						<div class="mb-3">
							<label for="fechaInicio" class="form-label">Fecha de
								inicio <span class="text-danger">*</span></label> 
							<input type="date" class="form-control" id="fechaInicio" name="fechaInicio"
								value="<%= request.getAttribute("fechaInicio") != null ? (String)request.getAttribute("fechaInicio") : "" %>"
								required
								oninvalid="this.setCustomValidity('Por favor ingrese la fecha de inicio.')"
								oninput="this.setCustomValidity('')">
							<div class="invalid-feedback">
								Por favor ingrese la fecha de inicio.
							</div>
						</div>

						<div class="mb-3">
							<label for="fechaFin" class="form-label">Fecha de fin <span class="text-danger">*</span></label> 
							<input type="date" class="form-control" id="fechaFin" name="fechaFin" 
								value="<%= request.getAttribute("fechaFin") != null ? (String)request.getAttribute("fechaFin") : "" %>"
								required
								oninvalid="this.setCustomValidity('Por favor ingrese la fecha de fin.')"
								oninput="this.setCustomValidity('')">
							<div class="invalid-feedback">
								Por favor ingrese la fecha de fin.
							</div>
						</div>

						<div class="mb-3">
							<label for="imagenEdicion" class="form-label">Imagen de
								la edición (opcional)</label> 
							<input class="form-control" type="file"
								id="imagenEdicion" name="imagen" accept="image/*">
							<small class="text-muted">Formatos admitidos: JPG, PNG, GIF. Tamaño máximo: 5MB</small>
						</div>

						<div class="mb-3">
							<label for="urlYoutube" class="form-label">URL de YouTube (opcional)</label>
							<input type="url" class="form-control" id="urlYoutube" 
								name="urlYoutube"
								placeholder="https://www.youtube.com/watch?v=..." 
								value="<%= request.getAttribute("urlYoutube") != null ? (String)request.getAttribute("urlYoutube") : "" %>"
								autocomplete="off">
							<small class="text-muted">URL completa del video de YouTube relacionado con la edición</small>
						</div>
						
						<div class="d-flex gap-2">
							<button type="submit" class="btn btn-primary">Guardar</button>
							<a href="<%= back %>" class="btn btn-secondary">Cancelar</a>
						</div>
					</form>
				</div>
			</section>

		</div>
	</main>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>