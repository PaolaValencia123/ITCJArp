package com.example.proyecto.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto.Historial.Historial;
import com.example.proyecto.Historial.RecyclerViewAdaptador;
import com.example.proyecto.R;
import com.example.proyecto.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private RecyclerView recyclerView;
    private RecyclerViewAdaptador adaptador;
    private FirebaseFirestore firebaseFirestore;
    List<Historial> info = new ArrayList<>();
    private Spinner AHSSearch;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.AHTest);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));


        AHSSearch = root.findViewById(R.id.AHSSearch);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(root.getContext(), R.array.search, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        AHSSearch.setAdapter(adapter);
        AHSSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE); /* if you want your item to be white */
                String param = AHSSearch.getSelectedItem().toString();

                decided(param);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(this, R.anim.item_animation_fall_down);
        //recyclerView.setLayoutAnimation(animationController);

        firebaseFirestore = FirebaseFirestore.getInstance();

        adaptador = new RecyclerViewAdaptador(info);
        recyclerView.setAdapter(adaptador);


        return root;
    }
    private void performSearchQuery(String field, String param){
        final int currentSize = info.size();
        info.clear();
        firebaseFirestore.collection("Reportes").whereEqualTo(field, param).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        info.add(new Historial(documentSnapshot.getString("Imagen"), documentSnapshot.getString("ReporteTipo"), documentSnapshot.getString("Estatus"), documentSnapshot.getString("Comentarios"), documentSnapshot.getString("Ubicación"), Optional.ofNullable(documentSnapshot.getString("Daño")).orElse(""), documentSnapshot.getTimestamp("timestamp")));
                    }
                    adaptador.notifyItemRangeRemoved(0, currentSize);
                    adaptador.notifyItemRangeInserted(0, task.getResult().size());
                }
            }
        });
    }

    private void decided(String param){
        String field = "";
        switch (param){
            case "Contenedor dañado":
            case "Contenedor lleno":
                field = "ReporteTipo";
                performSearchQuery(field, param);
                break;
            case "Enviado":
                field = "Estatus";
                param = "Enviado";
                performSearchQuery(field, param);
                break;
            case "Sin atender":
                field = "Estatus";
                param = "En espera";
                performSearchQuery(field, param);
                break;
            case "Fecha":
                param = "timestamp";
                performQuery(param);
                break;
            case "Dañado":
            case "Muy Dañado":
            case "Poco Dañado":
                field = "Daño";
                performSearchQuery(field, param);
                break;
            case "Ubicación":
                param = "Ubicación";
                performQuery(param);
                break;
        }
    }

    private void performQuery(String param){
        info.clear();
        firebaseFirestore.collection("Reportes").orderBy(param, Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        info.add(new Historial(documentSnapshot.getString("Imagen"), documentSnapshot.getString("ReporteTipo"), documentSnapshot.getString("Estatus"), documentSnapshot.getString("Comentarios"), documentSnapshot.getString("Ubicación"), Optional.ofNullable(documentSnapshot.getString("Daño")).orElse(""), documentSnapshot.getTimestamp("timestamp")));
                    }
                    adaptador.notifyDataSetChanged();
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}