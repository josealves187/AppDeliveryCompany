package br.com.jose.alves.freedeliverycliente.model;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import br.com.jose.alves.freedeliverycliente.util.ConfiguracaoFirebase;



public class User implements Serializable {

    private String telefone;
    private String nome2;
    private String nome;
    private String password;
    private String email;
    private String UrlImagem;
    private String idUsuario;
    private String cpf;
    private String tipoUsuario;
    private String status = "cadastrador";


    public User() {
    }

    public User(String telefone, String nome2, String nome, String password, String email,
                String urlImagem, String idUsuario, String cpf, String tipoUsuario) {
        this.telefone = telefone;
        this.nome2 = nome2;
        this.nome = nome;
        this.password = password;
        this.email = email;
        UrlImagem = urlImagem;
        this.idUsuario = idUsuario;
        this.cpf = cpf;
        this.tipoUsuario = tipoUsuario;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getNome2() {
        return nome2;
    }

    public void setNome2(String nome2) {
        this.nome2 = nome2;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrlImagem() {
        return UrlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        UrlImagem = urlImagem;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void salvar(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference usuarioRef = firebaseRef
                .child("UsuarioProfile")
                .child( getIdUsuario() );
        usuarioRef.setValue(this);

    }

    public void Userprofile(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference usuarioRef = firebaseRef
                .child("Userprofile")
                .child( getIdUsuario() );
        usuarioRef.setValue(this);

    }
}
