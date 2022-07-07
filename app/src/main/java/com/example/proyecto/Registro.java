package com.example.proyecto;

import static com.example.proyecto.Helper.ErrorMessages.getErrorMessage;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import static com.example.proyecto.Helper.ErrorMessages.getErrorMessage;

public class Registro extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mdb;

    private EditText edtNombre;
    private EditText edtEmail;
    private EditText edtPass;
    private EditText edtConfirmPass;
    private Spinner spnTipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Spinner
        spnTipo = findViewById(R.id.SpnTipo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTipo.setAdapter(adapter);
        spnTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK); /* if you want your item to be white */
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void enviarDatosRegistro(View view) {
        edtNombre = findViewById(R.id.txtName);
        edtEmail = findViewById(R.id.txtEmail);
        edtPass = findViewById(R.id.txtPass);
        edtConfirmPass = findViewById(R.id.txtConfirmPass);

        final String nombre = edtNombre.getText().toString();
        final String correo = edtEmail.getText().toString();
        final String passw = edtPass.getText().toString();
        String confPassw = edtConfirmPass.getText().toString();

        mAuth = FirebaseAuth.getInstance();
        mdb = FirebaseDatabase.getInstance().getReference();

        if (nombre.isEmpty() || correo.isEmpty() || passw.isEmpty() || confPassw.isEmpty()) {
            Toast.makeText(this, "Algún campo está vacío", Toast.LENGTH_SHORT).show();
        } else if (!passw.equals(confPassw)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        } else if (passw.length() >= 10) {
            mAuth.createUserWithEmailAndPassword(correo, passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("nombre", nombre);
                        map.put("correo", correo);
                        map.put("contraseña", passw);

                        String id = mAuth.getCurrentUser().getUid();

                        mdb.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Registro.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                                    cleanInputs();
                                    String id = mAuth.getCurrentUser().getUid();
                                }
                            }
                        });
                    } else {
                        getErrorMessage(Registro.this, MainActivity.getInstance(), 1, ((FirebaseAuthException) task.getException()).getErrorCode(), edtEmail, edtPass);
                    }
                }
            });
        } else {
            Toast.makeText(Registro.this, "La contraseña debe tener 10 o más caracteres", Toast.LENGTH_SHORT).show();
        }
    }


    public static Registro getInstance() {
        return new Registro();
    }

    public void cleanInputs() {
        edtNombre.setText("");
        edtEmail.setText("");
        edtPass.setText("");
        edtConfirmPass.setText("");
    }
}