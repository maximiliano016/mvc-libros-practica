package com.example.mvclibrospractica.model;

// Datos de una obra (titulo, precio y cuantos hay en stock)
// record crea solo el constructor y los metodos para leer cada dato
// no se puede cambiar, si algo cambia se crea una obra nueva
public record ObraRecord(String titulo, double precio, int stock) {
}
