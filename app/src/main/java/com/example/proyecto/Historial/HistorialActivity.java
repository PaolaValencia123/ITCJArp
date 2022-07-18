package com.example.proyecto.Historial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.proyecto.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HistorialActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdaptador adaptador;
    private FirebaseFirestore firebaseFirestore;
    List<Historial> info = new ArrayList<>();
    private Spinner AHSSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        recyclerView = findViewById(R.id.AHTest);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();

        AHSSearch = findViewById(R.id.AHSSearch);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.search, android.R.layout.simple_spinner_item);
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

        if(intent.hasExtra("param")){
            decided(intent.getStringExtra("param"));
        }else {
            performQuery("timestamp");
        }
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
}