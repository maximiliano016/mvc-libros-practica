package com.example.mvclibrospractica.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// Recibe lo que pide la pagina web y se lo pasa al StockController
// responde en la ruta "/libros"
@WebServlet("/libros")
public class LibrosServlet extends HttpServlet {

    // Un solo controlador para que todos vean las mismas obras
    private StockController controlador = new StockController();

    // get muestra la pagina con la lista de obras
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Mensaje que dejo la ultima accion, se borra para que no salga dos veces
        HttpSession sesion = request.getSession();
        request.setAttribute("mensaje", sesion.getAttribute("mensaje"));
        sesion.removeAttribute("mensaje");

        // Se mandan las obras a la pagina libros.jsp para que las dibuje
        request.setAttribute("obras", controlador.getObras());
        request.getRequestDispatcher("/WEB-INF/libros.jsp").forward(request, response);
    }

    // post recibe los formularios de agregar, vender y comprar
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Para que lleguen bien las tildes y la ñ
        request.setCharacterEncoding("UTF-8");

        // accion dice que formulario se envio
        String accion = request.getParameter("accion");
        String mensaje;

        try {
            if ("agregar".equals(accion)) {
                String titulo = request.getParameter("titulo");
                double precio = Double.parseDouble(request.getParameter("precio"));
                int stock = Integer.parseInt(request.getParameter("stock"));
                mensaje = controlador.agregarObra(titulo, precio, stock);

            } else if ("vender".equals(accion)) {
                int posicion = Integer.parseInt(request.getParameter("posicion"));
                int cantidad = Integer.parseInt(request.getParameter("cantidad"));
                mensaje = controlador.venderObra(posicion, cantidad);

            } else if ("comprar".equals(accion)) {
                int posicion = Integer.parseInt(request.getParameter("posicion"));
                int cantidad = Integer.parseInt(request.getParameter("cantidad"));
                double costo = Double.parseDouble(request.getParameter("costo"));
                mensaje = controlador.comprarStock(posicion, cantidad, costo);

            } else {
                mensaje = "Accion no valida";
            }
        } catch (NumberFormatException | NullPointerException e) {
            // Si falta un dato o no es un numero
            mensaje = "Revisa que todos los datos esten llenos y bien escritos";
        }

        // Se guarda el mensaje y se vuelve a la lista
        // asi al recargar la pagina no se repite la compra o la venta
        request.getSession().setAttribute("mensaje", mensaje);
        response.sendRedirect("libros");
    }
}
