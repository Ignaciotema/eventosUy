<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="webservices.*" %>

<%
  String ctx = request.getContextPath();
  DtDetalleEdicion ed = (DtDetalleEdicion) request.getAttribute("edicion");
  @SuppressWarnings("unchecked")
  Set<DtTipoRegistro> tiposRegistro = (Set<DtTipoRegistro>) request.getAttribute("tiposRegistro");

  String error   = (String) request.getAttribute("error");
  String mensaje = (String) request.getAttribute("mensaje");

  String edicionStr = request.getParameter("edicion");
  if (edicionStr == null || edicionStr.isBlank()) {
      Object eStr = request.getAttribute("edicionStr");
      edicionStr = (eStr == null ? (ed != null ? ed.getNombre() : "") : String.valueOf(eStr));
  }

  String tipoRegistroSel = String.valueOf(request.getAttribute("tipoRegistroSel"));
  if (tipoRegistroSel == null || "null".equals(tipoRegistroSel)) tipoRegistroSel = "";

  String formaRegistroSel = String.valueOf(request.getAttribute("formaRegistroSel"));
  if (formaRegistroSel == null || "null".equals(formaRegistroSel)) formaRegistroSel = "general";

  String codigoPatVal = String.valueOf(request.getAttribute("codigoPatrocinioVal"));
  if (codigoPatVal == null || "null".equals(codigoPatVal)) codigoPatVal = "";

  String imagenEdicion = (String) request.getAttribute("imagenEdicion");
  if (imagenEdicion == null || imagenEdicion.isBlank()) imagenEdicion = "uploads/ediciones/default.jpg";

  String nombreEvento = (String) request.getAttribute("nombreEvento");
  if (nombreEvento == null) nombreEvento = "";

  String imagenEvento = (String) request.getAttribute("imagenEvento");
  if (imagenEvento == null || imagenEvento.isBlank()) imagenEvento = "uploads/eventos/default.jpg";

  Boolean yaRegistrado = (Boolean) request.getAttribute("yaRegistrado");
  if (yaRegistrado == null) yaRegistrado = false;
%>

<!doctype html>
<html lang="es">
<head>
<meta charset="utf-8" />
<meta http-equiv="x-ua-compatible" content="ie=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>Registro a Edición — Eventos.uy</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/boxicons@latest/css/boxicons.min.css" rel="stylesheet">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Titillium+Web:wght@300;400;600&display=swap" rel="stylesheet">
<link rel="stylesheet" href="<%= ctx %>/assets/css/ConsultaEvento.css">
<link rel="stylesheet" href="<%= ctx %>/assets/css/styles.css">
<style>
:root { --bg:#f4f6f8; --surface:#fff; --text:#1f2937; --muted:#6b7280; --border:#e6e8eb; --accent:#6d5dfc; --accent-weak:#edeaff; --radius:8px; --shadow:0 6px 18px rgba(15,23,42,.06); }
body { font-family: "Titillium Web", system-ui, -apple-system, "Segoe UI", Roboto, Arial, sans-serif; background:var(--bg); color:var(--text); margin:0;}
main.contUser { padding:18px; }
.container-xl { max-width:980px; }
.card { border:1px solid var(--border); border-radius:var(--radius); box-shadow:var(--shadow); }
.form-label { font-weight:600; }
</style>
</head>

<body id="body-pd">

<jsp:include page="/WEB-INF/templates/header.jsp"></jsp:include>

<main class="contUser">
  <div class="container-xl">

    <section class="card mb-3">
      <div class="card-body d-flex align-items-center gap-3">
      <% String back =  ed != null ? "detalleEdicion?nombre=" + ed.getNombre() : "HomeServlet"; %>
        <a href="<%= back  %>" class="btn btn-light d-flex align-items-center justify-content-center p-2"
           style="width:40px; height:40px; border-radius:50%;">
          <i class='bx bx-chevron-left fs-4'></i>
        </a>
        <i class="bx bx-user-plus fs-3"></i>
        <h2 class="mb-0 fw-bold">Registro a Edición</h2>
      </div>
    </section>

    <% if (error != null) { %>
      <div class="alert alert-danger"><%= error %></div>
    <% } %>
    <% if (mensaje != null) { %>
      <div class="alert alert-success"><%= mensaje %></div>
    <% } %>
    <% if (yaRegistrado) { %>
      <div class="alert alert-warning">Ya estás registrado en esta edición.</div>
    <% } %>

    <section class="card mb-3">
      <div class="card-body">
        <form id="registroEdicionForm" method="post" action="<%= ctx %>/altaRegistro">
          <input type="hidden" name="edicion" value="<%= edicionStr %>">

          <!-- Edición + imagen -->
          <div class="mb-3 d-flex flex-column align-items-center justify-content-center">
            <label class="form-label">
              <%= (ed != null ? ed.getNombre() : edicionStr) %>
              <% if (nombreEvento != null && !nombreEvento.isBlank()) { %>
                — <span class="text-muted"><%= nombreEvento %></span>
              <% } %>
            </label>
            <img src="<%= imagenEdicion %>" alt="Imagen Edición"
                 class="img-fluid rounded"
                 style="width:120px; height:120px; object-fit:cover;">
          </div>

          <!-- Tipo de registro -->
          <div class="mb-3">
            <label for="tipoRegistroSelect" class="form-label">Tipo de registro</label>
            <select class="form-select" id="tipoRegistroSelect" name="tipoRegistro" required>
              <option value="">-- Seleccione un tipo de registro --</option>
              <% if (tiposRegistro != null) {
                   for (DtTipoRegistro tr : tiposRegistro) {
                      String nombreTr = "";
                      float  costoTr  = 0f;
                      try { nombreTr = (String) tr.getClass().getMethod("getNombre").invoke(tr); } catch (java.lang.Exception ignore) {}
                      try { Object c = tr.getClass().getMethod("getCosto").invoke(tr);
                            if (c instanceof Float f) costoTr = f;
                            if (c instanceof Double d) costoTr = d.floatValue();
                      } catch (java.lang.Exception ignore) {}
                      boolean sel = nombreTr.equals(tipoRegistroSel);
              %>
                <option value="<%= nombreTr %>" <%= sel ? "selected" : "" %>>
                  <%= nombreTr %> <% if (costoTr > 0) { %> — $<%= costoTr %> <% } %>
                </option>
              <% }} %>
            </select>
          </div>

          <!-- Forma de registro -->
          <div class="mb-3">
            <label class="form-label">Forma de registro</label>
            <div class="form-check">
              <input class="form-check-input" type="radio"
                     name="formaRegistro" id="formaGeneral" value="general"
                     <%= ("patrocinio".equalsIgnoreCase(formaRegistroSel) ? "" : "checked") %>>
              <label class="form-check-label" for="formaGeneral">General</label>
            </div>
            <div class="form-check">
              <input class="form-check-input" type="radio"
                     name="formaRegistro" id="formaPatrocinio" value="patrocinio"
                     <%= ("patrocinio".equalsIgnoreCase(formaRegistroSel) ? "checked" : "") %>>
              <label class="form-check-label" for="formaPatrocinio">Usando código de patrocinio</label>
            </div>
          </div>

          <!-- Código de patrocinio -->
          <div class="mb-3" id="codigoPatrocinioDiv"
               style="display: <%= ("patrocinio".equalsIgnoreCase(formaRegistroSel) ? "block" : "none") %>;">
            <label for="codigoPatrocinio" class="form-label">Código de patrocinio</label>
            <input type="text" class="form-control" id="codigoPatrocinio"
                   name="codigoPatrocinio" placeholder="Ingrese el código"
                   value="<%= codigoPatVal %>">
          </div>

          <div class="d-flex gap-2">
            <button type="submit" class="btn btn-primary" <%= yaRegistrado ? "disabled" : "" %>>
              Registrar
            </button>
            <a href="<%= back  %>" class="btn btn-secondary">Cancelar</a>
          </div>
        </form>
      </div>
    </section>

  </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
  // Toggle campo patrocinio
  document.querySelectorAll('input[name="formaRegistro"]').forEach(el => {
    el.addEventListener('change', function() {
      document.getElementById('codigoPatrocinioDiv').style.display =
        this.value === 'patrocinio' ? 'block' : 'none';
    });
  });
</script>
</body>
</html>
