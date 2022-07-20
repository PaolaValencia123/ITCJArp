package com.example.proyecto.Historial;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.proyecto.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import java.util.Date;
import java.util.List;


public class RecyclerViewAdaptador extends RecyclerView.Adapter<com.example.proyecto.Historial.RecyclerViewAdaptador.ViewHolder>{
    public List<Historial> historialList;
    private Context context;
    private MaterialButton sendButton;

    public RecyclerViewAdaptador(List<Historial> elementos) {
        this.historialList = elementos;
    }

    @NonNull
    @Override
    public com.example.proyecto.Historial.RecyclerViewAdaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.square, viewGroup, false);
        com.example.proyecto.Historial.RecyclerViewAdaptador.ViewHolder viewHolder = new com.example.proyecto.Historial.RecyclerViewAdaptador.ViewHolder(view);

        sendButton = view.findViewById(R.id.IRBInImage2);
        context = viewGroup.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final com.example.proyecto.Historial.RecyclerViewAdaptador.ViewHolder viewHolder, final int i) {


        viewHolder.ubicacion.setText(historialList.get(i).getUbicacion());
        viewHolder.Status.setText(historialList.get(i).getEstatus());

        if(historialList.get(i).getEstatus().equals("En espera")){
            viewHolder.Status.setTextColor(ContextCompat.getColor(context, R.color.colorOrange));
        }else if(historialList.get(i).getEstatus().equals("Sin atender")){
            viewHolder.Status.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
        }else if(historialList.get(i).getEstatus().equals("Enviado")){
            viewHolder.Status.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        }

        viewHolder.TipoReporte.setText(historialList.get(i).getTipoReporte());

        if(historialList.get(i).getTipoReporte().equals("Contenedor lleno")){
            viewHolder.TipoReporte.setBackgroundResource(R.color.colorOrange);
        }else if(historialList.get(i).getTipoReporte().equals("Contenedor dañado")){
            sendButton.setVisibility(View.INVISIBLE);
            viewHolder.TipoReporte.setBackgroundResource(R.color.colorRed);
        }else{
            viewHolder.TipoReporte.setBackgroundResource(R.color.colorPrimary);
        }
        viewHolder.State.setText(historialList.get(i).getDamage());

        Timestamp timestamp = historialList.get(i).getTimestamp();
        Date date = timestamp.toDate();
        android.text.format.DateFormat df = new android.text.format.DateFormat();

        viewHolder.Update.setText(df.format("dd-MM-yy hh:mm a", date).toString());

        RequestOptions placeholderRequest = new RequestOptions();
        placeholderRequest.placeholder(R.drawable.ic_iconfinder_unknown_403017);
        Glide.with(context).setDefaultRequestOptions(placeholderRequest).load(historialList.get(i).getImagen()).into(viewHolder.Bache);

        /*viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(viewHolder.itemView.getContext(), elementos.get(i).getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(viewHolder.itemView.getContext(), DetailsActivity.class);
                /*intent.putExtra("Title", modelos.get(i).getTitle());
                intent.putExtra("Image", grupos[modelos.get(i).getImage()]);
                viewHolder.itemView.getContext().startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return historialList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView ubicacion, Status, TipoReporte, Update, State;
        private ImageView Bache;
        private MaterialButton materialButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ubicacion = itemView.findViewById(R.id.IRubicacion1);
            Status = itemView.findViewById(R.id.IRState1);
            TipoReporte = itemView.findViewById(R.id.IRTipoReporte1);
            Update = itemView.findViewById(R.id.IRUpdate1);
            Bache = itemView.findViewById(R.id.IRBInImage1);
            State = itemView.findViewById(R.id.IRDamage1);

            materialButton = itemView.findViewById(R.id.IRBInImage2);

            materialButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:"));
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"itcjarp@gmail.com"});
                    intent.putExtra(Intent.EXTRA_SUBJECT, TipoReporte.getText().toString());
                    intent.putExtra(Intent.EXTRA_TEXT, "Buenos días el contenedor de " + ubicacion.getText().toString() + " se ha llenado y es necesario vaciarlo." );
                    view.getContext().startActivity(intent);
                }
            });
        }

        public void sendEmail(View view){

        }
    }
}
