package br.com.jose.alves.freedeliverycliente.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {
    private static DatabaseReference database;
    private static FirebaseAuth referenciaAutenticacao;
    private static StorageReference referenciaStorage;

    //retorna a referencia do database
    public static DatabaseReference getFirebase(){
        if( database == null ){
            database = FirebaseDatabase.getInstance().getReference();
        }
        return database;
    }

    //retorna a instancia do FirebaseAuth usuario
    public static FirebaseAuth getFirebaseAutenticacao(){
        if( referenciaAutenticacao == null ){
            referenciaAutenticacao = FirebaseAuth.getInstance();
        }
        return referenciaAutenticacao;
    }

    //Retorna instancia do FirebaseStorage
    public static StorageReference getFirebaseStorage(){
        if( referenciaStorage == null ){
            referenciaStorage = FirebaseStorage.getInstance().getReference();
        }
        return referenciaStorage;
    }

}

