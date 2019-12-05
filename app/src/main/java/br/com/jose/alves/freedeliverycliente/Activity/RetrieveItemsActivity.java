package br.com.jose.alves.freedeliverycliente.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.jose.alves.freedeliverycliente.R;
import br.com.jose.alves.freedeliverycliente.model.Address;
import br.com.jose.alves.freedeliverycliente.model.ItemPurchase;

public class RetrieveItemsActivity extends AppCompatActivity {

    private DatabaseReference firebaseRef;
    private List<ItemPurchase> itemPurchases = new ArrayList<>();

    private RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_items);
    }
//
//    private void recuperaritem() {
//        DatabaseReference enderecoRef = firebaseRef
//                .child("AddressLocal");
//
//        enderecoRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                itemPurchases.clear();
//
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//
//                    itemPurchases.add(ds.getValue(ItemPurchase.class));
//                }
//
//                adapterEndereco.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }
}
