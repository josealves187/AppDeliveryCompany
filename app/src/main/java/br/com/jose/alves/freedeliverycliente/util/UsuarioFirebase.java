package br.com.jose.alves.freedeliverycliente.util;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import br.com.jose.alves.freedeliverycliente.model.User;



public class UsuarioFirebase {

    public static String getIdUsuario(){

        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return autenticacao.getCurrentUser().getUid();

    }


    public static String getIdentificadorUsuario(){

        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String email = usuario.getCurrentUser().getEmail();
        String identificadorUsuario = Base64Custom.codificarBase64( email );

        return identificadorUsuario;

    }




    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();
    }


    public static boolean atualizarTipoUsuario(String tipo){

        try {

            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(tipo)
                    .build();
            user.updateProfile(profile);
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean atualizarFotoUsuario(Uri url){

        try {

            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri( url )
                    .build();

            user.updateProfile( profile ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if( !task.isSuccessful() ){
                        Log.d("Perfil", "Erro ao atualizar foto de perfil.");
                    }
                }
            });
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }


    }

    public static User getDadosUsuarioLogado(){

        FirebaseUser firebaseUser = getUsuarioAtual();

        User usuario = new User();
        usuario.setEmail( firebaseUser.getEmail() );
        usuario.setNome( firebaseUser.getDisplayName() );

        if ( firebaseUser.getPhotoUrl() == null ){
            usuario.setUrlImagem("");
        }else {
            usuario.setUrlImagem( firebaseUser.getPhotoUrl().toString() );
        }

        return usuario;

    }

}
