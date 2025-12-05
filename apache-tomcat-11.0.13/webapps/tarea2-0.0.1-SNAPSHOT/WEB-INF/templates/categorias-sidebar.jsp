<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Set" %>

<%
    @SuppressWarnings("unchecked")
    Set<String> categorias = (Set<String>) request.getSession().getAttribute("categorias");
    String categoriaSeleccionada = (String) request.getAttribute("categoriaSeleccionada");
    String nombreBusqueda = (String) request.getAttribute("nombreBusqueda");
    String tipoFiltro = (String) request.getAttribute("tipoFiltro");
    String ordenamiento = (String) request.getAttribute("ordenamiento");
%>

<!-- Componente de Categorías Lateral Reutilizable -->
<div class="carta-categorias h-100">

    <!-- Lista de categorías -->
    <ul class="list-group w-100 overflow-auto text-center">
        <!-- Opción "Todas las categorías" -->
        <%
            String hrefTodas = request.getContextPath() + "/eventos";
            String paramsSeparator = "?";
            
            if (nombreBusqueda != null && !nombreBusqueda.isEmpty()) {
                hrefTodas += paramsSeparator + "nombre=" + java.net.URLEncoder.encode(nombreBusqueda, "UTF-8");
                paramsSeparator = "&";
            }
            if (tipoFiltro != null && !tipoFiltro.isEmpty()) {
                hrefTodas += paramsSeparator + "tipo=" + java.net.URLEncoder.encode(tipoFiltro, "UTF-8");
                paramsSeparator = "&";
            }
            if (ordenamiento != null && !ordenamiento.isEmpty()) {
                hrefTodas += paramsSeparator + "orden=" + java.net.URLEncoder.encode(ordenamiento, "UTF-8");
            }
        %>
        <a href="<%=hrefTodas%>" 
           class="list-group-item list-group-item-action categoria-link <%=categoriaSeleccionada == null || categoriaSeleccionada.isEmpty() || "todas".equals(categoriaSeleccionada) ? "active" : ""%>">
            Todas las categorías
        </a>
        
        <!-- Lista de categorías dinámicas -->
        <%
        if (categorias != null && !categorias.isEmpty()) {
            for (String categoria : categorias) {
                String href = request.getContextPath() + "/eventos?categoria=" + java.net.URLEncoder.encode(categoria, "UTF-8");
                paramsSeparator = "&";
                
                if (nombreBusqueda != null && !nombreBusqueda.isEmpty()) {
                    href += paramsSeparator + "nombre=" + java.net.URLEncoder.encode(nombreBusqueda, "UTF-8");
                }
                if (tipoFiltro != null && !tipoFiltro.isEmpty()) {
                    href += paramsSeparator + "tipo=" + java.net.URLEncoder.encode(tipoFiltro, "UTF-8");
                }
                if (ordenamiento != null && !ordenamiento.isEmpty()) {
                    href += paramsSeparator + "orden=" + java.net.URLEncoder.encode(ordenamiento, "UTF-8");
                }
                
                boolean isActive = categoria.equals(categoriaSeleccionada);
        %>
            <a href="<%=href%>" 
               class="list-group-item list-group-item-action categoria-link <%=isActive ? "active" : ""%>">
                <%=categoria%>
            </a>
        <%
            }
        } else {
        %>
            <li class="list-group-item text-muted">
                <i class="bi bi-info-circle"></i> No hay categorías disponibles
            </li>
        <%
        }
        %>
    </ul>
</div>