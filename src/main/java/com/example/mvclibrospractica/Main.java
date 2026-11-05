package com.example.mvclibrospractica;

import com.example.mvclibrospractica.controller.StockController;
import com.example.mvclibrospractica.view.ConsoleView;

// Punto de arranque del programa
public class Main {
    public static void main(String[] args) {
        // Se crea la vista y se le pasa al controlador para que la use
        ConsoleView vista = new ConsoleView();
        StockController controlador = new StockController(vista);
        controlador.iniciar();
    }
}
