<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="webservices.*" %>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Inicio de Sesión - Eventos.uy</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css2?family=Inter&display=swap" />
<link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/icons/Logo.png">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/ConsultaEvento.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
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
</head>

<jsp:include page="../templates/header.jsp"></jsp:include>

<body>
	<div class="container d-flex justify-content-center">
		<div class="col-md-5 col-lg-7 col-13">
			<div class="col-md-5 col-lg-7 col-xxl-9 mt-3 mt-sm-3 mt-md-0 inicioSesion text-center">
				<h1 class="mb-5">
					<strong></strong>
				</h1>
				<h3 class="mb-4">
					<strong>Inicio de Sesión</strong>
				</h3>

				<!-- Mostrar mensaje de error si existe -->
				<%
				String error = (String) request.getAttribute("error");
				String registered = request.getParameter("registered");
				if ("true".equals(registered)) {
				%>
					<div class="alert alert-success" role="alert">
						<i class="bx bx-check-circle me-2"></i>
						Registro exitoso. Por favor inicie sesión.
					</div>
				<%
				}
				if (error != null) {
				%>
					<div class="alert alert-danger" role="alert">
						<i class="bx bx-error-circle me-2"></i>
						<%= error %>
					</div>
				<%
				}
				%>

				<p class="mb-3"></p>
				<form method="post" action="${pageContext.request.contextPath}/iniciosesion" class="needs-validation" novalidate autocomplete="off">
					<div class="mb-3">
						<input type="text" class="form-control" id="nickname" name="nickname"
							placeholder="Apodo (nickname) o mail" 
							value="<%= request.getAttribute("nickname") != null ? request.getAttribute("nickname") : "" %>"
							required autocomplete="off">
						<div class="invalid-feedback">
							Por favor ingrese su nickname o email.
						</div>
					</div>
					<div class="mb-3">
						<input type="password" class="form-control" id="password" name="password"
							placeholder="Contraseña" required autocomplete="off">
						<div class="invalid-feedback">
							Por favor ingrese su contraseña.
						</div>
					</div>

					<button type="submit" class="btn btn-dark">Ingresar</button>
				</form>

				<div class="mt-3">
					<p>¿No tienes cuenta? <a href="${pageContext.request.contextPath}/registro">Regístrate aquí</a></p>
				</div>
			</div>
		</div>
	</div>

	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
	<script>
		// Reload the page if it was restored from the browser cache (bfcache)
		window.addEventListener("pageshow", function(event) {
			if (event.persisted) {
				window.location.reload();
			}
		});
	</script>
</body>
</html>