package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReportActivity extends AppCompatActivity {

    private Spinner ARSCategoria, ARSDano;
    private TextInputEditText ARTEComentarios;
    private MaterialButton RAMbCancelar, ARMbSend;
    private ImageView ARUploadPhoto;
    private Uri mainImageURI;
    private String reporteOpcion;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        firebaseStorage = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        ARUploadPhoto = findViewById(R.id.ARUploadPhoto2);

        reporteOpcion = "Contenedor lleno";

        ARSCategoria = findViewById(R.id.ARSCategoria);
        ARSDano = findViewById(R.id.ARSDano);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ARSCategoria.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.estado, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ARSDano.setAdapter(adapter2);

        ARSDano.setVisibility(View.INVISIBLE);

        ARTEComentarios = findViewById(R.id.ARTEComentarios);
        RAMbCancelar = findViewById(R.id.ARMbCancelar);
        ARMbSend = findViewById(R.id.ARMbGuardar);

        RAMbCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ARMbSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData();
            }
        });

        ARUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(ReportActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(ReportActivity.this, "Permiso Denegado", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(ReportActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }else{
                        CropImage();
                    }
                }else{
                    CropImage();
                }
            }
        });
    }

    private void CropImage(){
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1).start(ReportActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageURI = result.getUri();
                ARUploadPhoto.setImageURI(mainImageURI);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void SaveData(){
        final String randomName = UUID.randomUUID().toString();
        final StorageReference imagePATH = firebaseStorage.child("Reportes_Imagenes").child(randomName + ".png");

        imagePATH.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    imagePATH.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map<String, Object> data = new HashMap<>();
                            if(uri !=null)
                                data.put("Imagen", uri.toString());
                            data.put("ReporteTipo", reporteOpcion);
                            data.put("Estatus", "En espera");
                            data.put("Comentarios", ARTEComentarios.getText().toString());
                            data.put("Ubicación", ARSCategoria.getSelectedItem().toString());
                            if(reporteOpcion.equals("Contenedor dañado"))
                                data.put("Daño", ARSDano.getSelectedItem().toString());
                            data.put("timestamp", FieldValue.serverTimestamp());
                            EditData(data);
                            finish();
                        }
                    });
                }
            }
        });
    }

    private void EditData(Map<String, Object> data){
        final String randomName = UUID.randomUUID().toString();
        firebaseFirestore.collection("Reportes").document(randomName).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ReportActivity.this, "Se han actualizado los datos.", Toast.LENGTH_LONG).show();
                }else{
                    String error = task.getException().getMessage();
                    Toast.makeText(ReportActivity.this, "Firestore Error: "+ error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.RBContenedorLleno:
                if (checked)
                    reporteOpcion = "Contenedor lleno";
                    ARSDano.setVisibility(View.INVISIBLE);
                    break;
            case R.id.RBContenedorDanado:
                if (checked)
                    reporteOpcion = "Contenedor dañado";
                    ARSDano.setVisibility(View.VISIBLE);
                    break;
        }
    }


}