<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.mvclibrospractica.model.ObraRecord" %>
<%-- Vista web, solo dibuja las obras y los formularios --%>
<%!
    // Evita que un titulo con etiquetas html rompa la pagina
    String limpiar(String texto) {
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
%>
<%
    // Datos que manda el LibrosServlet
    List<ObraRecord> obras = (List<ObraRecord>) request.getAttribute("obras");
    Object mensaje = request.getAttribute("mensaje");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Repositorio de Libros</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 950px; margin: 30px auto; padding: 0 15px; }
        table { width: 100%; border-collapse: collapse; margin-bottom: 25px; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background: #2c3e50; color: white; }
        input { width: 80px; padding: 4px; }
        button { padding: 4px 10px; cursor: pointer; }
        .mensaje { background: #e8f4fd; border-left: 4px solid #2980b9; padding: 10px; margin-bottom: 20px; }
        .agotada { color: #c0392b; font-weight: bold; }
        .nota { color: #555; font-size: 14px; }
    </style>
</head>
<body>

<h1>Repositorio de Libros</h1>
<p class="nota">Si una obra esta agotada (stock 0) y se compra stock nuevo, el precio pasa a ser costo nuevo x 1.30</p>

<%-- Mensaje de la ultima accion --%>
<% if (mensaje != null) { %>
    <div class="mensaje"><%= mensaje %></div>
<% } %>

<%-- Tabla con las obras, cada fila tiene su formulario de vender y comprar --%>
<table>
    <tr>
        <th>#</th>
        <th>Titulo</th>
        <th>Precio</th>
        <th>Stock</th>
        <th>Vender</th>
        <th>Comprar stock</th>
    </tr>
    <% for (int i = 0; i < obras.size(); i++) {
        ObraRecord obra = obras.get(i); %>
    <tr>
        <td><%= i + 1 %></td>
        <td><%= limpiar(obra.titulo()) %></td>
        <td>$<%= String.format("%.2f", obra.precio()) %></td>
        <td>
            <%= obra.stock() %>
            <% if (obra.stock() == 0) { %><span class="agotada">Agotada</span><% } %>
        </td>
        <td>
            <form method="post" action="libros">
                <input type="hidden" name="accion" value="vender">
                <input type="hidden" name="posicion" value="<%= i %>">
                <input type="number" name="cantidad" min="1" placeholder="Cantidad" required>
                <button type="submit">Vender</button>
            </form>
        </td>
        <td>
            <form method="post" action="libros">
                <input type="hidden" name="accion" value="comprar">
                <input type="hidden" name="posicion" value="<%= i %>">
                <input type="number" name="cantidad" min="1" placeholder="Cantidad" required>
                <input type="number" name="costo" min="0.01" step="0.01" placeholder="Costo c/u" required>
                <button type="submit">Comprar</button>
            </form>
        </td>
    </tr>
    <% } %>
</table>

<%-- Formulario para agregar una obra nueva --%>
<h2>Agregar obra</h2>
<form method="post" action="libros">
    <input type="hidden" name="accion" value="agregar">
    <input type="text" name="titulo" placeholder="Titulo" style="width: 250px;" required>
    <input type="number" name="precio" min="0" step="0.01" placeholder="Precio" required>
    <input type="number" name="stock" min="0" placeholder="Stock" required>
    <button type="submit">Agregar</button>
</form>

</body>
</html>
