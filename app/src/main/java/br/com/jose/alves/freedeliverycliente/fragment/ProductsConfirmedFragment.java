package br.com.jose.alves.freedeliverycliente.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.jose.alves.freedeliverycliente.R;
import br.com.jose.alves.freedeliverycliente.adapter.ReceiveMerchandiseAdapter;
import br.com.jose.alves.freedeliverycliente.listen.RecyclerItemClickListener;
import br.com.jose.alves.freedeliverycliente.model.ItemPurchase;
import br.com.jose.alves.freedeliverycliente.util.ConfiguracaoFirebase;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ProductsConfirmedFragment extends Fragment {

    RecyclerView rvReceive;
    private List<ItemPurchase> merchandises = new ArrayList<>();
    private ReceiveMerchandiseAdapter receiveMerchandiseAdapter;

    private StorageReference storageReference;
    private DatabaseReference firebaseRef;
    private FirebaseAuth autenticacao;

    private MaterialButton share;
    private static final int CAMERA_REQUEST_CODE = 100; // Inteiro aleatório
    private File imagePath;
    private ConstraintLayout mcvImageCapture;



    public ProductsConfirmedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products_confirmed, container, false);
        initializeFindViewById(view);
        askAboutCamera();
        eventButton();
        rvReceive = view.findViewById(R.id.rv_products_confirme);
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
                        dialog.setTitle("Confirmar a entregar do produtor");
                        dialog.setMessage("Finaliza entregar: " + pedido.getName() + " ?");
                        dialog.setPositiveButton("sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (merchandises.remove(pedido)) {
                                    removePedidos();
                                    Toast.makeText(getActivity(),
                                            "Produto confirmardo!!",
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


    //  permissão de armazenamento
    private void askAboutCamera(){
        EasyPermissions.requestPermissions(
                this,
                "A partir deste ponto a permissão de armazenamento é necessária.",
                CAMERA_REQUEST_CODE,
                Manifest.permission.READ_EXTERNAL_STORAGE
                ,WRITE_EXTERNAL_STORAGE);
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
                .equalTo("Entregue");

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

    // código abaixo para obter o bitmap da visualização que está vendo na
    // tela. Você pode especificar qual visualização criar bitmap
    public void saveBitmap(Bitmap bitmap) {
        imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    private void eventButton() {
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = takeScreenshot(mcvImageCapture);
                saveBitmap(bitmap);
                shareIt();
            }
        });

    }

    //criar botão de compartilhamento com clique no ouvinte
    public Bitmap takeScreenshot(View v) {
        v.setDrawingCacheEnabled(true);
        return v.getDrawingCache();
    }

    //Compartilhar captura de tela. compartilhando implementação
    private void shareIt() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Uri uri = Uri.fromFile(imagePath);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("message/rfc822");
        String shareBody = "Histórico de compra que não vieram";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Compartilhado pelo APP");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sharingIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"atmconsultoria@gmail.com" } );

        startActivity(Intent.createChooser(sharingIntent, "Escolha App de compartilhamento"));



    }

    private void initializeFindViewById(View view) {
        share = view.findViewById(R.id.mb_service_details);
        mcvImageCapture = view.findViewById(R.id.f);
    }


}
