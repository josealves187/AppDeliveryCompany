package br.com.jose.alves.freedeliverycliente.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import br.com.jose.alves.freedeliverycliente.R;
import br.com.jose.alves.freedeliverycliente.model.User;
import br.com.jose.alves.freedeliverycliente.util.ConfiguracaoFirebase;
import br.com.jose.alves.freedeliverycliente.util.Permissoes;
import br.com.jose.alves.freedeliverycliente.util.UsuarioFirebase;

public class RegistrationDataActivity extends AppCompatActivity {

    private MaterialToolbar mtToolbarDataRegistrion;
    private ImageView acivPhotoUserDetais;
    private EditText tvNameRegister;
    private TextView tvCpfRegister;
    private EditText etName2;
    private EditText tv_Celular;
    private MaterialButton btnRegisterd;

    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageReference;
    private String idUsuarioLogado;
    private String urlImagemSelecionada = "";
    private DatabaseReference firebaseRef;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_data);
        Permissoes.validarPermissoes(permissoes,this, 1);
        initializeComponents();
        referenceUser();
        setSupportActionBar(mtToolbarDataRegistrion);
        eventButton();

    }

    private void referenceUser() {
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();
    }

    private void eventButton() {
        mtToolbarDataRegistrion.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        btnRegisterd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDadosUsuarios(v);
                Toast.makeText(RegistrationDataActivity.this, "Dados Atualizados ", Toast.LENGTH_SHORT).show();

            }
        });

        acivPhotoUserDetais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                );
                if( i.resolveActivity(getPackageManager()) != null ){
                    startActivityForResult(i, SELECAO_GALERIA);
                }
            }
        });

        recueperarDadosUsuario();
    }

    private void recueperarDadosUsuario() {
        DatabaseReference user = firebaseRef
                .child("UsuarioComu")
                .child(idUsuarioLogado);
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    User e = dataSnapshot.getValue(User.class);
                    etName2.setText(e.getNome2());
                    tvCpfRegister.setText(e.getCpf());
                    tv_Celular.setText(e.getTelefone());
                    tvNameRegister.setText(e.getNome());

                    urlImagemSelecionada = e.getUrlImagem();
                    if (urlImagemSelecionada != ""){
                        Picasso.get()
                                .load(urlImagemSelecionada)
                                .into(acivPhotoUserDetais);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void validarDadosUsuarios(View v) {

        //Valida se os campos foram preenchidos
        String nome = tvNameRegister.getText().toString();
        String nome2 = etName2.getText().toString();
        String cpf = tvCpfRegister.getText().toString();
        String telefone = tv_Celular.getText().toString();
        ImageView acivPhotoUserDetais = null;


        if (!nome.isEmpty()) {
            if (!nome2.isEmpty()) {
                if (!cpf.isEmpty()) {
                    if (!telefone.isEmpty()) {

                        User user = new User();

                        user.setNome(nome);
                        user.setIdUsuario(idUsuarioLogado);
                        user.setNome2(nome2);
                        user.setCpf(cpf);
                        user.setTelefone(telefone);
                        user.setUrlImagem(urlImagemSelecionada);
                        user.Userprofile();


                    } else {
                        exibirMensagem("Digite o nome ");
                    }
                } else {
                    exibirMensagem("Campo vazio como deseja ser chamador ");
                }
            } else {
                exibirMensagem("Digite o cpf");
            }
        } else {
            exibirMensagem("Digite o telefone ");
        }
    }

    private void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onBackPressed(){

        SharedPreferences.Editor editor = getSharedPreferences("PARKOK", MODE_PRIVATE).edit();
        editor.putInt("SCREEN_ORIGEN", 4);
        editor.commit();
        super.onBackPressed();
    }

    private void initializeComponents() {
        mtToolbarDataRegistrion = findViewById(R.id.mt_toolbar_data_registrion);
        acivPhotoUserDetais = findViewById(R.id.aciv_photo_user_detais);
        tvNameRegister = findViewById(R.id.tv_name_register);
        tvCpfRegister = findViewById(R.id.tv_cpf);
        tv_Celular = findViewById(R.id.tv_Celular);
        etName2 = findViewById(R.id.tv_name22);
        btnRegisterd = findViewById(R.id.btn_regist);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissionsResultado : grantResults){
            if (permissionsResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }

        }
    }

    private void alertaValidacaoPermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {

                switch (requestCode) {
                    case SELECAO_GALERIA:
                        Uri localImagem = data.getData();
                        imagem = MediaStore.Images
                                .Media
                                .getBitmap(
                                        getContentResolver(),
                                        localImagem
                                );
                        break;
                }

                if (imagem != null) {

                    acivPhotoUserDetais.setImageBitmap(imagem);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    final StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("Usuario")
                            .child(idUsuarioLogado + "jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return imagemRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult();
                                urlImagemSelecionada = downloadUrl.toString();


                                Toast.makeText(RegistrationDataActivity.this, "Sucesso ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegistrationDataActivity.this, "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}

