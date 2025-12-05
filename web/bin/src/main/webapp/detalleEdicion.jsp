<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Consulta Edición - Eventos.uy</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet" href="../assets/css/styles.css">
<link rel="stylesheet" href="../assets/css/index.css">
<link rel="stylesheet" href="../assets/css/consultaEvento.css">
<link rel="stylesheet" href="../assets/css/consultaEdicion.css">
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css2?family=Inter&display=swap" />
<link rel="icon" type="image/x-icon" href="../assets/icons/Logo.png">
</head>

<body>

	<header>
		<nav class="navbar bg-white shadow-sm" style="height: 86px;">
			<div class="d-flex align-items-center">
				<a class="fw-bold text-dark fs-2 ms-4 text-decoration-none"
					href="index.html">Eventos.uy</a>
			</div>
			<div class="header-auth m-3">
				<a href="InicioDeSesion.html" class="text-decoration-none">
					<button type="button" class="button1 rounded-3">
						<div class="header-button">Iniciar sesión</div>
					</button>
				</a> <a href="RegistroBase.html" class="text-decoration-none">
					<button type="button" class="button2 rounded-3">
						<div class="header-button">Regístrarse</div>
					</button>
				</a>
			</div>
		</nav>
	</header>

	<!-- Columna derecha: categorías (opcional) -->

	<div class="container-fluid px-4 mt-4">
		<div class="carta-de-eventos mb-4 d-flex justify-content-center">
			<input type="search" class="busq" placeholder="Buscar ediciones...">
		</div>
	</div>
	<div class="container-fluid px-4 mt-3">
		<div class="row pb-4">
			<!-- Columna derecha: categorías estilo index -->
			<div class="col-md-3 col-lg-2 col-xl-2 mt-3 mt-md-0">
				<div class="carta-categorias align-items-center h-100">
					<h3 class="categorias fw-bold">Categorías</h3>
					<div class="form-check mb-2 d-flex justify-content-center">
						<input class="form-check-input" type="checkbox"
							id="checkboxEdiciones"> <label
							class="form-check-label ms-2" for="checkboxEdiciones">Ver
							ediciones</label>
					</div>
					<li class="list-group w-100 overflow-auto text-center" multiple>
						<a href="#" value="tecnologia"
						class="list-group-item categoria-link">Tecnología</a> <a href="#"
						value="innovacion" class="list-group-item categoria-link">Innovación</a>
						<a href="#" value="deporte" class="list-group-item categoria-link">Deporte</a>
						<a href="#" value="salud" class="list-group-item categoria-link">Salud</a>
					</li>
				</div>
				<!-- ...existing code... -->
				<script>
  // Redirige según el estado del checkbox al hacer click en una categoría
  document.addEventListener('DOMContentLoaded', function() {
    const checkboxEdiciones = document.getElementById('checkboxEdiciones');
    const categoriaLinks = document.querySelectorAll('.categoria-link');
    categoriaLinks.forEach(link => {
      link.addEventListener('click', function(e) {
        e.preventDefault();
        if (checkboxEdiciones.checked) {
          window.location.href = 'ListaEdiciones.html';
        } else {
          window.location.href = 'ListarEventos.html';
        }
      });
    });
  });
</script>
			</div>
			<!-- Imagen de la edición -->
			<div class="col-md-4 col-lg-3 col-xl-2 px-5">
				<img src="../assets/images/IMG-EDEV03.jpeg"
					alt="Maratón Montevideo 2024" class="img-fluid rounded"
					style="width: 180px; height: 180px; object-fit: cover; aspect-ratio: 1/1;">
			</div>
			<!-- Información principal de la edición -->
			<div class="col-md-5 col-lg-7 col-xl-8 mt-3 mt-md-0 info-evento">
				<h1 class="mb-3">Maratón Montevideo 2024</h1>
				<div class="mb-2 d-flex align-items-center">
					<strong class="me-2">Organizador:</strong> <a
						href="../pagesVisitante/miseventos_ConsultaUsuario_vistaExterna.html"
						class="d-flex align-items-center text-decoration-none"> <img
						src="../assets/images/IMG-US04.jpeg" alt="Miseventos"
						class="rounded-circle me-2"
						style="width: 32px; height: 32px; object-fit: cover; border: 1px solid rgba(0, 0, 0, .06);">
						<span class="fw-bold text-dark">Miseventos</span>
					</a>
				</div>
				<div class="mb-2 d-flex align-items-center">
					<strong class="me-2">Sigla:</strong> <span
						class="fw-bold text-dark">MARATON24</span>
				</div>
				<p class="mb-2">
					<strong>Fecha inicio:</strong> 14/09/2024
				</p>
				<p class="mb-2">
					<strong>Fecha fin:</strong> 14/09/2024
				</p>
				<p class="mb-2">
					<strong>Fecha alta:</strong> 21/04/2024
				</p>
				<p class="mb-2">
					<strong>País:</strong> Uruguay
				</p>
				<p class="mb-2">
					<strong>Ciudad:</strong> Montevideo
				</p>
				<p class="mb-2">
					<strong>Categorías:</strong> Deporte, Salud
				</p>
				<!-- Tipos de registros -->
				<div class="mt-4">
					<div class="d-flex align-items-baseline gap-2">
						<h5>Tipos de Registro</h5>
					</div>
					<div class="accordion" id="accordionTiposRegistro">
						<div class="accordion-item">
							<h2 class="accordion-header" id="heading21k">
								<button class="accordion-button collapsed" type="button"
									data-bs-toggle="collapse" data-bs-target="#collapse21k"
									aria-expanded="false" aria-controls="collapse21k">
									Corredor 21K</button>
							</h2>
							<div id="collapse21k" class="accordion-collapse collapse"
								aria-labelledby="heading21k"
								data-bs-parent="#accordionTiposRegistro">
								<div class="accordion-body registro-info">
									<p>
										<strong>Costo:</strong> $500
									</p>
									<p>
										<strong>Cupo:</strong> 500
									</p>
									<p>
										<strong>Descripción:</strong> Inscripción a la media maratón.
									</p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- Patrocinios -->
				<div class="mt-4">
					<div class="d-flex align-items-baseline gap-2">
						<h5>Patrocinadores</h5>
					</div>
					<div class="alert alert-secondary text-center mb-0" role="alert">
						No hay patrocinadores registrados para esta edición.</div>
				</div>


			</div>
		</div>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
