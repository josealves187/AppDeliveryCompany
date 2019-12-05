package br.com.jose.alves.freedeliverycliente.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import br.com.jose.alves.freedeliverycliente.R;
import br.com.jose.alves.freedeliverycliente.adapter.PaymentAdapter;
import br.com.jose.alves.freedeliverycliente.model.Address;
import br.com.jose.alves.freedeliverycliente.model.Time;

public class MarketAddressActivity extends AppCompatActivity {

    private RecyclerView rcvPayment;
    private RecyclerView rcvDayTime;
    private MaterialButton mbmShare;
    private TextView tvParkingAndress;
    private MaterialToolbar mtToolbarDetails;
    private ImageView iv_mt_parking_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_address);
        initializeFindViewById();
        recoveredData();
        eventButton();


    }

    @SuppressLint("SetTextI18n")
    private void recoveredData() {

        Bundle extras = getIntent().getExtras();
        Address address = extras.getParcelable("SCREEN_ORIGEN");
        if (address != null) {
            tvParkingAndress.setText(address.getLogradouro() + ", " + address.getNumero() + " - " +
                    address.getBairro() + ", " + address.getLocalidade() + " - " + address.getUf() + ", " + address.getCep());


            List<String> payment = new ArrayList<>();
            payment.add(("Dinheiro"));
            payment.add(("Cartão de crédito e débito"));
            rcvPayment.setLayoutManager(new LinearLayoutManager(this));
            rcvPayment.setAdapter(new PaymentAdapter(payment));

            List<Time> times = new ArrayList<>();

            times.add(new Time("Segunda:", "07:00 - 22:00"));
            times.add(new Time("Terça:", "07:00 - 22:00"));
            times.add(new Time("Quarta:", "07:00 - 22:00"));
            times.add(new Time("Quinta:", "07:00 - 22:00"));
            times.add(new Time("Sexta:", "07:00 - 22:00"));
            times.add(new Time("Sábado:", "07:00 - 22:00"));
            times.add(new Time("Domingo:", "07:00 - 18:30"));
            rcvDayTime.setLayoutManager(new LinearLayoutManager(this));
            rcvDayTime.setAdapter(new DayTimeAdapter(times, this));


                Glide.with(this)
                        .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ44iyqsWVQ7t1dumXd4gm3c9vcP01gJuaLs0rK3jF0ZeU3lwVEGw&s")
                        .into(iv_mt_parking_details);

        }
    }

    private void eventButton() {

        mbmShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String share = tvParkingAndress.getText().toString();
                shareText(share);
            }
        });

        mtToolbarDetails.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initializeFindViewById() {
        rcvPayment = findViewById(R.id.rcv_payment);
        rcvDayTime = findViewById(R.id.rcv_day_time);
        mbmShare = findViewById(R.id.mbm_check_share);
        tvParkingAndress = findViewById(R.id.tv_parking_andress);
        iv_mt_parking_details = findViewById(R.id.iv_mt_parking_details);

        //ADD Toobar
        mtToolbarDetails = findViewById(R.id.mt_toolbar_parking_details);




    }

    //metodo responsável para compartilhar rota
    private void shareText(String text) {


        String uri = "geo:0,0?q=" + text;
        Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));


        try {
            Intent chooserIntent = Intent.createChooser(mapIntent, "Escolha App de compartilhamento");
            if (chooserIntent == null) {
                onBackPressed();
                return;

            }
            // Attempt to start an activity that can handle the Intent
            startActivity(chooserIntent);
        } catch (Exception e) {
            startActivity(mapIntent);
            onBackPressed();

        }
    }

    @Override
    public void onBackPressed() {
        SharedPreferences.Editor editor = getSharedPreferences("PARKOK", MODE_PRIVATE).edit();
        editor.putInt("SCREEN_ORIGEN", 3);
        editor.commit();
        super.onBackPressed();
    }
}
