<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="webservices.DataUsuario" %>
<%@ page import="webservices.DtOrganizador" %>
<%@ page import="webservices.DtAsistente" %>
<%@ page import="webservices.TipoUsuario" %>

<%@ page import="java.time.LocalDate" %>

<%
  String ctx = request.getContextPath();

  DataUsuario du = (DataUsuario) request.getAttribute("usuario");

  if (du == null) {
      String nickParam = request.getParameter("usuario");
      if (nickParam == null || nickParam.isBlank()) {
          response.sendRedirect(ctx + "/listarUsuarios?action=listar");
          return;
      } else {
          request.setAttribute("error", "No se pudo cargar el usuario solicitado.");
      }
  }

  String error   = (String) request.getAttribute("error");
  String mensaje = (String) request.getAttribute("mensaje");

  String nombreAttr      = (String) request.getAttribute("nombre");
  String apellidoAttr    = (String) request.getAttribute("apellido");
  String fechaNacAttr    = (String) request.getAttribute("fechaNac");
  String descripcionAttr = (String) request.getAttribute("descripcion");
  String webAttr         = (String) request.getAttribute("web");

  String nick  = (du != null ? du.getNickname() : "");
  String email = (du != null ? du.getEmail()    : "");

  String nombreVal = "";
  String apellidoVal = "";
  String fechaNacVal = "";
  String descripcionVal = "";
  String webVal = "";

  if (du != null && du.getTipo() == TipoUsuario.ASISTENTE) {
	    DtAsistente as = (DtAsistente) request.getAttribute("detalleUsuario");
	    nombreVal   = (nombreAttr != null) ? nombreAttr : (as != null ? as.getNombre() : du.getNombre());
	    apellidoVal = (apellidoAttr != null) ? apellidoAttr : (as != null ? as.getApellido() : "");
	    if (fechaNacAttr != null) {
	        fechaNacVal = fechaNacAttr;
	    } else if (as != null && as.getFechaNacimiento() != null) {
	        fechaNacVal = as.getFechaNacimiento().toString();
	    }
	} else if (du != null && du.getTipo() == TipoUsuario.ORGANIZADOR) {
	    DtOrganizador org = (DtOrganizador) request.getAttribute("detalleUsuario");
	    nombreVal      = (nombreAttr != null)      ? nombreAttr      : (org != null ? org.getNombre()      : du.getNombre());
	    descripcionVal = (descripcionAttr != null) ? descripcionAttr : (org != null ? org.getDescripcion() : "");
	    webVal         = (webAttr != null)         ? webAttr         : (org != null ? org.getWeb()         : "");
	}
%>
<!doctype html>
<html lang="es">
<head>
<meta charset="utf-8" />
<meta http-equiv="x-ua-compatible" content="ie=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Modificar Perfil — Eventos.uy</title>

<link rel="stylesheet" href="<%= ctx %>/assets/css/styles.css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/boxicons@latest/css/boxicons.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Inter&display=swap" />
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Titillium+Web:wght@300;400;600&display=swap" rel="stylesheet">
<link rel="stylesheet" href="<%= ctx %>/assets/css/ConsultaEvento.css">

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
body { background: var(--bg); color: var(--text); margin: 0; }
main.contUser { padding: 18px; }
.container-xl { max-width: 980px; }
.card { border: 1px solid var(--border); border-radius: var(--radius); box-shadow: var(--shadow); }
.form-label { font-weight: 600; }
.avatar-preview { width: 80px; height: 80px; border-radius: 50%; object-fit: cover; border: 2px solid var(--accent-weak); }
</style>
</head>

<body id="body-pd">
<jsp:include page="/WEB-INF/templates/header.jsp" />
<main class="contUser">
  <div class="container-xl">

    <!-- Header del formulario -->
    <section class="card mb-3">
      <div class="card-body d-flex align-items-center gap-3">
        <a href="<%= ctx %>/perfil" class="btn btn-light d-flex align-items-center justify-content-center p-2" style="width:40px;height:40px;border-radius:50%;">
          <i class='bx bx-chevron-left fs-4'></i>
        </a>
        <i class="bx bx-user fs-3"></i>
        <h2 class="mb-0 fw-bold">Modificar Perfil</h2>
      </div>
    </section>

    <% if (error != null) { %>
      <div class="alert alert-danger"><%= error %></div>
    <% } %>
    <% if (mensaje != null) { %>
      <div class="alert alert-success"><%= mensaje %></div>
    <% } %>

    <!-- Formulario de edición -->
    <section class="card mb-3">
      <div class="card-body">
        <form id="modificarPerfilForm" method="post" action="<%= ctx %>/modificarDatos" enctype="multipart/form-data">
		  <input type="hidden" name="action" value="modificarDatos">
		  <input type="hidden" name="usuario" value="<%= nick %>">

          <div class="mb-3 text-center">
			  <img id="avatarPreview"
			       src="<%=request.getAttribute("imagenUsuario")%>"
			       alt="Avatar" class="avatar-preview mb-2">
			  <input class="form-control" type="file" id="avatarInput" name="avatar" accept="image/*">
		  </div>

          <% if (du != null && du.getTipo() == TipoUsuario.ASISTENTE) { %>
            <!-- ASISTENTE -->
            <div class="row">
              <div class="col-md-6 mb-3">
                <label for="nombre" class="form-label">Nombre</label>
                <input type="text" class="form-control" id="nombre" name="nombre" value="<%= (nombreVal==null?"":nombreVal) %>" required>
              </div>
              <div class="col-md-6 mb-3">
                <label for="apellido" class="form-label">Apellido</label>
                <input type="text" class="form-control" id="apellido" name="apellido" value="<%= (apellidoVal==null?"":apellidoVal) %>" required>
              </div>
            </div>

            <div class="mb-3">
              <label for="fechaNac" class="form-label">Fecha de nacimiento</label>
              <input type="date" class="form-control" id="fechaNac" name="fechaNac" value="<%= (fechaNacVal==null?"":fechaNacVal) %>" required>
            </div>

          <% } else if (du != null && du.getTipo() == TipoUsuario.ORGANIZADOR) { %>
            <!-- ORGANIZADOR -->
            <div class="mb-3">
              <label for="nombre" class="form-label">Nombre</label>
              <input type="text" class="form-control" id="nombre" name="nombre" value="<%= (nombreVal==null?"":nombreVal) %>" required>
            </div>

            <div class="mb-3">
              <label for="descripcion" class="form-label">Descripción</label>
              <textarea class="form-control" id="descripcion" name="descripcion" rows="3" required><%= (descripcionVal==null?"":descripcionVal) %></textarea>
            </div>

            <div class="mb-3">
              <label for="web" class="form-label">Sitio web</label>
              <input type="url" class="form-control" id="web" name="web" placeholder="https://" value="<%= (webVal==null?"":webVal) %>">
            </div>
          <% } %>

          <div class="mb-3">
            <label class="form-label">Nickname</label>
            <input type="text" class="form-control" value="<%= nick %>" disabled>
          </div>

          <div class="mb-3">
            <label class="form-label">Correo electrónico</label>
            <input type="email" class="form-control" value="<%= email %>" disabled>
          </div>

          <div class="d-flex gap-2">
            <button type="submit" class="btn btn-primary">Guardar</button>
            <a href="<%= ctx %>/" class="btn btn-secondary">Cancelar</a>
          </div>
        </form>
      </div>
    </section>

  </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
  // Preview de avatar (solo visual)
  const avatarInput = document.getElementById('avatarInput');
  const avatarPreview = document.getElementById('avatarPreview');
  if (avatarInput) {
    avatarInput.addEventListener('change', (e) => {
      const file = e.target.files[0];
      if (file) avatarPreview.src = URL.createObjectURL(file);
    });
  }
</script>
</body>
</html>