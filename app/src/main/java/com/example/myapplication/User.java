package com.example.myapplication;

public class User {
    private String nombre;
    private String apellido;
    private String email;
    private String role; // Agregado para identificar el rol del usuario

    public User() {
    }

    public User(String nombre, String apellido, String email, String role) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.role = role;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
