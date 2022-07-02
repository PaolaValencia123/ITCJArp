package com.example.proyecto;

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
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mdb;

    private EditText edtNombre;
    private EditText edtEmail;
    private EditText edtPass;
    private EditText edtConfirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
                        getErrorMessage(((FirebaseAuthException) task.getException()).getErrorCode());
                    }
                }
            });
        } else {
            Toast.makeText(Registro.this, "La contraseña debe tener 10 o más caracteres", Toast.LENGTH_SHORT).show();
        }
    }

    public void getErrorMessage(String errorCode) {
        switch (errorCode) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                Toast.makeText(Registro.this, "El formato del token personalizado es incorrecto. Por favor, compruebe la documentación.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                Toast.makeText(Registro.this, "El token personalizado corresponde a un público diferente.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_CREDENTIAL":
                Toast.makeText(Registro.this, "La credencial de autentificación suministrada está malformada o ha caducado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_EMAIL":
                Toast.makeText(Registro.this, "La dirección de correo electrónico está mal formateada.", Toast.LENGTH_LONG).show();
                edtEmail.setError("La dirección de correo electrónico está mal formateada.");
                edtEmail.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                Toast.makeText(Registro.this, "La contraseña no es válida o el usuario no tiene contraseña.", Toast.LENGTH_LONG).show();
                edtEmail.setError("La contraseña es incorrecta.");
                edtEmail.requestFocus();
                edtEmail.setText("");
                break;

            case "ERROR_USER_MISMATCH":
                Toast.makeText(Registro.this, "Las credenciales suministradas no se corresponden con el usuario previamente registrado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                Toast.makeText(Registro.this, "Esta operación es sensible y requiere una autentificación reciente. Inicie sesión de nuevo antes de reintentar esta solicitud.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                Toast.makeText(Registro.this, "Ya existe una cuenta con la misma dirección de correo electrónico pero con credenciales de acceso diferentes. Inicie sesión con un proveedor asociado a esta dirección de correo electrónico.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                Toast.makeText(Registro.this, "La dirección de correo electrónico ya está siendo utilizada por otra cuenta.", Toast.LENGTH_LONG).show();
                edtEmail.setError("La dirección de correo electrónico ya está siendo utilizada por otra cuenta.");
                edtEmail.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                Toast.makeText(Registro.this, "Esta credencial ya está asociada a otra cuenta de usuario.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_DISABLED":
                Toast.makeText(Registro.this, "La cuenta de usuario ha sido desactivada por un administrador.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                Toast.makeText(Registro.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión de nuevo.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_USER_NOT_FOUND":
                Toast.makeText(Registro.this, "No hay ningún registro de usuario correspondiente a este identificador. El usuario puede haber sido eliminado.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_INVALID_USER_TOKEN":
                Toast.makeText(Registro.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión de nuevo.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                Toast.makeText(Registro.this, "Esta operación no está permitida. Debe habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                break;

            case "ERROR_WEAK_PASSWORD":
                Toast.makeText(Registro.this, "La contraseña dada no es válida.", Toast.LENGTH_LONG).show();
                edtEmail.setError("La contraseña no es válida, debe tener al menos 6 caracteres");
                edtEmail.requestFocus();
                break;
        }
    }

        public void cleanInputs () {
            edtNombre.setText("");
            edtEmail.setText("");
            edtPass.setText("");
            edtConfirmPass.setText("");
        }


    }