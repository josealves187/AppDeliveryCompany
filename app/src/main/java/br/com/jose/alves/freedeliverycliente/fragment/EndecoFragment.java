package br.com.jose.alves.freedeliverycliente.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import br.com.jose.alves.freedeliverycliente.Activity.MarketAddressActivity;
import br.com.jose.alves.freedeliverycliente.R;
import br.com.jose.alves.freedeliverycliente.adapter.AdapteterEndereco;
import br.com.jose.alves.freedeliverycliente.adapter.listenes.EnderecoListeners;
import br.com.jose.alves.freedeliverycliente.model.Address;
import br.com.jose.alves.freedeliverycliente.util.ConfiguracaoFirebase;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EndecoFragment extends Fragment implements EnderecoListeners {

    private DatabaseReference firebaseRef;
    private List<Address> endereco = new ArrayList<>();
    private AdapteterEndereco adapterEndereco;
    private RecyclerView list;
    private MaterialSearchView searchView;




    public EndecoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_endeco, container, false);
        list = view.findViewById(R.id.list);
        searchView = view.findViewById(R.id.materialSearchView);
        firebaseRef = ConfiguracaoFirebase.getFirebase();

        //Configurações Toolbar
        Toolbar  toolbar = view.findViewById(R.id.mt_toolbar);
        ((AppCompatActivity) getContext()).setSupportActionBar(toolbar);




        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterEndereco = new AdapteterEndereco(endereco, this);
        list.addItemDecoration(new DividerItemDecoration(list.getContext(), layoutManager.getOrientation()));
        list.setAdapter(adapterEndereco);

        recuperarEndereco();


        //Configuração do search view
        searchView.setHint("Pesquisar Endereço");
        searchView.setVoiceSearch(true); //or false
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pesquisarEndereco(newText);
                return true;
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarEndereco();
    }

    @Override
    public void onResume() {
        super.onResume();
        recuperarEndereco();
    }

    private void recuperarEndereco() {
        DatabaseReference enderecoRef = firebaseRef
                .child("AddressLocal");

        enderecoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                endereco.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    endereco.add(ds.getValue(Address.class));
                }

                adapterEndereco.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void pesquisarEndereco(String pesquisa) {

        DatabaseReference usuarioRef = firebaseRef
                .child("AddressLocal");
        Query query = usuarioRef.orderByChild("bairro")
                .startAt(pesquisa)
                .endAt(pesquisa + "\uf8ff");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                endereco.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    endereco.add(ds.getValue(Address.class));
                }

                adapterEndereco.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.seach_menu, menu);

        //Configurar botao pesquisa
        MenuItem item = menu.findItem(R.id.menuPesquisa);
        searchView.setMenuItem(item);

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void addAreaOnClick(Address address, int position) {
        Intent intent = new Intent(getActivity(), MarketAddressActivity.class);
        intent.putExtra("SCREEN_ORIGEN", address);
        startActivity(intent);

    }
}
