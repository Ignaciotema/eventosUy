<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>


<html lang="es">
<head>
<meta charset="utf-8" />
<meta http-equiv="x-ua-compatible" content="ie=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<title>Alta de Evento</title>

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

main
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
					<a href="perfil"
						class="btn btn-light d-flex align-items-center justify-content-center p-2"
						style="width: 40px; height: 40px; border-radius: 50%;"> <i
						class='bx bx-chevron-left fs-4'></i>
					</a> <i class="bx bx-calendar-plus fs-3"></i>
					<h2 class="mb-0 fw-bold">Alta de Evento</h2>
				</div>
			</section>

			<!-- Formulario -->
			<section class="card mb-3">
				<div class="card-body">
					<!-- Show error message if exists and not from cache -->
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

					<form id="altaEventoForm" action="${pageContext.request.contextPath}/altaEvento" method="post" enctype="multipart/form-data" class="needs-validation"  autocomplete="off">

						<div class="mb-3">
							<label for="nombreEvento" class="form-label">Nombre del evento <span class="text-danger">*</span></label> 
							<input type="text" class="form-control" id="nombreEvento" 
								name="nombre"
								placeholder="Ingrese el nombre" 
								value="<%=  request.getAttribute("nombre") != null ? (String)request.getAttribute("nombre") : "" %>"
								required autocomplete="off"
								oninvalid="this.setCustomValidity('Por favor ingrese el nombre del evento.')"
								oninput="this.setCustomValidity('')">
							<div class="invalid-feedback">
								Por favor ingrese el nombre del evento.
							</div>
						</div>

						<div class="mb-3">
							<label for="siglaEvento" class="form-label">Sigla <span class="text-danger">*</span></label> 
							<input type="text" class="form-control" id="siglaEvento" 
								name="sigla"
								placeholder="Ej: EVT2025" 
								value="<%= request.getAttribute("sigla") != null ? (String)request.getAttribute("sigla") : "" %>"
								required autocomplete="off"
								oninvalid="this.setCustomValidity('Por favor ingrese la sigla del evento.')"
								oninput="this.setCustomValidity('')">
							<div class="invalid-feedback">
								Por favor ingrese la sigla del evento.
							</div>
						</div>

						<div class="mb-3">
							<label for="descripcionEvento" class="form-label">Descripción<span class="text-danger">*</span></label>
							<textarea class="form-control" id="descripcionEvento" 
								name="descripcion"
								required autocomplete="off"
								rows="3" placeholder="Ingrese la descripción del evento" autocomplete="off"
								oninvalid="this.setCustomValidity('Por favor ingrese la descripción del evento.')"
								oninput="this.setCustomValidity('')"><%=  request.getAttribute("descripcion") != null ? (String)request.getAttribute("descripcion") : "" %></textarea>
							<div class="invalid-feedback">
								Por favor ingrese la descripción del evento.
							</div>
						</div>

						<div class="mb-3">
							<label for="categoriasEvento" class="form-label">Categorías <span class="text-danger">*</span></label>
							<select class="form-select" id="categoriasEvento" name="categorias" multiple required oninvalid="this.setCustomValidity('Por favor seleccione al menos una categoría.')" oninput="this.setCustomValidity('')">
								<%
									java.util.Set<String> categorias = (java.util.Set<String>) request.getAttribute("categorias");
									String[] categoriasSeleccionadas = (String[]) request.getAttribute("categoriasSeleccionadas");
									
									java.util.Set<String> seleccionadas = new java.util.HashSet<>();
									if (categoriasSeleccionadas != null) {
										for (String cat : categoriasSeleccionadas) {
											seleccionadas.add(cat);
										}
									}
									
									if (categorias != null) {
										for (String categoria : categorias) {
											boolean selected = seleccionadas.contains(categoria);
								%>
									<option value="<%= categoria %>" <%= selected ? "selected" : "" %>><%= categoria %></option>
								<%
										}
									}
								%>
							</select>
							<div class="invalid-feedback">
								Por favor seleccione al menos una categoría.
							</div> 
							<small class="text-muted">Mantenga presionada la tecla Ctrl (Cmd en Mac) para seleccionar varias.</small>
						</div>

						<div class="mb-3">
							<label for="imagenEvento" class="form-label">Imagen del evento (opcional)</label> 
							<input class="form-control" type="file" id="imagenEvento" name="imagen" accept="image/*">
							<small class="text-muted">Formatos admitidos: JPG, PNG, GIF. Tamaño máximo: 5MB</small>
						</div>

						<div class="mb-3">
							<label for="urlYoutube" class="form-label">URL de YouTube (opcional)</label>
							<input type="url" class="form-control" id="urlYoutube" 
								name="urlYoutube"
								placeholder="https://www.youtube.com/watch?v=..." 
								value="<%= request.getAttribute("urlYoutube") != null ? (String)request.getAttribute("urlYoutube") : "" %>"
								autocomplete="off">
							<small class="text-muted">URL completa del video de YouTube relacionado con el evento</small>
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
   <script>
window.addEventListener("pageshow", function(event) {
  if (event.persisted) {
    // Si la página viene del caché del navegador, recargala
    window.location.reload();
  }
});
</script>
 	

</body>
</html>