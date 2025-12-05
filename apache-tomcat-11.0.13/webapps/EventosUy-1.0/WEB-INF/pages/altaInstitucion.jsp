<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>
<html lang="es">
<head>
<meta charset="utf-8" />
<meta http-equiv="x-ua-compatible" content="ie=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Alta de Institución — EVENTOS.UY</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
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
	href="https://fonts.googleapis.com/css2?family=Inter&display=swap" />

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

<jsp:include page="/WEB-INF/templates/header.jsp" />

<body id="body-pd">
	<main class="contUser">
		<div class="container-xl">

			<!-- Header del formulario -->
			<section class="card mb-3">
				<div class="card-body d-flex align-items-center gap-3">
					<a href="perfil"
						class="btn btn-light d-flex align-items-center justify-content-center p-2"
						style="width: 40px; height: 40px; border-radius: 50%;"> <i
						class='bx bx-chevron-left fs-4'></i>
					</a> <i class="bx bx-building fs-3"></i>
					<h2 class="mb-0 fw-bold">Alta de Institución</h2>
				</div>
			</section>

			<!-- Formulario -->
			<section class="card mb-3">
				<div class="card-body">
					<!-- Show messages if present (imitate AltaEvento.jsp) -->
					<% if (request.getAttribute("mensaje") != null ) { %>
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

					<form id="altaInstitucionForm" action="${pageContext.request.contextPath}/altaInstitucion" method="post" enctype="multipart/form-data">

						<div class="mb-3">
							<label for="nombreInstitucion" class="form-label">Nombre</label>
							<input type="text" class="form-control" id="nombreInstitucion" name="nombre"
								placeholder="Ingrese el nombre" required
								value="<%= request.getAttribute("nombre") != null ? (String)request.getAttribute("nombre") : "" %>"
								oninvalid="this.setCustomValidity('Por favor ingrese el nombre de la institución.')"
								oninput="this.setCustomValidity('')">
							<div class="invalid-feedback">Este nombre ya está en uso.
								Por favor ingrese otro.</div>
						</div>

						<div class="mb-3">
							<label for="descripcionInstitucion" class="form-label" >Descripción</label>
							<textarea class="form-control" id="descripcionInstitucion" name="descripcion"
								rows="3" placeholder="Ingrese la descripción" required
								oninvalid="this.setCustomValidity('Por favor ingrese la descripción de la institución.')"
								oninput="this.setCustomValidity('')"><%= request.getAttribute("descripcion") != null ? (String)request.getAttribute("descripcion") : "" %></textarea>
						</div>

						<div class="mb-3">
							<label for="sitioWebInstitucion" class="form-label" >Sitio
								web</label> <input type="url" class="form-control" name="url"
								id="sitioWebInstitucion" placeholder="https://www.ejemplo.com"
								required
								value="<%= request.getAttribute("url") != null ? (String)request.getAttribute("url") : "" %>"
								oninvalid="this.setCustomValidity('Por favor ingrese la URL del sitio web.')"
								oninput="this.setCustomValidity('')">
						</div>

						<div class="mb-3">
							<label for="logoInstitucion" class="form-label">Logo de
								la institución (opcional)</label> 
								<input class="form-control" type="file" id="logoInstitucion" name="logo" accept="image/*">
						</div>

						<div class="d-flex gap-2">
							<button type="submit" class="btn btn-primary">Guardar</button>
							<a href="perfil" class="btn btn-secondary">Cancelar</a>
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