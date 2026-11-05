package com.example.mvclibrospractica.controller;

import com.example.mvclibrospractica.model.ObraRecord;
import com.example.mvclibrospractica.view.ConsoleView;

import java.util.ArrayList;
import java.util.List;

// Aqui va la logica, la usan tanto la consola como la pagina web
public class StockController {

    // Ganancia del 30% sobre el costo
    private static final double MARGEN = 1.30;

    // Lista donde se guardan las obras
    private List<ObraRecord> obras = new ArrayList<>();

    public StockController() {
        // Obras de ejemplo para no empezar vacio
        obras.add(new ObraRecord("Cien anos de soledad", 25.00, 10));
        obras.add(new ObraRecord("Don Quijote de la Mancha", 30.00, 0));
        obras.add(new ObraRecord("La Odisea", 18.50, 3));
    }

    // ===== Logica del stock =====

    // Devuelve todas las obras para mostrarlas
    public List<ObraRecord> getObras() {
        return obras;
    }

    // Guarda una obra nueva y devuelve el mensaje para el usuario
    public String agregarObra(String titulo, double precio, int stock) {
        if (titulo == null || titulo.isBlank()) {
            return "El titulo no puede estar vacio";
        }
        if (precio < 0 || stock < 0) {
            return "El precio y el stock no pueden ser negativos";
        }

        obras.add(new ObraRecord(titulo.trim(), precio, stock));
        return "Obra agregada";
    }

    // Resta ejemplares del stock cuando se vende
    public String venderObra(int posicion, int cantidad) {
        if (!existeObra(posicion)) {
            return "Esa obra no existe";
        }

        ObraRecord obra = obras.get(posicion);
        if (cantidad <= 0 || cantidad > obra.stock()) {
            return "No se puede vender esa cantidad, hay " + obra.stock() + " en stock";
        }

        // Como el record no se puede cambiar se crea uno nuevo con el stock restado
        ObraRecord obraVendida = new ObraRecord(obra.titulo(), obra.precio(), obra.stock() - cantidad);
        obras.set(posicion, obraVendida);
        return "Venta hecha, quedan " + obraVendida.stock();
    }

    // Suma lo comprado al stock y actualiza el precio si hace falta
    public String comprarStock(int posicion, int cantidad, double costo) {
        if (!existeObra(posicion)) {
            return "Esa obra no existe";
        }
        if (cantidad <= 0 || costo <= 0) {
            return "La cantidad y el costo deben ser mayores a 0";
        }

        ObraRecord actualizada = actualizarPrecio(obras.get(posicion), cantidad, costo);
        obras.set(posicion, actualizada);
        return String.format("Nuevo precio $%.2f | Stock %d", actualizada.precio(), actualizada.stock());
    }

    // Contabilidad de costos
    // si el stock estaba en 0 el nuevo precio es el costo nuevo mas el 30%
    // si todavia quedaban ejemplares se mantiene el precio de antes
    // en los dos casos se suma lo que se compro al stock
    public ObraRecord actualizarPrecio(ObraRecord obra, int cantidadComprada, double costoNuevo) {
        double precio = obra.precio();

        if (obra.stock() == 0) {
            precio = costoNuevo * MARGEN;
        }

        int nuevoStock = obra.stock() + cantidadComprada;
        return new ObraRecord(obra.titulo(), precio, nuevoStock);
    }

    // Revisa que la posicion este dentro de la lista
    private boolean existeObra(int posicion) {
        return posicion >= 0 && posicion < obras.size();
    }

    // ===== Menu de consola =====

    // Repite el menu hasta que el usuario elija salir
    public void iniciarConsola(ConsoleView vista) {
        int opcion;
        do {
            vista.mostrarMenu();
            opcion = vista.pedirEntero("Opcion: ");

            switch (opcion) {
                case 1 -> vista.mostrarObras(obras);
                case 2 -> agregarDesdeConsola(vista);
                case 3 -> venderDesdeConsola(vista);
                case 4 -> comprarDesdeConsola(vista);
                case 0 -> vista.mostrarMensaje("Hasta luego");
                default -> vista.mostrarMensaje("Opcion no valida");
            }
        } while (opcion != 0);
    }

    // Pide los datos de la obra nueva
    private void agregarDesdeConsola(ConsoleView vista) {
        String titulo = vista.pedirTexto("Titulo: ");
        double precio = vista.pedirDecimal("Precio de venta: ");
        int stock = vista.pedirEntero("Cantidad en stock: ");
        vista.mostrarMensaje(agregarObra(titulo, precio, stock));
    }

    // Pide que obra y cuantos se venden
    private void venderDesdeConsola(ConsoleView vista) {
        int posicion = elegirObra(vista);
        if (posicion == -1) {
            return;
        }
        int cantidad = vista.pedirEntero("Cuantos se venden: ");
        vista.mostrarMensaje(venderObra(posicion, cantidad));
    }

    // Pide que obra, cuanto se compro y a que costo
    private void comprarDesdeConsola(ConsoleView vista) {
        int posicion = elegirObra(vista);
        if (posicion == -1) {
            return;
        }
        int cantidad = vista.pedirEntero("Cantidad comprada: ");
        double costo = vista.pedirDecimal("Costo de cada ejemplar: ");
        vista.mostrarMensaje(comprarStock(posicion, cantidad, costo));
    }

    // Muestra la lista y devuelve la posicion de la obra elegida, o -1 si no existe
    // en pantalla empiezan en 1 pero en la lista empiezan en 0
    private int elegirObra(ConsoleView vista) {
        vista.mostrarObras(obras);
        int posicion = vista.pedirEntero("Numero de la obra: ") - 1;

        if (!existeObra(posicion)) {
            vista.mostrarMensaje("Esa obra no existe");
            return -1;
        }
        return posicion;
    }
}
