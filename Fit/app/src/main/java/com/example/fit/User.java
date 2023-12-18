package com.example.fit;

import java.util.List;
import java.util.Map;

public class User {
    private String nome;
    private String email;
    private String senha;
    private boolean podeRedefinirSenha;
    private String genero;
    private int altura; // em cm
    private int peso; // em kg
    private List<Map<String, Object>> registrosHidratacao; // Lista de registros de hidratação

    // Construtor vazio necessário para o Firebase
    public User() {
    }

    // Construtor existente
    public User(String nome, String email, String senha, boolean podeRedefinirSenha,
                String genero, int altura, int peso, List<Map<String, Object>> registrosHidratacao) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.podeRedefinirSenha = podeRedefinirSenha;
        this.genero = genero;
        this.altura = altura;
        this.peso = peso;
        this.registrosHidratacao = registrosHidratacao;
    }

    // Novo construtor adicionado
    public User(String nome, String email, String senha, String genero, String age, String height, String weight) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.genero = genero;
        // Suponho que age seja um número em formato de texto, convertendo para inteiro
        this.altura = Integer.parseInt(age);
        // Convertendo altura de formato texto em metros para um número inteiro em centímetros
        this.altura = (int) (Float.parseFloat(height) * 100);
        // Convertendo peso de formato texto para um número inteiro em quilogramas
        this.peso = (int) Float.parseFloat(weight);
        // Inicialização das listas
        this.registrosHidratacao = null;  // ou new ArrayList<>() se desejar inicializá-la como uma lista vazia;
    }

    // Getters e setters
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

    public boolean getPodeRedefinirSenha() {
        return podeRedefinirSenha;
    }

    public void setPodeRedefinirSenha(boolean podeRedefinirSenha) {
        this.podeRedefinirSenha = podeRedefinirSenha;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public List<Map<String, Object>> getRegistrosHidratacao() {
        return registrosHidratacao;
    }

    public void setRegistrosHidratacao(List<Map<String, Object>> registrosHidratacao) {
        this.registrosHidratacao = registrosHidratacao;
    }

    // Mais métodos e lógica conforme necessário para a sua aplicação
}