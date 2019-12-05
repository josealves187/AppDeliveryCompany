package br.com.jose.alves.freedeliverycliente.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.jose.alves.freedeliverycliente.R;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.DetailsViewHolde> {

    private List<String> payment = new ArrayList<>();

    public PaymentAdapter(List<String> payment) {
        this.payment = payment;
    }

    @NonNull
    @Override
    public PaymentAdapter.DetailsViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_details, parent, false);
        return new DetailsViewHolde(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.DetailsViewHolde holder, int position) {

        holder.payment.setText(payment.get(position));
    }

    @Override
    public int getItemCount() {
        return payment.size();
    }

    public class DetailsViewHolde extends RecyclerView.ViewHolder {


        TextView payment;

        public DetailsViewHolde(@NonNull View itemView) {
            super(itemView);

            payment = itemView.findViewById(R.id.tv_parking_payment);

        }
    }
}
