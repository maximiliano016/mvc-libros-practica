package com.example.mvclibrospractica.view;

import com.example.mvclibrospractica.model.ObraRecord;

import java.util.List;
import java.util.Scanner;

// Aqui solo se muestra informacion y se pide datos al usuario
public class ConsoleView {

    // Lee lo que el usuario escribe en la consola
    private Scanner scanner = new Scanner(System.in);

    // Muestra las opciones del menu
    public void mostrarMenu() {
        System.out.println();
        System.out.println("===== Repositorio de Libros =====");
        System.out.println("1. Ver obras");
        System.out.println("2. Agregar obra");
        System.out.println("3. Vender obra");
        System.out.println("4. Comprar stock");
        System.out.println("0. Salir");
    }

    // Muestra todas las obras con un numero adelante para poder elegirlas
    public void mostrarObras(List<ObraRecord> obras) {
        if (obras.isEmpty()) {
            System.out.println("No hay obras registradas");
            return;
        }

        System.out.println();
        for (int i = 0; i < obras.size(); i++) {
            ObraRecord obra = obras.get(i);
            System.out.printf("%d. %s | Precio $%.2f | Stock %d%n",
                    i + 1, obra.titulo(), obra.precio(), obra.stock());
        }
    }

    // Muestra un mensaje cualquiera
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    // Pide un texto
    public String pedirTexto(String pregunta) {
        System.out.print(pregunta);
        return scanner.nextLine();
    }

    // Pide un numero entero y vuelve a preguntar si escriben algo que no es numero
    public int pedirEntero(String pregunta) {
        while (true) {
            String texto = pedirTexto(pregunta);
            try {
                return Integer.parseInt(texto.trim());
            } catch (NumberFormatException e) {
                System.out.println("Escribe un numero entero");
            }
        }
    }

    // Pide un numero con decimales y vuelve a preguntar si escriben algo que no es numero
    public double pedirDecimal(String pregunta) {
        while (true) {
            // Se cambia la coma por punto para aceptar 15,50 o 15.50
            String texto = pedirTexto(pregunta).replace(',', '.');
            try {
                return Double.parseDouble(texto.trim());
            } catch (NumberFormatException e) {
                System.out.println("Escribe un numero, por ejemplo 15.50");
            }
        }
    }
}
