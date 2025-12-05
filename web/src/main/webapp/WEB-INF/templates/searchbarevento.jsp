<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    // Obtener el valor de búsqueda actual para mantenerlo en el campo
    String valorBusqueda = request.getParameter("nombre");
    if (valorBusqueda == null) {
        valorBusqueda = (String) request.getAttribute("nombreBusqueda");
    }
    if (valorBusqueda == null) {
        valorBusqueda = "";
    }
%>

<!-- Componente de Barra de Búsqueda Reutilizable -->
<form action="<%=request.getContextPath()%>/eventos" method="get" class="w-100">
    <input type="search" name="nombre" class="search"
           placeholder="Buscar eventos..." 
           value="<%=valorBusqueda%>"
           style="width:100%;">
</form>