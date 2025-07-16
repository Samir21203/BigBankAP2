/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;

/**
 *
 * Implementação futura
 */
public class Gestor implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private long idFuncional;
    private String nome;
    private String login;
    private String senha;
    
    public Gestor() {
    }
    
    public Gestor (long idFuncional, String nome, String login, String senha) {
        this.idFuncional = idFuncional;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
    }
    
    public long getIdFuncional() {
        return idFuncional;
    }
    
    public String getNome () {
        return nome;
    }
    
    public void setNome (String nome) {
        this.nome = nome;
    }
    
    public String getLogin() {
        return login;
    }
    
    public void setLogin (String login) {
        this.login = login;
    }
    
    public String getSenha () {
        return senha;
    }
    
    public void setSenha (String senha) {
        this.senha = senha;
    }
    
    
    @Override
    public String toString() {
        return nome + " (Login: " + ")";
    }
    
    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Gestor gestor = (Gestor) o;
        
        if (login != null) {
            return login.equals(gestor.login);
        } else {
            return gestor.login == null;
        }
    }
    
    @Override
    public int hashCode() {
        return login != null ? login.hashCode() : 0;
    }
}
