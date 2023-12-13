package com.example.fit;

public class User {
    private String nome;
    private String email;
    private String senha;
    private String genero;
    private String age;
    private String height;
    private String weight;

    public User() {
    }

    public User(String nome, String email, String senha, String genero, String age, String height, String weight) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.genero = genero;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}