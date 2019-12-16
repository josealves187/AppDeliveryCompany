package br.com.jose.alves.freedeliverycliente.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.jose.alves.freedeliverycliente.R;
import br.com.jose.alves.freedeliverycliente.adapter.listenes.EnderecoListeners;
import br.com.jose.alves.freedeliverycliente.model.Address;

public class AdapteterEndereco extends RecyclerView.Adapter<AdapteterEndereco.MyViewHolder> {

    private List<Address> addresses;
    private Context context;
    private EnderecoListeners historicListener;


    public AdapteterEndereco(List<Address> addresses, Context context, EnderecoListeners historicListener) {
        this.addresses = addresses;
        this.context = context;
        this.historicListener = historicListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_adapter_endereco, parent, false);
        return new MyViewHolder(itemLista);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {


        Address address = addresses.get(i);
        holder.storyName.setText(address.getStoreName());
        OnClickListener(holder, i);

        SpannableString ss = new SpannableString(context.getString(R.string.address,address.getLogradouro() + ", " + address.getNumero() + " - " +
                address.getBairro() + ", " + address.getLocalidade() + " - " + address.getUf() + ", " + address.getCep()));

        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context,R.color.colorborder)), 0, 9, 0);


        ss.setSpan(
                new StyleSpan(Typeface.BOLD),
                0,
                9,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );


        holder.rua.setText(ss);

        SpannableString sss = new SpannableString(context.getString(R.string.phone,address.getPhone()));
        sss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context,R.color.colorborder)), 0, 9, 0);


        sss.setSpan(
                new StyleSpan(Typeface.BOLD),
                0,
                9,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );


        holder.phone.setText(sss);
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


        TextView rua;
        TextView ver;
        TextView phone;
        TextView storyName;


        public MyViewHolder(View itemView) {
            super(itemView);


            rua = itemView.findViewById(R.id.textNomeRua);
            ver = itemView.findViewById(R.id.ver);
            storyName = itemView.findViewById(R.id.storyName);
            phone = itemView.findViewById(R.id.tv_phone);

        }
    }
}
