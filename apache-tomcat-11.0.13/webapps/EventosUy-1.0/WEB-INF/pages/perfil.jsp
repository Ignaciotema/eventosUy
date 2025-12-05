<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="webservices.DataUsuario" %>
<%@ page import="webservices.DtOrganizador" %>
<%@ page import="webservices.DtAsistente" %>
<!doctype html>
<html lang="es">
<head>
<meta charset="utf-8" />
<meta http-equiv="x-ua-compatible" content="ie=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>MiPerfil - Eventos.uy</title>

<link href="https://cdn.jsdelivr.net/npm/boxicons@latest/css/boxicons.min.css" rel="stylesheet">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Titillium+Web:wght@300;400;600&display=swap" rel="stylesheet">

<link rel="stylesheet" href="assets/css/ConsultaEvento.css">
<link rel="stylesheet" href="assets/css/styles.css">

<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/RolVisitante_listarUsuarios.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Inter&display=swap" />
<link rel="icon" type="image/x-icon" href="<%=request.getContextPath()%>/assets/icons/Logo.png">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">

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
	font-family: system-ui, -apple-system, "Segoe UI", Roboto, "Titillium Web", Arial, sans-serif;
	background: var(--bg);
	color: var(--text);
	margin: 0;
	padding-left: var(--sidebar-w);
	transition: padding-left .2s ease;
}

/* main container spacing */
main.contUser { padding: 18px; }
.container-xl { max-width: 1200px; }
.card { border: 1px solid var(--border); border-radius: var(--radius); box-shadow: var(--shadow); }
.avatar { width: 127px; height: 127px; border-radius: 50%; object-fit: cover; background: #fff; }
.action-card { display: flex; gap: 12px; align-items: center; background: #fcfcff; border: 1px solid var(--border); border-radius: 10px; padding: 12px; text-decoration: none; color: inherit; transition: background .12s ease, transform .06s ease; }
.action-card:hover { background: #f6f7ff; border-color: #dfe3ff; transform: translateY(-1px); }

body.with-collapsed { padding-left: var(--sidebar-w-collapsed); }
@media ( max-width : 767.98px) { body { padding-left: var(--sidebar-w-collapsed); } .avatar { width: 96px; height: 96px; } .col-md-5, .col-md-7 { flex: 0 0 100%; max-width:100%; } }

.text-muted.small { font-size: .85rem; }
</style>
</head>
	<jsp:include page="../templates/header.jsp"/>

	<%
	Object uObj = request.getAttribute("usuario");
	if (uObj == null) {
		// If not set, try session user (defensive)
		DataUsuario su = (DataUsuario) request.getSession().getAttribute("usuario");
		if (su == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}
		uObj = su;
	}

	DtOrganizador org = null;
	DtAsistente asis = null;
	String nickname = null;
	String nombre = null;
	String apellido = null;
	String email = null;
	String descripcion = null;
	String web = null;
	
	if (uObj instanceof DtOrganizador) {
		org = (DtOrganizador) uObj;
		nickname = org.getNickname();
		nombre = org.getNombre();
		email = org.getEmail();
		descripcion = org.getDescripcion();
		web = org.getWeb();
	} else if (uObj instanceof DtAsistente) {
		asis = (DtAsistente) uObj;
		nickname = asis.getNickname();
		nombre = asis.getNombre();
		apellido = asis.getApellido();
		email = asis.getEmail();
	}

	

	String encodedNick = URLEncoder.encode(nickname == null ? "" : nickname, "UTF-8");
	%>

<body id="body-pd">

	<main class="contUser" id="mi-cuenta-user">
		<div class="container-xl">
			<div class="row">
				<div class="col-12 col-md-6 mb-3 mb-lg-0">
					<section class="card mb-3 bg-light">
						<div class="card-body">
							<div class="d-flex align-items-center justify-content-between gap-4">
								<div class="d-flex flex-column align-items-center">
									<div class="contenedor-fotoPerfil mb-2">
										<img class="foto-usuario" src="<%=request.getAttribute("imagenUsuario")%>" alt="fotoPerfil" style="height:127px;">
									</div>
									<div class="contenedor-NickRolUser text-center">
										<div class="nickname"><b><%= nickname %></b></div>
										<div class="rol"><%= (org!=null) ? "Organizador" : "Asistente" %></div>
									</div>
								</div>
								<div class="d-flex flex-column contenedor-datosUsuario">
									<div class="datosUsuario">
										<div class="nombre"><u>Nombre:</u> <%= nombre %></div>
										<% if (asis != null) { %>
											<div class="apellido"><u>Apellido:</u> <%= apellido %></div>
										<% } %>
										<div class="email"><u>Email:</u> <%= email %></div>
										<% if (org != null) { %>
											<div class="fechaNacimiento"><u>Descripción:</u> <%= descripcion %></div>
											<div class="institucion"><u>Web:</u> <a href="<%= web %>" target="_blank"><%= web %></a></div>
										<% } else { %>
											<div class="fechaNacimiento"><u>Fecha de Nacimiento:</u> <%= asis.getFechaNacimiento() %></div>
										<% } %>
										<% if (asis != null && asis.getInstitucion() != null && !asis.getInstitucion().isBlank()) { %>
                                                <div class="institucion">
                                                    <u>Institución:</u> <%= asis.getInstitucion() %>
                                                </div>
                                            <% } %>
                                            
                                        <!-- Información de seguidores y seguidos -->
                                        <div class="seguidores-info mt-3 pt-2" style="border-top: 1px solid var(--border);">
                                            <div class="seguidores mb-1">
                                                <u>Seguidores:</u> <span class="fw-bold"><%= request.getAttribute("cantidadSeguidores") != null ? request.getAttribute("cantidadSeguidores") : 0 %></span>
                                            </div>
                                            <div class="seguidos">
                                                <u>Siguiendo:</u> <span class="fw-bold"><%= request.getAttribute("cantidadSeguidos") != null ? request.getAttribute("cantidadSeguidos") : 0 %></span>
                                            </div>
                                        </div>
									</div>
								</div>
							</div>

							<div class="mt-4">
								<div class="contenedor-ediciones">
									<% if (org != null) { %>
										<div class="mb-2">
											<a class="action-card" href="<%=request.getContextPath()%>/listarEdiciones" data-hotkey="2" role="button">
												<div class="action-icon d-flex align-items-center justify-content-center rounded" style="width:44px;height:44px;border:1px solid var(--border);background:#fff">
													<i class="bi bi-collection-fill"></i>
												</div>
												<div class="flex-fill ms-2">
													<h3 class="mb-0 h6 fw-bold">Monitorear ediciones</h3>
													<p class="mb-0 small text-muted">Permite ver estado de las ediciones y consultar detalles.</p>
												</div>
												<i class="bx bx-right-arrow-alt fs-4 text-secondary"></i>
											</a>
										</div>
									<% } else { %>
										<div class="mt-4">
											<div class="contenedor-ediciones">
												<div class="mb-2">
													<a class="action-card" href="<%=request.getContextPath()%>/listarEdiciones" data-hotkey="2" role="button">
														<div class="action-icon d-flex align-items-center justify-content-center rounded" style="width:44px;height:44px;border:1px solid var(--border);background:#fff">
															<i class="bi bi-collection-fill"></i>
														</div>
														<div class="flex-fill ms-2">
															<h3 class="mb-0 h6 fw-bold">Mis Registros</h3>
															<p class="mb-0 small text-muted">Ver y administrar mis registros en las ediciones.</p>
														</div>
														<i class="bx bx-right-arrow-alt fs-4 text-secondary"></i>
													</a>
												</div>
											</div>
										</div>
									<% } %>
								</div>
							</div>
						</div>
					</section>
				</div>

				<div class="col-12 col-md-6 d-flex flex-column gap-3">
					<section class="card mb-3 bg-light">
						<div class="card-body p-0">
							<ul class="list-unstyled m-0">
								<li class="border-top">
									<a href="<%=request.getContextPath()%>/modificarDatos?usuario=<%=encodedNick%>" class="d-grid gap-2 g-0 text-decoration-none p-3 align-items-center list-link" id="mi-perfil-card">
										<div class="d-flex align-items-center">
											<div class="flex-fill">
												<i class="bi bi-pencil-square"></i><strong> Modificar mi usuario</strong>
												<div class="text-muted small">Ver y editar datos personales, direccion, web y más.</div>
											</div>
											<div class="ms-2 text-secondary" aria-hidden="true">
												<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" viewBox="0 0 16 16"><path d="M6 12.796V3.204L11.481 8 6 12.796zm.659.753 5.48-4.796a1 1 0 0 0 0-1.506L6.66 2.451C6.011 1.885 5 2.345 5 3.204v9.592a1 1 0 0 0 1.659.753z"/></svg>
											</div>
									</div>
									</a>
								</li>
							</ul>
						</div>
					</section>

					<% if (org != null) { %>
					<section class="card mb-3 bg-light">
						<div class="card-body">
							<div class="mb-3"><h2 class="h6 mb-0 fw-bold">Acciones</h2></div>
							<div class="col actions-grid" role="navigation" aria-label="Acciones de perfil">
								<div class="row-4 mb-2">
									<a class="action-card" href="<%=request.getContextPath()%>/altaEvento" data-hotkey="3" role="button">
										<div class="action-icon d-flex align-items-center justify-content-center rounded" style="width:44px;height:44px;border:1px solid var(--border);background:#fff"><i class='bx bx-calendar-plus fs-4' aria-hidden="true"></i></div>
										<div class="flex-fill ms-2"><h3 class="mb-0 h6 fw-bold">Alta evento</h3><p class="mb-0 small text-muted">Crea un nuevo evento con sus datos básicos.</p></div>
										<i class="bx bx-right-arrow-alt fs-4 text-secondary"></i>
									</a>
								</div>
								<!-- New action: Alta institución for organizers -->
								<div class="row-4 mb-2">
									<a class="action-card" href="<%=request.getContextPath()%>/altaInstitucion" data-hotkey="4" role="button">
										<div class="action-icon d-flex align-items-center justify-content-center rounded" style="width:44px;height:44px;border:1px solid var(--border);background:#fff"><i class='bx bx-building fs-4' aria-hidden="true"></i></div>
										<div class="flex-fill ms-2"><h3 class="mb-0 h6 fw-bold">Alta institución</h3><p class="mb-0 small text-muted">Registrar una nueva institución colaboradora.</p></div>
										<i class="bx bx-right-arrow-alt fs-4 text-secondary"></i>
									</a>
								</div>
							</div>
						</div>
					</section>
					<% } %>

				</div>
			</div>
		</div>
	</main>

	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
	<script>
	(function () {
	  var toggle = document.getElementById('header-toggle');
	  var sidebar = document.getElementById('nav-bar-perfil');
	  var body = document.getElementById('body-pd');

	  if (toggle && sidebar) {
	    toggle.addEventListener('click', function(){
	      var isCollapsed = sidebar.classList.toggle('collapsed');
	      document.body.classList.toggle('with-collapsed');
	      toggle.setAttribute('aria-expanded', String(!isCollapsed));
	    });
	  }

	  document.addEventListener('keydown', function(e){
	    if (e && e.key >= '1' && e.key <= '6' && !e.altKey && !e.ctrlKey && !e.metaKey) {
	      var selector = '[data-hotkey="' + e.key + '"]';
	      var el = document.querySelector(selector);
	      if (el) el.click();
	    }
	  });
	}());
	</script>
</body>
</html>