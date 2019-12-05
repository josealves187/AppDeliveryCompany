package br.com.jose.alves.freedeliverycliente.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import br.com.jose.alves.freedeliverycliente.R;
import br.com.jose.alves.freedeliverycliente.adapter.ReceiveMerchandiseAdapter;
import br.com.jose.alves.freedeliverycliente.listen.RecyclerItemClickListener;
import br.com.jose.alves.freedeliverycliente.model.ItemPurchase;
import br.com.jose.alves.freedeliverycliente.util.ConfiguracaoFirebase;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnverifiedProductsFragment extends Fragment {

    RecyclerView rvReceive;
    private List<ItemPurchase> merchandises = new ArrayList<>();
    private ReceiveMerchandiseAdapter receiveMerchandiseAdapter;
    private StorageReference storageReference;
    private DatabaseReference firebaseRef;
    private FirebaseAuth autenticacao;

    public UnverifiedProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_unverified_products, container, false);

        rvReceive = view.findViewById(R.id.rv_products_unverified);
        autenticacao = FirebaseAuth.getInstance();
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        firebaseRef = ConfiguracaoFirebase.getFirebase();

        rvReceive.setLayoutManager(new LinearLayoutManager(getActivity()));
        receiveMerchandiseAdapter = new ReceiveMerchandiseAdapter(merchandises, getContext());
        rvReceive.setAdapter(receiveMerchandiseAdapter);


        rvReceive.addOnItemTouchListener(new RecyclerItemClickListener(
                getContext(),
                rvReceive,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, final int position) {
                        final ItemPurchase pedido = merchandises.get(position);
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setTitle("Excluir o produtor");
                        dialog.setMessage("Finaliza entregar: " + pedido.getName() + " ?");
                        dialog.setPositiveButton("sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (merchandises.remove(pedido)) {
                                    removePedidos();
                                    Toast.makeText(getActivity(),
                                            "Produto Excluido!",
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }

                            }
                        });

                        dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (merchandises.remove(pedido)) {
                                    removePedidos();

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
        return view;

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
                .child("lista");

        Query pedidoPesquisa = enderecoRef.orderByChild("status")
                .equalTo("Nao_entregue");

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
                .child("lista");
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

