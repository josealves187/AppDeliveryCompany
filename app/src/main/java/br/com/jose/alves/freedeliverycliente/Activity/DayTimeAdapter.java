package br.com.jose.alves.freedeliverycliente.Activity;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.jose.alves.freedeliverycliente.R;
import br.com.jose.alves.freedeliverycliente.model.Time;


public class DayTimeAdapter extends RecyclerView.Adapter<DayTimeAdapter.DayTimeAdapterViewHolde> {


    private List<Time> times = new ArrayList<>();
    private Context context;


    public DayTimeAdapter(List<Time> payment, Context context) {
        this.times = payment;
        this.context = context;
    }

    @NonNull
    @Override
    public DayTimeAdapter.DayTimeAdapterViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_day_time, parent, false);
        return new DayTimeAdapterViewHolde(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayTimeAdapterViewHolde holder, int position) {
        holder.day.setText(times.get(position).getDay());
        holder.operation.setText(times.get(position).getOperation());

        if (times.get(position).getOperation().equals("Fechado")) {
            holder.operation.setTextColor(ContextCompat.getColor(context, R.color.colorTextView));
            holder.operation.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));


        } else

            holder.operation.setTextColor(ContextCompat.getColor(context, R.color.colorTextTextView));

    }

    @Override
    public int getItemCount() {
        return times.size();
    }

    public class DayTimeAdapterViewHolde extends RecyclerView.ViewHolder {


        TextView operation;
        TextView day;


        public DayTimeAdapterViewHolde(@NonNull View itemView) {
            super(itemView);

            operation = itemView.findViewById(R.id.tv_operation);
            day = itemView.findViewById(R.id.tv_day_time);

        }
    }
}

