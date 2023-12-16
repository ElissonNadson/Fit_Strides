package com.example.fit.ui.agua;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fit.R;
import com.example.fit.WaterRecord;

import com.example.fit.WaterRecordAdapter;

import java.util.ArrayList;
import java.util.List;

public class AguaFragment extends Fragment {

    private RecyclerView recyclerView;
    private WaterRecordAdapter waterRecordAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agua, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_today_records);

        // Configurar o RecyclerView com um layout manager e o adaptador
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Exemplo de dados (substitua com seus próprios dados)
        List<WaterRecord> waterRecords = new ArrayList<>();
        waterRecords.add(new WaterRecord(R.drawable.ic_agua, "12:00 PM", "Nome do Item", "250ml"));
        // Adicione mais itens conforme necessário

        waterRecordAdapter = new WaterRecordAdapter(getContext(), waterRecords);
        recyclerView.setAdapter(waterRecordAdapter);

        return view;
    }
}
