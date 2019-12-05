package br.com.jose.alves.freedeliverycliente.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import br.com.jose.alves.freedeliverycliente.R;
import br.com.jose.alves.freedeliverycliente.adapter.ReceiveMerchandiseAdapter;
import br.com.jose.alves.freedeliverycliente.listen.RecyclerItemClickListener;
import br.com.jose.alves.freedeliverycliente.model.ItemPurchase;
import br.com.jose.alves.freedeliverycliente.util.ConfiguracaoFirebase;
import br.com.jose.alves.freedeliverycliente.util.UsuarioFirebase;

public class ReceiveMerchandiseActivity extends AppCompatActivity {


    RecyclerView rvReceive;
    private List<ItemPurchase> merchandises = new ArrayList<>();
    private ReceiveMerchandiseAdapter receiveMerchandiseAdapter;
    private String idUsuarioLogado;
    private StorageReference storageReference;
    private DatabaseReference firebaseRef;
    private FirebaseAuth autenticacao;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_merchandise);
        rvReceive = findViewById(R.id.rv_receive);
        TextView t = findViewById(R.id.t);
        autenticacao = FirebaseAuth.getInstance();
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();


        if (getIntent().getExtras() != null) {
            Gson gson = new Gson();
            final List<ItemPurchase> itemPurchases;
            itemPurchases = gson.fromJson(getIntent().getExtras().getString("ITEMPURCHASES", ""), new TypeToken<List<ItemPurchase>>() {
            }.getType());


            if (itemPurchases.size() != 0) {


                rvReceive.setLayoutManager(new LinearLayoutManager(this));
                receiveMerchandiseAdapter = new ReceiveMerchandiseAdapter(itemPurchases, this);
                rvReceive.setAdapter(receiveMerchandiseAdapter);

//            DatabaseReference restaurantRef = FirebaseDatabase
//                    .getInstance()
//                    .getReference(idUsuarioLogado);
//            restaurantRef.push().setValue(itemPurchases);


//            ItemPurchase itemPurchase = new ItemPurchase();
//            itemPurchase.equals(itemPurchases);
//            itemPurchase.salvar();


                DatabaseReference restaurantRef = ConfiguracaoFirebase.getFirebase();
                DatabaseReference usuarioRef = restaurantRef
                        .child("list");
                usuarioRef.push().setValue(itemPurchases);


//            firebaseRef.child("list").setValue(itemPurchases);


//            DatabaseReference restaurantRef = ConfiguracaoFirebase.getFirebase();
//            DatabaseReference usuarioRef = restaurantRef
//                    .child("list")
//                    .child(idUsuarioLogado);
//            usuarioRef.push().setValue(itemPurchases);

//            ItemPurchase itemPurchase = new ItemPurchase();
//
//            itemPurchase.setIdUsuario(idUsuarioLogado);
//            itemPurchase.setAmount(0);
//            itemPurchase.setDescription("");
//            itemPurchase.setValue(0);
//            itemPurchase.salvar();

                recuperaitem();


                rvReceive.addOnItemTouchListener(new RecyclerItemClickListener(
                        this,
                        rvReceive,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onLongItemClick(View view, final int position) {
                                final ItemPurchase pedido = itemPurchases.get(position);
                                AlertDialog.Builder dialog = new AlertDialog.Builder(ReceiveMerchandiseActivity.this);
                                dialog.setTitle("Comfirmar pedido");
                                dialog.setMessage("Finaliza entregar: " + pedido.getName() + " ?");
                                dialog.setPositiveButton("sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (itemPurchases.remove(pedido)) {
                                            pedido.setStatus("Entregue");
                                            pedido.atualizarStatus();
                                            removePedidos();
                                            Toast.makeText(ReceiveMerchandiseActivity.this,
                                                    "Produto entregue!",
                                                    Toast.LENGTH_SHORT)
                                                    .show();
                                        }

                                    }
                                });

                                dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if (itemPurchases.remove(pedido)) {
                                            pedido.setStatus("Nao_entregue");
                                            pedido.atualizarStatus();
                                            removePedidos();
                                            Toast.makeText(ReceiveMerchandiseActivity.this,
                                                    "Produto não entregue!",
                                                    Toast.LENGTH_SHORT)
                                                    .show();
                                        }

                                    }

                                });
                                dialog.create();
                                dialog.show();


                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }));


            } else {
                Toast.makeText(this, "teste", Toast.LENGTH_SHORT).show();

                // Toast.makeText(this, "gyugfyjhdgfjh", Toast.LENGTH_SHORT).show();
                // Show something like a dialog that the json list is 0 or do whatever you want... here the jsonlist have a count of 0 so it's empty!
            }

        }

        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceiveMerchandiseActivity.this, DeliveryReceivedActivity.class);
                startActivity(intent);

            }
        });
    }



    @Override
    public void onStart() {
        super.onStart();
        recuperaitem();
    }

    @Override
    public void onResume() {
        super.onResume();
        recuperaitem();
    }



    private void recuperaitem() {

        DatabaseReference enderecoRef = firebaseRef
                .child("list");

        Query pedidoPesquisa = enderecoRef.orderByChild("status")
                .equalTo("pendente");

        pedidoPesquisa.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                merchandises.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    merchandises.add(ds.getValue(ItemPurchase.class));

                }

                receiveMerchandiseAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void removePedidos() {
        final DatabaseReference removePedidos = firebaseRef
                .child("list");
        removePedidos.limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    DataSnapshot removePedido = dataSnapshot.getChildren().iterator().next();
                    removePedido.getRef().removeValue();
                }

                receiveMerchandiseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
