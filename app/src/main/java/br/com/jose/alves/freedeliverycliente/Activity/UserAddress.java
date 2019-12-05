package br.com.jose.alves.freedeliverycliente.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import br.com.jose.alves.freedeliverycliente.Activity.Home.MainActivity;
import br.com.jose.alves.freedeliverycliente.R;

import br.com.jose.alves.freedeliverycliente.model.Address;
import br.com.jose.alves.freedeliverycliente.util.ConfiguracaoFirebase;
import br.com.jose.alves.freedeliverycliente.util.MaskEditText;
import br.com.jose.alves.freedeliverycliente.util.UsuarioFirebase;
import br.com.jose.alves.freedeliverycliente.viewmodel.RegisterAddressViewModel;

public class UserAddress extends AppCompatActivity {


   // private final List<Validator> validadores = new ArrayList<>();
    private MaterialButton    btnSendAddress;
    private TextInputEditText titCepAddress;
    private TextInputEditText tiePublicPlaceRegiste;
    private TextInputEditText tieComplementReceive;
    private TextInputEditText tieNeighborhoodReceive;
    private TextInputEditText tieCityReceive;
    private TextInputEditText tieConfirmUf;
    private TextInputEditText tieNumero;
    private RegisterAddressViewModel registerAddressViewModel;


    private DatabaseReference firebaseRef;
    private String idUsuarioLogado;




    private StorageReference storageReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_address);

        registerAddressViewModel = ViewModelProviders.of(this).get(RegisterAddressViewModel.class);

        //Configurações iniciais
        initializeComponents();
        ReferenceFireba();

    }


    //  inicializado todos os Componentes
    private void initializeComponents() {
        initFindViewById();
        //validatedobligatory();
        eventButton();
        loadDataAddress();
    }

    private void eventButton() {

        btnSendAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarDadosUsuario(view);

                Intent i = new Intent(UserAddress.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });
    }

    private void ReferenceFireba() {
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();
    }

    private void initFindViewById() {
        tiePublicPlaceRegiste = findViewById(R.id.tie_public_place_registe);
        tieComplementReceive = findViewById(R.id.tie_complement);
        tieNeighborhoodReceive = findViewById(R.id.tie_neighborhood_receive);
        tieCityReceive = findViewById(R.id.tie_city_receive);
        tieConfirmUf = findViewById(R.id.tie_confirm_uf_receive);
        btnSendAddress = findViewById(R.id.btn_send_address);
        titCepAddress = findViewById(R.id.tie_cep_address_receive_home);
        tieNumero = findViewById(R.id.tie_number_receive_home);

        titCepAddress.addTextChangedListener(MaskEditText.mask(titCepAddress, MaskEditText.FORMAT_CEP));

        //ADD Toobar
        Toolbar toolbar = findViewById(R.id.mt_toolbar_address);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    //Carrega o cep
    private void loadDataAddress() {
        titCepAddress.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 9) {
                    registerAddressViewModel.getAddress(titCepAddress.getText().toString())
                            .observe(UserAddress.this, new Observer<Address>() {
                                @Override
                                public void onChanged(Address address) {
                                    if (address != null) {
                                        address.toString().replace("-","");
                                        tiePublicPlaceRegiste.setText(address.getLogradouro());
                                        tieComplementReceive.setText(address.getComplemento());
                                        tieNeighborhoodReceive.setText(address.getBairro());
                                        tieCityReceive.setText(address.getLocalidade());
                                        tieConfirmUf.setText(address.getUf());

                                    }

                                }
                            });

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });

    }

    public void validarDadosUsuario(View view) {



        //Valida se os campos foram preenchidos
        String cep = titCepAddress.getText().toString();
        String rua = tiePublicPlaceRegiste.getText().toString();
        String complemento = tieComplementReceive.getText().toString();
        String cidade = tieCityReceive.getText().toString();
        String bairro = tieNeighborhoodReceive.getText().toString();
        String numero = tieNumero.getText().toString();
        String estado = tieConfirmUf.getText().toString();

        if (!cep.isEmpty()) {
            if (!rua.isEmpty()) {
                if (!complemento.isEmpty()) {
                    if (!cidade.isEmpty()) {
                        if (!bairro.isEmpty()) {
                            if (!numero.isEmpty()) {
                                if (!estado.isEmpty()) {

                                    Address End = new Address( );
                                    End.setIdUsuario(idUsuarioLogado);
                                    End.setCep(cep);
                                    End.setLogradouro(rua);
                                    End.setComplemento(complemento);
                                    End.setLocalidade(cidade);
                                    End.setNumero(numero);
                                    End.setBairro(bairro);
                                    End.setUf(estado);
                                    End.salvar();
                                    finish();
                                    exibirMensagem("Endereço salvo com sucesso");

                                } else {
                                    exibirMensagem("Digite um cep ");
                                }
                            } else {
                                exibirMensagem("Digite a rua ");
                            }
                        } else {
                            exibirMensagem("Digite o complemento");
                        }
                    } else {
                        exibirMensagem("Digite a cidade ");
                    }
                }else{
                    exibirMensagem("Digite o numero ");
                }

            } else {
                exibirMensagem("Digite o bairro ");
            }

        } else {
            exibirMensagem("Digite o estado ");
        }


    }

    private void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT)
                .show();
    }

}
