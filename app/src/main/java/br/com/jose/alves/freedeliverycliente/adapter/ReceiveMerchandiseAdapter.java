package br.com.jose.alves.freedeliverycliente.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import br.com.jose.alves.freedeliverycliente.R;
import br.com.jose.alves.freedeliverycliente.model.ItemPurchase;

public class ReceiveMerchandiseAdapter extends RecyclerView.Adapter<ReceiveMerchandiseAdapter.ReceiveMerchandiseViewHolder> {

    private List<ItemPurchase> merchandises;
    private Context context;

    public ReceiveMerchandiseAdapter(List<ItemPurchase> merchandises, Context context) {
        this.merchandises = merchandises;
        this.context = context;
    }

    @NonNull
    @Override
    public ReceiveMerchandiseAdapter.ReceiveMerchandiseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_receive_merchandise_adapter, parent, false);
        return new ReceiveMerchandiseViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiveMerchandiseAdapter.ReceiveMerchandiseViewHolder holder, int position) {


        SpannableString ss = new SpannableString(context.getString(R.string.tv_title_DESCRIÇÂO,merchandises.get(position).getName()));
        SpannableString sss = new SpannableString(context.getString(R.string.tv_title_QNT,String.valueOf(merchandises.get(position).getAmount())));
        SpannableString ssss = new SpannableString(context.getString(R.string.tv_title_VALOR,String.valueOf(merchandises.get(position).getValue())));
        SpannableString sssss = new SpannableString(context.getString(R.string.tv_title_description,merchandises.get(position).getDescription()));
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context,R.color.colorTitleTextView)), 0, 4, 0);
        sss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context,R.color.colorTitleTextView)), 0, 8, 0);
        ssss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context,R.color.colorTitleTextView)), 0, 5, 0);
        sssss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context,R.color.colorTitleTextView)), 0, 9, 0);

        ss.setSpan(
                new StyleSpan(Typeface.BOLD),
                0,
                4,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        sss.setSpan(
                new StyleSpan(Typeface.BOLD),
                0, 8,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        ssss.setSpan(
                new StyleSpan(Typeface.BOLD),
                0,
                5,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        sssss.setSpan(
                new StyleSpan(Typeface.BOLD),
                0,
                9,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        holder.nome.setText(ss);
        holder.amount.setText(sss);
        holder.description.setText(sssss);
        holder.price.setText(ssss);

    }



    @Override
    public int getItemCount() {
        return merchandises.size();
    }

    public class ReceiveMerchandiseViewHolder extends RecyclerView.ViewHolder {

        TextView nome, amount, price, description;
        public RelativeLayout viewBackground, viewForeground;



        public ReceiveMerchandiseViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.name);
            amount = itemView.findViewById(R.id.amount);
            price = itemView.findViewById(R.id.price);
            description = itemView.findViewById(R.id.description);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);

        }
    }

    public void removeItem(int position) {
        merchandises.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(ItemPurchase item, int position) {
        merchandises.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

}

