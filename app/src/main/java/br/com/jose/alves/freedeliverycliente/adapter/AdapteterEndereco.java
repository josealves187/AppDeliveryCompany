package br.com.jose.alves.freedeliverycliente.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.jose.alves.freedeliverycliente.R;
import br.com.jose.alves.freedeliverycliente.adapter.listenes.EnderecoListeners;
import br.com.jose.alves.freedeliverycliente.model.Address;

public class AdapteterEndereco extends RecyclerView.Adapter<AdapteterEndereco.MyViewHolder> {

    private List<Address> addresses;
    private EnderecoListeners historicListener;

    public AdapteterEndereco(List<Address> addresses, EnderecoListeners historicListener) {
        this.addresses = addresses;
        this.historicListener = historicListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_adapter_endereco, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {


        Address address = addresses.get(i);
        holder.cep.setText(address.getCep());
        holder.rua.setText(address.getLogradouro());
        holder.complemento.setText(address.getComplemento());
        holder.cidade.setText(address.getLocalidade());
        holder.estado.setText(address.getUf());
        holder.bairro.setText(address.getBairro());
        holder.numero.setText(address.getNumero());
//        holder.phone.setText(address.getPhone());
//        holder.storyName.setText(address.getStoreName());
        OnClickListener(holder, i);


    }

    private void OnClickListener(@NonNull final MyViewHolder holder, final int position) {
        holder.ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historicListener.addAreaOnClick(addresses.get(position), position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {


        TextView estado;
        TextView bairro;
        TextView cep;
        TextView rua;
        TextView complemento;
        TextView cidade;
        TextView numero;
        TextView ver;
//        TextView phone;
//        TextView storyName;


        public MyViewHolder(View itemView) {
            super(itemView);

            rua = itemView.findViewById(R.id.textNomeRua);
            bairro = itemView.findViewById(R.id.textbairro);
            complemento = itemView.findViewById(R.id.textcomplemento);
            cidade = itemView.findViewById(R.id.textcidade);
            cep = itemView.findViewById(R.id.textcep);
            estado = itemView.findViewById(R.id.textestado);
            numero = itemView.findViewById(R.id.textnumero);
            ver = itemView.findViewById(R.id.ver);

        }
    }
}
