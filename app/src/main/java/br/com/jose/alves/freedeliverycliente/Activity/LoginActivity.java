package br.com.jose.alves.freedeliverycliente.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import br.com.jose.alves.freedeliverycliente.Activity.Home.MainActivity;
import br.com.jose.alves.freedeliverycliente.R;
import br.com.jose.alves.freedeliverycliente.model.User;
import br.com.jose.alves.freedeliverycliente.util.ConfiguracaoFirebase;
import br.com.jose.alves.freedeliverycliente.util.FingerprintHandler;

@RequiresApi(api = Build.VERSION_CODES.M)
public class LoginActivity extends AppCompatActivity {

    FirebaseAuth.AuthStateListener mAuthListener;
    private TextInputEditText tvEmailLog;
    private TextInputEditText tvPassowodLog;
    private MaterialButton btnLoginLog;
    private MaterialButton btnRegister;
    private TextView EqueciMinhaSe;
    //Recuperando Botoes do layout Recupera senha
    private BootstrapButton btnSendEmail;
    private BootstrapButton btnCancelResgSen;
    private TextView editEmailRecovery;
    private ProgressDialog progressDialog;
    private Dialog dialog;
    private FirebaseUser currentUser;
    private User usuarios;
    private DatabaseReference referenceFirebase;
    private FirebaseAuth autenticacao;


    private KeyStore keyStore;
    // Variable used for storing the key in the Android Keystore container
    private static final String KEY_NAME = "androidHive";
    private Cipher cipher;
    private TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inicializaComponentes();
        autencicadorUse();
        verificarUsuarioLogado();
        eventoButton();

        // Initializing both Android Keyguard Manager and Fingerprint Manager
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);


            textView = findViewById(R.id.errorText);

            verificarUsuarioLogado();
            // Check whether the device has a Fingerprint sensor.
            if (!fingerprintManager.isHardwareDetected()) {
                /**
                 * An error message will be displayed if the device does not contain the fingerprint hardware.
                 * However if you plan to implement a default authentication method,
                 * you can redirect the user to a default authentication activity from here.
                 * Example:
                 * Intent intent = new Intent(this, DefaultAuthenticationActivity.class);
                 * startActivity(intent);
                 */
                textView.setText("Seu dispositivo não possui um sensor de impressão digital");
            } else {
                // Checks whether fingerprint permission is set on manifest
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                    textView.setText("Permissão de autenticação de impressão digital não ativada");
                } else {
                    // Check whether at least one fingerprint is registered
                    if (!fingerprintManager.hasEnrolledFingerprints()) {
                        textView.setText("Registre pelo menos uma impressão digital em Configurações");
                    } else {
                        // Checks whether lock screen security is enabled or not
                        if (!keyguardManager.isKeyguardSecure()) {
                            textView.setText("A segurança da tela de bloqueio não está ativada em Configurações");
                        } else {
                            generateKey();

                            if (cipherInit()) {
                                FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                                FingerprintHandler helper = new FingerprintHandler(this);
                                helper.startAuth(fingerprintManager, cryptoObject);
                            }
                        }
                    }
                }
            }
        }

    }


    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }

        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }


    private void verificarUsuarioLogado() {

        autenticacao = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    usuarioLogado();
                }
            }


        };

        btnLoginLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarAutenticacaoUsuario();
            }
        });


    }

    private void validarAutenticacaoUsuario() {

        String emailAddress = tvEmailLog.getText().toString().trim();
        if (tvPassowodLog.getText().toString().length() < 6) {
            tvPassowodLog.setError("Senha mínima contém 6 caracteres!");
            tvPassowodLog.requestFocus();
        }
        if (tvPassowodLog.getText().toString().equals("")) {
            tvPassowodLog.setError("por favor digite a senha!");
            tvPassowodLog.requestFocus();
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            tvEmailLog.setError("Por favor insira o endereço de e-mail válido!");
            tvEmailLog.requestFocus();
        }
        if (tvEmailLog.getText().toString().equals("")) {
            tvEmailLog.setError("Digite o endereço de e-mail!");
            tvEmailLog.requestFocus();
        } else if (tvPassowodLog.getText().toString().equals("")) {
            tvPassowodLog.setError("Preencha a Senha!");
            tvPassowodLog.requestFocus();

            usuarios = new User();
            usuarios.setEmail(tvEmailLog.getText().toString());
            usuarios.setPassword(tvPassowodLog.getText().toString());

        } else {

            logarUsuario(tvEmailLog.getText().toString(),
                    tvPassowodLog.getText().toString());
        }

    }

    private boolean usuarioLogado() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null;
    }

    private void autencicadorUse() {
        referenceFirebase = FirebaseDatabase.getInstance().getReference();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    }

    private void inicializaComponentes() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        tvEmailLog = findViewById(R.id.tv_tiet_emali);
        btnRegister = findViewById(R.id.btn_register);
        tvPassowodLog = findViewById(R.id.tv_tiet_Passwor);
        btnLoginLog = findViewById(R.id.btn_input);
        EqueciMinhaSe = findViewById(R.id.tv_title_forgot_my_password);
    }

    private void eventoButton() {
        EqueciMinhaSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Estou chamado o layout  alert na textview da tela de login em recupera senha
                abrirDialog();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Estou chamado o RegistroActivity
                Intent i = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(i);
            }
        });


    }

    private void abrirDialog() {

        dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.alert_recovery_password);
        btnCancelResgSen = dialog.findViewById(R.id.btnCancelResgSen);
        btnSendEmail = dialog.findViewById(R.id.btnSendEmail);
        editEmailRecovery = (BootstrapEditText)
                dialog.findViewById(R.id.editEmailRecovery);


        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editEmailRecovery.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Preencha E-mail!", Toast.LENGTH_SHORT).show();
                    editEmailRecovery.requestFocus();
                } else
                    autenticacao.sendPasswordResetEmail(editEmailRecovery.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("TAG", "Email sent.");
                                        Toast.makeText(LoginActivity.this, "Verifique sua caixa de e-mail!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "E-mail inválido!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                dialog.dismiss();
            }
        });

        btnCancelResgSen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void abriTelasPricipais(String emailUsuario) {
        chamandoFucionario(emailUsuario);
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = autenticacao.getCurrentUser();
        if (currentUser != null) {
            abriTelasPricipais(tvEmailLog.toString());
            SharedPreferences.Editor editor = getSharedPreferences("PARKOK", MODE_PRIVATE).edit();
            editor.putInt("SCREEN_ORIGEN", 0);
            editor.commit();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        autenticacao.addAuthStateListener(mAuthListener);
        SharedPreferences.Editor editor = getSharedPreferences("PARKOK", MODE_PRIVATE).edit();
        editor.putInt("SCREEN_ORIGEN", 0);
        editor.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        autenticacao.removeAuthStateListener(mAuthListener);
    }


    private void chamandoFucionario(final String emailUsuario) {
        referenceFirebase.child("UsuarioComu").orderByChild("email").equalTo("Comum")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String tipoUsuario = postSnapshot.child("tipoUsuario").getValue().toString();

                            switch (tipoUsuario) {
                                case "Comum":
                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                    break;

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void logarUsuario(final String email, String password) {

        autenticacao.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            abriTelasPricipais(email);

                            Toast.makeText(LoginActivity.this, "Login Efetuado com Sucesso!",
                                    Toast.LENGTH_LONG).show();
                        } else {

                            String excecao = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                excecao = "Usuário não cadastrada";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                excecao = " Email e senha não correspondem ao usuário!";
                            } catch (Exception e) {
                                excecao = "Erro ao cadastrar usuário!" + e.getMessage();
                                e.printStackTrace();
                            }
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, excecao,
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}

