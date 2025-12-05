<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="webservices.*" %>

<%
  String ctx = request.getContextPath();
  String edicion = (String) request.getAttribute("edicion");
  String q = (String) request.getAttribute("q");
  if (q == null) q = "";
  @SuppressWarnings("unchecked")
  List<Map.Entry<String, DtRegistro>> registros =
      (List<Map.Entry<String, DtRegistro>>) request.getAttribute("registros");
  String mensaje = (String) request.getAttribute("mensaje");
  if (registros == null) registros = java.util.Collections.emptyList();

  @SuppressWarnings("unchecked")
  Map<String,String> imgsUsuarios = (Map<String,String>) request.getAttribute("imgsUsuarios");
  if (imgsUsuarios == null) imgsUsuarios = java.util.Collections.emptyMap();
%>

<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Listar registros</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/RolVisitante_listarUsuarios.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/styles.css">
<link rel="icon" type="image/x-icon" href="<%= request.getContextPath() %>/assets/icons/Logo.png">
</head>

<body>
<jsp:include page="/WEB-INF/templates/header.jsp" />

<div class="container my-4">
  <div class="d-flex flex-wrap align-items-center gap-3 mb-3">
    <h2 class="mb-0">Registros a edición: <%= (edicion != null ? edicion : "") %></h2>

    <!-- Barra de búsqueda -->
    <form class="ms-auto" method="get" action="<%= request.getContextPath() %>/listar-registros" role="search">
      <input type="hidden" name="edicion" value="<%= edicion == null ? "" : edicion %>"/>
      <div class="input-group">
        <input type="search"
               class="form-control"
               name="q"
               placeholder="Buscar por nickname..."
               value="<%= q %>">
        <button class="btn btn-outline-secondary" type="submit">Buscar</button>
        <% if (q != null && !q.isBlank()) { %>
          <a class="btn btn-outline-secondary" href="<%= request.getContextPath() %>/listar-registros?edicion=<%= URLEncoder.encode(edicion == null ? "" : edicion, "UTF-8") %>">Limpiar</a>
        <% } %>
      </div>
    </form>
  </div>

  <% if (registros.isEmpty()) { %>
    <div class="alert alert-info">No hay registros para esta edición.</div>
  <% } else { %>
    <div class="listadoUsarios">
      <% for (Map.Entry<String, DtRegistro> e : registros) {
           String nick = e.getKey();
           DtRegistro r = e.getValue();
           String verDetalleUrl = ctx + "/ver-registro"
               + "?edicion=" + URLEncoder.encode(edicion == null ? "" : edicion, "UTF-8")
               + "&usuario=" + URLEncoder.encode(nick == null ? "" : nick, "UTF-8");

           String imgRel = imgsUsuarios.get(nick);
           if (imgRel == null || imgRel.isBlank()) imgRel = "uploads/usuarios/default.jpg";
      %>
        <div class="listadoUsuarios_itemUsuario" style="border-radius:1rem; padding:1rem; display:flex; align-items:center; gap:1rem; border:1px solid #ddd; margin-bottom:1rem;">
          <div class="contenedor-foto">
            <img class="foto-usuario"
                 src="<%= imgRel %>"
                 alt="<%= nick %>"
                 style="height:127px; width:127px; object-fit:cover; border-radius:50%; border:1px solid #ddd;">
          </div>
          <div class="contenedor-NickRolUser" style="flex:1;">
            <div class="nickname">
              <a href="<%= verDetalleUrl %>" style="font-weight:bold;"><%= nick %></a>
            </div>
            <div class="rol" style="font-style:italic;">Asistente</div>
          </div>
          <a href="<%= verDetalleUrl %>">
            <button type="button"
              style="margin-left:1rem; padding:0.5em 1em; border-radius:0.5em; border:1px solid #ccc; background:#fafafa; cursor:pointer;">
              Ver detalle registro
            </button>
          </a>
        </div>
      <% } %>
    </div>
  <% } %>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>