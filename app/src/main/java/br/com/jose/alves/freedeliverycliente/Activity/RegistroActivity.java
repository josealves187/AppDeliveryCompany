package br.com.jose.alves.freedeliverycliente.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.jose.alves.freedeliverycliente.R;
import br.com.jose.alves.freedeliverycliente.model.User;
import br.com.jose.alves.freedeliverycliente.util.ConfiguracaoFirebase;

public class RegistroActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtConfirmPassword;
    private MaterialButton btnRegister;

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth autenticacao;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        inicializaComponentes();
        eventoButton();
        database = FirebaseDatabase.getInstance();



    }

    private void eventoButton() {


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validandoCadastroUsuario();

            }
        });
    }

    private void validandoCadastroUsuario() {
        if (edtEmail.getText().toString().equals("")) {
            edtEmail.setError("Insira o seu E-mail!");
            edtEmail.requestFocus();
        } else if (edtPassword.getText().toString().equals("") && edtConfirmPassword.getText().toString().equals("")) {
            edtPassword.setError("Insira sua senha!");
            edtConfirmPassword.setError("Confirma senha");
            edtConfirmPassword.requestFocus();
            edtPassword.requestFocus();

        } else if (edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
            user = new User();
            user.setEmail(edtEmail.getText().toString());
            user.setPassword(edtPassword.getText().toString());
            user.setTipoUsuario("comum");

            cadastrarUsuario(user);

        } else {
            Toast.makeText(RegistroActivity.this, "As senhas não correspondem!",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void cadastrarUsuario(final User user) {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            inserirFuncionarioDatabase(user);
                            telaUsuario();

                        } else {
                            String excecao = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                excecao = " Digite Uma senha Mais Forte!";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                excecao = " Por Favor, Digite Um Email Válido!";
                            } catch (FirebaseAuthUserCollisionException e) {
                                excecao = "Esta conta já foi cadastrada";
                            } catch (Exception e) {
                                excecao = "Erro ao cadastrar usuário!" + e.getMessage();
                                e.printStackTrace();
                            }


                            Toast.makeText(RegistroActivity.this, excecao,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private boolean inserirFuncionarioDatabase(User user){

        try {
            reference = ConfiguracaoFirebase.getFirebase().child("UsuarioComu");
            String key = reference.push().getKey();
            user.setIdUsuario(key);
            reference.child(key).setValue(user);
            return true;

        }catch (Exception e){
            Toast.makeText(this, "Erro ao salva Usuario!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        }
    }

    private void telaUsuario() {
        startActivity(new Intent(RegistroActivity.this, CadastroActivityDados.class));
        finish();
    }



    private void inicializaComponentes() {
        btnRegister =  findViewById(R.id.btn_send);
        edtEmail =  findViewById(R.id.tv_email_register);
        edtPassword =  findViewById(R.id.tv_password_register);
        edtConfirmPassword = findViewById(R.id.tv_confirm_password);

        Toolbar toolbar = findViewById(R.id.mt_toolbar_recover_password);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
