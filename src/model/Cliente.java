
package model;

import java.io.Serializable;
import java.time.LocalDate;

public class Cliente implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private long id;
    private final String cpf;
    
    private String nome;
    private LocalDate dataNascimento;
    private String endereco;
    private String telefone;
    private String email;
    private String senha;
    
    public Cliente(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }
    
    // Getters
    public long getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getCpf() {
        return cpf;
    }
    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
    public String getEndereco() {
        return endereco;
    }
    
    public String getTelefone () {
        return telefone;
    }
    
    public String getEmail () {
        return email;
    }
    public String getSenha() {
        return senha;
    }
    
    // Setters
    public void setId (long id) {
        this.id = id;
    }
    public void setNome (String nome) {
        this.nome = nome;
    }
    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    public void setEndereco (String endereco) {
        this.endereco = endereco;
    }
    public void setTelefone (String telefone) {
        this.telefone = telefone;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setSenha (String senha) {
        this.senha = senha;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cliente outroCliente = (Cliente) o;

        if (this.cpf != null) {
            return this.cpf.equals(outroCliente.cpf);
        } else {
            return outroCliente.cpf == null;
        }
    }
    
    @Override 
    public String toString() {
        return this.nome;
    }
    
    @Override
    public int hashCode() {
        // Verifica se o atributo 'cpf' não é nulo.
        if (this.cpf != null) {
            // Se não for nulo, retorna o código hash do próprio CPF.
            return this.cpf.hashCode();
        } else {
            // Se for nulo, retorna 0, conforme a convenção do Java.
            return 0;
        }
    }
}
