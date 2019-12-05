package br.com.jose.alves.freedeliverycliente.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

import br.com.jose.alves.freedeliverycliente.R;
import br.com.jose.alves.freedeliverycliente.util.ConfiguracaoFirebase;

public class SimpleSlideActivity extends IntroActivity {

    private FirebaseAuth autenticacao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_slide);

//        setButtonBackVisible(false);
//        setButtonNextVisible(false);
//
//        addSlide( new FragmentSlide.Builder()
//                .background(android.R.color.white)
//                .fragment(R.layout.intro_1)
//                .build());
//
//        addSlide( new FragmentSlide.Builder()
//                .background(android.R.color.white)
//                .fragment(R.layout.intro_2)
//                .build());
//
//        addSlide( new FragmentSlide.Builder()
//                .background(android.R.color.white)
//                .fragment(R.layout.intro_cadastro)
//                .build());
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        verificarUsuarioLogado();
//    }

//    public void verificarUsuarioLogado(){
//        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
//        //autenticacao.signOut();
//        if( autenticacao.getCurrentUser() != null ){
//            abrirTelaPrincipal();
//        }
//    }
//
//    public void abrirTelaPrincipal(){
//        startActivity(new Intent(this, CadastroActivityDados.class));
//        finish();
//    }
//
//    public void btCadastrar(View view){
//        startActivity(new Intent(this, CadastroActivityDados.class));
//        finish();
//    }
}
