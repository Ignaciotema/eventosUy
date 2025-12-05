<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="webservices.*"%>
<%
  String ctx = request.getContextPath();
  DtRegistro reg = (DtRegistro) request.getAttribute("registro");
  if (reg == null) { %>
  <h2>No hay datos de registro para mostrar.</h2>
<%  return; }

  String imgUsuario  = (String) request.getAttribute("imagenUsuario");
  String imgEdicion  = (String) request.getAttribute("imagenEdicion");
  if (imgUsuario == null || imgUsuario.isBlank()) imgUsuario = "uploads/usuarios/default.jpg";
  if (imgEdicion == null || imgEdicion.isBlank()) imgEdicion = "uploads/ediciones/default.jpg";

  String nickUsuario   = (String) request.getAttribute("usuario");
  String nombreEdicion = reg.getNombreEdicion();
  String tipoReg       = reg.getTipoRegistro();
  boolean asistencia   = reg.isAsistencia();
%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover">
<title>Detalle de Registro - Eventos.uy</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/styles.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/detalleregistro.css">
<link rel="icon" type="image/x-icon" href="<%= request.getContextPath() %>/assets/icons/Logo.png">

<style>
  body { font-size: 1.05rem; } /* sube todo un poco */
  .dr-title { font-size: 2rem; font-weight: 800; letter-spacing: .2px; } /* grande en mobile */
  .dr-muted { color: #6c757d; } /* similar a .text-muted, pero controlable */

  .dr-list .list-group-item {
    padding: 1rem 1.1rem;
    font-size: 1.15rem;   /* texto grande en ítems */
    line-height: 1.25;
  }

  .badge-xl {
    font-size: 1.15rem;
    padding: .65rem 1rem;
    border-radius: .75rem;
  }

  .btn-block { width: 100%; }

  .dr-avatars img {
    width: 150px; height: 150px; object-fit: cover;
  }

  .detalle-registro-card { border-radius: 1rem; }
  .detalle-registro-card .card-body { padding: 1.25rem; }

  @media (min-width: 768px) {
    body { font-size: 1.05rem; }          /* mantenemos tamaño */
    .dr-title { font-size: 2.25rem; }     /* un toque más grande en md+ */
    .dr-avatars img { width: 170px; height: 170px; }
    .detalle-registro-card .card-body { padding: 2rem; }
  }
</style>
</head>
<body>
  <jsp:include page="/WEB-INF/templates/header.jsp" />

  <div class="container d-flex flex-column align-items-stretch justify-content-center py-4">
    <div class="card shadow detalle-registro-card mx-auto" style="max-width: 820px;">
      <div class="card-body">
        <div class="dr-avatars row g-3 align-items-center justify-content-center mb-3 mb-md-4">
          <div class="col-12 col-md-auto d-flex justify-content-center">
            <img src="<%= imgUsuario %>" alt="Foto Usuario"
                 class="img-fluid rounded-circle shadow-sm">
          </div>
          <div class="col-12 col-md-auto d-flex justify-content-center">
            <img src="<%= imgEdicion %>" alt="Foto Edición"
                 class="img-fluid rounded shadow-sm">
          </div>
        </div>

        <div class="info-evento text-center"
             data-usuario="<%= nickUsuario %>"
             data-edicion="<%= (nombreEdicion == null ? "" : nombreEdicion) %>">

          <h1 class="dr-title mb-3">Detalle de Registro</h1>

          <!-- Datos -->
          <ul class="list-group dr-list text-start mx-auto mb-3" style="max-width: 640px;">
            <li class="list-group-item d-flex justify-content-between align-items-center">
              <span class="dr-muted">Usuario</span>
              <strong class="ms-3"><%= nickUsuario %></strong>
            </li>
            <li class="list-group-item d-flex justify-content-between align-items-center">
              <span class="dr-muted">Edición</span>
              <strong class="ms-3"><%= (nombreEdicion == null ? "" : nombreEdicion) %></strong>
            </li>
            <li class="list-group-item d-flex justify-content-between align-items-center">
              <span class="dr-muted">Tipo de registro</span>
              <strong class="ms-3"><%= (nombreEdicion == null ? "" : tipoReg) %></strong>
            </li>
            <li class="list-group-item d-flex justify-content-between align-items-center">
              <span class="dr-muted">Fecha de registro</span>
              <strong class="ms-3"><%= reg.getFechaRegistro() %></strong>
            </li>
            <li class="list-group-item d-flex justify-content-between align-items-center">
              <span class="dr-muted">Costo</span>
              <strong class="ms-3">$<%= reg.getCosto() %></strong>
            </li>
          </ul>

          <!-- botncito para confirmra la asistencia -->
          <div class="mt-2">
            <div class="d-flex flex-column flex-sm-row align-items-center justify-content-center gap-3">

              <div class="w-100 w-sm-auto">
				  <% if (asistencia) { %>
				    <a class="btn btn-outline-primary btn-lg btn-block"
				       id="btnDescargarAsistencia"
				       href="<%= ctx %>/descargar-asistencia?usuario=<%= nickUsuario %>&edicion=<%= nombreEdicion %>"
				       target="_blank" rel="noopener">
				      Descargar asistencia
				    </a>
				  <% } %>
				</div>

            </div>
          </div>
        </div>

      </div>
    </div>
  </div>


  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
  <script>
(function(){
  const container = document.querySelector('.info-evento');
  const usuario = container?.dataset?.usuario || '';
  const edicion = container?.dataset?.edicion || '';

  const btnDownload = document.getElementById('btnDescargarAsistencia');
  const badge = document.getElementById('asistenciaEstado');

  const btnOk   = document.getElementById('btnModalConfirmar');


  if (btnOk) {
    btnOk.addEventListener('click', async () => {
      try {
        const body = new URLSearchParams({ usuario, edicion });
        const resp = await fetch('<%= ctx %>/confirmar-asistencia', {
          method: 'POST',
          headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' },
          body
        });

        const data = await resp.json();
        if (!resp.ok || !data.ok) throw new Error(data.error || 'Error desconocido');

        if (badge) {
          badge.textContent = 'Confirmada';
          badge.className = 'badge badge-xl bg-success';
        }
        if (modal) modal.hide();

        if (btnConfirm) {
        	  const parent = btnConfirm.parentElement;
        	  const a = document.createElement('a');
        	  a.id = 'btnDescargarAsistencia';
        	  a.className = 'btn btn-outline-primary btn-lg btn-block';
        	  a.textContent = 'Descargar asistencia';
        	  a.href = '<%= ctx %>/descargar-asistencia?usuario=' + encodeURIComponent(usuario) +
        	           '&edicion=' + encodeURIComponent(edicion);
        	  a.target = '_blank';
        	  a.rel = 'noopener';
        	  parent.replaceChild(a, btnConfirm);
        	}

      } catch (e) {
        alert('No se pudo confirmar la asistencia: ' + (e.message || e));
      }
    });
  }
})();
</script>

</body>
</html>
