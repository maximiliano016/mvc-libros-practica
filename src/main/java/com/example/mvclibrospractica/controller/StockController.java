package com.example.mvclibrospractica.controller;

import com.example.mvclibrospractica.model.ObraRecord;
import com.example.mvclibrospractica.view.ConsoleView;

import java.util.ArrayList;
import java.util.List;

// Aqui va la logica, conecta lo que pide la vista con los datos de las obras
public class StockController {

    // Ganancia del 30% sobre el costo
    private static final double MARGEN = 1.30;

    // Lista donde se guardan las obras
    private List<ObraRecord> obras = new ArrayList<>();
    private ConsoleView vista;

    public StockController(ConsoleView vista) {
        this.vista = vista;

        // Obras de ejemplo para no empezar vacio
        obras.add(new ObraRecord("Cien anos de soledad", 25.00, 10));
        obras.add(new ObraRecord("Don Quijote de la Mancha", 30.00, 0));
        obras.add(new ObraRecord("La Odisea", 18.50, 3));
    }

    // Repite el menu hasta que el usuario elija salir
    public void iniciar() {
        int opcion;
        do {
            vista.mostrarMenu();
            opcion = vista.pedirEntero("Opcion: ");

            switch (opcion) {
                case 1 -> vista.mostrarObras(obras);
                case 2 -> agregarObra();
                case 3 -> venderObra();
                case 4 -> comprarStock();
                case 0 -> vista.mostrarMensaje("Hasta luego");
                default -> vista.mostrarMensaje("Opcion no valida");
            }
        } while (opcion != 0);
    }

    // Pide los datos y guarda una obra nueva
    private void agregarObra() {
        String titulo = vista.pedirTexto("Titulo: ");
        double precio = vista.pedirDecimal("Precio de venta: ");
        int stock = vista.pedirEntero("Cantidad en stock: ");

        obras.add(new ObraRecord(titulo, precio, stock));
        vista.mostrarMensaje("Obra agregada");
    }

    // Resta ejemplares del stock cuando se vende
    private void venderObra() {
        int posicion = elegirObra();
        if (posicion == -1) {
            return;
        }

        ObraRecord obra = obras.get(posicion);
        int cantidad = vista.pedirEntero("Cuantos se venden: ");

        if (cantidad <= 0 || cantidad > obra.stock()) {
            vista.mostrarMensaje("No se puede vender esa cantidad, hay " + obra.stock() + " en stock");
            return;
        }

        // Como el record no se puede cambiar se crea uno nuevo con el stock restado
        ObraRecord obraVendida = new ObraRecord(obra.titulo(), obra.precio(), obra.stock() - cantidad);
        obras.set(posicion, obraVendida);
        vista.mostrarMensaje("Venta hecha, quedan " + obraVendida.stock());
    }

    // Pide cuanto se compro y a que costo, y luego actualiza la obra
    private void comprarStock() {
        int posicion = elegirObra();
        if (posicion == -1) {
            return;
        }

        int cantidad = vista.pedirEntero("Cantidad comprada: ");
        double costo = vista.pedirDecimal("Costo de cada ejemplar: ");

        if (cantidad <= 0 || costo <= 0) {
            vista.mostrarMensaje("La cantidad y el costo deben ser mayores a 0");
            return;
        }

        ObraRecord actualizada = actualizarPrecio(obras.get(posicion), cantidad, costo);
        obras.set(posicion, actualizada);
        vista.mostrarMensaje(String.format("Nuevo precio $%.2f | Stock %d", actualizada.precio(), actualizada.stock()));
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

    // Muestra la lista y devuelve la posicion de la obra elegida, o -1 si no es valida
    private int elegirObra() {
        if (obras.isEmpty()) {
            vista.mostrarMensaje("No hay obras registradas");
            return -1;
        }

        vista.mostrarObras(obras);
        int numero = vista.pedirEntero("Numero de la obra: ");

        // En pantalla empiezan en 1 pero en la lista empiezan en 0
        if (numero < 1 || numero > obras.size()) {
            vista.mostrarMensaje("Esa obra no existe");
            return -1;
        }
        return numero - 1;
    }
}
