package com.example.fit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WaterRecordAdapter extends RecyclerView.Adapter<WaterRecordAdapter.WaterRecordViewHolder> {

    private Context context;
    private List<WaterRecord> waterRecords;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public WaterRecordAdapter(Context context, List<WaterRecord> waterRecords) {
        this.context = context;
        this.waterRecords = waterRecords;
    }

    @NonNull
    @Override
    public WaterRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_water_record, parent, false);
        return new WaterRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WaterRecordViewHolder holder, int position) {
        WaterRecord waterRecord = waterRecords.get(position);
        // Configurar os dados para o item
        holder.imgWaterIcon.setImageResource(waterRecord.getIconResId());
        holder.tvWaterTime.setText(waterRecord.getTime());
        holder.tvWaterName.setText(waterRecord.getName());
        holder.tvWaterAmount.setText(waterRecord.getAmount());

        // Configurar cliques nos botões (editar e deletar)
        holder.btnEdit.setOnClickListener(view -> {
            if (listener != null) {
                listener.onEditClick(position);
            }
        });

        holder.btnDelete.setOnClickListener(view -> {
            if (listener != null) {
                listener.onDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return waterRecords.size();
    }

    public static class WaterRecordViewHolder extends RecyclerView.ViewHolder {
        ImageView imgWaterIcon;
        TextView tvWaterTime;
        TextView tvWaterName;
        TextView tvWaterAmount;
        ImageButton btnEdit;
        ImageButton btnDelete;

        public WaterRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            imgWaterIcon = itemView.findViewById(R.id.imgWaterIcon);
            tvWaterTime = itemView.findViewById(R.id.tvWaterTime);
            tvWaterName = itemView.findViewById(R.id.tvWaterName);
            tvWaterAmount = itemView.findViewById(R.id.tvWaterAmount);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
