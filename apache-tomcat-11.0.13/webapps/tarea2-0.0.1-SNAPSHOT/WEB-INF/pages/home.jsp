<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="webservices.*" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Eventos.uy</title>
    <link rel="stylesheet" href="assets/css/styles.css">
    <link rel="stylesheet" href="assets/css/index.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Inter&display=swap"/>
    <link rel="icon" type="image/x-icon" href="assets/icons/Logo.png">    <!-- Icono de la pestaña -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>
</head>
<body>
	<jsp:include page="../templates/header.jsp"></jsp:include>

    <div class="hero-actions">
        <div class="text-content-title">
            <b class="title">Eventos.uy</b>
            <div class="subtitle">Donde tus ideas se vuelven experiencias.</div>
        </div>
    </div>

    <img class="section-icon" alt="" src="assets/images/section-image.jpg">
    
    <div class="row row-cols-2 row-cols-md-3 w-100 justify-content-center mt-4 dflex">

        <div class="carta-categorias align-items-center col-12 col-md-8 col-xl-3 h-100 my-3">
            <h3 class="categorias fw-bold">Categorías</h3>
            <jsp:include page="../templates/categorias-sidebar.jsp"></jsp:include>
            <div class="d-flex justify-content-center mt-3">
                <a href="<%= request.getContextPath() %>/listarUsuarios"
                   class="btn btn-dark rounded-pill px-4 py-2 d-flex align-items-center gap-2 text-white"
                   style="font-weight: 500;"> 
                    <i class="bi bi-people" style="font-size: 1.2rem; color: #fff;"></i>
                    <span class="text-white">Usuarios</span>
                </a>
            </div>
        </div>

<div class="container col-12 col-md-8">
   <div class="row container m-2">
   		
            <div class="row gx-0 g-xl-2 gy-2">
                
			<% 
			List<DtDetalleEvento> eventos = (List<DtDetalleEvento>) request.getAttribute("eventos_recientes");
			for (DtDetalleEvento evento : eventos) { %>
				<div class="col-xl-6">
				<a class="text-decoration-none" href="detalleEvento?nombre=<%= evento.getNombre() %>"
					data-hotkey="5" role="button">
					<div class="carta p-4">
						<div class="text-heading">
							<div class="search-text-heading"><%= evento.getNombre() %></div>
						</div>
						<div class="avatar-block">
							<div class="avatar">
								<img class="shape-icon" alt=""
									src="<%= request.getAttribute(evento.getNombre()) %>">
							</div>
							<div class="info">
								<div class="description"> <%= evento.getDescripcion() %> </div>
							</div>
						</div>
					</div>
				</a>
			</div>
			<%
			}
			
			%>

                

            </div>
            


        </div>
    </div>
</div>

    
   <div class="modal fade" id="mensajeModal" tabindex="-1" aria-labelledby="mensajeModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header" style="display: flex !important; width: 100% !important;">
			    <h5 class="modal-title" id="mensajeModalLabel" style="flex-grow: 1 !important; margin: 0 !important;">Mensaje</h5>
			    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" style="flex-shrink: 0 !important; margin-left: auto !important;"></button>
			</div>
            <div class="modal-body" id="mensajeModalBody"></div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
            </div>
        </div>
     </div>
	</div>


	<script src="assets/js/main.js"></script>
	<script>
	    (function() {
	        var mensaje = '<%= request.getAttribute("mensaje") %>';
	        if (mensaje && mensaje.length > 0 && mensaje !== 'null') {
	            var body = document.getElementById('mensajeModalBody');
	            if (body) body.textContent = mensaje;
	            var modalEl = document.getElementById('mensajeModal');
	            if (modalEl) {
	                var modal = new bootstrap.Modal(modalEl);
	                modal.show();
	            } else {
	                alert(mensaje);
	            }
	        }
	    })();
	</script>
	
	</body>
</html>
</body>
</html>