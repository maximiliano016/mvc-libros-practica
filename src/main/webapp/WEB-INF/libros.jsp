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
</head>
<body>

<h1>Repositorio de Libros</h1>

<%-- Mensaje de la ultima accion --%>
<% if (mensaje != null) { %>
    <p><b>Resultado: <%= mensaje %></b></p>
<% } %>

<%-- Parte 1, la lista de obras solo para ver como esta el stock --%>
<h2>1. Obras en stock</h2>
<table border="1" cellpadding="6">
    <tr>
        <th>#</th>
        <th>Título</th>
        <th>Precio de venta</th>
        <th>Stock</th>
    </tr>
    <% for (int i = 0; i < obras.size(); i++) {
        ObraRecord obra = obras.get(i); %>
    <tr>
        <td><%= i + 1 %></td>
        <td><%= limpiar(obra.titulo()) %></td>
        <td>$<%= String.format("%.2f", obra.precio()) %></td>
        <td>
            <%= obra.stock() %>
            <% if (obra.stock() == 0) { %>(agotada)<% } %>
        </td>
    </tr>
    <% } %>
</table>

<hr>

<%-- Parte 2, vender resta ejemplares del stock --%>
<h2>2. Vender</h2>
<p>Elige la obra y cuántos ejemplares se venden.</p>
<form method="post" action="libros">
    <input type="hidden" name="accion" value="vender">

    Obra:
    <select name="posicion">
        <% for (int i = 0; i < obras.size(); i++) { %>
            <option value="<%= i %>"><%= i + 1 %>. <%= limpiar(obras.get(i).titulo()) %> (stock <%= obras.get(i).stock() %>)</option>
        <% } %>
    </select>
    <br><br>

    Cantidad a vender:
    <input type="number" name="cantidad" min="1" required>
    <br><br>

    <button type="submit">Vender</button>
</form>

<hr>

<%-- Parte 3, comprar suma stock y aplica la regla del precio --%>
<h2>3. Comprar stock</h2>
<p>
    Si la obra está agotada (stock 0), el precio nuevo será <b>costo x 1.30</b>.<br>
    Si todavía tiene stock, el precio no cambia.
</p>
<form method="post" action="libros">
    <input type="hidden" name="accion" value="comprar">

    Obra:
    <select name="posicion">
        <% for (int i = 0; i < obras.size(); i++) { %>
            <option value="<%= i %>"><%= i + 1 %>. <%= limpiar(obras.get(i).titulo()) %> (stock <%= obras.get(i).stock() %>)</option>
        <% } %>
    </select>
    <br><br>

    Cantidad comprada:
    <input type="number" name="cantidad" min="1" required>
    <br><br>

    Costo de cada ejemplar: $
    <input type="number" name="costo" min="0.01" step="0.01" required>
    <br><br>

    <button type="submit">Comprar</button>
</form>

<hr>

<%-- Parte 4, agregar una obra nueva a la lista --%>
<h2>4. Agregar obra nueva</h2>
<form method="post" action="libros">
    <input type="hidden" name="accion" value="agregar">

    Título:
    <input type="text" name="titulo" required>
    <br><br>

    Precio de venta: $
    <input type="number" name="precio" min="0" step="0.01" required>
    <br><br>

    Stock inicial:
    <input type="number" name="stock" min="0" required>
    <br><br>

    <button type="submit">Agregar</button>
</form>

</body>
</html>
